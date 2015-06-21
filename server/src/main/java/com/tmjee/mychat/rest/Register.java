package com.tmjee.mychat.rest;

import com.tmjee.mychat.domain.GenderEnum;
import com.tmjee.mychat.service.RegisterServices;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.security.NoSuchAlgorithmException;

/**
 * @author tmjee
 */
@Provider
public class Register extends V1<Register.Req, Register.Res> {

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(Req r) {
        return action(r, this::g);
    }

    public Res g(Req req) throws NoSuchAlgorithmException {
        System.out.println("****** "+req.gender);
        RegisterServices registerServices = getInstance(RegisterServices.class);
        return registerServices.register(req);
    }


    public static class Req extends V1.Req {

        public String email;
        public String password;
        public String fullname;
        public GenderEnum gender;

        @Override
        protected void validate() {

        }
    }

    public static class Res extends V1.Res {

    }

}
