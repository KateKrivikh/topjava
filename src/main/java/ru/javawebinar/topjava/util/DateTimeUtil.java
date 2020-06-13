package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.function.Function;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    public static boolean isBetweenHalfOpen(LocalDateTime current, LocalDateTime start, LocalDateTime end) {
        return current.compareTo(start) >= 0 && current.compareTo(end) < 0;
    }


//    public static <T extends TemporalAccessor> boolean isBetweenHalfOpen(T currentPeriod, T startPeriod, T endPeriod) {
//        Function<TemporalAccessor, Comparable<? extends TemporalAccessor>> from = currentPeriod.isSupported(ChronoField.YEAR) ? LocalDateTime::from : LocalTime::from;
//
//        Comparable<? extends TemporalAccessor> current = from.apply(currentPeriod);
//        Comparable<? extends TemporalAccessor> start = from.apply(startPeriod);
//        Comparable<? extends TemporalAccessor> end = from.apply(endPeriod);
//
//        // TODO why is not compiled?
//        return current.compareTo(start) >= 0 && current.compareTo(end) < 0;
//    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

