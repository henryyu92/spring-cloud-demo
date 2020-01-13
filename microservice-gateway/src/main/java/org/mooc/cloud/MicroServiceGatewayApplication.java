package org.mooc.cloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author
 */
@SpringBootApplication
//@EnableEurekaClient
//@EnableHystrix
public class MicroServiceGatewayApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(MicroServiceGatewayApplication.class).run(args);
	}

}
