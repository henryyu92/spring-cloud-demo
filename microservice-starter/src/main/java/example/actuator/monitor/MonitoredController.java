package example.actuator.monitor;

import org.springframework.stereotype.Controller;

@Controller
public class MonitoredController implements Monitor {

    @Monitored("qps")
    public void test(){
        System.out.println("hello");
    }
}
