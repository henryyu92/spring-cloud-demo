package example.util;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import static java.time.temporal.ChronoField.*;

public class DateUtil {

    private static TemporalAdjuster nextDay = (temporal) -> temporal.with(DAY_OF_YEAR, temporal.range(DAY_OF_YEAR).getMaximum());
    private static TemporalAdjuster nextDayTime = (temporal) ->temporal.with(DAY_OF_YEAR, temporal.range(DAY_OF_YEAR).getMaximum());

    public static void main(String[] args) {

        // LocalDate & LocalTime & LocalDateTime
        LocalDate localDate1 = LocalDate.now();
        System.out.println(localDate1);
        LocalDate localDate2 = LocalDate.of(2020, Month.APRIL, 1);
        System.out.println(localDate2);
        System.out.println(localDate2.getMonth());
        System.out.println(localDate2.getDayOfMonth());
        System.out.println(localDate1.get(DAY_OF_YEAR));

        LocalTime localTime1 = LocalTime.now();
        System.out.println(localTime1);
        LocalTime localTime2 = LocalTime.of(10, 20, 30);
        System.out.println(localTime2);
        System.out.println(localTime1.get(HOUR_OF_DAY));

        LocalDateTime localDateTime1 = LocalDateTime.now();
        System.out.println(localDateTime1);
        LocalDateTime localDateTime2 = LocalDateTime.of(2020, Month.AUGUST, 1, 12, 20, 30);
        System.out.println(localDate1.atTime(localTime1));
        System.out.println(localTime1.atDate(localDate1));
        System.out.println(localDateTime1.get(CLOCK_HOUR_OF_DAY));

        // Instant & Clock
        System.out.println(Instant.now());
        System.out.println(Instant.now().toEpochMilli());


        // Duration & Period
        Duration duration1 = Duration.between(localTime1, localTime2);
        System.out.println(duration1);
        System.out.println(Duration.between(localDateTime1, localDateTime2));
        System.out.println(Duration.of(2, ChronoUnit.HOURS));

        Period period = Period.between(localDate1, localDate2);
        System.out.println(period);


        LocalDate localDate = LocalDate.of(2020, Month.JANUARY, 1);
        LocalDate updateLocalDate1 = localDate.withYear(2008);
        System.out.println(localDate + " " + updateLocalDate1);
        LocalDate updateLocalDate2 = localDate.with(YEAR, 2019).with(MONTH_OF_YEAR, 10).with(DAY_OF_MONTH, 1);
        System.out.println(localDate + " " + updateLocalDate2);

        System.out.println(localDate.with(TemporalAdjusters.lastDayOfMonth()));
        System.out.println(localDate.with(TemporalAdjusters.firstInMonth(DayOfWeek.FRIDAY)));


        // DateTimeFormatter

    }

    // localDate & localTime & localDateTime
    public LocalDate lastDayOfMonth(LocalDate current){
        return current.with(TemporalAdjusters.lastDayOfYear());
    }

    public LocalDateTime nextDay(LocalDateTime current){
        return current.with(nextDayTime);
    }


    // ZoneId & ZoneOffset & ZonedDateTime




}
