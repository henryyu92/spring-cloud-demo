package example.actuator;


import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

public interface ApiEndpoint {

    @ReadOperation
    Metric perform();
}
