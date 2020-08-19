package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.util.ValidationUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView conflict(HttpServletRequest req, DataIntegrityViolationException e) throws Exception {
        log.error("Exception at request " + req.getRequestURL(), e);
        Throwable modified = ValidationUtil.transformExceptionIfUniqueKeyDuplication(e);

        return createExceptionView(modified, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        log.error("Exception at request " + req.getRequestURL(), e);
        Throwable rootCause = ValidationUtil.getRootCause(e);

        return createExceptionView(rootCause, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ModelAndView createExceptionView(Throwable e, HttpStatus httpStatus) {
        ModelAndView mav = new ModelAndView("exception",
                Map.of("exception", e, "message", e.toString(), "status", httpStatus));

        mav.setStatus(httpStatus);

        // Interceptor is not invoked, put userTo
        AuthorizedUser authorizedUser = SecurityUtil.safeGet();
        if (authorizedUser != null) {
            mav.addObject("userTo", authorizedUser.getUserTo());
        }

        return mav;
    }
}
