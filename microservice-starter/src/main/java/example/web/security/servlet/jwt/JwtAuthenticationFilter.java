package example.web.security.servlet.jwt;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private RequestMatcher requiresAuthenticationRequestMatcher;
    private List<RequestMatcher> permissiveRequestMatchers;
    private AuthenticationManager authenticationManager;

    private final String JWT_REQUEST_HEADER = "Authorization";

    private AuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
    private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();

    public JwtAuthenticationFilter(){
        this.requiresAuthenticationRequestMatcher = new RequestHeaderRequestMatcher(JWT_REQUEST_HEADER);
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        Assert.notNull(authenticationManager, "authenticationManager must be specified");
        Assert.notNull(successHandler, "");
        Assert.notNull(failureHandler, "");
    }

    protected String getJwtToken(HttpServletRequest request){
        String authInfo = request.getHeader(JWT_REQUEST_HEADER);
        if (authInfo.startsWith("Bearer ")){
            return authInfo.substring("Bearer ".length());
        }
        return authInfo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!requiresAuthentication(request, response)){
            filterChain.doFilter(request, response);
            return;
        }
        Authentication authResult = null;
        AuthenticationException failed = null;
        try{
            String token = getJwtToken(request);
            if(StringUtils.isEmpty(token)){
                failed = new InsufficientAuthenticationException("jwt is empty");
            }else{
                JwtAuthenticationToken authToken = new JwtAuthenticationToken(token);
                authResult = getAuthenticationManager().authenticate(authToken);
            }
        } catch (AuthenticationException e){
            failed = e;
        }
        if (authResult == null){
            successfulAuthentication(request, response, filterChain, authResult);
        }else if (!permissiveRequest(request)){
            unsuccessfulAuthentication(request, response, failed);
        }

        filterChain.doFilter(request, response);

    }

    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    protected AuthenticationManager getAuthenticationManager(){
        return authenticationManager;
    }

    protected  boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response){
        return requiresAuthenticationRequestMatcher.matches(request);
    }

    protected boolean permissiveRequest(HttpServletRequest request){
        if (permissiveRequestMatchers == null){
            return false;
        }
        for (RequestMatcher permissiveMatcher : permissiveRequestMatchers){
            if (permissiveMatcher.matches(request)){
                return true;
            }
        }
        return false;
    }

    public void setPermissiveUrl(String... urls) {
        if(permissiveRequestMatchers == null)
            permissiveRequestMatchers = new ArrayList<>();
        for(String url : urls)
            permissiveRequestMatchers .add(new AntPathRequestMatcher(url));
    }

    public void setAuthenticationSuccessHandler(
            AuthenticationSuccessHandler successHandler) {
        Assert.notNull(successHandler, "successHandler cannot be null");
        this.successHandler = successHandler;
    }

    public void setAuthenticationFailureHandler(
            AuthenticationFailureHandler failureHandler) {
        Assert.notNull(failureHandler, "failureHandler cannot be null");
        this.failureHandler = failureHandler;
    }

    protected AuthenticationSuccessHandler getSuccessHandler() {
        return successHandler;
    }

    protected AuthenticationFailureHandler getFailureHandler() {
        return failureHandler;
    }
}
