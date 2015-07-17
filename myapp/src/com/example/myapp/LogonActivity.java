package com.example.myapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * @author tmjee
 */
public class LogonActivity extends Activity {

    public static final String INTENT_EXTRA_ORIGINAL_ACTIVITY = "INTENT_EXTRA_ORIGINAL_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntent().getStringExtra(INTENT_EXTRA_ORIGINAL_ACTIVITY);

        setContentView(R.layout.a_logon);
        getFragmentManager().beginTransaction()
                .replace(R.id.f_container, LogonFragment.newInstance())
                .commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private static class LogonAsyncTask extends AsyncTask<Bundle, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Bundle... params) {
            return null;
        }
    }
}
