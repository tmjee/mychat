package com.tmjee.mychat.rest;

import com.tmjee.mychat.service.ContactServices;
import org.glassfish.jersey.message.internal.MediaTypes;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author tmjee
 */
public class AddContacts extends V1<AddContacts.Req, AddContacts.Res> {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addContacts(Req req) {
        return action(req, this::f);
    }


    private Res f(Req req) {
        ContactServices contactServices = getInstance(ContactServices.class);
        return contactServices.addContact(req);
    }


    public static class Req extends V1.Req {

        @Override
        protected void validate() {

        }
    }


    public static class Res extends V1.Res {

    }
}
