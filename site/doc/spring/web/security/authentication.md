## 认证

认证(`Authentication`) 是验证试图访问特定资源的用户的身份的方法，一旦用户身份得以认证就可以根据身份执行授权。

在  Spring Security 中，请求经过需要认证的 `Filter` 时会触发认证流程，Spirng Security 提供了用于不同机制的身份认证框架，包含几个核心组件：

- `SecurityContext`：从 `SecurityContextHolder` 获取，保存了当前已认证的用户的身份信息
- `Authentication`：用户的认证信息，可以传入 `AuthenticationManager` 用于认证

- `AuthenticationManager`：认证的管理类，所有需要认证的请求都是通过 `AuthenticationManager` 完成认证，并根据认证的结果调用具体的 Handler 来处理
- `AuthenticationProvider`：请求认证的具体实现，每一种 `AuthenticationProvider` 对应一种认证方式的实现

#### `SecurityContext`

`SecurityContext` 由 `SecurityContextHolder` 维护，包含了用户凭据 Authentication 对象。

![security context](../../resources/security_context.png)

Spring Security 并不关心 `SecurityContext` 如何处理用户凭证对象，只要是包含该对象则认为将其认为当前已经认证的用户。基于 Spring Security 的这种特性，可以直接向其中设置 `Authentication` 对象完成用户认证从而绕过 Spring Security 的认证体系：

```java
// 创建一个新的 SecurityContext 而不是使用原有的，避免多线程安全问题
SecurityContext context = SecurityContextHolder.createEmptyContext();
Authentication authentication = 
    new TestingAuthenticationToken("username", "password", "ROLE_USER");
context.setAuthentication(authentication);
SecurityContextHolder.setContext(context);
```

默认情况下 `SecurityContextHolder` 使用 `ThreadLocal` 策略来存储 `SecurityContext`，这意味着在相同线程中的任何方法都可以安全的获取 `SecurityContext` 并且在请求处理完成之后清除。

除了 `ThreadLocal` 策略存储 `SecurityContext`，`SecurityContextHolder` 还提拱了多种存储策略，包括：

- `MODE_THREADLOCAL`：ThreadLocal 策略
- `MODE_INHERITABLETHREADLOCAL`：线程和其派生的线程共享 `SecurityContext`
- `MODE_GLOBAL`：所有的线程共享 `SecurityContext`

通过实现 `SecurityContextHolderStrategy` 接口可以自定义 `SecurityContext` 存储策略，指定存储策略有两种方式：

- 启动应用时添加参数 `-Dspring.security.strategy=MODE_GLOBAL` 或者设置系统属性 `spring.security.strategy=MODE_GLOBAL`
- 调用 `SecurityContextHolder#setStrategyName` 指定策略类名(自定义策略)或者策略名(Spring Security 提供)

```java
// 使用参数
java -jar -Dspring.security.strategy=MODE_GLOBAL application.jar

// 使用代码
SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_GLOBAL);
```



#### `Authentication`

Spring Security 中 `Authentication` 有两个主要的作用：

-  作为 `AuthenticationManager`  的输入提供用于认证的凭证，此时 `isAuthenticated()` 方法返回 `false`
- 表示已经经过认证的用户，可以从 `SecurityContext` 中获取到用户认证信息，此时 `isAuthenticated()` 方法返回 `true`



`Authentication` 中包含几个核心属性：

- `principal`：表示请求认证的用户，在使用用户名/密码进行身份认证时通常是一个 `UserDetail` 实例

- `credentials`：表示认证的凭据，通常是密码，一般在用户通过认证之后就会被清除以免造成泄露

- `authorities`：`GrantedAuthority` 的集合，表示用户被授予的权限。这些权限通常是“角色”，如 `ROLE_ADMINISTRATOR` 或者 `ROLE_HR_SUPERVISOR`，这些角色会用于后续的方法授权、领域对象授权等。一般情况下 `GrantedAuthority` 表示应用程序范围内的权限，而不是表示具体领域对象的权限，在需要控制具体领域对象的权限时应该使用项目具体的权限控制。

  

`Authentication` 接口有一些实现类，各自应用与不同的场景：

- `TestingAuthenticationToken`

- `UsernamePasswordAuthenticationToken`



#### `AuthenticationManager`

`AuthenticationManager` 定义了 Spring Security 的过滤器如何执行身份认证。Spring Security 的过滤器通过 `AuthenticationManager` 完成身份认证后会将认证信息 `Authentication` 存放在 `SecurityContext` 中。

`ProviderManager` 是 `AuthenticationManager` 的一个常用的实现类，`ProviderManager` 管理一个 `AuthenticationProvider` 列表，每个 `AuthenticationProvider` 都可以认证自己支持的身份认证机制或者交由下游 `AuthenticationProvider` 进行认证，如果所有配置的 `AuthenticationProvider`  都不能执行认证则表示认证失败并抛出 `ProviderNotFoundException` 的异常，这个异常是一种特殊的 `AuthenticationException` 异常，用于表明 `ProviderManager` 没有配置支持当前的 `Authentication` 。

![ProviderManager](../../resources/provider_manager.png)

默认情况下，`ProviderManager` 会尝试将已经认证的 `Authentication` 中的敏感信息清除，但是这样在使用用户对象缓存的时候可能会导致问题。如果 `Authentication` 中包含了缓存对象的一个引用(比如 `UserDetail` 实例)，如果清除了凭证(credentials)则后续将不可能通过缓存的值进行身份认证。



