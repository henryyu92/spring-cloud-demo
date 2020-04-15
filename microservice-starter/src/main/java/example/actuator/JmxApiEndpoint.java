package example.actuator;

import org.springframework.boot.actuate.endpoint.jmx.annotation.JmxEndpoint;

@JmxEndpoint
public class JmxApiEndpoint implements ApiEndpoint {
    @Override
    public Metric perform() {
        return null;
    }
}
