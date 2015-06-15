package com.tmjee.mychat.rest;

import com.google.inject.Injector;
import com.tmjee.mychat.MyChatGuiceServletContextListener;
import com.tmjee.mychat.service.LogonServices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author tmjee
 */
public class Logon extends V1 {


    @GET
    @Path("/logon")
    @Produces("application/json")
    public String logon(String email, String password) {

        Injector injector = MyChatGuiceServletContextListener.getV1Injector();

        LogonServices logonServices = injector.getInstance(LogonServices.class);

        logonServices.logon(email, password);


        return "{test:'one'}";
    }
}
