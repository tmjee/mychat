package com.tmjee.mychat.server.rest;

import com.tmjee.mychat.server.service.PersonalServices;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * @author tmjee
 */
@Provider
public class WhatsUp extends V1<WhatsUp.Req, WhatsUp.Res> {


    @POST
    @Path("/whatsup/{myChatUserId}/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response whatsUp(Req req,
                            @PathParam("myChatUserId") Integer myChatUserId) {
        req.myChatUserId = myChatUserId;
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
