package com.tmjee.mychat.service;

/**
 * @author tmjee
 */
public class UserPreferences {

    private String accessToken;
    private String applicationToken;


    public synchronized void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public synchronized String getAccessToken() {
        return this.accessToken;
    }

    public synchronized void setApplicationToken(String applicationToken) {
        this.applicationToken = applicationToken;
    }

    public synchronized String getApplicationToken() {
        return applicationToken;
    }
}
