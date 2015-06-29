package com.tmjee.mychat.server.domain;

import org.jooq.Record;

/**
 * @author tmjee
 */
public class LogonResult {

    private final boolean ok;
    private final String message;
    private final String accessToken;

    private LogonResult(boolean ok, String message) {
        this.ok = ok;
        this.message = message;
        this.accessToken = null;
    }

    private LogonResult(boolean ok, String message, String accessToken) {
        this.ok = ok;
        this.message = message;
        this.accessToken = accessToken;
    }

    public boolean isOk() {
        return ok;
    }

    public String message() {
        return message;
    }

    public String accessToken() {
        return accessToken;
    }

    public static final LogonResult failed() {
        return new LogonResult(false, "logon failed");
    }

    public static final LogonResult success(String accessToken, Record record) {
        return new LogonResult(true, "logon success", accessToken);
    }



}
