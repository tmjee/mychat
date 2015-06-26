package com.tmjee.mychat.rest;

import com.tmjee.jooq.generated.tables.records.AvatarRecord;
import com.tmjee.mychat.service.ProfileServices;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.InputStream;

/**
 * @author tmjee
 */
@Provider
public class GetAvatar extends V1<GetAvatar.Req, GetAvatar.Res> {


    @POST
    @Path("/avatar/{myChatUserId}/get")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvatar(Req req) {
        return action(req, this::f);
    }

    private Res f(Req req) {
        ProfileServices profileServices = getInstance(ProfileServices.class);
        return profileServices.getAvatar(req);
    }


    public static class Req extends V1.Req {

        public Integer myChatUserId;

        @Override
        protected void validate() {

        }
    }

    public static class Res extends V1.Res {

        public byte[] b;
        public byte[] e;
        public String filename;
        public String mimeType;

        public static Res success(AvatarRecord avatarRecord) {
            Res res = new Res();
            res.b = avatarRecord.getBytes();
            res.e = avatarRecord.getEncoded();
            res.filename = avatarRecord.getFilename();
            res.mimeType = avatarRecord.getMimeType();
            return res;
        }
    }
}
