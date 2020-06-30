## Web

Spring 提供了对 Web 的支持

### Spring MVC

Spring MVC 是基于 Servlet 构建的 Web 框架。

![Web MVC 执行流程](../../resources/mvc.png)

#### DispatcherServlet

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

### HandlerAdapters

#### RequestMappingHandlerAdapter

### RequestMapping

### HandlerMethodArgumentResolver & HandlerMethodReturnValueHandler

`HandlerMethodArgumentResolver` 接口是 Spring MVC 中用于将方法中 HttpServletRequest 中的参数转换为方法中的参数.

Spring 提供了大量的 HandlerMethodArgumentResolver 用于支持不同的参数类型:
- RequestParamMapMethodArgumentResolver 用于处理 @RequestParam 注解的参数
- RequestHeaderMapMethodArgumentResolver 用于处理 @RequestHeader 注解的参数
- PathVariableMapMethodArgumentResolver 用于处理 @PathVariable 注解的参数
- ModelAttributeMethodProcessor 用于处理 @ModelAttribute 注解的参数
- RequestResponseBodyMethodProcessor 用于处理 @RequestBody 注解的参数

`HandlerMethodArgumentResolver` 接口包含 2 个方法:
- supportsParameter:判断当前的 resolver 是否支持这个参数
- resolveArgument:用于解析支持的参数到指定的参数类型

`HandlerMethodReturnValueHandler` 接口是 Spring MVC 中用于



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


### Reactive
