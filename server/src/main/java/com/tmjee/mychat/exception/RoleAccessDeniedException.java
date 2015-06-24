package com.tmjee.mychat.exception;

/**
 * @author tmjee
 */
public class RoleAccessDeniedException extends MyChatException {
    public RoleAccessDeniedException(String message, Exception e) {
        super(message, e);
    }

    public RoleAccessDeniedException(Exception e) {
        super(e);
    }

    public RoleAccessDeniedException(String message) {
        super(message);
    }
}
