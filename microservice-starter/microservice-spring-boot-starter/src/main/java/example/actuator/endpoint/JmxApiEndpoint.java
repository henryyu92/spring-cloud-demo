package example.actuator.endpoint;

import example.actuator.metric.Metrics;
import org.springframework.boot.actuate.endpoint.jmx.annotation.JmxEndpoint;

@JmxEndpoint(id = "jmx")
public class JmxApiEndpoint implements ApiEndpoint {
    @Override
    public Metrics perform() {
        return null;
    }

    @Override
    public Metrics writeOp() {
        return null;
    }

    @Override
    public Metrics deleteOp() {
        return null;
    }
}
