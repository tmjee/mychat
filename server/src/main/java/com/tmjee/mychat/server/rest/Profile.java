package com.tmjee.mychat.server.rest;

import com.tmjee.mychat.server.jooq.generated.Tables;
import com.tmjee.mychat.server.jooq.generated.tables.records.AvatarRecord;
import com.tmjee.mychat.server.jooq.generated.tables.records.MychatUserRecord;
import com.tmjee.mychat.server.jooq.generated.tables.records.ProfileRecord;
import com.tmjee.mychat.server.service.ProfileServices;
import org.jooq.Record;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.sql.Timestamp;

/**
 * @author tmjee
 */
@Provider
public class Profile extends V1<Profile.Req, Profile.Res> {


    @POST
    @Path("/profile/{myChatUserId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response contactDetails(Req req,
                                   @PathParam("myChatUserId") Integer myChatUserId) {
        req.myChatUserId = myChatUserId;
        return action(req, this::f);
    }

    private Res f(Req req) {
        ProfileServices profileServices = getInstance(ProfileServices.class);
        return profileServices.contactDetails(req);
    }


    public static class Req extends V1.Req {

        public Integer myChatUserId;


        @Override
        protected void validate() {

        }
    }


    public static class Res extends V1.Res {

        public Integer myChatUserId;
        public String name;
        public String gender;
        public Timestamp dateCreated;
        public String whatsup;
        public String status;
        public String identification;
        public String identificationType;


        public static Res success(Record r) {
            AvatarRecord avatarRecord = r.into(Tables.AVATAR);
            MychatUserRecord mychatUserRecord = r.into(Tables.MYCHAT_USER);
            ProfileRecord profileRecord = r.into(Tables.PROFILE);

            Res res = new Res();
            res.addMessage("Profile succesfully retrieved");
            res.myChatUserId = mychatUserRecord.getMychatUserId();
            res.name = profileRecord.getFullname();
            res.dateCreated = mychatUserRecord.getCreationDate();
            res.gender = profileRecord.getGender();
            res.whatsup = profileRecord.getWhatsup();

            res.status = mychatUserRecord.getStatus();
            res.identification = mychatUserRecord.getIdentification();
            res.identificationType = mychatUserRecord.getIdentificationType();

            return res;
        }
    }

}
