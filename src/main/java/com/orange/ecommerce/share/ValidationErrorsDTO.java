package com.orange.ecommerce.share;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorsDTO {

    private List<String> globalErrorMessages = new ArrayList<>();
    private List<FieldErrorDTO> fieldErrors = new ArrayList<>();

    public void addError(String message) {
        globalErrorMessages.add(message);
    }

    public void addFieldError(String field, String message) {
        FieldErrorDTO fieldError = new FieldErrorDTO(field, message);
        fieldErrors.add(fieldError);
    }

    public List<String> getGlobalErrorMessages() {
        return globalErrorMessages;
    }

    public List<FieldErrorDTO> getFieldErrors() {
        return fieldErrors;
    }
}

class FieldErrorDTO {
    private String field;
    private String message;

    public FieldErrorDTO(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
