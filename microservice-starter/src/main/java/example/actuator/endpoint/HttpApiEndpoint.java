package example.actuator.endpoint;

import example.actuator.metric.Metric;
import example.actuator.sink.AbstractSink;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;

@WebEndpoint(id = "webApi")
public class HttpApiEndpoint extends AbstractSink implements ApiEndpoint {

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
