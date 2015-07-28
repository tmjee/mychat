package com.tmjee.mychat.android;

import android.app.Fragment;
import android.os.Bundle;

/**
 * @author tmjee
 */
public class ChatsFragment extends Fragment {


    public static ChatsFragment newInstance() {
        Bundle bundle = new Bundle();
        ChatsFragment chatsFragment = new ChatsFragment();
        chatsFragment.setArguments(bundle);
        return chatsFragment;
    }
}
