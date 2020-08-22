package example.web.servlet.mvc.mapping;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerMappingConfig {

    @Bean
    public OrderedHandlerMapping orderedHandlerMapping(){
        return new OrderedHandlerMapping();
    }
}
