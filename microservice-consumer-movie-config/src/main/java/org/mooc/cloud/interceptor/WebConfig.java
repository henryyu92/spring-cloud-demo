package org.mooc.cloud.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author : henry
 * @version : 1.0
 * @Description :
 * @Copyright : Sinaif All Rights Reserved
 * @Company : 海南新浪爱问普惠科技有限公司-富鱼
 * @Create Date : 2018/9/6 10:59
 */

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
