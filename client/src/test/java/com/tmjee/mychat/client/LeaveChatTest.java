package com.tmjee.mychat.client;

import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static com.tmjee.mychat.client.Static.*;

/**
 * @author tmjee
 */
public class LeaveChatTest {

    @Test
    public void testLeaveChat() throws Exception {

        MYCHAT_CLIENT.logon("toby@gmail.com", "test");

        MYCHAT_CLIENT.logon("toby@gmail.com", "test");
        Map<String, Object> m = MYCHAT_CLIENT.createMyChat(MYCHAT_CLIENT.getMySelf().getMyChatUserId(),
                Arrays.asList(new Integer[]{1, 2, 3, 4, 5}), "Chat_"+System.currentTimeMillis());

        Integer chatId = (Integer)m.get("chatId");

        MYCHAT_CLIENT.logon("jack@gmail.com", "test");
        MYCHAT_CLIENT.leaveChat(chatId,
                MYCHAT_CLIENT.getMySelf().getMyChatUserId());

        MYCHAT_CLIENT.logon("biz@gmail.com", "test");
        MYCHAT_CLIENT.leaveChat(chatId,
                MYCHAT_CLIENT.getMySelf().getMyChatUserId());
    }
}
