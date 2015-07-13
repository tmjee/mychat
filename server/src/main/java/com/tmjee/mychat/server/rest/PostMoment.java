package com.tmjee.mychat.server.rest;

import com.tmjee.mychat.server.jooq.generated.tables.records.MomentRecord;
import com.tmjee.mychat.server.service.MomentServices;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author tmjee
 */
@Provider
public class PostMoment extends V1<PostMoment.Req, PostMoment.Res> {


    @POST
    @Path("/moments/{myChatUserId}/post")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postMoment(@FormDataParam("media") InputStream is,
                               @FormDataParam("media") FormDataBodyPart b,
                               @FormDataParam("message") String message,
                               @FormDataParam("applicationToken") String applicationToken,
                               @FormDataParam("accessToken") String accessToken,
                               @PathParam("myChatUserId") Integer myChatUserId) {
        Req req = new Req();
        req.myChatUserId = myChatUserId;
        req.is = is;
        req.b = b;
        req.message = message;
        req.applicationToken = applicationToken;
        req.accessToken = accessToken;

        return action(req, this::f);
    }


    private Res f(Req req) throws IOException {
        MomentServices momentServices = getInstance(MomentServices.class);
        return momentServices.postMoment(req);
    }


    public static class Req extends V1.Req {

        public Integer myChatUserId;
        public String message;
        public InputStream is;
        public FormDataBodyPart b;

        @Override
        protected void validate() {

        }
    }

    public static class Res extends V1.Res {

        public static Res success(MomentRecord momentRecord) {
            Res res = new Res();
            res.addMessage("Moment posted");
            return res;
        }
    }
}
