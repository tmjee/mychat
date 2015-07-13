package com.tmjee.mychat.client;

import org.junit.Test;

import static com.tmjee.mychat.client.Static.*;

/**
 * @author tmjee
 */
public class ChatDetailsTest {

    @Test
    public void testChatDetails() throws Exception {
        MYCHAT_CLIENT.logon("toby@gmail.com", "test");
        MYCHAT_CLIENT.chatDetails(1, MYCHAT_CLIENT.getMySelf().getMyChatUserId(), 0, 10);
    }
}
