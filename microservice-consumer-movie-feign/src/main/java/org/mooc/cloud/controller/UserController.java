package org.mooc.cloud.controller;

import org.mooc.cloud.entity.User;
import org.mooc.cloud.feign.UserFeignClient;
import org.mooc.cloud.feign.UserHystrixClient;
import org.mooc.cloud.feign.UserRegistryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class UserController {

    @Autowired
    private UserRegistryClient userRegistryClient;
    @Autowired
    private UserHystrixClient hystrixClient;

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") Long id){

        User user = userRegistryClient.getUser(id);

        return user;
    }

    @GetMapping("/hystrix/user/{id}")
    public User getHystrixUser(@PathVariable("id") Long id){
        User user = userRegistryClient.getUser(id);

        return user;
    }
}