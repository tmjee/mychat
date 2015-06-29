package com.tmjee.mychat.server.rest;

import com.tmjee.jooq.generated.tables.records.MychatUserRecord;
import com.tmjee.mychat.server.service.LogonServices;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.security.NoSuchAlgorithmException;

/**
 * @author tmjee
 */
@Provider
public class Logon extends V1<Logon.Req, Logon.Res> {

    @POST
    @Path("/logon")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logon(Req r) throws NoSuchAlgorithmException {

        System.out.println("****** r.email="+r.email);
        System.out.println("****** r.password="+r.password);

        return action(r,
                this::f);
    }

    private Res f(Req req) throws NoSuchAlgorithmException {
        LogonServices logonServices = getInstance(LogonServices.class);
        return logonServices.logon(req);
    }


    public static final class Req extends V1.Req {
        public String email;
        public String password;

        @Override
        protected void validate() {

        }
    }

    public static final class Res extends V1.Res {

        public String accessToken;

        public static Res success(String accessToken, MychatUserRecord myChatUserRecord) {
            Res res = new Res();
            res.accessToken = accessToken;
            res.addMessage("Logon success");
            return res;
        }

        public static Res failed() {
            Res res = new Res();
            res.valid = false;
            res.addMessage("Logon failed");
            return res;
        }

        public static Res failedBadUsernamePasswordCombination() {
            Res res = new Res();
            res.valid = false;
            res.addMessage("Bad password/username combinations");
            return res;
        }
    }
}
