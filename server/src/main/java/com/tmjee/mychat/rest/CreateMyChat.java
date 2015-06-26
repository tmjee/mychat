package com.tmjee.mychat.rest;

import com.tmjee.jooq.generated.Tables;
import com.tmjee.jooq.generated.tables.records.ChatMemberRecord;
import com.tmjee.jooq.generated.tables.records.ChatRecord;
import com.tmjee.jooq.generated.tables.records.MychatUserRecord;
import com.tmjee.jooq.generated.tables.records.ProfileRecord;
import com.tmjee.mychat.service.ChatServices;
import org.jooq.Record;
import org.jooq.Result;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.*;

import static java.lang.String.format;

/**
 * Created by tmjee on 24/06/15.
 */
@Provider
public class CreateMyChat extends V1<CreateMyChat.Req, CreateMyChat.Res> {

    @POST
    @Path("/mychats/{myChatUserId}/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createChat(Req req) {
        return action(req, this::f);
    }


    private Res f(Req req) {
        ChatServices chatServices = getInstance(ChatServices.class);
        return chatServices.createChat(req);
    }


    public static class Req extends V1.Req {

        public Integer myChatUserId;
        public List<Integer> membersMyChatUserIds;
        public String chatName;

        @Override
        protected void validate() {

        }
    }

    public static class Res extends V1.Res {

        public List<Map<String, String>> members;

        public static Res success(ChatRecord chatRecord, Result<Record> chatMemberJoinMyChatUserJoinProfileResult) {
            Res res = new Res();
            res.members = new ArrayList<>();
            res.addMessage(format("chat %s created", chatRecord.getName()));
            Iterator<Record> i = chatMemberJoinMyChatUserJoinProfileResult.iterator();
            while(i.hasNext()) {
                Map<String, String> m = new HashMap<>();
                Record o =  i.next();

                MychatUserRecord mychatUserRecord = o.into(Tables.MYCHAT_USER);
                ChatMemberRecord chatMemberRecord = o.into(Tables.CHAT_MEMBER);
                ProfileRecord profile = o.into(Tables.PROFILE);

                m.put("name", profile.getFullname());
                m.put("myChatUserId", mychatUserRecord.getMychatUserId().toString());
                m.put("status", chatMemberRecord.getStatus());
                res.members.add(m);
            }
            return res;
        }
    }
}
