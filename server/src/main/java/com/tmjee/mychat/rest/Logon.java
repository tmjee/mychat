package com.tmjee.mychat.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author tmjee
 */
//@Path("/v1")
public class Logon extends V1 {

    @GET
    @Path("/test")
    @Produces("application/json")
    public String test() {
        return "{test:'one'}";
    }

    @GET
    @Path("/logon")
    @Produces("application/json")
    public String logon() {



        return "{test:'one'}";
    }
}
