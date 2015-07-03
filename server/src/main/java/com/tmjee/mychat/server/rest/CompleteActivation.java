package com.tmjee.mychat.server.rest;

import com.tmjee.mychat.server.jooq.generated.tables.records.MychatUserRecord;
import com.tmjee.mychat.server.jooq.generated.tables.records.ProfileRecord;
import com.tmjee.mychat.server.service.ActivationServices;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import static java.lang.String.format;

/**
 * @author tmjee
 */
@Provider
public class CompleteActivation extends V1<CompleteActivation.Req, CompleteActivation.Res> {


    @POST
    @Path("/activation/{activationToken}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response completeActivation(Req req,
                                       @PathParam("activationToken") String activationToken) {
        req.activationToken = activationToken;
        return action(req, this::f);
    }


    private Res f(Req req) {
        ActivationServices activationServices = getInstance(ActivationServices.class);
        return activationServices.completeActivation(req);
    }


    public static class Req extends V1.Req {
        public String activationToken;

        @Override
        protected void validate() {

        }
    }

    public static class Res extends V1.Res {
        public static Res success(MychatUserRecord mychatUserRecord) {
            Res res = new Res();
            res.addMessage(format("Successfully activated %s", mychatUserRecord.getIdentification()));
            return res;
        }

        public static Res failed(MychatUserRecord mychatUserRecord) {
            Res res = new Res();
            res.addMessage(format("Failed to activate %s ", mychatUserRecord.getIdentification()));
            return res;
        }
    }
}
