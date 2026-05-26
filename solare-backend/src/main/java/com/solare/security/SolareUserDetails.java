/**
 * Adaptador de {@link com.solare.model.entity.UserEntity} a {@link org.springframework.security.core.userdetails.UserDetails}.
 * <p>
 * El nombre de usuario en Spring Security es el correo electrónico; el id se expone aparte para JWT y servicios.
 * </p>
 */
package com.solare.security;

import com.solare.model.entity.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Principal autenticado en la API; autoridades derivadas de roles de la entidad.
 */
@Getter
public class SolareUserDetails implements UserDetails {

    /** Identificador numérico del usuario (claim {@code sub} del JWT). */
    private final Long id;
    /** Correo electrónico (username en Spring Security). */
    private final String email;
    /** Contraseña hasheada; {@code null} si solo OAuth. */
    private final String password;
    /** Indica si la cuenta puede autenticarse. */
    private final boolean enabled;
    /** Roles convertidos a {@link GrantedAuthority}. */
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * Construye el principal a partir del usuario persistido (roles → {@link SimpleGrantedAuthority}).
     */
    public SolareUserDetails(UserEntity user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.enabled = user.isEnabled();
        this.authorities = user.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getName().name()))
                .collect(Collectors.toSet());
    }

    /** Roles del usuario como autoridades Spring Security. */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /** Hash de contraseña local; puede ser {@code null} en cuentas solo OAuth. */
    @Override
    public String getPassword() {
        return password;
    }

    /** En Spring Security el "username" es el email para login local. */
    @Override
    public String getUsername() {
        return email;
    }

    /** La cuenta no expira en este modelo. */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /** La cuenta no se bloquea por intentos fallidos en este modelo. */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /** Las credenciales no caducan por antigüedad en este modelo. */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /** Refleja el campo {@code enabled} de la entidad usuario. */
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
