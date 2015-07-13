package com.tmjee.mychat.client;

import org.junit.Test;

import static com.tmjee.mychat.client.Static.*;

/**
 * @author tmjee
 */
public class SendChatTest {

    @Test
    public void testSendChat() throws Exception {
        MYCHAT_CLIENT.logon("toby@gmail.com", "test");

        MYCHAT_CLIENT.sendChat(1,
                MYCHAT_CLIENT.getMySelf().getMyChatUserId(),
                "my chat message "+System.currentTimeMillis());
    }
}

