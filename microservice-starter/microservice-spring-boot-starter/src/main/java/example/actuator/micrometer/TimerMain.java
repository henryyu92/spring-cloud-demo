package example.actuator.micrometer;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TimerMain {

    private static final Random R = new Random();

    public static void main(String[] args) {
        MeterRegistry registry = new SimpleMeterRegistry();
        Timer timer = Timer.builder("my.timer")
                .description("timer for record time cost")
                .tags("tag", "test")
                .register(registry);

        timer.record(TimerMain::createOrder);


    }

    public static void createOrder() {
        try {
            TimeUnit.SECONDS.sleep(R.nextInt(100));
        } catch (Exception e) {

        }
    }
}
