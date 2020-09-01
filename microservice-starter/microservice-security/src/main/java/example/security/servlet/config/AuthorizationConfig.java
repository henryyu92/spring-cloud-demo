package example.security.servlet.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public class AuthorizationConfig extends WebSecurityConfigurerAdapter {

    // 显式设置授权的请求需要认证认证
    private void authenticated(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->{
            authorizeRequests.anyRequest().authenticated();
        });
    }

    // 声明授权规则
    private void handlerAccess(HttpSecurity http) throws Exception{
        http.authorizeRequests(authorizeRequests ->{
            // 授权规则按照被声明的顺序
            authorizeRequests
                    .mvcMatchers("/resources/**", "/signup", "/about").permitAll()
                    .mvcMatchers("/admin/**").hasRole("ADMIN")
                    .mvcMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
                    .anyRequest().denyAll();
        });
    }

    // 使用表达式的方式控制授权
    private void expression(HttpSecurity http) throws Exception{
        http.authorizeRequests(authorizeRequests ->{
            // 使用内置的表达式
            authorizeRequests.antMatchers("/admin*").access("hasRole('admin') and hasIpAddress('192.168.1.0/24')");
            // @webSecurity 表示 bean 的名称，checkUserId 是 bean 处理权限的接口
            authorizeRequests.antMatchers("/user/{userId}/**").access("@webSecurity.checkUserId(authentication,#userId)");
            // path variable 也支持
            authorizeRequests.antMatchers("/user/{userId}/**").access("@webSecurity.checkUserId(authentication,#userId)");
        });
    }


    // 使用注解来控制授权，需要添加注解 @EnableGlobalMethodSecurity
    private void methodExpression(HttpSecurity http){

    }
}
