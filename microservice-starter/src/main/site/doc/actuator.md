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
management.endpoint.shutdown.enable=true
```
可以通过 ```management.endpoints.enabled-by-default``` 来关闭默认配置而全部采用自定义的配置：
```properties
# 关闭全局默认设置
management.endpoint.enabled-by-default=false
# 自定义设置
management.endpoint.shutdown.enable=true
```
Endpoint 被禁用后会从应用上下文删除，从而相关的信息不会被监控到。

#### 暴露 Endpoint

#### 自定义 Endpoint