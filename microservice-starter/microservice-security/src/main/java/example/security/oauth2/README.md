## OAuth2
OAuth 是一个开放标准，允许用户让第三方应用访问该用户在另一应用上存储的私密信息，

### OAuth 角色

### OAuth 认证流程



https://www.hangge.com/blog/cache/detail_2683.html



Spring Security 支持 OAuth2 的所有授权方式：
- Authorization Code
- Refresh Token
- Client Credentials
- Resource Owner Password Credentials

## 核心接口
- `ClientRegistration`：表示通过 OAuth2.0 注册的客户端，保存了 client_id, client_secret, grant_type, redirect_url, token_url 等信息
- `ClientRegistrationRepository`：表示 `ClientRegistration` 的存储，通常是从授权服务器获取的  `ClientRegistration` 信息，默认使用 `InMemoryClientRegistrationRepository`
- `OAuth2AuthorizedClient`：表示已经获得授权的客户端，当用户(资源所有者)对客户端访问受保护的资源授权时，客户端认为已授权
- `OAuth2AuthorizedClientRepository/OAuth2AuthorizedClientService`：负责管理和持久化已经获取授权的客户端，通过这两个类可以获取客户端的 `OAuth2AccessToken`，默认使用 `InMemoryOAuth2AuthorizedClientService`
- `OAuth2AuthorizedClientManager/OAuth2AuthorizedClientProvider`：负责 `OAuth2AuthorizedClient` 的总体管理，其职能包括
  - 使用 `OAuth2AuthorizedClientProvider` 对 OAuth2 客户端进行授权
  - 委派 `OAuth2AuthorizedClientRepository/OAuth2AuthorizedClientService` 持久化 `OAuth2AuthorizedClient`
  - OAuth2 客户端授权成功后委派 `OAuth2AuthorizationSuccessHandler` 进行处理
  - OAuth2 客户端授权成功后委派 `OAuth2AuthorizationFailureHandler` 进行处理
`OAuth2AuthorizedClientManager` 的默认实现是 `DefaultOAuth2AuthorizedClientManager`，它和 `OAuth2AuthorizedClientProvider` 关联，提供使用基于委托的组合来支持多种授权类型，`OAuth2AuthorizedClientProviderBuilder` 可以用来配置和构建基于委托的组合。

