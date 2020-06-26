package example.container.annotation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ImportClass.class)
public class TestApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TestApplication.class);

        ImportClass bean = context.getBean(ImportClass.class);
        System.out.println(bean);
        bean.importTest();

        System.out.println(context.getBean(DogTest.class));

        System.out.println(context.getBean(ConfigurationClass.class));
    }
}
