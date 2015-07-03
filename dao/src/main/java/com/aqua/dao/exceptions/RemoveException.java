package com.aqua.dao.exceptions;

/**
 * @author: vzenkov
 */
public class RemoveException extends RuntimeException {

    public RemoveException() {
    }

    public RemoveException(String message) {
        super(message);
    }

    public RemoveException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoveException(Throwable cause) {
        super(cause);
    }
}
