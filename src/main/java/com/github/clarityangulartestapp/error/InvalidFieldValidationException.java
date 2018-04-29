package com.github.clarityangulartestapp.error;

public class InvalidFieldValidationException extends Exception {

    private static final long serialVersionUID = 8370163225294378278L;

    private String validationMsg;
    private ValidationErrorField validationErrorField;

    public InvalidFieldValidationException(String validationMsg, ValidationErrorField validationErrorField) {
        super(new Exception(validationMsg));
        this.setValidationMsg(validationMsg);
        this.setValidationErrorField(validationErrorField);
    }

    public String getValidationMsg() {
        return validationMsg;
    }

    public void setValidationMsg(String validationMsg) {
        this.validationMsg = validationMsg;
    }

    public ValidationErrorField getValidationErrorField() {
        return validationErrorField;
    }

    public void setValidationErrorField(ValidationErrorField validationErrorField) {
        this.validationErrorField = validationErrorField;
    }
}
