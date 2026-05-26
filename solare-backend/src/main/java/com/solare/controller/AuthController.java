/**
 * API de autenticación: registro, login JWT, perfil y recuperación de contraseña.
 * <p>
 * Rutas públicas salvo {@code GET /auth/me}. OAuth2 Google redirige fuera de este controlador.
 * </p>
 */
package com.solare.controller;

import com.solare.dto.auth.AuthResponse;
import com.solare.dto.auth.ForgotPasswordRequest;
import com.solare.dto.auth.LoginRequest;
import com.solare.dto.auth.RegisterRequest;
import com.solare.dto.auth.ResetPasswordRequest;
import com.solare.dto.auth.UserProfileDto;
import com.solare.security.SolareUserDetails;
import com.solare.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Expone registro, login, perfil y flujo de recuperación de contraseña bajo {@code /auth}.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación")
public class AuthController {

    private final AuthService authService;

    /**
     * Registra un usuario local y devuelve JWT.
     *
     * @param req datos de registro validados
     * @return token Bearer y metadatos del usuario
     */
    @PostMapping("/register")
    @Operation(summary = "Registro de usuario")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    /**
     * Autentica por correo y contraseña.
     *
     * @param req credenciales
     * @return respuesta con JWT
     */
    @PostMapping("/login")
    @Operation(summary = "Inicio de sesión (JWT)")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    /**
     * Perfil del usuario actual (requiere JWT válido).
     *
     * @param user principal extraído del filtro JWT
     * @return DTO de perfil
     */
    @GetMapping("/me")
    @Operation(summary = "Perfil del usuario autenticado")
    public ResponseEntity<UserProfileDto> me(@AuthenticationPrincipal SolareUserDetails user) {
        return ResponseEntity.ok(authService.me(user));
    }

    /**
     * Inicia recuperación de contraseña (respuesta genérica por seguridad).
     *
     * @param req correo del usuario
     * @return mensaje informativo
     */
    @PostMapping("/forgot-password")
    @Operation(summary = "Solicitar recuperación de contraseña (token en logs en desarrollo)")
    public ResponseEntity<Map<String, String>> forgot(@Valid @RequestBody ForgotPasswordRequest req) {
        authService.forgotPassword(req);
        return ResponseEntity.ok(Map.of("message", "Si el correo existe, recibirás instrucciones."));
    }

    /**
     * Aplica nueva contraseña con token de recuperación válido.
     *
     * @param req token y nueva contraseña
     * @return confirmación de actualización
     */
    @PostMapping("/reset-password")
    @Operation(summary = "Restablecer contraseña con token")
    public ResponseEntity<Map<String, String>> reset(@Valid @RequestBody ResetPasswordRequest req) {
        authService.resetPassword(req);
        return ResponseEntity.ok(Map.of("message", "Contraseña actualizada"));
    }

    /**
     * Devuelve la URL de autorización OAuth2 de Google relativa al host de la petición.
     *
     * @param request petición HTTP para derivar esquema, host y contexto
     * @return mapa con clave {@code url}
     */
    @GetMapping("/oauth2/google")
    @Operation(summary = "URL para iniciar login con Google")
    public ResponseEntity<Map<String, String>> googleUrl(jakarta.servlet.http.HttpServletRequest request) {
        // Construye la URL de autorización OAuth2 relativa al host/puerto de la petición actual
        String base = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();
        return ResponseEntity.ok(Map.of("url", base + "/oauth2/authorization/google"));
    }
}
