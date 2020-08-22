package example.actuator.endpoint;

import org.springframework.boot.actuate.endpoint.web.EndpointServlet;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpoint;

import java.util.function.Supplier;


@ServletEndpoint(id = "servletEndpoint")
public class ServletApiEndpint implements Supplier<EndpointServlet> {
    @Override
    public EndpointServlet get() {
        return null;
    }
}
