package org.mooc.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient    // 使用 zookeeper 或者 consul 作为注册中心时使用
@EnableFeignClients
@EnableHystrix
public class OrderFeignApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderFeignApplication.class, args);
	}

}
