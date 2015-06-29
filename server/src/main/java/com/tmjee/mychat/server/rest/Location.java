package com.tmjee.mychat.server.rest;

import com.tmjee.mychat.server.service.LocationServices;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * @author tmjee
 */
@Provider
public class Location extends V1<Location.Req, Location.Res> {

    public Response location(Req req) {
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
