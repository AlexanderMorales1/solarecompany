package com.solare.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "solare")
public class SolareProperties {

    private Jwt jwt = new Jwt();
    private Cors cors = new Cors();
    private Frontend frontend = new Frontend();
    private Storage storage = new Storage();

    @Data
    public static class Jwt {
        private String secret;
        private long expirationMs;
    }

    @Data
    public static class Cors {
        private List<String> allowedOrigins;
    }

    @Data
    public static class Frontend {
        private String url = "http://localhost:4200";
    }

    @Data
    public static class Storage {
        private String uploadDir = "uploads";
        private String publicBasePath = "/uploads";
    }
}
