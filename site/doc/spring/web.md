## Web

- [MVC](#mvc)
  - [DispatcherServlet](#dispatcherservlet)
  - [HandlerMapping](#handlermapping)
  - [HandlerAdapter](#handleradapter)
  - [HandlerMethodArgumentResolver](#HandlerMethodArgumentResolver)
  - [HandlerMethodReturnValueHandler](#HandlerMethodReturnValueHandler)
  - [HandlerExceptionResolver](#HandlerExceptionResolver)
  - [HttpMessageConverter](#HttpMessageConverter)
  - [WebMvcConfigurer](#WebMvcConfigurer)
- [WebSocket](#WebSocket)
- [WebFlux](#WebFlux)

## MVC

Spring MVC 是基于 Servlet 构建的 Web 框架。Spring MVC 的整体架构是基于前端控制器(DispatcherServlet)设计,HTTP 请求都是由 DispatcherServlet 代理并通过设置的 HandlerMapping 将请求映射到对应的处理器链(HandlerExecutionChain),然后通过 HandlerAdapter 适配的处理器链完成请求的处理,请求处理的结果由 DispatcherServlet 代理给 ViewResolver 渲染后返回,从而完成整个 HTTP 请求.

![Web MVC 执行流程](../../resources/mvc.png)

### DispatcherServlet

Dispatcher 是 Spring MVC 框架的中央处理器,所有的请求都会通过 DispatcherServlet 完成处理并返回渲染后的结果.

Dispatcher 并没有直接参与请求的处理以及结果的渲染,而是通过代理给其内部的组件完成这些功能.Spring MVC 为 DispatcherServlet 的内部组件提供了默认的实现,并将这些 bean 交由容器来管理,通过自定义这些 bean 可以扩展或者替换它们:
- HandlerMapping：将请求映射到 handler 和 拦截器链用于预处理和后处理，映射的条件取决于不同的实现类
- HandlerAdapter：将不同的处理器处理方法适配到统一的方法中并处理方法参数,数据绑定,消息转换等
- HandlerExceptionResolver：请求映射或者请求处理过程中的异常处理接口
- ViewResolver：渲染处理器返回的处理结果为真正的视图

`DispatcherServlet` 在 Servlet 容器初始化之后的 `onRefresh` 方法中会初始化内部的组件,在初始化组件的过程中会先到容器中获取,如果没有获取到则会到 `DispatcherServlet.properties` 中获取默认的组件:
```java
public class DispatcherServlet extends FrameworkServlet {

    // Servlet 容器初始化后调用
    protected void onRefresh(ApplicationContext context) {
        initStrategies(context);
    }

    // 初始化内部组件
	protected void initStrategies(ApplicationContext context) {
	    initMultipartResolver(context);
	    initLocaleResolver(context);
	    initThemeResolver(context);
	    initHandlerMappings(context);
	    initHandlerAdapters(context);
	    initHandlerExceptionResolvers(context);
	    initRequestToViewNameTranslator(context);
	    initViewResolvers(context);
	    initFlashMapManager(context);
	}
}
```
`DispatcherServlet` 始化完内部组件后就可以对外提供 web 服务了,基于 Servlet 容器的 Web 应用的 HTTP 请求会在 Servlet 的 `doService` 中被处理, `DispatcherServlet` 在 `doService` 中在 request 中设置了一些后续需要使用到的 attribute 之后调用 `doDispatch` 来真正处理请求:
```java
public class DispatcherServlet extends FrameworkServlet {
    
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception { 
        HttpServletRequest processedRequest = request;
        HandlerExecutionChain mappedHandler = null;
        boolean multipartRequestParsed = false;
        
        WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
        
        try { 
            ModelAndView mv = null;
            Exception dispatchException = null;
            
            try {
                // 如果是文件流请求则处理,返回 MultipartHttpServletRequest
                processedRequest = checkMultipart(request);
                multipartRequestParsed = (processedRequest != request);
                
                // Determine handler for the current request.
                mappedHandler = getHandler(processedRequest);
                if (mappedHandler == null) { 
                    noHandlerFound(processedRequest, response);
                    return; 
                }
                
                // Determine handler adapter for the current request.
                HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
                
                // Process last-modified header, if supported by the handler.
                String method = request.getMethod();
                boolean isGet = "GET".equals(method);
                if (isGet || "HEAD".equals(method)) { 
                    long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
                    if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) { 
                        return; 
                    } 
                }
                
                if (!mappedHandler.applyPreHandle(processedRequest, response)) { 
                    return; 
                }
                
                // Actually invoke the handler.
                mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
                
                if (asyncManager.isConcurrentHandlingStarted()) { 
                    return; 
                }
                
                applyDefaultViewName(processedRequest, mv);
                mappedHandler.applyPostHandle(processedRequest, response, mv); 
            } 
            catch (Exception ex) { 
                dispatchException = ex; 
            } 
            catch (Throwable err) {
                // As of 4.3, we're processing Errors thrown from handler methods as well,
                // making them available for @ExceptionHandler methods and other scenarios.
                dispatchException = new NestedServletException("Handler dispatch failed", err); 
            }
            // 处理请求的结果
            processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException); 
        } 
        catch (Exception ex) { 
            triggerAfterCompletion(processedRequest, response, mappedHandler, ex); 
        } 
        catch (Throwable err) { 
            triggerAfterCompletion(processedRequest, response, mappedHandler,
                    new NestedServletException("Handler processing failed", err)); 
        } 
        finally { 
            if (asyncManager.isConcurrentHandlingStarted()) {
                // Instead of postHandle and afterCompletion
                if (mappedHandler != null) { 
                    mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response); 
                } 
            } 
            else {
                // Clean up any resources used by a multipart request.
                if (multipartRequestParsed) { 
                    cleanupMultipart(processedRequest); 
                } 
            } 
        } 
    }
}
```

### HandlerMapping

HandlerMapping 负责映射 URL 和对应的处理类，Spring 提供了 `RequestMappingHandlerMapping` 支持 @RequestMapping 注解映射, `SimpleUrlHandlerMapping` 维护 url 和处理器的映射,默认使用 `BeanNameUrlHandlerMapping` 作为实现。

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

`RequestMappingHandlerAdapter` 继承 `AbstractHandlerMethodAdapter` 并实现了 `InitializingBean` 接口,在重写方法 `afterPropertiesSet` 中初始化了 `HandlerMethodArgumentResolver` 和 `HandlerMethodReturnValueHandler` 这两个组件,这两个组件用于处理请求参数和请求返回的结果.
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

### HandlerExceptionResolver


### HttpMessageConverter

`HttpMessageConverter` 接口是 Spring MVC 中用于将 HTTP 中的字节流数据和应用程序中的对象进行转换.

Spring 提供了常用的数据格式的转换:
- ProtobufHttpMessageConverter


### WebMvcConfigurer

WebMvcConfigure 接口是 Spring 提供的通过代码方式配置 MVC 的入口,通过实现接口并重写接口方法可以使用自定义的 Spring MVC 组件.

```java

```



https://www.jianshu.com/p/333ed5ee958dhttps://www.jianshu.com/p/333ed5ee958d

https://www.scienjus.com/custom-http-message-converter/

https://segmentfault.com/a/1190000012658289

https://www.jianshu.com/p/333ed5ee958d

https://blog.csdn.net/u013887008/article/details/100183087

### @ControllerAdvice

https://www.cnblogs.com/lenve/p/10748453.html


## WebSocket


## WebFlux