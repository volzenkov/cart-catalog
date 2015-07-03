package com.aqua.dao.exceptions;

/**
 * @author: vzenkov
 */
public class UpdateException extends RuntimeException {

    public UpdateException() {
    }

    public UpdateException(String message) {
        super(message);
    }

    public UpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateException(Throwable cause) {
        super(cause);
    }
}
