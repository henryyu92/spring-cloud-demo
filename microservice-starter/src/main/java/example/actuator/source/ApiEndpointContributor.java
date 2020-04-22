package example.actuator.source;

import example.actuator.metric.Metric;

public class ApiEndpointContributor implements Source<Metric> {
    @Override
    public Metric collect() {
        return null;
    }
}
