package com.tmjee.mychat.server.rest;

import com.tmjee.mychat.server.jooq.generated.Tables;
import com.tmjee.mychat.server.jooq.generated.tables.records.*;
import com.tmjee.mychat.server.service.ChatServices;
import org.jooq.Record;
import org.jooq.Result;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author tmjee
 */
@Provider
public class ChatDetails extends V1<ChatDetails.Req, ChatDetails.Res> {

    @POST
    @Path("/chats/{chatId}/details")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response chatDetails(Req req,
                                @PathParam("chatId") Integer chatId) {
        req.chatId = chatId;
        return action(req, this::f);
    }


    private Res f(Req req) {
        ChatServices chatServices = getInstance(ChatServices.class);
        return chatServices.chatDetails(req);
    }


    public static class Req extends V1.Req {

        public Integer chatId;
        public Integer limit;
        public Integer offset;
        public Integer myChatUserId;

        @Override
        protected void validate() {

        }
    }


    public static class Res extends V1.Res {

        public Integer chatId;
        public String chatName;
        public List<Map<String, String>> members;
        public List<Message> messages;

        public static Res success(ChatRecord chatRecord, Result<Record> chatMembersRecords, Result<Record> chatMessagesRecords) {
            Res res = new Res();

            // fill in chat
            res.chatId = chatRecord.getChatId();
            res.chatName = chatRecord.getName();

            // fill in members
            res.members = new ArrayList<>();
            chatMembersRecords.forEach((r)->{
                MychatUserRecord mychatUserRecord = r.into(Tables.MYCHAT_USER);
                ChatMemberRecord chatMemberRecord = r.into(Tables.CHAT_MEMBER);
                ProfileRecord profileRecord = r.into(Tables.PROFILE);

                Map<String, String> m = new HashMap<>();
                m.put("name", profileRecord.getFullname());
                m.put("myChatUserId", mychatUserRecord.getMychatUserId().toString());
                m.put("status", chatMemberRecord.getStatus());

                res.members.add(m);
            });

            // fill in messages
            res.messages = new ArrayList<>();
            Iterator<Record> i = chatMessagesRecords.iterator();
            while(i.hasNext()) {
                Record r = i.next();
                ChatMessageRecord chatMessageRecord = r.into(Tables.CHAT_MESSAGE);
                ChatMemberRecord chatMemberRecord = r.into(Tables.CHAT_MEMBER);
                MychatUserRecord mychatUserRecord = r.into(Tables.MYCHAT_USER);
                ProfileRecord profileRecord = r.into(Tables.PROFILE);

                Message m = new Message();
                m.message = chatMessageRecord.getChatMessage();
                m.myChatUserId = mychatUserRecord.getMychatUserId();
                m.chatter = profileRecord.getFullname();
                m.creationDate = chatMessageRecord.getCreationDate();
                res.messages.add(m);
            }


            return res;
        }

        public static class Message {
            public String message;
            public Integer myChatUserId;
            public String chatter;
            public Timestamp creationDate;
        }
    }
}
