package it.mapsgroup.gzoom.security.model;

import it.mapsgroup.gzoom.mybatis.dto.UserLogin;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The authentication entity for an authenticated principal or one that is going to be authenticated.
 *
 * @author Andrea Fossi
 */
public class JwtAuthentication extends AbstractAuthenticationToken {
    private static final long serialVersionUID = -111373718772460955L;
    private String jwtToken;
    private UserLogin principal;

    /**
     * Creates a non-authenticated instance whose credentials are the JWT token (and no principal is specified).
     *
     * @param jwtToken The JWT token
     */
    public JwtAuthentication(String jwtToken) {
        super(null);
        this.jwtToken = jwtToken;
    }

    /**
     * Creates an authenticated instance of this {@link Authentication} which relies on the given principal.
     *
     * @param jwtToken  The generated authentication token
     * @param principal The user profiler
     */
    public JwtAuthentication(String jwtToken, UserLogin principal) {
        super(authorities(principal));
        this.principal = principal;
        this.jwtToken = jwtToken;
        setAuthenticated(true);
    }

    /**
     * Returns the JWT token.
     * <p>This works exactly like {@link #getCredentials()} but is a more concise way to get the token.</p>
     *
     * @return The JWT token.
     */
    public String getJwtToken() {
        return jwtToken;
    }

    @Override
    public Object getCredentials() {
        return jwtToken;
    }

    @Override
    public Object getPrincipal() {
        // TOTO return an immutable copy of the principal
        return principal != null ? principal : null;
    }

    @Override
    public String getName() {
        return principal != null ? principal.getUsername() : "";
    }

    private static Collection<GrantedAuthority> authorities(UserLogin user) {
        List<GrantedAuthority> permissions;
       /* if (user != null && user.getPermissions() != null) {
            Set<LmmPermission> perms = LmmPermission.fromList(user.getPermissions());
            permissions = newArrayList(transform(perms, role -> new SimpleGrantedAuthority(role.name())));
        } else {*/
            permissions = new ArrayList<>();
        //}
        return permissions;
    }
}
