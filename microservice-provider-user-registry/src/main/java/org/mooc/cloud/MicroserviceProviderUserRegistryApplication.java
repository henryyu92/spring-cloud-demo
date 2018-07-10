package org.mooc.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MicroserviceProviderUserRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceProviderUserRegistryApplication.class, args);
	}
}
