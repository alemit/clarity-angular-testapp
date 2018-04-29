package com.github.clarityangulartestapp.error;

public class UnathorizedAccessException extends RuntimeException {
    private static final long serialVersionUID = 7558910938524569035L;
    private String validationMsg;

    public UnathorizedAccessException() {
    }

    public UnathorizedAccessException(String validationMsg) {
        super(new Exception(validationMsg));
        this.setValidationMsg(validationMsg);
    }

    public String getValidationMsg() {
        return validationMsg;
    }

    public void setValidationMsg(String validationMsg) {
        this.validationMsg = validationMsg;
    }
}
