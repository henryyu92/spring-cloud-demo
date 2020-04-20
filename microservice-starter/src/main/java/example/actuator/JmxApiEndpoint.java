package example.actuator;

import org.springframework.boot.actuate.endpoint.jmx.annotation.JmxEndpoint;

@JmxEndpoint(id = "jmx")
public class JmxApiEndpoint implements ApiEndpoint {
    @Override
    public Metric perform() {
        return null;
    }

    @Override
    public Metric writeOp() {
        return null;
    }

    @Override
    public Metric deleteOp() {
        return null;
    }
}
