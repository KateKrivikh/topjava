package ru.javawebinar.topjava.util;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import ru.javawebinar.topjava.HasId;
import ru.javawebinar.topjava.util.exception.IllegalRequestDataException;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.*;
import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidationUtil {
    public static final String USER_DUPLICATE_EMAIL_MESSAGE = "user.duplicatedKey";
    public static final String MEAL_DUPLICATE_DATETIME_MESSAGE = "meal.duplicatedKey";

    private static final Validator validator;

    static {
        //  From Javadoc: implementations are thread-safe and instances are typically cached and reused.
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        //  From Javadoc: implementations of this interface must be thread-safe
        validator = factory.getValidator();
    }

    private ValidationUtil() {
    }

    public static <T> void validate(T bean) {
        // https://alexkosarev.name/2018/07/30/bean-validation-api/
        Set<ConstraintViolation<T>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean + " must be with id=" + id);
        }
    }

    //  http://stackoverflow.com/a/28565320/548473
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static String getErrorResponseForHtml(BindingResult result) {
        return getErrorResponse(result).collect(Collectors.joining("<br>"));
    }

    public static String getErrorResponseForRest(BindingResult result) {
        return getErrorResponse(result).collect(Collectors.joining("\",\"", "[\"", "\"]"));
    }

    private static Stream<String> getErrorResponse(BindingResult result) {
        return result.getFieldErrors().stream()
                .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()));
    }

    public static Exception transformExceptionIfUniqueKeyDuplication(DataIntegrityViolationException e) {
        Throwable rootCause = e.getRootCause();
        if (rootCause instanceof SQLException && "23505".equals(((SQLException) rootCause).getSQLState()))
            if (rootCause.getMessage().contains("users_unique_email_idx"))
                return new IllegalRequestDataException(USER_DUPLICATE_EMAIL_MESSAGE);
            else if (rootCause.getMessage().contains("meals_unique_user_datetime_idx"))
                return new IllegalRequestDataException(MEAL_DUPLICATE_DATETIME_MESSAGE);
        return e;
    }
}