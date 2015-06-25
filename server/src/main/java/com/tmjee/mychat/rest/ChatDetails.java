package com.tmjee.mychat.rest;

import com.tmjee.jooq.generated.Tables;
import com.tmjee.jooq.generated.tables.records.ChatMemberRecord;
import com.tmjee.jooq.generated.tables.records.ChatMessageRecord;
import com.tmjee.jooq.generated.tables.records.MychatUserRecord;
import com.tmjee.jooq.generated.tables.records.ProfileRecord;
import com.tmjee.mychat.service.ChatServices;
import org.jooq.Record;
import org.jooq.Result;

import javax.ws.rs.core.Response;
import java.util.*;

/**
 * @author tmjee
 */
public class ChatDetails extends V1<ChatDetails.Req, ChatDetails.Res> {


    public Response chatDetails(Req req) {
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

        @Override
        protected void validate() {

        }
    }


    public static class Res extends V1.Res {

        public Integer chatId;
        public String chatName;
        public List<Map<String, String>> members;
        public List<Message> messages;

        public static Res success(Map<Integer, List<Record>> chattersMap, Result<Record> chats) {
            Res res = new Res();

            // fill in members
            res.members = new ArrayList<>();
            chattersMap.values().forEach((l)->{
                Iterator<Record> i = l.iterator();
                if (i.hasNext()) {
                    Record r = i.next();
                    MychatUserRecord mychatUserRecord = r.into(Tables.MYCHAT_USER);
                    ChatMemberRecord chatMemberRecord = r.into(Tables.CHAT_MEMBER);
                    ProfileRecord profileRecord = r.into(Tables.PROFILE);

                    Map<String, String> m = new HashMap<>();
                    m.put("name", profileRecord.getFullname());
                    m.put("myChatUserId", mychatUserRecord.getMychatUserId().toString());
                    m.put("status", chatMemberRecord.getStatus());

                    res.members.add(m);
                }
            });

            // fill in messages
            res.messages = new ArrayList<>();
            Iterator<ChatMessageRecord> i = chats.iterator();
            while(i.hasNext()) {
                ChatMessageRecord r = i.next();
                Message m = new Message();
                m.message = r.getChatMessage();
                r.getChatMemberId()
                res.messages.add(c);
            }


            return res;
        }

        public static class Message {
            public String message;
        }
    }
}
