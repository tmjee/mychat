package com.tmjee.mychat.client;

import org.junit.Test;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.tmjee.mychat.client.Static.*;

/**
 * @author tmjee
 */
public class GetAvatarTest {

    private static final Logger LOGGER = Logger.getLogger(GetAvatarTest.class.getName());

    @Test
    public void testGetAvatar() throws Exception {

        LOGGER.log(Level.INFO, "testGetAvtar");
        Map<String, Object> m = MYCHAT_CLIENT.logon("toby@gmail.com", "test");

        m = MYCHAT_CLIENT.getAvatar(MYCHAT_CLIENT.getMySelf().getMyChatUserId());
    }
}
