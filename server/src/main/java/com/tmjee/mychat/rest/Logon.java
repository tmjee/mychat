package com.tmjee.mychat.rest;

import com.google.inject.Injector;
import com.tmjee.mychat.MyChatGuiceServletContextListener;
import com.tmjee.mychat.service.LogonServices;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.security.NoSuchAlgorithmException;

/**
 * @author tmjee
 */
public class Logon extends V1 {

    @GET
    @Path("/logon")
    @Produces("application/json")
    @Consumes("application/json")
    public String logon(String email, String password) throws NoSuchAlgorithmException {

        Injector injector = MyChatGuiceServletContextListener.getV1Injector();

        LogonServices logonServices = injector.getInstance(LogonServices.class);

        logonServices.logon(email, password);

        return "{test:'one'}";
    }
}
