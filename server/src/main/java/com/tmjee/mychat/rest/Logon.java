package com.tmjee.mychat.rest;

import com.google.inject.Injector;
import com.tmjee.mychat.MyChatGuiceServletContextListener;
import com.tmjee.mychat.domain.LogonResult;
import com.tmjee.mychat.service.LogonServices;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.security.NoSuchAlgorithmException;

/**
 * @author tmjee
 */
@Provider
public class Logon extends V1 {

    @POST
    @Path("/logon")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logon(Req r) throws NoSuchAlgorithmException {

        Injector injector = MyChatGuiceServletContextListener.getV1Injector();

        LogonServices logonServices = injector.getInstance(LogonServices.class);

        System.out.println("****** r.email="+r.email);
        System.out.println("****** r.password="+r.password);

        LogonResult logonResult = logonServices.logon(r.email, r.password);

        if (logonResult.isOk()) {
            return Response.ok()
                    .entity(new Entity(logonResult)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new Entity(logonResult)).build();
    }

    private static final class Req {
        public String email;
        public String password;
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
