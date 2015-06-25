package com.tmjee.mychat.rest;

import com.tmjee.jooq.generated.tables.records.ChatMessageRecord;
import com.tmjee.mychat.service.ChatServices;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * @author tmjee
 */
@Provider
public class SendChat extends V1<SendChat.Req, SendChat.Res> {


    public Response sendChat(Req req) {
        return action(req, this::f);
    }


    private Res f(Req req) {
        ChatServices chatServices = getInstance(ChatServices.class);
        return chatServices.sendChat(req);
    }


    public static class Req extends V1.Req {

        public Integer chatId;
        public Integer myChatUserId;
        public String message;

        @Override
        protected void validate() {

        }
    }


    public static class Res extends V1.Res {

        public static Res success(ChatMessageRecord chatMessageRecord) {
            Res res = new Res();
            res.addMessage("Message added successfully");
            return res;
        }
    }
}
