package com.basecamp.exception;

public class InvalidStatusException extends BaseException {
    public static final String ERROR = "Status validation failed. ";

    public InvalidStatusException(String message) {
        super(ERROR + message);
    }

    public InvalidStatusException(final String fmt, final Object... args) {
        super(fmt, args);
    }
}
