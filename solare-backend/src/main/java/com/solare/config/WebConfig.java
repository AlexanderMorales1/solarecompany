/**
 * Configuración web transversal: CORS y servicio estático de archivos subidos.
 * <p>
 * Relación: usa {@link SolareProperties} para orígenes CORS y rutas de {@code uploads/}.
 * Complementa {@link SecurityConfig} (que habilita CORS con {@code Customizer.withDefaults()}).
 * </p>
 */
package com.solare.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final SolareProperties solareProperties;

    /**
     * Define la política CORS global aplicada a todas las rutas {@code /**}.
     *
     * @return fuente de configuración CORS registrada en Spring Security y MVC
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration c = new CorsConfiguration();
        c.setAllowedOrigins(solareProperties.getCors().getAllowedOrigins());
        c.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        c.setAllowedHeaders(List.of("*"));
        c.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", c);
        return source;
    }

    /**
     * Mapea la URL pública de uploads al directorio físico configurado en propiedades.
     * Permite servir imágenes de productos sin pasar por un controlador dedicado.
     *
     * @param registry registro de manejadores de recursos estáticos de Spring MVC
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(solareProperties.getStorage().getUploadDir()).toAbsolutePath().normalize();
        String resourceLocation = uploadPath.toUri().toString();
        registry.addResourceHandler(solareProperties.getStorage().getPublicBasePath() + "/**")
                .addResourceLocations(resourceLocation);
    }
}
