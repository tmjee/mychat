package com.tmjee.mychat.server.rest;

import com.tmjee.jooq.generated.tables.records.ChannelRecord;
import com.tmjee.jooq.generated.tables.records.MychatUserRecord;
import com.tmjee.mychat.common.domain.GenderEnum;
import com.tmjee.mychat.server.service.RegisterServices;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.security.NoSuchAlgorithmException;

import static java.lang.String.format;

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

        public Integer myChatUserId;

        public static Res success(MychatUserRecord r, ChannelRecord re) {
            Res res = new Res();
            res.addMessage(format("MyChat user %s added successfully", r.getMychatUserId()));
            res.myChatUserId = r.getMychatUserId();
            return res;
        }

        public static Res alreadyExists(Req req) {
            Res res = new Res();
            res.addMessage(format("email %s is already registered", req.email));
            return res;
        }

        public static Res failed(Req req) {
            Res res = new Res();
            res.addMessage(format("Failed to add MyChat user %s", req.email));
            return res;
        }

    }

}
