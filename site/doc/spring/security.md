## Spring Security

### Servlet

Spring Security 对 Servlet 的支持是基于 Servlet 的 Filter 机制，Spring Security 提供了一个名为 `DelegatingFilterProxy` 的过滤器用于在 Servlet 容器的生命周期和 Spring 的 ApplicationContext 之间建立桥梁。DelegatignFilterProxy 通过 Servlet 容器机制注册，但是将所有工作委托给实现 Filter 的 Spring Bean。


### Authentication

Authentication 是验证用户身份的合法性，Spring Security 提供了多个组件用于认证：
- AuthenticationManager：认证的管理类，所有需要认证的请求都是通过 AuthenticationManager 的 authenticate 方法完成认证，并根据认证的结果调用具体的 Handler 来处理
- AuthenticationProvider：具体实现请求的认证，一个 provider 是一种认证方式的实现，Spring Security 提供了多种认证方式
- Authentication：表示由 AuthenticationManager 的 authenticate 方法完成认证后的认证请求或者已认证的主体的令牌。一旦身份认证完成之后就会将 Authentication 存储在当前认证机制使用的 SecurityContextHolder 管理的 Thread-Local 的 SecurityContext 中。除非 Authentication 的 authenticated 属性设置为 true，否则后续的安全相关的拦截器会再次认证

#### 用户密码认证机制

表单认证


基本认证


### WebFlux


### JWT

### OAuth