package com.tmjee.mychat.android;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MainActivity extends AbstractAuthenticationActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InternalTabListener tabListener = new InternalTabListener();

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(
                actionBar.newTab()
                    .setText("Chats")
                    .setTabListener(tabListener));
        actionBar.addTab(
                actionBar.newTab()
                    .setText("Contacts")
                    .setTabListener(tabListener));
        actionBar.addTab(
                actionBar.newTab()
                    .setText("Discover")
                    .setTabListener(tabListener));
        actionBar.addTab(
                actionBar.newTab()
                    .setText("Myself")
                    .setTabListener(tabListener));
        setContentView(R.layout.main);
    }



    private static class InternalTabListener implements ActionBar.TabListener {

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            fragmentTransaction.replace()
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }
    }
}
