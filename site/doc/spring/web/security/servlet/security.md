## Spring Security

Spring Security 使用标准的 Servlet Filter 与 Servlet 容器集成，也就是说 Spring Security 可以和任何运行在 Servlet 容器中的应用程序一起工作。

### 过滤器链

Spring 提供了 `DelegatingFilterProxy` 作为 Servlet 容器的和 Spring 的 `ApplicationContext` 之间的桥梁。`DelegatignFilterProxy` 实现了 Servlet 的 `Filter` 接口，在 `doFilter` 方法中将请求代理给  `FilterChainProxy` 处理，`FilterChainProxy` 是 Spring 提供的一个由容器管理的特殊的 `Filter`，容器在初始化时创建名为 `springSecurityFilterChain` 的 `FilterChainProxy`。

![FilterChain](../../resources/security_chain.png)

`Filter` 通过  `SecurityFilterChain` 插入到  `FilterChainProxy` 中，`Filter` 插入的顺序决定了拦截请求的顺序。  `Spring Security` 提供了大量的 `Filter` 组件用于过滤请求：

- `CorsFilter`
- `CsrfFilter`
- `OAuth2LoginAuthenticationFilter`
- `UsernamePasswordAuthenticationFilter`
- `BasicAuthenticationFilter`
- `ExceptionTranslationFilter`
- ...



#### `ExceptionTranslationFilter`

`ExceptionTranslationFilter`  是插入到 `FilterChainProxy` 的一种过滤器，可以将后级过滤器或者应用程序抛出的 `AccessDeniedException` 和 `AuthenticationException`  翻译成 HTTP 响应。其处理逻辑为：

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

#### `AbstractAuthenticationProcessingFilter`

`AbstractAuthenticationProcessingFilter` 是用户认证过滤器的基类，用户的凭证一般是通过 `AuthenticationEntryPoint` 获得，然后 `AbstractAuthenticationProcessingFilter` 就会验证所有提交的认证请求：

- 用户提交携带凭证的请求时，`AbstractAuthenticationProcessingFilter` 根据待认证的请求创建 `Authentication`，具体类型取决于 `AbstractAuthenticationProcessingFilter` 的实现类，例如 `UsernamePasswordAuthenticatioinFilter` 创建的是从请求中获取的用户名和密码组成的 `UsernamePasswordAuthenticationToken`
- 创建的 `Authentication` 会被传入 `AuthenticationManager` 用于认证
- 如果认证失败则会清空 `SecurityContextHolder`，调用 `RememberMeService.loginFail` 方法(如果配置了)，调用 `AuthenticationFailureHandler` 处理器
- 如果认证成功则

#### `FilterSecurityInterceptor`

`FilterSecurityInterceptor` 是 `FilterChain` 中的一员，用于对 `HttpServletRequest` 进行授权，其处理流程为：

- 从 `SecurityContxtHolder` 中获取 `Authentication`，并且根据传入的 `HttpServletRequest`、`HttpServletResponse` 和 `FilterChain` 构造 `FilterInvocation`
- 将 `FilterInvocation` 传入 `SecurityMetadataSource` 以获得 `ConfigAttribute`
- 将 `Authentication`、`FilterInvocation` 和 `ConfigAttribute` 传入 `AccessDecisionManager` 用于授权决策
  - 如果拒绝授权，则会跑出 `AccessDeniedException`，此时 `ExceptionTranslationFilter` 会处理抛出的异常
  - 如果授权通过，`FilterSecurityInterceptor` 会完成处理使得 `FilterChain` 能够继续执行后续处理

默认情况下，Spirng Security 的所有授权都需要事先完成认证，如果想要显式的配置则需要在 `WebSecurityConfigAdapter#configure` 中配置：

```java
protected void configure(HttpSecurity http) throws Exception {
    http
        // ...
        .authorizeRequests(authorize -> authorize
            // 所有请求都必须认证
            .anyRequest().authenticated()
        );
}
```

#### 自定义过滤器


http://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html



https://www.hangge.com/blog/cache/detail_2683.html



https://www.cnblogs.com/bug9/p/11584816.html

