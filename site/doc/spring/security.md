## Spring Security

Spring Security 使用标准的 Servlet Filter 与 Servlet 容器集成，也就是说 Spring Security 可以和任何运行在 Servlet 容器中的应用程序一起工作。

### FilterChain

Spring 提供了 `DelegatingFilterProxy` 作为 Servlet 容器的和 Spring 的 `ApplicationContext` 之间的桥梁。`DelegatignFilterProxy` 实现了 Servlet 的 `Filter` 接口，在 `doFilter` 方法中将请求代理给  `FilterChainProxy` 处理，`FilterChainProxy` 是 Spring 提供的一个由容器管理的特殊的 `Filter`，容器在初始化时创建名为 `springSecurityFilterChain` 的 `FilterChainProxy`。

![FilterChain](../../resources/security_chain.png)

```java
DelegatingFilterProxy#initBean
    
   WebSecurityConfiguration#springSecurityFilterChain
```

#### Filter

`Filter` 通过  `SecurityFilterChain` 插入到  `FilterChainProxy` 中，`Filter` 插入的顺序决定了拦截请求的顺序。  `Spring Security` 提供了大量的 `Filter` 组件用于过滤请求：

- `CorsFilter`
- `CsrfFilter`
- `OAuth2LoginAuthenticationFilter`
- `UsernamePasswordAuthenticationFilter`
- `BasicAuthenticationFilter`
- `ExceptionTranslationFilter`
- ...

#### `ExceptionTranslationFilter`

`ExceptionTranslationFilter`  是插入到 `FilterChainProxy` 的一种过滤器，可以将后级过滤器或者应用程序抛出的 `AccessDeniedException` 和 `AuthenticationException`  翻译成 HTTP 响应。

![todo]()

在 `ExceptionTranslationFilter#doFilter` 方法中，整个处理逻辑为：

- 直接执行 `chain.doFilter(request, response)` 执行后续的处理
- 如果后续处理抛出 `AuthenticationException` 则会调用 `sendStartAuthentication` 方法处理异常
- 如果后续处理抛出 `AccessDeniedException`，则会判断是否是 `Anonymous` 或者 `RememberMe`，如果是则调用 `sendStartAuthentication`  方法处理，否则调用 `accessDeniedHandler#handle` 处理

`sendAuthentication` 方法处理认证失败的异常，处理过程包含三步：

- 将 `SecurityContext` 中保存的 `Authentication` 清除，因为发生认证异常说明当前的 `Authentication` 失效
- 缓存 `HttpServletRequest` 和 `HttpServletResponse` 到 `RequestCache` 中，因为当后续认证成功后需要从缓存中获取原始的请求和响应
- 调用 `AuthenticationEntryPoint` 从客户端获取用户认证凭证，例如重定向到登录页面

`accessDeniedHandler#handle` 方法处理权限不足的异常，

todo

> **Tips**
>
> 如果后续处理没有抛出异常，或者抛出的异常不是 `AuthenticationException` 或者 `AccessDeniedException` 则 `ExceptionTranslationFilter` 不会有任何的处理。

### AutoConfiguration

Spring Security 使用 `@EnableWebSecurity` 实现自动配置，该注解向容器中注入自动配置类 `WebSecurityConfiguration` 。



Spring Security 使用`@EnableWebSecurity` 启动，用于创建过滤器链 (SecurityFilterChain) 并完成安全配置工作。

Spring 上下文会调用 `WebSecurityConfiguration#setFilterChainProxySecurityConfigurer` 方法对 WebSecurity 进行初始化，然后利用 WebSecurity 在 `springSecurityFilterChain` 方法中创建

#### Spring Boot

Spring Boot 在引入`spring-security-starter` 后会自动配置 Spring Security，核心配置为 `SecurityFilterAutoConfiguration`。



### Authentication

`Authentication` 是验证试图访问特定资源的用户的身份的方法，一旦用户身份得以认证就可以根据身份执行授权。

在  Spring Security 中，请求经过需要认证的 `Filter` 时会触发认证流程，Spirng Security 提供了用于不同机制的身份认证框架，包含几个核心组件：

- `SecurityContext`：从 `SecurityContextHolder` 获取，保存了当前已认证的用户的身份信息
- `Authentication`：用户的认证信息，可以传入 `AuthenticationManager` 用于认证

- `AuthenticationManager`：认证的管理类，所有需要认证的请求都是通过 `AuthenticationManager` 完成认证，并根据认证的结果调用具体的 Handler 来处理
- `AuthenticationProvider`：请求的认证的具体实现，由 `ProviderManager` 调用

#### `SecurityContext`

#### `Authentication`

表示由 AuthenticationManager 的 authenticate 方法完成认证后的认证请求或者已认证的主体的令牌。一旦身份认证完成之后就会将 Authentication 存储在当前认证机制使用的 SecurityContextHolder 管理的 Thread-Local 的 SecurityContext 中。除非 Authentication 的 authenticated 属性设置为 true，否则后续的安全相关的拦截器会再次认证

#### `AuthenticationManager`

`ProviderManager` 是 `AuthenticationManager` 的一个实现类

#### `AuthenticaionEntryPoint`

`AuthenticationEntryPoint` 用于发送向客户端请求认证凭证的 HTTP 响应。

#### `AuthenticationProvider`


### Authorization

Authentication 对象中保存了一个 GrantedAuthority 列表，表示请求主体已经获取的授权。GrantedAuthority 对象通过 AuthenticationManager 插入到 Authentication 对象中，并且由 AccessDecisionManager 在做出授权决策时读取。

Spring Security提供了拦截器，用于控制对安全对象（如方法调用或web请求）的访问。AccessDecisionManager将在调用前决定是否允许继续调用。


### JWT

### OAuth




https://www.jianshu.com/nb/29347401


https://blog.csdn.net/u012702547/article/details/89629415