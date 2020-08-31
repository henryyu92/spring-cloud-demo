package example.security.servlet.config;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import java.security.acl.Permission;
import java.util.ArrayList;
import java.util.List;

/**
 * 方法的权限可以使用注解的形式控制，使用这种形式需要添加注解 @EnableGlobalMethodSecurity
 *  - @PreAuthorize     方法调用前需要获得授权
 *  - @PostAuthorize    方法调用后执行权限检查
 *  - @PreFilter
 *  - @PostFilter
 */
@EnableGlobalMethodSecurity
public class MethodSecurityExpression {


    @PreAuthorize("hasRole('USER')")
    public void create(String contact){

    }

    @PreAuthorize("hasPermission(#contact, 'admin')")
    public void deletePermission(String contact, String recipient, Permission permission){

    }

    @PostAuthorize("returnObject == 'world'")
    public String hello(String hello){
        return "world";
    }

    @PreAuthorize("hasRole('USER')")
    @PostFilter("hasPermission(filterObject, 'read') or hasPermission(filterObject, 'admin')")
    public List<String> getAll(){
        return new ArrayList<>();
    }
}
