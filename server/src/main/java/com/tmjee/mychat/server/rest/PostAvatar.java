package com.tmjee.mychat.server.rest;

import com.tmjee.mychat.server.service.ProfileServices;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

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
public class PostAvatar extends V1<PostAvatar.Req, PostAvatar.Res> {


    @POST
    @Path("/avatar/{myChatUserId}/post")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postAvatar(@PathParam("myChatUserId") Integer myChatUserId,
                               @FormDataParam("applicationToken") String applicationToken,
                               @FormDataParam("accessToken") String accessToken,
                               @FormDataParam("avatar") InputStream is,
                               @FormDataParam("avatar") FormDataBodyPart b) {
        Req req = new Req();
        req.applicationToken = applicationToken;
        req.accessToken = accessToken;
        req.myChatUserId = myChatUserId;
        req.is = is;
        req.b = b;

        return action(req, this::f);
    }

    private Res f(Req req) throws IOException {
        ProfileServices profileServices = getInstance(ProfileServices.class);
        return profileServices.postAvatar(req);
    }

    public static class Req extends V1.Req {

        public Integer myChatUserId;

        public InputStream is;

        public FormDataBodyPart b;

        @Override
        protected void validate() {
        }
    }

    public static class Res extends V1.Res {

        public static Res success() {
            Res res = new Res();
            res.addMessage("Avatar successfully updated");
            return res;
        }
    }
}
