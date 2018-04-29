package com.github.clarityangulartestapp.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorInfo handleAnyException(Exception exception) {
        ErrorInfo errorInfo = new ErrorInfo(exception);
        logger.error("[handleAnyException]: " + errorInfo, exception);
        return errorInfo;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfo handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ErrorInfo errorInfo = new ErrorInfo(exception);
        logger.trace("[handleMethodArgumentNotValidException]: " + errorInfo.validationErrorInfo());
        return errorInfo;
    }

    @ExceptionHandler(InvalidFieldValidationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfo handleNetworkInfoValidationException(InvalidFieldValidationException exception) {
        ErrorInfo errorInfo = new ErrorInfo(exception);
        logger.trace("[handleNetworkInfoValidationException]: " + errorInfo.validationErrorInfo());
        return errorInfo;
    }

    @ExceptionHandler(UnathorizedAccessException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorInfo handleUnautorizedException(UnathorizedAccessException exception) {
        ErrorInfo errorInfo = new ErrorInfo(exception);
        logger.trace("[handleUnautorizedException]: " + errorInfo.validationErrorInfo());
        return errorInfo;
    }
}
