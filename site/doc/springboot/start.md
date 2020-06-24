## Spring Boot





### Spring Boot 启动



## 创建 Spring boot 项目
创建spring boot 项目，pom.xml 中添加：
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <vsersion>version</version>
</parent>
```
创建 Web 项目，pom.xml 中添加:
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```
使用 spring boot 自动打包，pom.xml 中添加:
```xml
<build>
    <plugins>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
    </plugins>
</build>
```
该方式会将项目源码编译后的文件、项目的资源文件以及项目所依赖的 jar 都同意打成一个 jar 包，该 jar 包可能会比较大，因此一般采用自定义的方式：
```xml

``` 
开发时文件修改且 build 后能够自动重启，在 pom.xml 中添加：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <!-- 表示可选 -->
    <optional>true</optional>
</dependency>
```


### SpringApplication

### 自定义启动时 Banner
Spring boot 在启动时会打印 *banner.txt* 中的内容，可以通过修改 banner.txt 并且使用 *spring.banner.location* 指定 banner 文件位置，*spirng.banner.charset* 指定编码来自定义启动时 banner。Sprign boot 除了支持 .txt 文件外，还支持 .gif, .jpg, .png 等图片文件，使用 *spring.banner.image.location* 在 application.properties 中指定位置即可：
```
spring.banner.location=/banner.txt
spring.banner.location=utf-8

spirng.banner.image.location=/banner.gif
```
除了使用配置文件外，也可以使用代码来自定义启动时 banner
```
new SpringApplicationBuilder().banner(new Banner(){
    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out){
        ...
    }
})
```

### 开发 Web 应用
#### Spring MVC
如果希望使用 Spring MVC 的特性(interceptors, formatters, view controllers 等)，使用 @Configuration 注解一个实现了 WebMvcConfigurer 的类：
```java
@Configuration
public class WebConf implements WebMvcConfigurer{
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        ...
    }

    ...
}
```
#### HttpMessageConverters
Spring MVC 使用 HttpMessageConverter 接口来转换 Http 请求和响应，可以通过 Spring boot 提供的 HttpMessageConverters 类来自定义转换：
```java
@Configuration
public class MyConf{
    @Bean
    public HttpMessageConverters customConverters(){
        HttpMessageConverter<?> additional = ...
        HttpMessageConverter<?> another = ...
        return new HttpMessageConverters(additional, another);
    }
}
```
