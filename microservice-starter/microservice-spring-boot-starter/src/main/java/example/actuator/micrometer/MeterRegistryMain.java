package example.actuator.micrometer;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

public class MeterRegistryMain {

    public static void main(String[] args) {

        compositeMeterRegistry();

        simpleMeterRegistry();

    }


    private static void compositeMeterRegistry(){
        CompositeMeterRegistry composite = new CompositeMeterRegistry();

        composite.add(new SimpleMeterRegistry());


        Counter counter = composite.counter("counter", "tagName", "tagValue");

        System.out.println(counter.getClass().getName());
        counter.increment();

        System.out.println(counter.measure());
    }

    private static void simpleMeterRegistry(){
        SimpleMeterRegistry simple = new SimpleMeterRegistry();

        Counter counter = simple.counter("simple_counter");

        System.out.println(counter.getClass().getName());

        counter.increment();

        System.out.println(counter.measure());
    }

}
