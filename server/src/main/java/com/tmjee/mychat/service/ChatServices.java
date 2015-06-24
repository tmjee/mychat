package com.tmjee.mychat.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tmjee.jooq.generated.tables.ChatMember;
import com.tmjee.jooq.generated.tables.MychatUser;
import com.tmjee.jooq.generated.tables.records.ChatRecord;
import com.tmjee.mychat.domain.ChatMemberStatusEnum;
import com.tmjee.mychat.rest.CreateChat;
import com.tmjee.mychat.service.annotations.DSLContextAnnotation;
import org.jooq.*;

import java.sql.Timestamp;

import static com.tmjee.jooq.generated.Tables.*;

/**
 * @author tmjee
 */
public class ChatServices {

    private final Provider<DSLContext> dslProvider;

    @Inject
    public ChatServices(@DSLContextAnnotation Provider<DSLContext> dslProvider) {
        this.dslProvider = dslProvider;
    }

    public CreateChat.Res createChat(CreateChat.Req req) {
        DSLContext dsl = dslProvider.get();

        // CHAT
        ChatRecord chatRecord = dsl.insertInto(CHAT)
                .columns(CHAT.CREATOR_MYCHAT_USER_ID, CHAT.NAME, CHAT.CREATION_DATE)
                .values(
                    req.myChatUserId,
                    req.chatName == null ? "" : req.chatName,
                    new Timestamp(System.currentTimeMillis())
                ).returning().fetchOne();

        // CHAT_MEMBER
        InsertValuesStep2 step = dsl.insertInto(CHAT_MEMBER)
                .columns(CHAT_MEMBER.CHAT_MEMBER_MYCHAT_USER_ID, CHAT_MEMBER.STATUS);
        for (Integer memberMyChatUserId : req.membersMyChatUserIds) {
                    step.values(memberMyChatUserId, ChatMemberStatusEnum.JOINED.name());
        }
        Result chatMemberResult = step.returning().fetch();


        // CHAT_MEMBER join with MYCHAT_USER
        ChatMember chatMember = CHAT_MEMBER.as("cm");
        MychatUser myChatUser = MYCHAT_USER.as("mcu");
        Result<Record> chatMemberJoinMyChatUserResult = dsl.select()
                .from(chatMember)
                .leftOuterJoin(myChatUser).on(CHAT_MEMBER.CHAT_MEMBER_MYCHAT_USER_ID.eq(MYCHAT_USER.MYCHAT_USER_ID))
                .and(CHAT_MEMBER.CHAT_ID.eq(chatRecord.getChatId()))
                .fetch();



        return CreateChat.Res.success(chatRecord, chatMemberJoinMyChatUserResult);
    }
}
