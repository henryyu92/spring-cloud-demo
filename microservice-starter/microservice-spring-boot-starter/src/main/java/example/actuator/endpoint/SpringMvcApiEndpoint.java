package example.actuator.endpoint;

import example.actuator.metric.Metrics;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@ControllerEndpoint(id = "mvc")
@RequestMapping("/mvc")
public class SpringMvcApiEndpoint {

    @GetMapping("/log")
    public Metrics log(){
        return null;
    }
}
