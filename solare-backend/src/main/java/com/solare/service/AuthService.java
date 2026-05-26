/**
 * Servicio de autenticación local: registro, login JWT, perfil y recuperación de contraseña.
 * <p>
 * Relación: usa {@link com.solare.security.JwtTokenProvider} y {@link com.solare.controller.AuthController}.
 * </p>
 */
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

/**
 * Gestiona el ciclo de vida de credenciales locales (no OAuth2).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Registra un usuario con proveedor LOCAL, asigna ROLE_USER y devuelve JWT.
     *
     * @param req datos de registro
     * @return token y metadatos del usuario
     */
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

    /**
     * Autentica por correo y contraseña y emite JWT.
     *
     * @param req credenciales
     * @return respuesta con token Bearer
     */
    public AuthResponse login(LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail().toLowerCase().trim(), req.getPassword()));
        return buildAuthResponse(auth);
    }

    /**
     * Devuelve el perfil del usuario autenticado en la petición actual.
     *
     * @param user principal extraído del JWT
     * @return DTO de perfil
     */
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

    /**
     * Genera token de restablecimiento solo para cuentas LOCAL (silencioso si el correo no existe).
     * <p>
     * En desarrollo el token se escribe en logs; en producción debería enviarse por correo.
     * </p>
     *
     * @param req correo solicitado
     */
    @Transactional
    public void forgotPassword(ForgotPasswordRequest req) {
        userRepository.findByEmail(req.getEmail().toLowerCase().trim()).ifPresent(user -> {
            // OAuth2 no usa contraseña local: se ignora la solicitud sin error explícito
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

    /**
     * Aplica nueva contraseña si el token de recuperación es válido y no ha expirado.
     *
     * @param req token y nueva contraseña
     */
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

    /** Construye la respuesta de autenticación con JWT y roles del usuario. */
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
