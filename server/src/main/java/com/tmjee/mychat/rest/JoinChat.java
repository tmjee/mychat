package com.tmjee.mychat.rest;

import com.tmjee.jooq.generated.tables.records.ChatMemberRecord;
import com.tmjee.mychat.service.ChatServices;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tmjee
 */
@Provider
public class JoinChat extends V1<JoinChat.Req, JoinChat.Res> {


    public Response joinChat(JoinChat.Req req) {
       return action(req, this::f);
    }


    private Res f(Req req) {
        ChatServices chatServices = getInstance(ChatServices.class);
        return chatServices.joinChat(req);
    }


    public static class Req extends V1.Req {

        public Integer chatId;
        public Integer myChatUserId;
        public List<Integer> membersMyChatUserIds;

        @Override
        protected void validate() {
        }
    }

    public static class Res extends V1.Res {

        public List<String> members;

        public static Res success(List<ChatMemberRecord> r) {
            Res res = new Res();
            res.members = new ArrayList<>();
            res.addMessage("members successfully added");
            r.forEach((r1)->{
                res.members.add(r1.getChatMemberMychatUserId().toString());
            });
            return res;
        }

        public static Res failed() {
            Res res = new Res();
            res.addMessage("no members added might have already exists");
            return res;
        }
    }
}
