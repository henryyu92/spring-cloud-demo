package org.mooc.cloud.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.mooc.cloud.entity.User;
import org.mooc.cloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/user/{id}")
    @HystrixCommand(fallbackMethod = "processFallback")
    public User getUser(@PathVariable("id") Long id){
        User user = userService.getUser(id);
        if (user == null){
            throw new RuntimeException("can not find user");
        }
        return user;
    }

    @GetMapping("/user/instances")
    public Object instances(){
        List<String> services = discoveryClient.getServices();
        return services;
    }

    public User processFallBack(@PathVariable("id") Long id){
        User user = new User();
        user.setUsername("error");
        return user;
    }
}
