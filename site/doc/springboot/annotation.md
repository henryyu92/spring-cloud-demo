## 自动配置注解

### 条件注入
SpringBoot 扩展了 Spring 的 `@Conditional` 注解并在此基础上实现了多种条件注入容器的实现。
- `ConditionalOnClass`
- `ConditionalOnMissingBean`
- `ConditionalOnResource`
- `AutoConfigureOrder`
- `AutoConfigureAfter`