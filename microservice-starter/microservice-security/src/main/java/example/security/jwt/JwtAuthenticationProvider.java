package example.security.jwt;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private JwtUserService userService;

    public JwtAuthenticationProvider(JwtUserService userService){
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String token = ((JwtAuthenticationToken) authentication).getToken();
        // todo jwt 解码 token
        String username = "jack";
        String password = "encoded password";
        String expire = "expire data";

        // 保存的用户信息
        UserDetails userInfo = userService.getUserLoginInfo(username);

        // todo 用户信息校验


        return new JwtAuthenticationToken(userInfo, token, userInfo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }
}
