package org.mooc.cloud.discovery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RefreshScope
public class DiscoveryController {

    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 获取注册的服务信息
     */
    public void discovery(){
        List<String> services = discoveryClient.getServices();
        services.forEach(service ->{
            List<ServiceInstance> instances = discoveryClient.getInstances(service);
            instances.forEach(instance ->{
                System.out.println(instance.getHost());
                System.out.println(instance.getInstanceId());
            });
        });

    }
}
