package example.actuator.micrometer;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

public class LongTaskMain {

    public static void main(String[] args) {

        MeterRegistry registry = new SimpleMeterRegistry();

        registry.more().longTaskTimer("longTaskTimer").record(
                () -> {
                    // Task 逻辑
                }
        );
    }
}
