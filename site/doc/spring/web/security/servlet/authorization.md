## Authorization

授权(`Authorization`)



`Authentication` 对象中保存了 `GrantedAuthority` 列表，表示请求主体已经获取的授权。`GrantedAuthority` 对象通过 `AuthenticationManager` 插入到 `Authentication` 对象中，并且由 `AccessDecisionManager` 在做出授权决策时读取。



Spring Security提供了拦截器，用于控制对安全对象（如方法调用或web请求）的访问。AccessDecisionManager将在调用前决定是否允许继续调用。

#### AccessDecisionManager

`AccessDecisionManager` 负责权限控制，由 `AbstractSecurityInterceptor` 在访问被保护的方法之前调用，决定被保护的方法是否允许调用。

`AccessDecisionManager` 接口定义了三个方法，其中 `decide` 方法根据传入的参数决定是否授权，参数 `object ` 表示需要被授权检查的方法，如果拒绝访问就会抛出 `AccessDeniedException`

#### Vote-Based AccessDecisionManager

自定义 `AccessDecisionManager` 的实现类可以实现不同的授权机制，Spring Security 提供了一些基于投票(Vote) 机制的 `AccessDecisionManager` 实现类：

- `AffirmativeBased`：只要收到一个 `ACCESS_GRANTED` 的投票则会授予访问权限，如果全部都是 `ACCESS_ABSTAIN`  则会根据参数 `allowIfAllAbstainDecisions` 来确定是否授予权限，默认为 `false` 
- `ConsensusBased`：如果收到的 `ACCESS_GRANTED` 投票数大于等于 `ACCESS_DENIED` 投票数则会授予权限，如果全部都是 `ACCESS_ABSTAIN`  则会根据参数 `allowIfAllAbstainDecisions` 来确定
- `UnanimousBased`：只要收到一个 `ACCESS_DENIED` 投票则不予授权，如果全部为 `ACCESS_ABSTAIN` 则根据参数 `allowIfAllAbstainDecisions` 来确定

`Vote-Based` 机制的授权机制包括一系列的 `AccessDecisionVoter` 实现类，`AccessDecisionManager` 根据这些 `AccessDecisionVoter` 的投票结果来决定是否抛出 `AccessDeniedException`。

`AccessDecisionVoter` 接口定义了三个方法，其中 `vote` 方法表示进行投票，方法返回值是 `int` 类型，取值是 `AccessDecisionVoter` 的静态属性 `ACCESS_ABSTAIN`、`ACCESS_DENIED` 、`ACCESS_GRANTED` 之一。当表决器不能做出决定时返回 `ACCESS_ABSTAIN`，否则返回 `ACCESS_DENIED`  或者 `ACCESS_GRANTED` 。

#### AccessDecisionVoter

`AccessDecisionVoter` 接口是授权时的投票器，`AccessDecisionManager` 根据这些投票的结果确定是否授予权限。

实现 `AccessDecisionVoter` 接口可以自定义投票器，Spring Security 内置了两种常见的投票器用于实现特定的投票机制：

- `RoleVoter`：将传入的 `ConfigAttribute` 参数作为角色名，然后根据当前用户的角色投票。如果 `GrantedAuthority` 返回的 字符串对象是以 `ROLE_` 开头并且和 `ConfigAttribute` 参数相匹配则返回 `ACCESS_GRANTED`，如果不匹配则返回 `ACCESS_DENIED`，如果 `ConfigAttribute` 参数中没有以 `ROLE_` 开头的则返回 `ACCESS_ABSTAIN`
- `AuthenticatedVoter`：用于区分匿名的、完全认证的和 remember me 认证的用户。如果使用 `IS_AUTHENTICATED_ANONYMOUSLY`属性来授予匿名用户权限，则这个属性就会由 `AuthenticatedVoter` 处理

#### AfterInvocationManager

`AccessDecisionManager` 由 `AbstractSecuriyInterceptor` 在调用被保护的方法之前调用，当被保护的方法被调用完成之后 `AbstractSecurityInterceptor` 还可以调用 `AfterInvocationMananger` 来修改方法调用的结果。

Spring Security 提供了 `AfterInvocationManager` 的实现类 `AfterInvocationProviderManager`，该类包含一个 `AfterInvocationProvider` 列表。每个 `AfterInvocationProvider` 都可以修改方法调用的结果，最终的修改结果作为方法调用的结果返回。
