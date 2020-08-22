package example.actuator.sink;

/**
 * 监控输出
 */
public interface Sink<T> {

    void output(T t);
}
