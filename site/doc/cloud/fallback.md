## 服务降级

当服务器压力剧增的情况下，根据实际业务情况及流量，对一些服务和页面有策略的不处理或者简单处理，从而释放服务器资源以保证核心服务正常运行。

服务降级主要用于在服务负载超出了设置的阈值导致服务超时、服务不可用等情况，此时可以将一些不重要或不紧急的服务进行延迟使用或暂停使用。

服务熔断指的是当服务出现不可用或响应超时的情况时，为了防止系统出现雪崩，暂时停止对该服务的调用。

服务限流指的是当服务负载达到设置的阈值时，为了保护系统对后续的流量进行限制措施，如：延迟处理、拒绝处理等。

### Hystrix

Hystrix 是 Spring Cloud 中用于服务降级限流的组件，Hystrix 提供了资源隔离、降级、熔断、缓存等功能。

- 资源隔离：包括线程池隔离和信号量隔离，限制调用分布式服务的资源使用，某一个调用的服务出现问题不会影响其他服务的调用
- 降级：包括超时降级和资源不足降级，降级后可以配合降级接口返回兜底数据
- 熔断：当失败率达到阈值时自动触发降级，熔断器触发的快熟失败会进行快速恢复
- 缓存：返回结果可以缓存，之后的请求调用从缓存中获取结果

 Hystrix 在使用时需要创建 HystrixCommand 对象用于封装请求，在 HystrixCommand 对象的构造方法中配置执行时的参数，配置参数控制者 Hystrix 的限流熔断机制。Hystrix 的整个工作流程如下：
- 判断是否开启请求缓存(requestCacheEnabled)，如果开启且能查找到对应缓存则直接返回缓存
- 检查是否开启了断路器(circuitBreakerEnabled)，如果开启断路器且断路器处于打开状态(即依赖服务异常)，则不会调用 command 而是调用 getFallback 方法降级
- 如果这个 command 线程池和队列已满，或者 semaphore 信号量已满，那么也不会执行 command，而是直接去调用 fallback 降级机制
- HystrixObservableCommand 对象的 construct() 方法，或者 HystrixCommand 的 run() 方法真正调用以来的服务，如果服务调用超时或者异常则会调用 getFallback 方法降级，方法执行的状态都会用于断路器的统计

**资源隔离**

Hystrix 提供了两种资源隔离的方式：线程池隔离模式和信号量隔离模式。

为每个业务请求的对应 Command 配置一个线程池，堆积的业务 Command 先进入线程池队列，这种方式需要为每个依赖的服务申请一个线程池，如订单服务分别调用用户服务、商品服务时就需要分别为用户服务和商品服务设置独立的线程池。线程池隔离模式可以在突发流量到来时进入队列。

线程池模式中发起服务调用线程和真正执行调用服务的线程不是同一个，而使用信号量模式时二者是同一个线程，信号量模式为每个依赖的服务维护一个原子计数器用于记录当前有多少线程在调用该服务，如果正在调用服务的线程数超过信号量阈值则会调用降级处理。信号量隔离模式可以控制并发请求量，防止请求线程大面积阻塞从而达到限流和防止雪崩的目的。

**降级**

Hystrix 在发生下面几种情况时会执行降级逻辑：
- 执行 run 方法抛出异常
- 熔断器处于打开状态
- 命令的线程池队列溢出或者线程数达到信号量阈值
- 命令方法执行超时

Hystrix 提供了三种降级方式：快速模式、故障转移、主次模式。
- 快速模式时如果调用服务失败则立即返回失败。即没有重写降级逻辑
- 故障转移时如果服务调用失败则调用降级服务，如果都失败可以从默认缓存中返回数据。
- 主次模式时当服务调用有多中实现时可以设置切换。

**熔断**

Hystrix 在运行过程中会向每个 commandKey 对应的熔断器报告成功、失败、超时和拒绝的状态，熔断器维护并统计这些数据并根据这些统计数据来决策熔断开关是否打开。如果熔断器处于打开状态，则后续的请求会直接进行降级处理，一段时间后(默认 5s)才会尝试将熔断器置于半打开状态，允许一部分流量进入，相当于对依赖的服务进行一次健康检查，如果请求成功则将熔断器置于关闭状态。

Hystrix 断路器的工作原理：
- 调用 allowRequest 方法判断是否可以允许线程调用，如果断路器处于开启状态则不允许
- 调用 isOpen 判断当前熔断器的状态，统计周期内的请求数、失败请求数，如果达到阈值则使熔断器处于开启状态执行降级操作，Hystrix 使用 Bucket 来存储每秒的请求状态(成功、失败、超时、拒绝)的数量，并维护桶的数量和统计周期一致
- 如果断路器置于开启状态，则在等待 sleepWindowInMilliseconds 后置于半开状态，允许放行一个探测请求，如果请求成功则将断路器置于关闭状态

 ### Sentinel