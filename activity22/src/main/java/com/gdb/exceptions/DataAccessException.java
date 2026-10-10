package com.gdb.exceptions;

/** Unchecked wrapper so database failures do not force SQLException into every caller. */
public class DataAccessException extends RuntimeException {
    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
