package com.tmjee.mychat.client;

import org.junit.Test;

import static com.tmjee.mychat.client.Static.*;

/**
 * @author tmjee
 */
public class ListMyChatTest {

    @Test
    public void testListMyChats() throws Exception {
        MYCHAT_CLIENT.logon("toby@gmail.com", "test");
        MYCHAT_CLIENT.listMyChats(MYCHAT_CLIENT.getMySelf().getMyChatUserId(), 0, 10);
    }
}
