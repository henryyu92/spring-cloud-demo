package example.web.servlet.filter;

import java.util.EventListener;
import javax.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletFilterConfig {

    @Bean
    public FilterRegistrationBean<Filter> filterRegistrationBean(){
        FilterRegistrationBean<Filter> filterBean = new FilterRegistrationBean<>();
        filterBean.setFilter(new TestFilter());
        filterBean.addUrlPatterns("/*");

        return filterBean;
    }

    @Bean
    public ServletListenerRegistrationBean<EventListener> servletListenerRegistrationBean(){
        ServletListenerRegistrationBean<EventListener> listenerBean = new ServletListenerRegistrationBean<>();
        listenerBean.setListener(new TestHttpSessionListener());

        return listenerBean;
    }
}
