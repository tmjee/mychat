package com.tmjee.mychat.android;

import android.os.Bundle;
import com.tmjee.mychat.android.R;

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
