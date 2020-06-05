package org.mooc.cloud.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * Eureka clients will not generally possess a valid cross site request forgery (CSRF) token
         * you will need to disable this requirement for the /eureka/** endpoints.
         */
        http.csrf().ignoringAntMatchers("/eureka/**");
        super.configure(http);
    }
}
