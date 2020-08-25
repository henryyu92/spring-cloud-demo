package example.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import static java.time.temporal.ChronoField.DAY_OF_YEAR;

public class DateUtil {

    private static TemporalAdjuster nextDay = (temporal) -> temporal.with(DAY_OF_YEAR, temporal.range(DAY_OF_YEAR).getMaximum());
    private static TemporalAdjuster nextDayTime = (temporal) ->{temporal.with(DAY_OF_YEAR, temporal.range(DAY_OF_YEAR).getMaximum())}

    public long current() {
        return Instant.now().toEpochMilli();
    }

    public LocalDate lastDayOfMonth(LocalDate current){
        return current.with(TemporalAdjusters.lastDayOfYear());
    }

    public LocalDateTime nextDay(LocalDateTime current){
        return current.with(nextDayTime);
    }
}
