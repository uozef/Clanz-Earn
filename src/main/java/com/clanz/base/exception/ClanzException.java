package com.clanz.base.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter
@Setter
public class ClanzException extends RuntimeException {
    private final Exception exception;
    private final HttpStatus statusCode;
    private final String message;
    private final String logMessage;

    public ClanzException(HttpStatus status, String message) {
        this(null, status, message, null);
    }

    public ClanzException(Exception ex, HttpStatus status, String message) {
        this(ex, status, message, null);
    }

    public ClanzException(HttpStatus status, String message, String logMessage) {
        this(null, status, message, logMessage);
    }

    public ClanzException(Exception ex, HttpStatus status, String message, String logMessage) {
        this.exception = ex;
        this.statusCode = status;
        this.message = message;
        this.logMessage = logMessage;
    }
}
