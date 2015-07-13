package com.tmjee.mychat.client;

import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static com.tmjee.mychat.client.Static.*;

/**
 * @author tmjee
 */
public class JoinChatTest {

    @Test
    public void test() throws Exception {

        MYCHAT_CLIENT.logon("toby@gmail.com", "test");
        Map<String, Object> m = MYCHAT_CLIENT.createMyChat(MYCHAT_CLIENT.getMySelf().getMyChatUserId(),
                Arrays.asList(new Integer[]{2, 3}), "Chat_"+System.currentTimeMillis());

        Integer chatId = (Integer)m.get("chatId");

        MYCHAT_CLIENT.joinChat(chatId,
                MYCHAT_CLIENT.getMySelf().getMyChatUserId(),
                Arrays.asList(new Integer[] { 1, 4, 5}));
    }
}
