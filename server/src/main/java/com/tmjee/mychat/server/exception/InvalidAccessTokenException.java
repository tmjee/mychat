package com.tmjee.mychat.server.exception;

/**
 * @author tmjee
 */
public class InvalidAccessTokenException extends MyChatException {
    public InvalidAccessTokenException(String message) {
        super(message);
    }
    public InvalidAccessTokenException(String message, Exception e) {
        super(message, e);
    }
}
