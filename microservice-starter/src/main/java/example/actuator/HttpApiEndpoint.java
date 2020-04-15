package example.actuator;

import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;

@WebEndpoint(id = "webApi")
public class HttpApiEndpoint implements ApiEndpoint {
    @Override
    public Metric perform() {
        return null;
    }
}
