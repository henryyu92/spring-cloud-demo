## SpringBoot 监控
SpringBoot 自带监控功能 Actuator，可以监控程序内部的运行情况，包括环境信息、bean 信息等。使用 Actuator 需要在 pom.xml 中添加依赖：
```mxl
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### Endpoint
Endpoint 用于监控用户应用的交互，SpringBoot 内置了多种 Endpoint，通过这些 Endpoint 可以监控到应用的基本信息。可以通过浏览器访问 ```/actuator``` 访问到已经暴露的 Endpoint，默认地址是 localhost:8080

每个 Endpoint 可以启用或禁用，这也控制着 Endpoint 是否创建，以及所对应的 bean 是否存在于应用程序上下文中。

#### 启用 Endpoint
默认情况下，除 ShutdownEndpoint 以外的所有 Endpoint 都会启动，如果需要控制单个 Endpoint 的启动，需要在配置文件中配置：
```properties
# 启用 ShutdownEndpoint
management.endpoint.shutdown.enabled=true
```
可以通过 ```management.endpoints.enabled-by-default``` 来关闭默认配置而全部采用自定义的配置：
```properties
# 关闭全局默认设置
management.endpoints.enabled-by-default=false
# 自定义设置
management.endpoint.info.enabled=true
```
Endpoint 被禁用后会从应用上下文删除，从而相关的信息不会被监控到。

#### 暴露 Endpoint
Endpoint 启用后可以设置是否对外暴露，Spring 默认提供 JMX 和 HTTP 暴露方式，在配置中可以配置在这写暴露方式下暴露的 Endpoint：
```properties
# 设置 JXM 暴露的 Endpoint
management.endpoints.jmx.exposure.include=health,info

# 设置 Web 暴露除了 env, beans 外的所有 Endpoint
management.endpoints.web.exposure.include=*
management.endpoints.web.exposure.exclude=env,beans
```
Spring 默认 Http 方式暴露的 /actuator 可以看到所有的 Endpoint，也是其他 Endpoint 的基础路径，如果改变则其他 Endpoint 的路径也会改变，通过 ```management.endpoints.web.base-path``` 设置基础路径；如果需要将某个 Endpoint 的路径独立于基础路径，则可以通过 ```management.endpoints.web.path-mapping``` 将其路径映射到指定路径：
```properties
# 设置基础路径
management.endpoints.web.base-path=/actuator/base-path
# 将 health Endpoint 路径映射到 /healthcheck
management.endpoints.web.path-mapping.health=healthcheck
```

#### 自定义 Endpoint
在 @Bean 注解的类上使用 @Endpoint 即可以将当前 Bean 作为 Endpoint 通过 JMX 或者 HTTP 方式暴露。使用 @JmxEndpoint 只能通过 JMX 方式暴露，@WebEndpoint 只能通过 HTTP 方式暴露。

使用 @EndpointWebExtension 和 @EndpointJmxEndpoint 来扩展 Endpoint 的功能。

Endpont 中的方法可以用 @ReadOperation, @WriteOperation 或 @DeleteOperation 注解来自动通过 JXM 或者 HTTP 暴露。

通过在一个实现了 Supplier<Endpoint> 接口的类上增加 @ServletEndpoint 注解可以创建一个 Servelt Endpoint，这样可以将 Servlet 作为 Endpoint 暴露。

@ControllerEndpoint 和 @RestControllerEndpoint 则可以用于实现基于 Spring MVC 的 Endpoint，其访问的方法通过 @RequestMapping 和 GetMapping 来映射。



https://blog.csdn.net/alinyua/article/details/80009435
https://my.oschina.net/u/3266761/blog/2960774
## Micrometer
spring-actuator 中集成了 Micrometer 框架作为度量系统。

Micrometer 中的核心接口是 Meter 表示用于收集应用中的度量接口，其具体类型包括：Timer, Counter, Gauge, DistributionSummary, LongTaskTimer, FunctionCounter, FunctionTimer 和 TimeGauge。

- Counter 是一种单值的度量类型，是一个增量为正数的单值计数器，可以应用在记录总量或者计数的场景
- FunctionCounter

https://www.cnblogs.com/rolandlee/p/11343848.html