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
        m = MYCHAT_CLIENT.logon("toby@gmail.com", "xxxx");
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

/*    public static void main(String[] args) throws Exception {

        MyChatClient c = new MyChatClientBuilder()
                .withApplicationToken("75b92637-2750-4eb9-8da2-7df486fbfb23")
                .withHostConnectionUrl("http://localhost:8080/v1")
                .build();

        Map<String, Object> m = c.logon("toby@gmail.com", "test");

        String uuid = UUID.randomUUID().toString();

        m = c.register(uuid+"@gmail.com", "test", uuid, GenderEnum.FEMALE);
    }*/
}
