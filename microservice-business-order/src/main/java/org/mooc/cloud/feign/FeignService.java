package org.mooc.cloud.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("feign")
public interface FeignService {

    @GetMapping("/payment/get/{id}")
    String getPayment(@PathVariable("id") Long id);
}
