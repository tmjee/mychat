package com.tmjee.mychat.server.exception;

/**
 * @author tmjee
 */
public class InvalidApplicationTokenException extends MyChatException {
    public InvalidApplicationTokenException(String message) {
        super(message);
    }

    public InvalidApplicationTokenException(String message, Exception e) {
        super(message, e);
    }
}
