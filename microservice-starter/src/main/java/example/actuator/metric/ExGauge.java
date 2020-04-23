package example.actuator.metric;

import io.micrometer.core.instrument.Gauge;

public class ExGauge implements Gauge {
    @Override
    public double value() {
        return 0;
    }

    @Override
    public Id getId() {
        return null;
    }
}
