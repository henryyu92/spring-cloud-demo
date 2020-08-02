package example.web.servlet.filter;

import org.apache.catalina.SessionListener;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.EventListener;

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
        ServletListenerRegistrationBean<EventListener> listenerBean = new ServletListenerRegistrationBean();
        listenerBean.setListener(new TestHttpSessionListener());

        return listenerBean;
    }
}
