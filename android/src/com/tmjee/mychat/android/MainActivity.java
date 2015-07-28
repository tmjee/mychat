package com.tmjee.mychat.android;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AbstractAuthenticationActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!super.onOptionsItemSelected(item)) {
            int itemId = item.getItemId();
            switch(itemId) {
                case R.id.chatsOptionsMenuItem:
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.f_container, ChatsFragment.newInstance())
                            .commit();
                    return true;
                case R.id.contactsOptionsMenuItem:
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.f_container, ContactsFragment.newInstance())
                            .commit();
                    return true;
                case R.id.discoverOptionsMenuItem:
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.f_container, DiscoverFragment.newInstance())
                            .commit();
                    return true;
                case R.id.myselfOptionsMenuItem:
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.f_container, MyselfFragment.newInstance())
                            .commit();
                    return true;
            }
        }
        return false;
    }
}
