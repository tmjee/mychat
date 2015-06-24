package com.tmjee.mychat.rest;

import com.tmjee.jooq.generated.tables.ChatMember;
import com.tmjee.jooq.generated.tables.records.ChatMemberRecord;
import com.tmjee.jooq.generated.tables.records.ChatRecord;
import com.tmjee.mychat.service.ChatServices;
import org.jooq.Record;
import org.jooq.Result;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * Created by tmjee on 24/06/15.
 */
@Provider
public class CreateChat extends V1<CreateChat.Req, CreateChat.Res> {

    @POST
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

        public static Res success(ChatRecord chatRecord, Result<Record> chatMemberResult) {
            Res res = new Res();
            res.addMessage(format("chat %s created", chatRecord.getName()));
            Iterator i = chatMemberResult.iterator();
            while(i.hasNext()) {
                Map<String, String> m = new HashMap<>();
                Record o = (Record) i.next();
                //m.put("name" , o.get
                m.put("myChatUserId", o.getValue()getChatMemberMychatUserId().toString());
                m.put("status", o.getChatMemberMychatUserId().toString());


            }
            return res;
        }
    }
}
