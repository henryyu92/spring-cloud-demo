package example.actuator.monitor;

public interface Monitor {

    @Monitored("qps")
    void test();
}
