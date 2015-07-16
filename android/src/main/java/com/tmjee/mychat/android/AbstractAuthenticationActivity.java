package com.tmjee.mychat.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * @author tmjee
 */
public class AbstractAuthenticationActivity extends Activity {

    public static final String AUTH_PREFERENCES = "AUTH_PREFERENCES";
    public static final String APPLICATION_TOKEN = "d5b87197-495c-47ca-959d-332921004a52";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences(AUTH_PREFERENCES, MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken", null);
        if (accessToken == null) {

            Intent i = new Intent(this, LogonActivity.class);
            i.putExtra(LogonActivity.INTENT_EXTRA_ORIGINAL_ACTIVITY, getClass().getName());
            startActivity(i);
            finish();
        }
    }
}
