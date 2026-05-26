/**
 * Punto de entrada de la aplicación backend Solare.
 * <p>
 * Este archivo arranca el contexto de Spring Boot que expone la API REST del e-commerce
 * de lentes de sol. El resto de módulos (controladores, servicios, seguridad, persistencia)
 * se registran automáticamente mediante el escaneo de componentes bajo el paquete {@code com.solare}.
 * </p>
 *
 * @see com.solare.config
 * @see com.solare.controller
 * @see com.solare.service
 */
package com.solare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Spring Boot.
 * <p>
 * Responsabilidad: iniciar el servidor embebido y cargar la configuración por defecto
 * de Spring Boot (autoconfiguración de web, JPA, seguridad, etc.).
 * </p>
 */
@SpringBootApplication
public class SolareApplication {

    /**
     * Método {@code main} estándar de Java que delega el arranque a Spring Boot.
     *
     * @param args argumentos de línea de comandos pasados al proceso JVM (no se usan de forma explícita aquí)
     */
    public static void main(String[] args) {
        SpringApplication.run(SolareApplication.class, args);
    }
}
