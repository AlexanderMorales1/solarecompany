/**
 * Configuración de documentación OpenAPI/Swagger para la API REST.
 * <p>
 * Expone la UI en {@code /swagger-ui.html} y define el esquema de seguridad Bearer JWT
 * para probar endpoints protegidos desde Swagger.
 * </p>
 */
package com.solare.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    /**
     * Bean {@link OpenAPI} con metadatos del proyecto y autenticación HTTP Bearer.
     *
     * @return definición OpenAPI registrada en el contexto Spring
     */
    @Bean
    public OpenAPI solareOpenAPI() {
        final String scheme = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("Solare API")
                        .description("REST API e-commerce — lentes de sol")
                        .version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList(scheme))
                .components(new Components().addSecuritySchemes(scheme,
                        new SecurityScheme()
                                .name(scheme)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
