package com.example.myapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        return view;
    }
}
