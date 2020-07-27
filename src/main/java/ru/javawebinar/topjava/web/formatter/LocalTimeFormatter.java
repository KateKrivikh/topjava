package ru.javawebinar.topjava.web.formatter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalTimeFormatter implements Formatter<LocalTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public LocalTime parse(String s, Locale locale) throws ParseException {
        return LocalTime.parse(s, formatter);
    }

    @Override
    public String print(LocalTime localTime, Locale locale) {
        return formatter.format(localTime);
    }
}
