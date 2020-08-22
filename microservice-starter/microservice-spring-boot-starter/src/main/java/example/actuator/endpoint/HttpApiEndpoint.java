package example.actuator.endpoint;

import example.actuator.metric.Metrics;
import example.actuator.sink.AbstractSink;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;

@WebEndpoint(id = "webApi")
public class HttpApiEndpoint extends AbstractSink implements ApiEndpoint {

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
