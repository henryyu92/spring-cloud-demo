package example.actuator.micrometer;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class GaugeMain {

    private static final MeterRegistry MR = new SimpleMeterRegistry();
    private static final BlockingQueue<String> QUEUE = new ArrayBlockingQueue<>(500);
    private static BlockingQueue<String> REAL_QUEUE;

    static {
        REAL_QUEUE = MR.gauge("messageGauge", QUEUE, Collection::size);
    }

    public static void main(String[] args) throws Exception {

        consume();

        REAL_QUEUE.put("message");


    }

    private static void consume() throws Exception {
        new Thread(() -> {
            while (true) {
                try {
                    String message = REAL_QUEUE.take();
                    //handle message
                    System.out.println(message);
                } catch (InterruptedException e) {
                    //no-op
                }
            }
        }).start();
    }
}
