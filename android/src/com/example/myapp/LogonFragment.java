package com.example.myapp;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.*;


/**
 * @author tmjee
 */
public class LogonFragment extends Fragment {


    public static LogonFragment newInstance() {
        Bundle bundle = new Bundle();
        LogonFragment fragment = new LogonFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_logon, container, false);

        final EditText email = (EditText) view.findViewById(R.id.email);
        final EditText password = (EditText) view.findViewById(R.id.password);

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Bundle bundle = new Bundle();
                    bundle.putString("email", email.getText().toString());
                    bundle.putString("password", password.getText().toString());

                    LogonAsyncTask asyncTask = new LogonAsyncTask(LogonFragment.this);
                    asyncTask.execute(bundle);

                    return true;
                }
                return false;
            }
        });
        return view;
    }


    public void prepareForLoading() {
        LinearLayout logonFrame = (LinearLayout) getView().findViewById(R.id.logonFrame);
        FrameLayout loadingFrame = (FrameLayout) getView().findViewById(R.id.loadingFrame);

        logonFrame.setVisibility(View.GONE);
        loadingFrame.setVisibility(View.VISIBLE);
    }

    public void doneOfLoading() {
        LinearLayout logonFrame = (LinearLayout) getView().findViewById(R.id.logonFrame);
        FrameLayout loadingFrame = (FrameLayout) getView().findViewById(R.id.loadingFrame);

        logonFrame.setVisibility(View.VISIBLE);
        loadingFrame.setVisibility(View.GONE);
    }


    /**
     * @author tmjee
     */
    private static class LogonAsyncTask extends AsyncTask<Bundle, Integer, Bundle> {

        private final LogonFragment logonFragment;

        public LogonAsyncTask(LogonFragment logonFragment) {
            this.logonFragment = logonFragment;
        }

        @Override
        protected Bundle doInBackground(Bundle... params) {
            try {
                Thread.sleep(1000);
                publishProgress(10);
                Thread.sleep(1000);
                publishProgress(10);
                Thread.sleep(1000);
                publishProgress(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            logonFragment.prepareForLoading();
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            super.onPostExecute(bundle);
            logonFragment.doneOfLoading();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            System.out.println(values);
        }

        @Override
        protected void onCancelled(Bundle bundle) {
            super.onCancelled(bundle);
            logonFragment.doneOfLoading();
        }
    }
}
