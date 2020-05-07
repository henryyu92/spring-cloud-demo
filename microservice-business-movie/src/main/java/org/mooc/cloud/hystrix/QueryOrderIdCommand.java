package org.mooc.cloud.hystrix;

import com.netflix.hystrix.*;

/**
 * Hystrix 调用原理：
 *      判断是否开启请求缓存，如果开启且能查找到对应缓存则直接返回缓存
 *      检查是否开启了断路器，如果开启断路器且断路器被打开(依赖服务异常)，则不会调用 command 而是调用 getFallback 方法降级
 *      如果这个 command 线程池和队列已满，或者 semaphore 信号量已满，那么也不会执行 command，而是直接去调用 fallback 降级机制
 *      HystrixObservableCommand 对象的 construct() 方法，或者 HystrixCommand 的 run() 方法来实际执行这个 command
 *
 *
 * Hystrix 会把每一个依赖服务的调用成功、失败、Reject、Timeout 等事件发送给 circuit breaker 断路器。
 * 断路器就会对这些事件的次数进行统计，根据异常事件发生的比例来决定是否要进行断路（熔断）。如果打开了断路器，那么在接下来一段时间内，会直接断路，返回降级结果。
 *
 * 如果在之后，断路器尝试执行 command，调用没有出错，返回了正常结果，那么 Hystrix 就会把断路器关闭。
 *
 *
 *
 * https://blog.csdn.net/loushuiyifan/article/details/82702522
 *
 */
public class QueryOrderIdCommand extends HystrixCommand<Integer> {

    /**
     * Command 配置
     * @param group
     */
    protected QueryOrderIdCommand(HystrixCommandGroupKey group) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(""))
            .andCommandKey(HystrixCommandKey.Factory.asKey(""))
            .andCommandPropertiesDefaults(
                HystrixCommandProperties.Setter()
                .withRequestCacheEnabled(false)   // 不开启请求缓存
                .withCircuitBreakerEnabled(true)  // 启动断路器
                .withCircuitBreakerRequestVolumeThreshold(10)   // 触发熔断的请求数
                .withCircuitBreakerErrorThresholdPercentage(50) // 触发熔断的请求比例
                .withCircuitBreakerSleepWindowInMilliseconds(5000)  // 熔断后直接拒绝请求的时长，之后熔断器进入 half-open 状态
                .withExecutionTimeoutEnabled(true)          // 请求超时调用 fallback
                .withExecutionTimeoutInMilliseconds(1000)   // 请求超时时长
            )
            .andThreadPoolPropertiesDefaults(
                HystrixThreadPoolProperties.Setter()
                .withCoreSize(10))
        );
    }

    @Override
    protected Integer run() throws Exception {
        return null;
    }

    /**
     * 降级方法
     * @return
     */
    @Override
    protected Integer getFallback() {
        return super.getFallback();
    }
}
