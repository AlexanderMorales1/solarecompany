package com.solare.service;

import com.solare.dto.auth.AuthResponse;
import com.solare.dto.auth.ForgotPasswordRequest;
import com.solare.dto.auth.LoginRequest;
import com.solare.dto.auth.RegisterRequest;
import com.solare.dto.auth.ResetPasswordRequest;
import com.solare.dto.auth.UserProfileDto;
import com.solare.exception.BadRequestException;
import com.solare.exception.ResourceNotFoundException;
import com.solare.model.entity.RoleEntity;
import com.solare.model.entity.UserEntity;
import com.solare.repository.RoleRepository;
import com.solare.repository.UserRepository;
import com.solare.security.JwtTokenProvider;
import com.solare.security.SolareUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.findByEmail(req.getEmail().toLowerCase().trim()).isPresent()) {
            throw new BadRequestException("El correo ya está registrado");
        }
        RoleEntity userRole = roleRepository.findByName(RoleEntity.RoleName.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("Rol ROLE_USER no inicializado"));
        UserEntity user = UserEntity.builder()
                .email(req.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(req.getPassword()))
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .provider("LOCAL")
                .enabled(true)
                .roles(Set.of(userRole))
                .build();
        userRepository.save(user);
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), req.getPassword()));
        return buildAuthResponse(auth);
    }

    public AuthResponse login(LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail().toLowerCase().trim(), req.getPassword()));
        return buildAuthResponse(auth);
    }

    @Transactional(readOnly = true)
    public UserProfileDto me(SolareUserDetails user) {
        UserEntity u = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return UserProfileDto.builder()
                .userId(u.getId())
                .email(u.getEmail())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .roles(u.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toSet()))
                .build();
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest req) {
        userRepository.findByEmail(req.getEmail().toLowerCase().trim()).ifPresent(user -> {
            if (!"LOCAL".equalsIgnoreCase(user.getProvider())) {
                return;
            }
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setResetTokenExpiry(Instant.now().plus(1, ChronoUnit.HOURS));
            userRepository.save(user);
            log.info("Recuperación de contraseña para {} — token (solo desarrollo): {}", user.getEmail(), token);
        });
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest req) {
        UserEntity user = userRepository.findByResetToken(req.getToken())
                .orElseThrow(() -> new BadRequestException("Token inválido o expirado"));
        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(Instant.now())) {
            throw new BadRequestException("Token inválido o expirado");
        }
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }

    private AuthResponse buildAuthResponse(Authentication auth) {
        SolareUserDetails principal = (SolareUserDetails) auth.getPrincipal();
        String token = jwtTokenProvider.createToken(auth);
        UserEntity u = userRepository.findById(principal.getId()).orElseThrow();
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(u.getId())
                .email(u.getEmail())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .roles(u.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toSet()))
                .build();
    }
}
