package example.web.security.authentication;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationMain {

    public static void main(String[] args) {
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(true);
    }
}
