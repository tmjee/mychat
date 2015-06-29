package com.tmjee.mychat.server.rest;

import com.tmjee.mychat.server.service.ProfileServices;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    public Response postAvatar(Req req) {
       return action(req, this::f);
    }

    private Res f(Req req) throws IOException {
        ProfileServices profileServices = getInstance(ProfileServices.class);
        return profileServices.postAvatar(req);
    }

    public static class Req extends V1.Req {

        public Integer myChatUserId;

        @FormDataParam("avatar")
        public InputStream is;

        @FormDataParam("avatar")
        public FormDataContentDisposition d;

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
