package com.tmjee.mychat.client;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Logger;

import static com.tmjee.mychat.client.Static.*;

/**
 * @author tmjee
 */
public class PostAvatarTest {

    private static final Logger LOGGER = Logger.getLogger(PostAvatarTest.class.getName());

    @Test
    public void testPostAvatar() throws Exception {

        LOGGER.info("test good logon");
        Map<String, Object> m = MYCHAT_CLIENT.logon("toby@gmail.com", "test");

        Path p = Paths.get(getClass().getResource("/avatar_1.jpg").toURI());
        m = MYCHAT_CLIENT.postAvatar(MYCHAT_CLIENT.getMySelf().getMyChatUserId(), "image/jpg", Files.readAllBytes(p));
    }
}
