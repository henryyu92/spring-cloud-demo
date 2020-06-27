package example.web.security.servlet.jwt;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

public class JwtLoginConfigurer<T extends JwtLoginConfigurer<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {
}
