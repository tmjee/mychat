package com.tmjee.mychat.client;


import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.tmjee.mychat.client.Static.*;

/**
 * @author tmjee
 */
public class UpdateWhatsupTest {

    @Test
    public void testUpdateWhatsup() throws IOException, URISyntaxException {
        MYCHAT_CLIENT.logon("toby@gmail.com", "test");
        MYCHAT_CLIENT.whatsUp(MYCHAT_CLIENT.getMySelf().getMyChatUserId(), "What's up from Toby "+System.currentTimeMillis());
    }
}
