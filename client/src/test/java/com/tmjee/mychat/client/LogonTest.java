package com.tmjee.mychat.client;


import org.junit.Test;

import java.util.Map;
import java.util.logging.Logger;

import static com.tmjee.mychat.client.Static.*;

/**
 * @author tmjee
 */
public class LogonTest {

    private static final Logger LOGGER = Logger.getLogger(LogonTest.class.getName());

    @Test
    public void testGoodLogon() throws Exception {
        LOGGER.info("test good logon");
        Map<String, Object> m = MYCHAT_CLIENT.logon("toby@gmail.com", "test");
    }

    @Test
    public void testBadPasswordLogon() throws Exception {
        LOGGER.info("test bad password logon");
        Map<String, Object> m = MYCHAT_CLIENT.logon("toby@gmail.com", "xxx");
    }

    @Test
    public void testBadUsernameLogon() throws Exception {
        LOGGER.info("test bad username logon");
        Map<String, Object> m = MYCHAT_CLIENT.logon("xxx@gmail.com", "xxx");
    }

}
