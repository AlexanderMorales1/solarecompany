/**
 * Manejador post-login OAuth2 (Google): vincula o crea usuario y redirige al frontend con JWT.
 * <p>
 * Relación: configurado en {@link com.solare.config.SecurityConfig} para el flujo OAuth2.
 * </p>
 */
package com.solare.security;

import com.solare.config.SolareProperties;
import com.solare.model.entity.RoleEntity;
import com.solare.model.entity.UserEntity;
import com.solare.repository.RoleRepository;
import com.solare.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Tras autenticación Google exitosa, emite JWT y redirige a {@code /auth/callback?token=...} en el SPA.
 */
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final SolareProperties solareProperties;

    /**
     * Resuelve o crea usuario Google, emite JWT y redirige al SPA con el token en query.
     *
     * @param request  petición HTTP del callback OAuth2
     * @param response respuesta para redirección
     * @param authentication principal OAuth2 autenticado
     */
    @Override
    @Transactional
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {
        OAuth2User oauth = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attrs = oauth.getAttributes();
        String sub = String.valueOf(attrs.get("sub"));
        String email = attrs.get("email") != null ? String.valueOf(attrs.get("email")) : oauth.getName();
        String given = attrs.get("given_name") != null ? String.valueOf(attrs.get("given_name")) : "";
        String family = attrs.get("family_name") != null ? String.valueOf(attrs.get("family_name")) : "";

        // Prioridad: usuario Google existente → vincular cuenta local por email → crear nuevo
        UserEntity user = userRepository.findByProviderAndProviderId("GOOGLE", sub)
                .orElseGet(() -> userRepository.findByEmail(email.toLowerCase().trim())
                        .map(existing -> linkGoogle(existing, sub))
                        .orElseGet(() -> createGoogleUser(email, given, family, sub)));

        String roles = user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.joining(","));
        String token = jwtTokenProvider.createTokenForUser(user.getId(), user.getEmail(), roles);
        String base = solareProperties.getFrontend().getUrl();
        String target = base + "/auth/callback?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);
        getRedirectStrategy().sendRedirect(request, response, target);
    }

    /** Asocia proveedor GOOGLE a un usuario que ya existía (p. ej. registro local previo). */
    private UserEntity linkGoogle(UserEntity existing, String sub) {
        existing.setProvider("GOOGLE");
        existing.setProviderId(sub);
        return userRepository.save(existing);
    }

    /** Alta de usuario OAuth sin contraseña local. */
    private UserEntity createGoogleUser(String email, String first, String last, String sub) {
        RoleEntity userRole = roleRepository.findByName(RoleEntity.RoleName.ROLE_USER)
                .orElseThrow();
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(userRole);
        UserEntity u = UserEntity.builder()
                .email(email.toLowerCase().trim())
                .password(null)
                .firstName(first)
                .lastName(last)
                .provider("GOOGLE")
                .providerId(sub)
                .enabled(true)
                .roles(roles)
                .build();
        return userRepository.save(u);
    }
}
