package com.tmjee.mychat.client;

import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;

import static com.tmjee.mychat.client.Static.*;

/**
 * @author tmjee
 */
public class CreateMyChatTest {

    @Test
    public void testCreateMyChat() throws Exception {
        MYCHAT_CLIENT.logon("toby@gmail.com", "test");
        MYCHAT_CLIENT.createMyChat(
                MYCHAT_CLIENT.getMySelf().getMyChatUserId(),
                Arrays.asList(new Integer[]{2, 3, 4, 5}),
                "testChat_"+ System.currentTimeMillis());
    }
}
