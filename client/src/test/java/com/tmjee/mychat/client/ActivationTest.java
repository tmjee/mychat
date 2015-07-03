package com.tmjee.mychat.client;

import com.tmjee.mychat.common.domain.GenderEnum;
import org.junit.Test;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.tmjee.mychat.client.Static.*;

/**
 * @author tmjee
 */
public class ActivationTest {

    private static final Logger LOGGER = Logger.getLogger(ActivationTest.class.getName());

    @Test
    public void testActivate() throws Exception {
        LOGGER.log(Level.INFO, "testActivate");
        String uuid = UUID.randomUUID().toString();
        Map<String, Object> m = MYCHAT_CLIENT.register(uuid+"@gmail.com", "test", uuid, GenderEnum.FEMALE);

        String activationToken = (String) m.get("activationToken");

        LOGGER.log(Level.INFO, "activate registration "+m.get("myChatUserId"));

        m = MYCHAT_CLIENT.activate(activationToken);
    }
}
