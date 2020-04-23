package example.actuator.source;

import example.actuator.metric.Metrics;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Api 性能监控
 */
public class ApiPerformanceContributor implements Source<Metrics>{

    private Map<String, Metrics> qps = new ConcurrentHashMap<>(32);
    private Map<String, Metrics> tps = new ConcurrentHashMap<>(32);

    @Override
    public Metrics collect() {
        return null;
    }
}
