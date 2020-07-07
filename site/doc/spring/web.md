## Web

- [MVC](#MVC)
  - [DispatcherServlet](#DispatcherServlet)
  - [HandlerMapping](#HandlerMapping)
  - [HandlerAdapter](#HandlerAdapter)
  - [HandlerMethodArgumentResolver](#HandlerMethodArgumentResolver)
  - [HandlerMethodReturnValueHandler](#HandlerMethodReturnValueHandler)
  - [HttpMessageConverter](#HttpMessageConverter)
- [WebSocket](#WebSocket)
- [WebFlux](#WebFlux)

## MVC

Spring MVC 是基于 Servlet 构建的 Web 框架。

![Web MVC 执行流程](../../resources/mvc.png)

### DispatcherServlet

Spring MVC 和大多数的 Web 框架一样是围绕着前端控制器模式来设计，也就是一个中央 Servlet(DispatcherServlet) 为请求提供一个共享算法，而实际的工作是由可配置的组件完成，这种模式具有很大的灵活性并且支持多种工作流。

DispatcherServlet 将请求委托给特殊的 bean 来处理并响应，这些 bean 由 Spring 管理并提供了默认实现，沟通自定义这些 bean 可以扩展或者替换它们：
- HandlerMapping：将请求映射到 handler 和 拦截器列表用于预处理和后处理，映射的条件取决于不同的实现类。Spring 提供了两种实现类，其中 `RequestMappingHandlerMapping` 支持 @RequestMapping 注解方式映射，而 `SimpleUrlHandlerMapping` 维护显示注册的 url 映射
- HandlerApdater：帮助 DispatcherServlet 调用映射到请求的处理程序，而不管程序实际是如何调用的
- HandlerExceptionResolver：解析应用异常的策略
- ViewResolver：将从处理程序返回的基于逻辑字符串的视图名称解析为要呈现给响应的实际视图
- LocalResolver & LocalContextResolver
- MultipartResolver
- FlashMapManager




https://www.jianshu.com/p/8a20c547e245

### HandlerMapping

HandlerMapping 负责映射 URL 和对应的处理类，Spring 提供了 `BeanNameUrlHandlerMapping` 和 `RequestMappingHandlerMapping` 两个实现类，如果没有指定的话 Spring MVC 会默认使用 `BeanNameUrlHandlerMapping` 作为实现。

`HandlerMapping` 接口定义了 `getHandler` 方法接收 `HttpServletRequest` 并返回 `HandlerExecutionChain`,方法的返回值中包含了该请求对应的处理类.

请求的处理类被包装在 `HandlerMapping` 中，可以和 `HandlerInterceptor` 一起使用，`DispatcherServlet` 将首先按给定的顺序调用每个 `HandlerInterceptor` 的 `CodePreHandle` 方法，如果返回 true 则调用处理类本身。

可以向 `DispatcherServlet` 提供多个 `HandlerMapping`，通过实现 `Ordered` 接口可以指定 `HandlerMapping` 的优先级，从而使得其可以按照优先级的顺序来处理请求和处理类的映射。

```java
```

https://blog.csdn.net/qq_38410730/article/details/79507465

### HandlerAdapter

请求经过 `HandlerMapping` 处理后得到处理请求的 `HandlerExecutionChain`，不同的 `HandlerMapping` 映射的 `HandlerExecutionHandlerChain` 中的处理类是不同的，Spring 利用适配器模式通过 `HandlerAdapter` 接口将不同的处理类以统一的方式来处理请求。

`HandlerAdapter` 接口定义了三个方法用于适配不同的请求处理链,实现类通过实现接口并重写这三个接口方法实现自定义的适配器.`supports` 方法表示当前的适配器是否支持传入的 handler,`handle` 方法用于适配处理类,`getLastModified` 方法表示上次修改的时间.

```java

```

`RequestMappingHandlerAdapter` 是 Spring 中使用的最多的 `HandlerMapping` 实现类,该实现类的 `supports` 只支持 `HandlerMethod` 类型的处理类,而这个处理类是 `@RequestMapping` 注解的映射处理类.
```java
```

`RequestMappingHandlerAdapter` 实现了 `InitializingBean` 接口,在重写方法 `afterPropertiesSet` 中初始化了 `HandlerMethodArgumentResolver` 和 `HandlerMethodReturnValueHandler` 这两个组件,这两个组件用于处理请求参数和请求返回的结果.
```java
```

`RequestMappingHandlerAdapter` 重载的 `handleInternal` 方法

```java
```

https://www.jianshu.com/p/1ccd4b326cff

https://www.jianshu.com/p/23ad68d8b421


### HandlerMethodArgumentResolver

`HandlerMethodArgumentResolver` 接口将 `HttpServletRequest` 中的请求参数转换为处理类方法的参数.

`HandlerMethodArgumentResolver` 接口定义了 2 个方法用于将请求参数转换为处理类方法参数,通过实现该接口并重写这 2 个方法可以实现自定义的请求参数处理.
- `supportsParameter`:判断当前的 resolver 是否支持这个参数
- `resolveArgument`:用于解析支持的参数到指定的参数类型

```java
```
Spring 提供了默认的 `HandlerMethodArgumentResolver` 的实现类,不同的实现类可以支持将请求参数转换成不同类型的参数:
- RequestParamMapMethodArgumentResolver 用于处理 @RequestParam 注解的参数
- RequestHeaderMapMethodArgumentResolver 用于处理 @RequestHeader 注解的参数
- PathVariableMapMethodArgumentResolver 用于处理 @PathVariable 注解的参数
- ModelAttributeMethodProcessor 用于处理 @ModelAttribute 注解的参数
- RequestResponseBodyMethodProcessor 用于处理 @RequestBody 注解的参数

### HandlerMethodReturnValueHandler


### HttpMessageConverter

`HttpMessageConverter` 接口是 Spring MVC 中用于将 HTTP 中的字节流数据和应用程序中的对象进行转换.

Spring 提供了常用的数据格式的转换:
- ProtobufHttpMessageConverter




https://www.jianshu.com/p/333ed5ee958dhttps://www.jianshu.com/p/333ed5ee958d

https://www.scienjus.com/custom-http-message-converter/

https://segmentfault.com/a/1190000012658289

### @ResponseBody & @RequestBody

https://www.jianshu.com/p/333ed5ee958d

https://blog.csdn.net/u013887008/article/details/100183087

### @ControllerAdvice

https://www.cnblogs.com/lenve/p/10748453.html


## WebSocket


## WebFlux