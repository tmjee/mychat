package com.tmjee.mychat.client;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.tmjee.mychat.client.Static.*;

/**
 * @author tmjee
 */
public class PostMomentTest {

    @Test
    public void testPostMoment() throws Exception {
        MYCHAT_CLIENT.logon("toby@gmail.com", "test");
        MYCHAT_CLIENT.postMoment(MYCHAT_CLIENT.getMySelf().getMyChatUserId(),
                "this is my moment "+System.currentTimeMillis(),
                null, null, null);

        Path p = Paths.get(getClass().getResource("/avatar_1.jpg").toURI());

        MYCHAT_CLIENT.postMoment(MYCHAT_CLIENT.getMySelf().getMyChatUserId(),
                "this is my moment with image "+System.currentTimeMillis(),
                p.getFileName().toString(), "image/jpeg", Files.readAllBytes(p));
    }
}
