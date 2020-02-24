package org.mooc.cloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 * @author
 */
@SpringBootApplication
//@EnableEurekaClient
@EnableHystrix
public class GatewayApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(GatewayApplication.class).run(args);
	}

}
