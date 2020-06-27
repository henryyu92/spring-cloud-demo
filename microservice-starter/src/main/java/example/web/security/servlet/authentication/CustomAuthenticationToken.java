package example.web.security.servlet.authentication;


import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * AuthenticationToken 是 Authentication 的子类
 */
public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    public CustomAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
