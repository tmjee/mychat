package com.tmjee.mychat.server.rest;

import com.tmjee.mychat.server.jooq.generated.tables.records.ActivationRecord;
import com.tmjee.mychat.server.service.ActivationServices;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    public Response completeActivation(Req req) {
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

        public static Res success(ActivationRecord activationRecord) {
            Res res = new Res();
            res.addMessage(format("Successfully activated %s %s", activationRecord.getType(),
                    activationRecord.getTypeId()));
            return res;
        }

        public static Res failed(ActivationRecord activationRecord) {
            Res res = new Res();
            res.addMessage(format("Failed to activate %s %s with status %s", activationRecord.getType(),
                    activationRecord.getTypeId(), activationRecord.getStatus()));
            return res;
        }
    }
}
