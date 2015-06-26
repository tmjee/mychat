package com.tmjee.mychat.rest;

import com.tmjee.jooq.generated.Tables;
import com.tmjee.jooq.generated.tables.records.MychatUserRecord;
import com.tmjee.jooq.generated.tables.records.ProfileRecord;
import com.tmjee.mychat.service.ChatServices;
import org.jooq.Record;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import static java.lang.String.format;

/**
 * @author tmjee
 */
@Provider
public class LeaveChat extends V1<LeaveChat.Req, LeaveChat.Res> {

    @POST
    @Path("/chats/{chatId}/leave")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response leaveChat(Req req) {
       return action(req, this::f);
    }

    private Res f(Req req) {
        ChatServices chatServices = getInstance(ChatServices.class);
        return chatServices.leaveChat(req);
    }

    public static class Req extends V1.Req {

        public Integer chatId;
        public Integer myChatUserId;

        @Override
        protected void validate() {

        }
    }

    public static class Res extends V1.Res {

        public static Res success(Record myChatUserJoinProfileRecord) {
            MychatUserRecord mychatUserRecord = myChatUserJoinProfileRecord.into(Tables.MYCHAT_USER);
            ProfileRecord profileRecord = myChatUserJoinProfileRecord.into(Tables.PROFILE);

            Res res = new Res();
            res.addMessage(format("Chatter %s left", profileRecord.getFullname()));
            return res;
        }

        public static Res failed(Req req) {
            Res res = new Res();
            res.addMessage("Chatter not join / already left");
            return res;
        }
    }
}
