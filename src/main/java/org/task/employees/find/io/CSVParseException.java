package org.task.employees.find.io;

public class CSVParseException extends RuntimeException {
    public CSVParseException(String message) {
        super(message);
    }

    public CSVParseException(String message, Throwable cause) {
        super(message, cause);
    }
}

