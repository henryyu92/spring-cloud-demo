package org.mooc.cloud.feign;

import org.mooc.cloud.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("microservice-provider-user-registry")
public interface UserFeignClient {

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") Long id);
}