package com.tmjee.mychat.rest;

import com.tmjee.mychat.service.ContactServices;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * @author tmjee
 */
@Provider
public class AcceptContacts extends V1<AcceptContacts.Req, AcceptContacts.Res> {



    @POST
    @Path("/contacts/accept")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response acceptContacts(Req req) {
        return action(req, this::f);
    }

    private Res f(Req req) {
        ContactServices contactServices = getInstance(ContactServices.class);
        return contactServices.acceptContact(req);
    }


    public static class Req extends V1.Req {

        public Integer myChatUserId;
        public Integer contactMyChatUserId;

        @Override
        protected void validate() {

        }
    }

    public static class Res extends V1.Res {

        public static Res success() {
            Res res = new Res();
            res.addMessage("Contact accepted");
            return res;
        }

        public static Res failureNoPendingConfirmation() {
            Res res = new Res();
            res.addMessage("No pending confirmation for this contact");
            res.valid = false;
            return res;
        }

        public static Res failureNoPendingAccept() {
            Res res = new Res();
            res.addMessage("No pending accept for this contact");
            res.valid = false;
            return res;
        }
    }
}
