package org.mooc.cloud.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebMvc
@EnableConfigurationProperties(InterceptorProperties.class)
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private InterceptorProperties interceptorProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestCheckInterceptor())
                .addPathPatterns(interceptorProperties.getIncludePattens())
                .excludePathPatterns(interceptorProperties.getExcludePattens());
    }
}
