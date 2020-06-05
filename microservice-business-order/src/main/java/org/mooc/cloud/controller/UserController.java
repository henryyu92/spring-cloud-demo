package org.mooc.cloud.controller;

import org.mooc.cloud.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class UserController {

    private static final String providerHost = "http://microservice-payment";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") Long id){

        User user = restTemplate.getForObject(providerHost + "/user/" + id, User.class);

        ResponseEntity<User> entity = restTemplate.getForEntity(providerHost + "", User.class);
        if (entity.getStatusCode().is2xxSuccessful()){
            return entity.getBody();
        }

        return user;
    }

    @GetMapping("/test")
    public String test(){
        String forObject = restTemplate.getForObject("http://localhost:7000/get", String.class);
        return forObject;
    }
}
