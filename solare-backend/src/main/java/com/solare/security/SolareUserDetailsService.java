/**
 * Carga de usuarios para Spring Security (login por email o reconstrucción desde JWT por id).
 */
package com.solare.security;

import com.solare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación de {@link UserDetailsService} respaldada por {@link com.solare.repository.UserRepository}.
 */
@Service
@RequiredArgsConstructor
public class SolareUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Usado en autenticación por contraseña: {@code username} es el correo electrónico.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(SolareUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    /**
     * Usado por {@link JwtAuthenticationFilter} tras validar el token (subject = id numérico).
     *
     * @param id identificador del usuario en JWT
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long id) {
        return userRepository.findById(id)
                .map(SolareUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
