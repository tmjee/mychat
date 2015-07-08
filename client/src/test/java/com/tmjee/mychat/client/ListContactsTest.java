package com.tmjee.mychat.client;

import org.junit.Test;

import static com.tmjee.mychat.client.Static.*;

/**
 * @author tmjee
 */
public class ListContactsTest {

    @Test
    public void testListContacts() throws Exception {
        MYCHAT_CLIENT.logon("toby@gmail.com", "test");

        MYCHAT_CLIENT.listContacts(MYCHAT_CLIENT.getMySelf().getMyChatUserId(), 10, 0);

    }
}
