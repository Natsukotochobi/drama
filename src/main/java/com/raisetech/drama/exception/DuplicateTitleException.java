package com.raisetech.drama.exception;

public class DuplicateTitleException extends RuntimeException {
    public DuplicateTitleException() {
        super();
    }

    public DuplicateTitleException(String message) {
        super(message);
    }

    public DuplicateTitleException(Throwable cause) {
        super(cause);
    }

    public DuplicateTitleException(String message, Throwable cause) {
        super(message, cause);
    }
}
