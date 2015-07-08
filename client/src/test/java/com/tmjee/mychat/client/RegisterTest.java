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
public class RegisterTest {

    private static final Logger LOG = Logger.getLogger(RegisterTest.class.getName());

    @Test
    public void testGoodRegister() throws Exception {
        LOG.log(Level.INFO, "test good register");
        String uuid = UUID.randomUUID().toString();
        Map<String, Object> m = MYCHAT_CLIENT.register(uuid+"@gmail.com", "test", uuid, GenderEnum.FEMALE);
    }
}
