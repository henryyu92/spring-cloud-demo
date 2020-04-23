package example.actuator.endpoint;


import example.actuator.metric.Metrics;
import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;

public interface ApiEndpoint {

    /**
     * 通过 GET 方法访问
     * @return
     */
    @ReadOperation
    Metrics perform();

    /**
     * 通过 POST 方法访问
     * @return
     */
    @WriteOperation
    Metrics writeOp();

    /**
     * 通过 DELETE 方法访问
     * @return
     */
    @DeleteOperation
    Metrics deleteOp();
}
