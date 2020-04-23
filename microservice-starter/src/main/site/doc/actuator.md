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
spring-actuator 中集成了 Micrometer 框架作为度量系统。使用 Micrometer 统计应用数据，Prometheus 进行数据收集，Grafana 进行数据展示，可用用户监控生产环境机器的性能指标和业务指标。

Micrometer 中的核心接口是 ```Meter```， 表示一组用于收集应用中的度量数据的接口。```Meter``` 是由 ```MeterRegistry``` 创建和保存的，一般而言每个 JVM 应用在使用 Micrometer 的时候必须创建一个 ```MeterRegistry``` 的具体实现。

Micrometer 中提供了多个实现 ```Meter``` 接口的具体度量类型，包括：```Counter```, ```Timer```, ```Gauge```, ```LongTaskTimer```, ```DistributionSummary```。

每个 ```Meter``` 都需要通过 Name 和 Tag 作为唯一标识，这样可以通过 Name 进行标记，通过 Tag 区分多维度进行数据统计。

### MeterRegistry
MeterRegistry 是一个抽象类，Micrometer 中实现了三种 MeterRegistry：
- ```SimpleMeterRegistry```：统计的数据位于应用的内存中，不会发布到其他系统，多用于调试
- ```CompositeMeterRegistry```：聚合多个 MeterRegistry，内部维护了一个 MeterRegistry 的列表

CompositeMeterRegistry 在初始化时，内部持有的 ```MeterRegistry``` 列表是空的，此时对新增的 Meter 实例的操作是无效的。

```java
CompositeMeterRegistry registry = new CompositeMeterRegistry();
registry.add(new SimpleMeterRegistry());

Counter counter = registry.counter("counterName", "tagName", "tagValue");
// 计数加 1
counter.increment();

System.out.println(counter.meter());
```

### Tag
Micrometer 中，Meter 的命名约定使用英文逗号 (,) 分隔单词，通过底层的命名转换接口 ```NamingConvertion```` 进行转换以适配不同的监控系统，同时可以消除监控系统不允许的特殊字符的名称和标记等。

可以实现 NamingConvertion 接口来自定义命名转换规则，并通过 ```registry.config().namingConvertion(NamingConvertion)``` 方法注册自己实现的命名转换规则。

Tag 是 Meter 的一种重要的功能，根据 Tag 的命名可以推断出它指向的数据代表什么维度或者什么类型的度量指标。
```java
MeterRegistry registry = ...
registry.counter("database.calls", "db", "users")
registry.counter("http.requests", "rui", "/api/users")
```
可以在 MeterRegistry 上定义全局的 Tag，这样当前 MeterRegistry 中的所由 Meter 都会附加上指定的 Tag：
```java
MeterRegistry registry = ...
registry.config().commonTags("stack", "prod", "region")
```
需要注意的是 Tag 不能为 null 并且在 Tag 是 key-value 形式的，因此 Meter 指定 Tag 时必须是偶数。

可以使用 MeterFilter 过滤 Tag 或者为 Meter 设置白名单，这样就可以自定义 Meter 过滤链：
```java
MeterRegistry registry = ...
registry.config()
        .meterFilter(MeterFilter.ignoreTags("http"))
        .meterFilter(MeterFilter.denyNameStartsWith("jvm"));
```

### Meters

Micrometer 实现了多种 Meter 包括：```Timer```, ```Counter```, ```LongTaskTimer``` 和 ```DistributionSummary```，每种 Meter 都有各自适用的场景。

通过实现 Meter 接口或者实现 Micrometer 提供的 Meter 可以自定义度量类型。

#### Counter
```Counter``` 是一种单值的度量类型，允许使用一个固定值(正数)进行计数。通常用于记录总量或者计数值，适用于一些增长类型的统计。

```java
Metrics.counter("order.create",
                "channel", order.getChannel(),
                "createTime", FORMATTER.format(order.getCreateTime()))
                .increment();
```
#### Timer
```Timer``` 适用于记录耗时比较短的事件的执行时间，通过时间分布展示事件的序列和发生频率。所有的 ```Timer``` 的实现至少记录了发生的事件的数量和这些事件的总耗时，从而生成一个时间序列。```Timer``` 的基本单位基于服务端的指标而定，但是实际上我们不需要过于关注 ```Timer``` 的基本单位，因为 Micrometer 在存储生成的时间序列的时候会自动选择适当的基本单位。

```java
timer.record(TimerMain::createOrder);
```
在实际生产环境中，可以通过spring-aop把记录方法耗时的逻辑抽象到一个切面中，这样就能减少不必要的冗余的模板代码。Timer的使用还可以基于它的内部类Timer.Sample，通过start和stop两个方法记录两者之间的逻辑的执行耗时：
```java
Timer.Sample sample = Timer.start(registry);

// 这里做业务逻辑
Response response = ...

sample.stop(registry.timer("my.timer", "response", response.status()));
```

#### LongTaskTimer
```LongTaskTimer``` 主要用于记录长时间执行的任务的持续时间，在任务完成之前被检测的事件或者任务仍然处于运行状态，任务完成的时候，任务执行的总耗时才会被记录下来。

```java
registry.more().longTaskTimer("longTaskTimer").record(
        () -> {
            // Task 逻辑
        }
);
```

#### Gauge
```Gauge``` 是获取当前度量记录值的句柄，也就是表示一个可以任意上下浮动的单数值度量。```Gauge``` 通常用于变动的测量值，测量值用 ```ToDoubleFunction``` 参数的返回值设置，一般用于监测有自然上界的事件或者任务。
```java
List<String> list = registry.gauge("listGauge", Collections.emptyList(), new ArrayList<>(), List::size); 
List<String> list2 = registry.gaugeCollectionSize("listSize2", Tags.empty(), new ArrayList<>()); 
Map<String, Integer> map = registry.gaugeMapSize("mapGauge", Tags.empty(), new HashMap<>());
```

#### DistributionSummary
Summary(摘要)主要用于跟踪事件的分布，在Micrometer中，对应的类是DistributionSummary(分发摘要)。它的使用方式和Timer十分相似，但是它的记录值并不依赖于时间单位。常见的使用场景：使用DistributionSummary测量命中服务器的请求的有效负载大小。

```java
DistributionSummary summary = registry.summary("response.size");
```

https://www.cnblogs.com/rolandlee/p/11343848.html