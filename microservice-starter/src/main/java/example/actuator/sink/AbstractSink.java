package example.actuator.sink;

import example.actuator.source.Source;

public abstract class AbstractSink<T> implements Sink<T> {

    private Source source;

    @Override
    public void output(T t) {

    }
}
