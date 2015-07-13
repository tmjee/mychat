package com.tmjee.mychat.client;

import com.tmjee.mychat.common.domain.GenderEnum;
import org.junit.Test;

import java.util.Map;
import java.util.UUID;

import static com.tmjee.mychat.client.Static.*;

/**
 * @author tmjee
 */
public class AcceptContactTest {

    @Test
    public void testAcceptContact() throws Exception {

        String u1 = UUID.randomUUID().toString();

        Map<String, Object> m = MYCHAT_CLIENT.register(u1+"@gmail.com", "test", u1, GenderEnum.FEMALE);

        String activationToken = m.get("activationToken").toString();
        Integer registeredMyChatUserId = (Integer) m.get("myChatUserId");

        MYCHAT_CLIENT.activate(activationToken);

        MYCHAT_CLIENT.logon("toby@gmail.com", "test");
        Integer initiatorMyChatUserId = MYCHAT_CLIENT.getMySelf().getMyChatUserId();
        MYCHAT_CLIENT.addContact(initiatorMyChatUserId, registeredMyChatUserId);

        MYCHAT_CLIENT.logon(u1+"@gmail.com", "test");
        MYCHAT_CLIENT.acceptContact(registeredMyChatUserId, initiatorMyChatUserId);
    }
}