`ProviderManager` 中可以注入多个 `AuthenticationProvider`，每个 `AuthenticationProvider` 表示一种特殊的认证机制，Spring Security 内置了多种 `AuthenticationProvider` 可以直接使用，也可以通过实现 `AuthenticationProvider` 接口来自定义认证机制：

- `DaoAuthenticationProvider`
- `JwtAuthenticationProvider`
- ...

`DaoAuthenticationProvider` 是 Spring Security 内置的认证实现，它利用 `UserDetailService` 和 `PasswordEncoder` 来验证用户名和密码。`DaoAuthenticationProvider` 的工作流程如下：

- 请求经过 `Filter` 拦截后获取到用户名和密码构造 `UsernamePasswordAuthenticationToken`后作为参数传入 `AuthenticationManager` 用于认证
- `ProviderManager` 使用配置的 `DaoAuthenticationProvider` 来实现具体认证，`DaoAuthenticationProvider` 通过 `UserDetailService` 获取 `UserDetails`
- `DaoAuthenticationProvider` 使用 `PasswordEncoder` 来验证 `Authentication` 中的密码和 `UserDetails` 中的密码是否匹配
- 如果认证成功(密码匹配)，则 `UsernamePasswordAuthenticationToken` 作为 `Authentication` 返回，并且 `UserDetails` 作为 `Authentication` 的 `principal`。然后 `UsernamePasswordAuthenticationToken` 放入 `SecurityContextHolder`



#### `AuthenticaionEntryPoint`

`AuthenticationEntryPoint` 用于发送向客户端请求认证凭证的 HTTP 响应。在一些情况中，请求携带了用户名/密码等凭据来请求资源，此时 Spring Security 不需要提供向客户端请求认证凭据的 HTTP 响应；但是在一些没有携带凭据但是请求需要认证的资源时，`AuthenticationEntryPoint` 就用于向客户端请求认证凭据，此时`AuthenticationEntryPoint` 的实现类通常是重定向到登陆页面或者返回带有 `WWW-Authenticate` 头的响应。

### 认证机制

Spring Security 内置了用户名/密码方式的认证机制，同时提供了与多种认证机制的集成。

#### 用户名/密码

用户名/密码认证是一种常见的认证方式，Spring Security 内置了多个基于用户名/密码认证的机制，包括：

- `Form Login`
- `Basic Authentication`
- `Digest Authentication`
- ...
##### `Form Login`

Spring Security 提供了通过 html 的表单提供用户名和密码进行认证，重定向到登录表单页面一般通过以下流程触发：

- 未经认证的用户请求了需要授权的资源
- `FilterSecurityInterceptor` 鉴定请求未经认证，抛出 `AccessDeniedException`
- 由于请求未经认证，`ExceptionTranslationFilter` 经由 `LoginUrlAuthenticationEntryPoint` 将请求重定向到登录页面
- 浏览器请求重定向的登陆了页面，填写用户名和密码后发起登录请求

携带用户名和密码的登录请求会被 `UsernamePasswordAuthenticationFilter` 处理，`UsernamePasswordAuthenticationFilter` 是 `AbstractAuthenticationProcessingFilter` 的子类，其处理流程如下：

- 从请求中获取用户名和密码创建`Authentication` 的实现类  `UsernamePasswordAuthenticationToken`
- 将 `UsernamePasswordAuthenticationToken` 传入 `AuthenticationManager` 用于认证
- 如果认证失败则
  - 清理 `SecurityContextHolder`
  - 如果配置了 `Remember Me`，则调用 `RememberMeService.loginFail` 方法
  - 调用 `AuthenticationFailureHandler`
- 如果认证成功则
  - 通知 `SessionAuthenticationStrategy`
  - 将 `Authentication` 放入 `SecurityContextHolder`
  - 如果配置了 `Remember Me` ，则调用 `RememberMeService.loginSuccess` 方法
  - `ApplicationEventPublisher` 发布 `InteractiveAuthenticationSuccessEvent`

##### `Basic` 

`Basic` 认证是一种比较简单的认证方式，客户端通过明文(Base64 编码)传输用户名和密码到服务端进行认证，通常需要和 HTTP 一起来使用保证信息传输安全。

Spring Security 通过 `BasicAuthenticationFilter` 实现了 `Basic` 认证，其认证流程为：

- 当携带用户名和密码的请求经过 `BasicAuthenticationFilter` 时，请求头中的用户名和密码会被获取用于创建 `Authentication` 的实现类 `UsernamPasswordAuthenticationToken`
- `UsernamePasswordAuthenticationToken` 被传入 `AuthenticationManager` 用于认证
- 如果认证失败则
  - 清理 `SecurityContextHolder`
  - 如果设置了 `remember me` 则调用 `RememberMeService.loginFail`
  - `BasicAuthenticationEntryPoint`  返回带有 `WWW-Authenticate` 头的响应，使客户端再次发送请求
- 如果认证成功则
  - 将 `Authentication` 放入 `SecurityContextHolder`
  - 如果设置了 `remember me` 则调用 `RememberMeService.loginSuccess`
  - 调用 `FilterChain.doFilter(request, response)` 方法处理后续的逻辑

Spring Security 默认开启了 `Basic` 认证，但是一旦提供了任何基于 Servlet 的配置，就必须显式地提供 `Basic` 认证。

```java

```