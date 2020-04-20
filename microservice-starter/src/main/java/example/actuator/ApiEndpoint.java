package example.actuator;


import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;

public interface ApiEndpoint {

    /**
     * 通过 GET 方法访问
     * @return
     */
    @ReadOperation
    Metric perform();

    /**
     * 通过 POST 方法访问
     * @return
     */
    @WriteOperation
    Metric writeOp();

    /**
     * 通过 DELETE 方法访问
     * @return
     */
    @DeleteOperation
    Metric deleteOp();
}
