package com.tmjee.mychat.domain;

import org.jooq.Record;

/**
 * @author tmjee
 */
public class LogonResult {

    private String message;
    private String accessToken;

    private LogonResult(String message, String accessToken) {

    }

    public static final LogonResult failed() {

        return null;
    }

    public static final LogonResult success(String accessToken, Record record) {

        return null;

    }



}
