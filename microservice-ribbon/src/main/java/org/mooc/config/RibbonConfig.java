package org.mooc.config;

import com.netflix.loadbalancer.*;
import org.springframework.cloud.netflix.ribbon.ZonePreferenceServerListFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义 Ribbon Client 配置
 */
@Configuration
public class RibbonConfig {

    /**
     * 负载均衡策略
     * @return
     */
    @Bean
    public IRule ribbonRule(){
        return new RandomRule();
    }

    /**
     * 注册服务列表
     * @return
     */
    @Bean
    public ServerList<Server> serverList(){
        return new ConfigurationBasedServerList();
    }

    /**
     * 服务过滤
     * @return
     */
    @Bean
    public ServerListFilter serverListFilter(){
        return new ZonePreferenceServerListFilter();
    }

    /**
     * 负载均衡器
     * @return
     */
    @Bean
    public ILoadBalancer loadBalancer(){
        return new ZoneAwareLoadBalancer<Server>();
    }
}
