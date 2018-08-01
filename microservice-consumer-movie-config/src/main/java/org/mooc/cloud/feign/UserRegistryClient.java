package org.mooc.cloud.feign;

import org.mooc.cloud.entity.User;
import org.mooc.cloud.feign.impl.UserRegistryFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="microservice-provider-user-config", fallback = UserRegistryFallback.class)
public interface UserRegistryClient {

    @GetMapping("/user/{id}")
    User getUser(@PathVariable("id") Long id);
}
