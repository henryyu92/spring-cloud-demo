package org.mooc.cloud;

import org.mooc.ribbon.LoadBalanceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
@EnableDiscoveryClient    // 使用 zookeeper 或者 consul 作为注册中心时使用
@RibbonClient(name = "ribbon", configuration = LoadBalanceConfig.class)			// 自定义 Ribbon 负载均衡策略
public class OrderRibbonApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderRibbonApplication.class, args);
	}

}
