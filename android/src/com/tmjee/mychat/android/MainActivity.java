package com.tmjee.mychat.android;

import android.os.Bundle;

public class MainActivity extends AbstractAuthenticationActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
