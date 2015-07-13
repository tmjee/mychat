package com.tmjee.mychat.server.rest;

import com.tmjee.mychat.server.service.LocationServices;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * @author tmjee
 */
@Provider
public class UpdateLocation extends V1<UpdateLocation.Req, UpdateLocation.Res> {

    @POST
    @Path("/location/{myChatUserId}/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response location(Req req,
                             @PathParam("myChatUserId") Integer myChatUserId) {
        req.myChatUserId = myChatUserId;
        return action(req, this::f);
    }


    private Res f(Req req) {
        LocationServices locationServices = getInstance(LocationServices.class);
        return locationServices.contributeLocation(req);
    }

    public static class Req extends V1.Req {

        public Integer myChatUserId;
        public Double longitude;
        public Double latitude;
        public String name;

        @Override
        protected void validate() {

        }
    }

    public static class Res extends V1.Res {

        public static Res success() {
            Res res = new Res();
            res.addMessage("Location updated");
            return res;
        }
    }
}
