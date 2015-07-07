package com.tmjee.mychat.client;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.tmjee.mychat.client.Static.*;

/**
 * @author tmjee
 */
public class ProfileTest {

    @Test
    public void testProfile() throws IOException, URISyntaxException {

        MYCHAT_CLIENT.logon("toby@gmail.com", "test");
        MySelfAware.MySelf myself = MYCHAT_CLIENT.getMySelf();


    }
}
