package org.mooc.cloud;

import org.mooc.config.RibbonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
// 指定自定义的 Ribbon 配置
@RibbonClient(name = "provider-user-ribbon", configuration = RibbonConfig.class)
public class MicroserviceConsumerMovieRibbonApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceConsumerMovieRibbonApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
