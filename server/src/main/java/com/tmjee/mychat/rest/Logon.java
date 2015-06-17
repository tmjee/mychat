package com.tmjee.mychat.rest;

import com.google.inject.Injector;
import com.tmjee.mychat.MyChatGuiceServletContextListener;
import com.tmjee.mychat.domain.LogonResult;
import com.tmjee.mychat.service.LogonServices;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;

/**
 * @author tmjee
 */
public class Logon extends V1 {

    @GET
    @Path("/logon")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logon(String email, String password) throws NoSuchAlgorithmException {

        Injector injector = MyChatGuiceServletContextListener.getV1Injector();

        LogonServices logonServices = injector.getInstance(LogonServices.class);

        LogonResult logonResult = logonServices.logon(email, password);

        if (logonResult.isOk()) {
            return Response.ok()
                    .entity(new Entity(logonResult)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new Entity(logonResult)).build();
    }


    private static final class Entity {

        public String message;
        public String accessToken;

        public Entity(LogonResult logonResult) {
            this.message = logonResult.message();
            this.accessToken = logonResult.accessToken();
        }
    }
}
