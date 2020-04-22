package example.actuator.source;

import example.actuator.metric.Metric;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Api 性能监控
 */
public class ApiPerformanceContributor implements Source<Metric>{

    private Map<String, Metric> qps = new ConcurrentHashMap<>(32);
    private Map<String, Metric> tps = new ConcurrentHashMap<>(32);

    @Override
    public Metric collect() {
        return null;
    }
}
