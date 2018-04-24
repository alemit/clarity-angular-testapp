package com.github.clarityangulartestapp.error;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class ErrorInfo implements Serializable {

    private static final long serialVersionUID = 2463051546096296872L;

    public String errorMessage;
    public String errorUCode;
    public String errorClass;
    public boolean fieldValidation;
    public List<ValidationErrorField> errors;

    public ErrorInfo() {
        setErrorUCode(randomErrorCodeGenerator());
    }

    public ErrorInfo(Throwable throwable) {
        throwable.printStackTrace();
        setErrorMessage(throwable);
        setErrorUCode(randomErrorCodeGenerator());
        setErrorClass(throwable);
        setFieldValidation(false);
    }

    public ErrorInfo(MethodArgumentNotValidException exception) {
        this.errorMessage = exception.getMessage();
        setErrorUCode(randomErrorCodeGenerator());
        setErrorClass(exception);

        if (errors == null) {
            errors = new ArrayList<ValidationErrorField>();
        }

        if (exception.getBindingResult() != null) {
            setFieldValidation(true);
            if (exception.getBindingResult().getAllErrors() != null && exception.getBindingResult().getAllErrors().size() > 0) {
                for (ObjectError error : exception.getBindingResult().getAllErrors()) {
                    errors.add(new ValidationErrorField(error.getObjectName(), ((FieldError) error).getField(), error.getDefaultMessage()));
                }
            }
        }
    }

    public ErrorInfo(NetworkInfoValidationException exception) {
        this.errorMessage = exception.getMessage();
        setErrorUCode(randomErrorCodeGenerator());
        setErrorClass(exception);
        setFieldValidation(true);

        if (errors == null) {
            errors = new ArrayList<ValidationErrorField>();
        }
        errors.add(exception.getValidationErrorField());
    }

    private String randomErrorCodeGenerator() {
        return UUID.randomUUID().toString();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Throwable throwable) {
        this.errorMessage = throwable.getLocalizedMessage();
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorUCode() {
        return errorUCode;
    }

    public void setErrorUCode(String errorUCode) {
        this.errorUCode = errorUCode;
    }

    public String getErrorClass() {
        return errorClass;
    }

    public void setErrorClass(Throwable throwable) {
        this.errorClass = throwable.getClass().getName();
    }

    public boolean isFieldValidation() {
        return fieldValidation;
    }

    public void setFieldValidation(boolean fieldValidation) {
        this.fieldValidation = fieldValidation;
    }

    public List<ValidationErrorField> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationErrorField> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("errorMessage: ").append(errorMessage).append(System.lineSeparator()).append("errorUCode: ").append(errorUCode)
                .append(System.lineSeparator()).append("errorClass: ").append(errorClass);
        return output.toString();
    }

    public String validationErrorInfo() {
        StringBuilder output = new StringBuilder();
        output.append("errorMessage: ").append(errorMessage).append(System.lineSeparator()).append("errorUCode: ").append(errorUCode)
                .append(System.lineSeparator()).append("errorClass: ").append(errorClass).append(System.lineSeparator()).append("errors: ")
                .append(errors.toString());
        return output.toString();
    }
}
