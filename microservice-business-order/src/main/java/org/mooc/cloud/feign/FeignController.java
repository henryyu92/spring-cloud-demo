package org.mooc.cloud.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeignController {

    @Autowired
    private FeignService feignService;

    @GetMapping("/consumer/get/{id}")
    public void test(@PathVariable("id") Long id){
        feignService.getPayment(id);
    }
}
