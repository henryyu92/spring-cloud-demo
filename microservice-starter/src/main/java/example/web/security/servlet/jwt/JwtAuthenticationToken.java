package example.web.security.servlet.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private UserDetails principal;
    private String credentials;
    private String token;

    public JwtAuthenticationToken(String token){
        super(Collections.emptyList());
        this.token = token;
    }

    public JwtAuthenticationToken(UserDetails principal, String token, Collection<? extends GrantedAuthority> authorities){
        super(authorities);
        this.token = token;
        this.principal = principal;
    }

    @Override
    public void setDetails(Object details) {
        super.setDetails(details);
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getToken(){
        return token;
    }
}
