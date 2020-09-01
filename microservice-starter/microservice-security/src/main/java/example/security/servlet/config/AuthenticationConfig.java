package example.security.servlet.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public class AuthenticationConfig extends WebSecurityConfigurerAdapter {

    public void formLogin(HttpSecurity http) throws Exception {
        http.formLogin(formLogin->
                formLogin
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginPage("/authentication/login")
                        .failureUrl("/authentication/login?failed")
                        .loginProcessingUrl("/authentication/login/process")
                        .permitAll(true)
        );
    }


    public void basicAuthentication(HttpSecurity http) throws Exception{
        http.httpBasic(httpBasic ->
                httpBasic
                        .realmName("")
        );
    }
}
