package ru.javawebinar.topjava.web.formatter;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Set;

public class LocalDateTimeFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<LocalDateTimeFormat> {
    private static final Set<Class<?>> FIELD_TYPES = Collections.unmodifiableSet(Set.of(LocalDate.class, LocalTime.class));

    public LocalDateTimeFormatAnnotationFormatterFactory() {
    }

    @Override
    public Set<Class<?>> getFieldTypes() {
        return FIELD_TYPES;
    }

    @Override
    public Printer<?> getPrinter(LocalDateTimeFormat annotation, Class<?> fieldType) {
        return getFormatter(fieldType);
    }

    @Override
    public Parser<?> getParser(LocalDateTimeFormat annotation, Class<?> fieldType) {
        return getFormatter(fieldType);
    }

    protected Formatter<?> getFormatter(Class<?> fieldType) {
        if (fieldType == LocalDate.class) {
            return new LocalDateFormatter();
        } else if (fieldType == LocalTime.class) {
            return new LocalTimeFormatter();
        }
        throw new IllegalArgumentException();
    }
}
