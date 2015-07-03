package com.aqua.dao.exceptions;

/**
 * @author: vzenkov
 */
public class CreateException extends RuntimeException {

    public CreateException() {
    }

    public CreateException(String message) {
        super(message);
    }

    public CreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateException(Throwable cause) {
        super(cause);
    }
}
