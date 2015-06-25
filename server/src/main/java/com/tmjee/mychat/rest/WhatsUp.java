package com.tmjee.mychat.rest;

import com.tmjee.mychat.service.PersonalServices;

import javax.ws.rs.core.Response;

/**
 * @author tmjee
 */
public class WhatsUp extends V1<WhatsUp.Req, WhatsUp.Res> {


    public Response whatsUp(Req req) {
        return action(req, this::f);
    }

    private Res f(Req req) {
        PersonalServices personalServices = getInstance(PersonalServices.class);
        return personalServices.whatsUp(req);
    }


    public static class Req extends V1.Req {

        public Integer myChatUserId;
        public String whatsUp;

        @Override
        protected void validate() {

        }
    }

    public static class Res extends V1.Res {

        public static Res success() {
            Res res = new Res();
            res.addMessage("Updated what's up.");
            return res;
        }
    }
}
