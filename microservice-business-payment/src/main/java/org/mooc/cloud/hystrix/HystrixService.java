package org.mooc.cloud.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@DefaultProperties(defaultFallback = "fallback")
public class HystrixService {

    /**
     * 服务降级
     */
    @HystrixCommand(fallbackMethod = "fallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    })
    public void test(){

    }

    @HystrixCommand
    public void test2(){

    }

    public void fallback(){

    }


    /**
     * 服务熔断
     */
    @HystrixCommand(fallbackMethod = "fallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enable", value = "true"),
    })
    public void test3(){

    }


}
