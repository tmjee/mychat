package com.tmjee.mychat.client;

import org.junit.Test;

import static com.tmjee.mychat.client.Static.*;

/**
 * @author tmjee
 */
public class ListMomentTest {

    @Test
    public void testListMoment() throws Exception {
        MYCHAT_CLIENT.logon("toby@gmail.com", "test");
        MYCHAT_CLIENT.listMoments(MYCHAT_CLIENT.getMySelf().getMyChatUserId(), 0, 10);
    }
}
