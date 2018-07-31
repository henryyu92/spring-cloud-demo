package org.mooc.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class MicroserviceZuulApplication {

	/**
	 *
	 * Spring Cloud has created an embedded Zuul proxy to ease the development of a common use case
	 * where a UI application wants to make proxy calls to one or more back end services.
	 */
	public static void main(String[] args) {
		SpringApplication.run(MicroserviceZuulApplication.class, args);
	}

}
