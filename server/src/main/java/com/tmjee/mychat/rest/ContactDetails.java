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
public class ContactDetails extends V1<ContactDetails.Req, ContactDetails.Res> {


    @POST
    @Path("/contacts/{myChatUserId}/details")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response contactDetails(Req req) {
        return action(req, this::f);
    }

    private Res f(Req req) {
        ContactServices contactServices = getInstance(ContactServices.class);
        return contactServices.contactDetails(req);
    }


    public static class Req extends V1.Req {

        public Integer myChatUserId;


        @Override
        protected void validate() {

        }
    }


    public static class Res extends V1.Res {

    }

}
