## Web

Web MVC 执行流程

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
