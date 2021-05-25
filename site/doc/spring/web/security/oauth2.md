## OAuth2

OAuth2(开放授权) 是一个开放标准，该标准允许用户让第三方应用访问该用户在某一网站上存储的私密资源，在这个过程中无需将用户名和密码提供给第三方应用。

OAuth2 使用令牌机制，在获取授权后网站会生成一个令牌 (token) 给第三方应用用于获取资源，令牌是有过期时间的，对于特定的令牌只能访问特定的资源。

OAuth2 标准定义了几个角色：

- 客户端(Client)：表示试图访问受限资源的第三方应用，在实现访问之前需要资源拥有者授权并通过认证服务器认证
- 资源所有者(Resource Owner)：授权第三方应用，一般指的是用户
- 授权服务器(Authorization Server)：授权服务器验证客户端返回的授权信息，验证通过后返回令牌给客户端
- 资源服务器(Resource Server)：资源服务器包含受限的资源，在验证令牌通过后返回资源给客户端

### 授权模式
OAuth2 定义了四种授权模式：
- 授权码(`Authorization Code`)模式：
- 简化(`Refresh Token`)模式
- 客户端凭证(`Client Credentials`)
- 密码模式(`Resource Owner Password Credentials`)

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