package org.task.employees.upload;

public class ApiError {

    private final String errorMessage;
    public ApiError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
