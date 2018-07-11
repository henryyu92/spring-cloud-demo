package org.mooc.cloud.feign;

import org.mooc.cloud.entity.User;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RibbonClient("microservice-provider-user-hystrix")
public interface UserHystrixClient {

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") Long id);
}
