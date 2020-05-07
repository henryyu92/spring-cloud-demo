package example.container;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@ComponentScan(basePackages = "example.container")
@Configuration
public class StarterAnnotationApplication {


    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(StarterAnnotationApplication.class);

        Arrays.asList(context.getBeanDefinitionNames()).forEach(x-> System.out.println(x));
    }

}
