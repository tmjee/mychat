package com.tmjee.mychat.server.service;

import com.tmjee.mychat.server.domain.RolesEnum;

import java.util.EnumSet;

/**
 * @author tmjee
 */
public class UserPreferences {

    private String accessToken;
    private String applicationToken;
    private EnumSet<RolesEnum> roles;


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

    public synchronized void setRoles(EnumSet<RolesEnum> roles) {
        this.roles = roles;
    }

    public synchronized EnumSet<RolesEnum> getRoles() {
        return this.roles;
    }
}
