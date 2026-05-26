/**
 * Configuración de Spring Security: JWT, OAuth2, roles y rutas públicas/protegidas.
 * <p>
 * Relación: integra {@link com.solare.security.JwtAuthenticationFilter},
 * {@link com.solare.security.OAuth2LoginSuccessHandler} y {@link com.solare.security.SolareUserDetailsService}.
 * Las rutas {@code /admin/**} exigen rol ADMIN; el resto de la API requiere autenticación salvo excepciones listadas.
 * </p>
 */
package com.solare.config;

import com.solare.security.JwtAuthenticationFilter;
import com.solare.security.OAuth2LoginSuccessHandler;
import com.solare.security.SolareUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SolareUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    /** Codificador BCrypt para contraseñas locales y registro de usuarios. */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Proveedor de autenticación por usuario/contraseña contra la base de datos.
     *
     * @return proveedor configurado con {@link SolareUserDetailsService} y {@link PasswordEncoder}
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService);
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    /**
     * Expone el {@link AuthenticationManager} de Spring para uso en servicios de login.
     *
     * @param cfg configuración de autenticación de Spring Security
     * @return gestor de autenticación del contexto
     * @throws Exception si la configuración de seguridad no puede construirse
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    /**
     * Cadena de filtros HTTP: desactiva CSRF (API stateless con JWT), habilita CORS,
     * define autorización por ruta y registra OAuth2 + filtro JWT antes del filtro de usuario/contraseña.
     *
     * @param http builder de configuración de seguridad web
     * @return cadena de filtros publicada como bean
     * @throws Exception propagada por la API de configuración de Spring Security
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/auth/**",
                                "/oauth2/**",
                                "/login/oauth2/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/products/**", "/categories/**", "/discounts/public/**", "/home-banners/**", "/uploads/**")
                        .permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2Login(o -> o.successHandler(oAuth2LoginSuccessHandler))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
