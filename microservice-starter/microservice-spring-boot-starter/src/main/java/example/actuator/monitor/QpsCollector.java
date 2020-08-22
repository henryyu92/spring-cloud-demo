package example.actuator.monitor;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.search.Search;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QpsCollector {

    static {
        Metrics.addRegistry(new SimpleMeterRegistry());
    }

    public static void collect(String id){
        count(id);
    }

    public static void count(String methodName){
        // 调用次数
        Metrics.counter(
                "qps",
                "method", methodName,
                "timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .increment();


    }

    public static void statistic(){
        Search.in(Metrics.globalRegistry).meters().forEach(meter -> {
            StringBuilder builder = new StringBuilder();
            builder.append("name:")
                    .append(meter.getId().getName())
                    .append(",tags:")
                    .append(meter.getId().getTags())
                    .append(",type:").append(meter.getId().getType())
                    .append(",value:").append(meter.measure());
            System.out.println(builder.toString());
        });
    }

    public static void main(String[] args) {
        count("test");
        count("test");

        statistic();
    }
}
