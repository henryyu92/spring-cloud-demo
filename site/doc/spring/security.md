## Spring Security

Spring Security 使用标准的 Servlet Filter 与 Servlet 容器集成，也就是说 Spring Security 可以和任何运行在 Servlet 容器中的应用程序一起工作。

### FilterChain

Spring 提供了 `DelegatingFilterProxy` 过滤器作为 Servlet 容器的和 Spring 的 `ApplicationContext` 之间的桥梁。`DelegatignFilterProxy` 实现了 Servlet 的 Filter 接口，在 `doFilter` 方法中将请求代理给 `ApplicationContext` 容器中的 `FilterChainProxy` 对象处理。`FilterChainProxy` 是 Spring 提供的一个由容器管理的特殊的 `Filter`，容器在初始化时创建名为 `springSecurityFilterChain` 的 `FilterChainProxy`。

![FilterChain](../../resources/security_chain.png)

```java
DelegatingFilterProxy#initBean
    
   WebSecurityConfiguration#springSecurityFilterChain
```

### AutoConfiguration

Spring Security 使用`@EnableWebSecurity` 启动，该注解向容器中注入自动配置类 `WebSecurityConfiguration` 用于创建过滤器链 (SecurityFilterChain) 并完成安全配置工作。

```java
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
@Import({ WebSecurityConfiguration.class,
		SpringWebMvcImportSelector.class,
		OAuth2ImportSelector.class })
@EnableGlobalAuthentication
@Configuration
public @interface EnableWebSecurity {

	boolean debug() default false;
}
```



Spring 上下文会调用 `WebSecurityConfiguration#setFilterChainProxySecurityConfigurer` 方法对 WebSecurity 进行初始化，然后利用 WebSecurity 在 `springSecurityFilterChain` 方法中创建

SpringSecurityFilterChain 的初始化过程中会调用

`ExceptionTranslationFilter` 用于处理过滤器链抛出的 `AccessDeniedException` 和 `AuthenticationException`, 如果检测到抛出的一场是 `AccessDeniedException` 则会启动 `AuthenticationEntryPoint`, 如果检测到抛出的异常为 `AccessDeniedException` 则会判断是否是匿名用户,如果启动 `AuthenticationEntryPoint` 否则会采用 `AccessDeniedHandler`来处理.

`ExceptionTranslationFilter` 为 Java 异常和 HTTP 响应之间建立起了关联

### Authentication

`Authentication` 是验证试图访问特定资源的用户的身份的方法，一旦用户身份得以认证就可以根据身份执行授权。

Spring Security 身份认证包含拦截请求的 `Filter` 和处理身份认证的 `AuthenticationManager`两部分。Spring Security 提供了大量的 `Filter` 组件用于过滤请求。

- `CorsFilter`
- `CsrfFilter`
- `OAuth2LoginAuthenticationFilter`
- `BasicAuthenticationFilter`
- ...

Spring Security 处理包含大量的 `Filter` 组件外，还提供了一套用于不同机制的身份认证架构，包含几个核心组件：

- `AuthenticationManager`：认证的管理类，所有需要认证的请求都是通过 AuthenticationManager 的 authenticate 方法完成认证，并根据认证的结果调用具体的 Handler 来处理
- `AuthenticationProvider`：具体实现请求的认证，一个 provider 是一种认证方式的实现，Spring Security 提供了多种认证方式
- `Authentication`：表示由 AuthenticationManager 的 authenticate 方法完成认证后的认证请求或者已认证的主体的令牌。一旦身份认证完成之后就会将 Authentication 存储在当前认证机制使用的 SecurityContextHolder 管理的 Thread-Local 的 SecurityContext 中。除非 Authentication 的 authenticated 属性设置为 true，否则后续的安全相关的拦截器会再次认证
- `AuthenticationEntryPoint`：


### Authorization

Authentication 对象中保存了一个 GrantedAuthority 列表，表示请求主体已经获取的授权。GrantedAuthority 对象通过 AuthenticationManager 插入到 Authentication 对象中，并且由 AccessDecisionManager 在做出授权决策时读取。

Spring Security提供了拦截器，用于控制对安全对象（如方法调用或web请求）的访问。AccessDecisionManager将在调用前决定是否允许继续调用。


### JWT

### OAuth




https://www.jianshu.com/nb/29347401


https://blog.csdn.net/u012702547/article/details/89629415