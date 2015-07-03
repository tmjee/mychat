package com.tmjee.mychat.server.rest;

import com.tmjee.mychat.server.jooq.generated.tables.records.ChatMemberRecord;
import com.tmjee.mychat.server.jooq.generated.tables.records.ChatRecord;
import com.tmjee.mychat.server.jooq.generated.tables.records.MychatUserRecord;
import com.tmjee.mychat.server.jooq.generated.tables.records.ProfileRecord;
import com.tmjee.mychat.server.service.ChatServices;
import org.jooq.Record;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tmjee.mychat.server.jooq.generated.Tables.*;

/**
 * @author tmjee
 */
@Provider
public class ListMyChats extends V1<ListMyChats.Req, ListMyChats.Res> {


    @POST
    @Path("/mychats/{myChatUserId}/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response listChats(Req req,
                              @PathParam("myChatUserId") Integer myChatUserId) {
        req.myChatUserId = myChatUserId;
        return action(req, this::f);
    }


    private Res f(Req req) {
        ChatServices chatServices = getInstance(ChatServices.class);
        return chatServices.listChats(req);
    }


    public static class Req extends V1.Req {

        public Integer myChatUserId;

        @Override
        protected void validate() {

        }
    }

    public static class Res extends V1.Res {

        public List<Chat> chats;

        public static Res success(Map<Integer, List<Record>> m) {
            Res res = new Res();
            res.chats = new ArrayList<>();
            res.addMessage("Chats retrieved");
            m.forEach((chatId, records)->{
                Chat chat = new Chat();
                records.forEach(record->{
                    ChatRecord chatRecord = record.into(CHAT);
                    ChatMemberRecord chatMemberRecord = record.into(CHAT_MEMBER);
                    MychatUserRecord mychatUserRecord = record.into(MYCHAT_USER);
                    ProfileRecord profileRecord = record.into(PROFILE);
                    chat.name = chatRecord.getName();
                    if (chatMemberRecord.getChatMemberMychatUserId().equals(profileRecord.getMychatUserId())){
                        chat.creatorName = profileRecord.getFullname();
                    }
                    chat.creatorMyChatUserId = chatRecord.getCreatorMychatUserId();
                    chat.creationDate = chatRecord.getCreationDate();

                    if (chat.members == null) {
                        chat.members = new ArrayList<>();
                    }
                    Map<String, String> m1 = new HashMap<>();
                    m1.put("name", profileRecord.getFullname());
                    m1.put("myChatUserId", mychatUserRecord.getMychatUserId().toString());
                    chat.members.add(m1);
                });
                res.chats.add(chat);
            });
            return res;
        }


        public static class Chat {
            public String name;
            public String creatorName;
            public Integer creatorMyChatUserId;
            public Timestamp creationDate;

            public List<Map<String, String>> members;
        }
    }

}
