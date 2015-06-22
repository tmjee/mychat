package com.tmjee.mychat.exception;

/**
 * @author tmjee
 */
public class MyChatException extends RuntimeException {

    public MyChatException(String message, Exception e) {
        super(message, e);
    }
    public MyChatException(Exception e) {
        super(e);
    }
    public MyChatException(String message) {
        super(message);
    }
}
