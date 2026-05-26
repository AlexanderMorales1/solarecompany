/**
 * Propiedades tipadas de la aplicación cargadas desde {@code application.yml} bajo el prefijo {@code solare}.
 * <p>
 * Responsabilidad: centralizar JWT, CORS, URL del frontend y rutas de almacenamiento de imágenes.
 * Consumida por {@link WebConfig}, {@link com.solare.security.JwtTokenProvider} y servicios de medios.
 * </p>
 */
package com.solare.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Bean de configuración externa mapeada desde propiedades {@code solare.*}.
 */
@Data
@Component
@ConfigurationProperties(prefix = "solare")
public class SolareProperties {

    private Jwt jwt = new Jwt();
    private Cors cors = new Cors();
    private Frontend frontend = new Frontend();
    private Storage storage = new Storage();

    /** Parámetros del token JWT (secreto y tiempo de vida). */
    @Data
    public static class Jwt {
        /** Clave HMAC para firmar tokens. */
        private String secret;
        /** Duración del token en milisegundos. */
        private long expirationMs;
    }

    /** Orígenes permitidos para peticiones cross-origin del frontend Angular. */
    @Data
    public static class Cors {
        private List<String> allowedOrigins;
    }

    /** URL base del cliente SPA usada en redirecciones OAuth2. */
    @Data
    public static class Frontend {
        private String url = "http://localhost:4200";
    }

    /** Directorio físico y ruta pública HTTP para archivos subidos (imágenes de productos). */
    @Data
    public static class Storage {
        private String uploadDir = "uploads";
        private String publicBasePath = "/uploads";
    }
}
