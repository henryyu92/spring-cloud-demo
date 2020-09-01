package example.security.servlet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> authorize
                .antMatchers("/css/**", "/index").permitAll()
                .antMatchers("/user/**").hasRole("USER")
        ).formLogin(formLogin -> formLogin.loginPage("/login")
                .failureUrl("/login-error"));
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username("username").password("password").roles("USER").build();
        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    public AuthenticationConfig authenticationConfig(){
        return new AuthenticationConfig();
    }

    @Bean
    public AuthorizationConfig authorizationConfig(){
        return new AuthorizationConfig();
    }
}
