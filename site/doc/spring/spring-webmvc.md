# Spring Web MVC
>xxx
## DispatcherServlet
> DispatcherSevlet 作为 Spring MVC 的前端控制器，为所有请求的处理提供一套共享算法，而实际工作由可配置的组件执行。</br>
> DispatcherServlet 和其他 Servlet 一样需要使用 Java 或者在 web.xml 中根据 Servlet 规范进行声明和映射。</br>

使用 [Java 方式](../cinfig)声明：
```java
public class MyWebApplicationInitializer implements WebApplicationInitializer{
    @Override
    public void onStartup(ServletContext servletCxt){
        // Load Spring web application configuration
        AnnotationConfigWebApplicationContext ac = new AnnotationApplicationContext();
        ac.register(AppConfig.class);
        ac.refresh();

        // Create and register the DispatcherServlet
        DispatcherServlet servlet = new DispatcherServlet(ac);
        ServletRegistration.Dynamic registration = servletCxt.addServlet("app", servlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/app/*");
    }
}
```
使用 [web.xml]() 方式声明：
```xml
```

## Annotated Controllers
### Declaration
### RequestMapping
### Handler Method
> @RequestMapping 注解的方法签名可以支持多种参数和返回值。
#### 方法参数
- WebRequest/NativeWebRequest：无需直接使用 Servlet API 就可以直接获取 request 参数和 session 的属性值; 
- javax.servlet.ServletRequest/ServeltResponse：任何一个 request 或者 response 类型，如 ServletRequest、HttpServeltRequest、MultipartRequest、MultipartHttpServletRequest
- javax.servlet.http.HttpSession：
- @PathVariable
- @RequestParam：用于绑定 Servlet 请求参数到contorller 中的方法参数
  ```java
  @Controller
  @RequestMapping("/pets")
  public class EditPetForm{

      @GetMapping("/get")
      public String setupFrom(@RequestParam("petid") int petId, Model model){
          ...
      }
  }
  ```
- @RequestHeader：用于将请求头绑定到 controller 的方法参数
  ```java
  @GetMapping("/demo")
  public void handle(@RequetHeader("Accept-Encoding")String encode, @RequestHeader("Keep-Alive") long keepAlive){
      ...
  }

  @GetMapping("/demo")
  public void handle(@RequestHeader Map<String, String> header){
      ...
  }
  ```
- @RequestBody：通过 HttpMessageConverter 将请求体反序列化成实体
  ```java
  @PostMapping("/accounts")
  public void handle(@RquestBody Account account){
      ...
  }
  ```
- HttpEntity<B>：和 @ResponseBody 差不多，但是可以
- java.util.Map/org.springframework.ui.Model/org.springframework.ui.ModelMap：
### Model
### DataBinder
### Exceptions
### Controller Advice
