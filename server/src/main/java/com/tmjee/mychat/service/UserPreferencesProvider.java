package com.tmjee.mychat.service;

import com.google.inject.Provider;

/**
 * @author tmjee
 */
public class UserPreferencesProvider implements Provider<UserPreferences> {

    @Override
    public UserPreferences get() {
        return new UserPreferences();
    }
}
