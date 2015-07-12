package com.tmjee.mychat.client;

import com.tmjee.mychat.common.domain.GenderEnum;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.UUID;

import static com.tmjee.mychat.client.Static.*;

/**
 * @author tmjee
 */
public class AddContactTest {

    @Test
    public void testAddContact() throws IOException, URISyntaxException {
        String u1 = UUID.randomUUID().toString();

        Map<String, Object> m = MYCHAT_CLIENT.register(u1+"@gmail.com", "test", u1, GenderEnum.FEMALE);

        String activationToken = m.get("activationToken").toString();
        Integer registeredMyChatUserId = (Integer) m.get("myChatUserId");

        MYCHAT_CLIENT.activate(activationToken);

        MYCHAT_CLIENT.logon("toby@gmail.com", "test");
        MYCHAT_CLIENT.addContact(registeredMyChatUserId);
    }
}
