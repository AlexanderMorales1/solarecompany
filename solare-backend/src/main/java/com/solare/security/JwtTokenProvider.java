/**
 * Creación y validación de tokens JWT (JJWT) con claims de id, email y roles.
 * <p>
 * Configuración: {@code solare.jwt.secret} y {@code solare.jwt.expiration-ms}.
 * </p>
 */
package com.solare.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Proveedor de tokens firmados con HMAC; el subject del JWT es el id del usuario como cadena.
 */
@Component
public class JwtTokenProvider {

    /** Clave HMAC-SHA derivada de {@code solare.jwt.secret}. */
    private final SecretKey key;
    /** Vida útil del token en milisegundos ({@code solare.jwt.expiration-ms}). */
    private final long expirationMs;

    /**
     * Inicializa clave HMAC; exige secreto de al menos 32 bytes UTF-8.
     */
    public JwtTokenProvider(
            @Value("${solare.jwt.secret}") String secret,
            @Value("${solare.jwt.expiration-ms}") long expirationMs) {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalArgumentException("solare.jwt.secret debe tener al menos 32 caracteres");
        }
        this.key = Keys.hmacShaKeyFor(bytes);
        this.expirationMs = expirationMs;
    }

    /**
     * Genera JWT tras login local a partir del {@link Authentication} de Spring.
     */
    public String createToken(Authentication authentication) {
        SolareUserDetails principal = (SolareUserDetails) authentication.getPrincipal();
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .subject(String.valueOf(principal.getId()))
                .claim("email", principal.getUsername())
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(exp)
                .signWith(key)
                .compact();
    }

    /**
     * Genera JWT explícito (p. ej. tras OAuth2) sin pasar por {@link Authentication}.
     *
     * @param userId   id del usuario
     * @param email    correo
     * @param rolesCsv roles separados por coma
     */
    public String createTokenForUser(Long userId, String email, String rolesCsv) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("roles", rolesCsv)
                .issuedAt(now)
                .expiration(exp)
                .signWith(key)
                .compact();
    }

    /**
     * Verifica firma y expiración; devuelve false ante cualquier excepción de parseo.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrae el id de usuario del claim {@code sub}.
     */
    public Long getUserId(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.parseLong(claims.getSubject());
    }
}
