## Spring Security

### Authentication

### Authorization

### OAuth2

#### 授权码模式

安全性较高，通常用于用户登陆

- 用户访问客户端，客户端将用户重定向到认证服务，需要携带 redirect_url 表示认证服务需要重定向的地址
- 用户确认授权后，认证服务会将用户重定向到 redirect_url 并携带认证服务返回的授权码 code
- 客户端在重定向的请求中向认证服务请求 token，需要携带 client_id, client_secret, code 和 redirect_url
- 认证服务校验后返回 access token 和 refresh token，客户端认证成功

```
# 客户端重定向
https://b.com/oauth/auth?
	response_type=code&
	client_id=CLIENT_ID&
	redirect_uri=CALLBACK_URL&
	client_id=CLIENT_ID
	
# 认证服务重定向
https://a.com/CALLBACK_URL?code=AUTH_CODE

# 客户端发送认证请求
https://b.com/oauth?token?
	client_id=CLIENT_ID&
	client_secret=CLIENT_SECRET&
	grant_type=authorization_code&
	code=AUTH_CODE&
	redirect_uri=REDIRECT_URL
```



#### 简化模式

没有授权码，直接向客户端返回 token

- 用户访问客户端，客户端将用户重定向到认证服务，需要携带 redirect_url
- 用户确认授权后，将用户重定向到 redirect_url 并且携带 token，客户端认证成功

```
# 客户端请求
https://b.com/oauth/authorize?
	response_type=token&
	client_id=CLIENT_ID&
	redirect_uri=CALLBACK_URL&
	scope=read
	
# 认证服务重定向
https://a.com/CALLBACK_URL#token=ACCESS_TOKEN
```



#### 客户端凭证

用于第三方系统集成，直接请求令牌

- 客户端向认证服务发送获取 token 请求，认证服务校验后返回 token

```
https://oauth.b.com/token?
	grant_type=client_credentials&
	client_id=CLIENT_ID&
	client_secret=CLIENT_SECRET
```



#### 密码模式

使用用户名密码到认证服务获取授权，会泄漏用户名密码

- 客户端需要用户填写认证服务的用户名和密码，并作为参数向认证服务发起请求
- 认证服务校验后返回 token，客户端认证成功

```
https://oauth.b.com/token?
	grant_type=password&
	username=USERNAME&
	password=PASSWORD&
	client_id=CLIENT_ID
```

#### SpringSecurity Auth2

- `AuthorizationEndPoint`：服务认证请求端点，默认 `/oauth/authorize`
- `TokenEndpoint`：令牌请求端点，默认 `/oauth/token`
- `OAuth2AuthenticationProcessingFilter`：对请求给出的 token 进行解析鉴权

### JWT

