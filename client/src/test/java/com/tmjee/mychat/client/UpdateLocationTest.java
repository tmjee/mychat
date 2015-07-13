package com.tmjee.mychat.client;

import static com.tmjee.mychat.client.Static.*;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author tmjee
 */
public class UpdateLocationTest {

    @Test
    public void testUpdateLocation() throws Exception {

        float latitude = ThreadLocalRandom.current().nextFloat();
        float longitude = ThreadLocalRandom.current().nextFloat();

        MYCHAT_CLIENT.logon("toby@gmail.com", "test");
        MYCHAT_CLIENT.updateLocation(MYCHAT_CLIENT.getMySelf().getMyChatUserId(),
                UUID.randomUUID().toString(), latitude, longitude);
    }
}
