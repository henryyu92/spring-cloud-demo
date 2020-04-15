package example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class StarterApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(StarterApplication.class, args);

    }
}
