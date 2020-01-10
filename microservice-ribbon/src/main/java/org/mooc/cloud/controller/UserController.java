package org.mooc.cloud.controller;

import org.mooc.cloud.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class UserController {

    private static final String host = "http://MICROSERVICE-PROVIDER-USER-REGISTRY";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") Long id){
        User user = restTemplate.getForObject(host + "/user/" + id, User.class);
        return user;
    }
}
