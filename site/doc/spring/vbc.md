## 数据绑定

## 类型转换

### PropertyEditor

### Converter

### Formatter
`core.convert` 包定义了一个通用的类型转换系统，提供了一个统一的 `ConversionService` API 用于实现从一个类型转换到另一个类型，Spring 容器使用这个系统来绑定 bean 的属性，Spring 的表达式语言(SpEL) 和 `DataBinder` 都是使用这个系统来绑定字段值。
## 数据校验

Spring 提供了 Validator 框架用于参数的校验，它可以使得参数的校验可以在应用的每一层，并且可以和任何的 validator 插件组合。

Spring 的参数校验主要由 `Validator` 和 `DataBinder` 构成，

## 自动配置

SpringBoot 自动配置文件 `spring.factories` 中配置 Spring Boot 应用启动时加载的 Web 自动配置类 `WebMvcAutoConfiguration`，类上的注解 `@AutoConfigureAfter` 表明在配置该类之前需要配置 `ValidationAutoConfiguration`。



```flow
st=>start: Start
op=>operation: Your OPeration
cond=>condition: Yes or No?
e=>end
st->op->cond
cond(yes)->e
cond(no)->op
```

```sequence
TiTle:时序图示例
客户端->服务端:我想访问你 SYN
服务端->客户端:我收到请求，开始通信吧 ACK+SYN
客户端->服务端:我收到你的确认啦 ACK
客户端-->服务端:虚线实心箭头
服务端->>客户端:实线小箭头
客户端-->>服务端:虚线小箭头
Note right of 服务端:我是一个服务端
Note left of 客户端:我是一个客户端
Note over 服务端,客户端: TCP 三次握手
participant 观察者
```

https://blog.csdn.net/kl28978113/article/details/93617103



https://segmentfault.com/a/1190000006247465