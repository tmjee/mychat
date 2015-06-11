package com.tmjee.mychat.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author tmjee
 */
@Path("/logon")
public class Logon extends Application{

    @GET
    @Produces("json/tex")
    public String test() {
        return "{test:'one'}";
    }

    @GET
    @Produces("json/text")
    public String logon() {



        return "{test:'one'}";
    }
}
