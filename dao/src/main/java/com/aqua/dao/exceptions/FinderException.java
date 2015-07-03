package com.aqua.dao.exceptions;

/**
 * @author: vzenkov
 */
public class FinderException extends RuntimeException {

    public FinderException() {
    }

    public FinderException(String message) {
        super(message);
    }

    public FinderException(String message, Throwable cause) {
        super(message, cause);
    }

    public FinderException(Throwable cause) {
        super(cause);
    }
}
