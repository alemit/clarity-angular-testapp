package com.github.clarityangulartestapp.error;

import java.io.Serializable;

public class ValidationErrorField implements Serializable {

    private static final long serialVersionUID = -943242952153648899L;

    private String objectName;
    private String field;
    private String errorMsg;

    public ValidationErrorField(String objectName, String field, String errorMsg) {
        super();
        this.objectName = objectName;
        this.field = field;
        this.errorMsg = errorMsg;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("objectName: ").append(objectName).append(", field: ").append(field).append(", errorMsg: ").append(errorMsg);
        return output.toString();
    }
}
