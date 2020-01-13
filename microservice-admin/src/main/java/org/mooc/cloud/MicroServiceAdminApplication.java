package org.mooc.cloud;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author
 */
@EnableAdminServer
@SpringBootApplication
public class MicroServiceAdminApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(MicroServiceAdminApplication.class).run(args);
	}

}
