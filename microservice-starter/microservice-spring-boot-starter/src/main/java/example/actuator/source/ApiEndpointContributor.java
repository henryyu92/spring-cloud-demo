package example.actuator.source;

import example.actuator.metric.Metrics;

public class ApiEndpointContributor implements Source<Metrics> {
    @Override
    public Metrics collect() {
        return null;
    }
}
