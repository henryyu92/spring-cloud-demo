package example.vbc.convert.formatter;


import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


public class FormatterEntity {

    @DateTimeFormat
    private Long startTime;

    @DateTimeFormat
    private Date endTime;

    @DateTimeFormat
    private LocalDateTime localDateTime;

    @DateTimeFormat
    private LocalDate localDate;

    @DateTimeFormat
    private Instant instant;
}
