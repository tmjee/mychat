package com.tmjee.mychat.server.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tmjee.mychat.server.jooq.generated.Tables;
import com.tmjee.mychat.server.jooq.generated.tables.ChatMember;
import com.tmjee.mychat.server.jooq.generated.tables.MychatUser;
import com.tmjee.mychat.server.jooq.generated.tables.Profile;
import com.tmjee.mychat.server.jooq.generated.tables.records.ChatMemberRecord;
import com.tmjee.mychat.server.jooq.generated.tables.records.ChatMessageRecord;
import com.tmjee.mychat.server.jooq.generated.tables.records.ChatRecord;
import com.tmjee.mychat.common.domain.ChatMemberStatusEnum;
import com.tmjee.mychat.server.rest.*;
import com.tmjee.mychat.server.service.annotations.AccessTokenAnnotation;
import com.tmjee.mychat.server.service.annotations.ApplicationTokenAnnotation;
import com.tmjee.mychat.server.service.annotations.TransactionAnnotation;
import com.tmjee.mychat.server.service.annotations.DSLContextAnnotation;
import org.jooq.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tmjee.mychat.server.jooq.generated.Tables.*;

/**
 * @author tmjee
 */
public class ChatServices {

    private final Provider<DSLContext> dslProvider;

    @Inject
    public ChatServices(@DSLContextAnnotation Provider<DSLContext> dslProvider) {
        this.dslProvider = dslProvider;
    }

    @TransactionAnnotation
    @ApplicationTokenAnnotation
    @AccessTokenAnnotation
    public CreateMyChat.Res createChat(CreateMyChat.Req req) {
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
        InsertValuesStep3 step = dsl.insertInto(CHAT_MEMBER)
                .columns(
                        CHAT_MEMBER.CHAT_MEMBER_MYCHAT_USER_ID,
                        CHAT_MEMBER.CHAT_ID,
                        CHAT_MEMBER.STATUS);
        for (Integer memberMyChatUserId : req.membersMyChatUserIds) {
                    step.values(
                            memberMyChatUserId,
                            chatRecord.getChatId(),
                            ChatMemberStatusEnum.JOINED.name());
        }
        Result chatMemberResult = step.returning().fetch();


        // CHAT_MEMBER join with MYCHAT_USER join with PROFILE
        Result<Record> chatMemberJoinMyChatUseJoinProfileResult = dsl.select()
                .from(Tables.CHAT_MEMBER)
                .leftOuterJoin(Tables.MYCHAT_USER).on(CHAT_MEMBER.CHAT_MEMBER_MYCHAT_USER_ID.eq(MYCHAT_USER.MYCHAT_USER_ID))
                .leftOuterJoin(Tables.PROFILE).on(PROFILE.MYCHAT_USER_ID.eq(MYCHAT_USER.MYCHAT_USER_ID))
                .where(CHAT_MEMBER.CHAT_ID.eq(chatRecord.getChatId()))
                .fetch();

        return CreateMyChat.Res.success(chatRecord, chatMemberJoinMyChatUseJoinProfileResult);
    }


    @TransactionAnnotation
    @ApplicationTokenAnnotation
    @AccessTokenAnnotation
    public JoinChat.Res joinChat(JoinChat.Req req) {
        DSLContext dsl = dslProvider.get();

        List<Integer> existingChatMemberIds = dsl.selectFrom(CHAT_MEMBER)
                .where(CHAT_MEMBER.CHAT_ID.eq(req.chatId))
                .fetch(CHAT_MEMBER.CHAT_MEMBER_MYCHAT_USER_ID);

        List<ChatMemberRecord> r = new ArrayList<>(1);
        req.membersMyChatUserIds
                .stream()
                .filter((i) -> (!existingChatMemberIds.contains(i)))
                .forEach((x) -> {
                    ChatMemberRecord chatMemberRecord =
                            dsl.insertInto(CHAT_MEMBER)
                                    .columns(CHAT_MEMBER.CHAT_ID, CHAT_MEMBER.CHAT_MEMBER_MYCHAT_USER_ID, CHAT_MEMBER.STATUS)
                                    .values(req.chatId, x, ChatMemberStatusEnum.JOINED.name())
                                    .returning()
                                    .fetchOne();
                    r.add(chatMemberRecord);
                });

        if (!r.isEmpty()) {
            return JoinChat.Res.success(r);
        }
        return JoinChat.Res.failed();
    }


    @TransactionAnnotation
    @ApplicationTokenAnnotation
    @AccessTokenAnnotation
    public SendChat.Res sendChat(SendChat.Req req) {
        DSLContext dsl = dslProvider.get();

        ChatMessageRecord chatMessageRecord =
            dsl.insertInto(CHAT_MESSAGE)
                .columns(CHAT_MESSAGE.CHAT_ID, CHAT_MESSAGE.CREATION_DATE, CHAT_MESSAGE.CHAT_MESSAGE_, CHAT_MESSAGE.CHAT_MEMBER_ID)
                .values(req.chatId, new Timestamp(System.currentTimeMillis()), req.message,
                        dsl.selectFrom(CHAT_MEMBER)
                                .where(CHAT_MEMBER.CHAT_MEMBER_ID.eq(req.myChatUserId))
                                .fetchOne()
                                .getChatMemberId())
                .returning()
                .fetchOne();


        return SendChat.Res.success(chatMessageRecord);
    }

    @TransactionAnnotation
    @ApplicationTokenAnnotation
    @AccessTokenAnnotation
    public ListMyChats.Res listChats(ListMyChats.Req req) {
        DSLContext dsl = dslProvider.get();

        Map<Integer, List<Record>> m = dsl.select()
                .from(CHAT)
                .leftOuterJoin(CHAT_MEMBER).on(CHAT_MEMBER.CHAT_ID.eq(CHAT.CHAT_ID))
                .leftOuterJoin(MYCHAT_USER).on(MYCHAT_USER.MYCHAT_USER_ID.eq(CHAT_MEMBER.CHAT_MEMBER_MYCHAT_USER_ID))
                .leftOuterJoin(PROFILE).on(PROFILE.MYCHAT_USER_ID.eq(CHAT_MEMBER.CHAT_MEMBER_MYCHAT_USER_ID))
                .where(CHAT_MEMBER.CHAT_MEMBER_MYCHAT_USER_ID
                        .in(req.myChatUserId)
                        .and(CHAT_MEMBER.STATUS.eq(ChatMemberStatusEnum.JOINED.name())))
                .limit(req.limit)
                .offset(req.offset)
                .fetch()
                .stream()
                .collect(
                            Collectors.groupingBy(r->r.getValue(CHAT.CHAT_ID))
                        )
        ;
        return ListMyChats.Res.success(m);
    }


    @TransactionAnnotation
    @ApplicationTokenAnnotation
    @AccessTokenAnnotation
    public LeaveChat.Res leaveChat(LeaveChat.Req req) {
        DSLContext dsl = dslProvider.get();

        Result<ChatMemberRecord> chatMemberResults = dsl.update(CHAT_MEMBER)
                .set(CHAT_MEMBER.STATUS, ChatMemberStatusEnum.LEFT.name())
                .where(
                    CHAT_MEMBER.CHAT_ID.eq(req.chatId)
                    .and(CHAT_MEMBER.CHAT_MEMBER_MYCHAT_USER_ID.eq(req.myChatUserId))
                    .and(CHAT_MEMBER.STATUS.eq(ChatMemberStatusEnum.JOINED.name()))
                )
                .returning()
                .fetch();


        if (!chatMemberResults.isEmpty()) {

            Record myChatUserJoinProfileRecord = dsl.select()
                    .from(MYCHAT_USER)
                    .leftOuterJoin(PROFILE).on(PROFILE.MYCHAT_USER_ID.eq(MYCHAT_USER.MYCHAT_USER_ID))
                    .where(MYCHAT_USER.MYCHAT_USER_ID.eq(req.myChatUserId))
                    .fetchOne();
            return LeaveChat.Res.success(myChatUserJoinProfileRecord);
        } else {
            return LeaveChat.Res.failed(req);
        }
    }

    @TransactionAnnotation
    @ApplicationTokenAnnotation
    @AccessTokenAnnotation
    public ChatDetails.Res chatDetails(ChatDetails.Req req) {
        DSLContext dsl = dslProvider.get();

        ChatRecord chatRecord =
        dsl.selectFrom(CHAT)
                .where(CHAT.CHAT_ID.eq(req.chatId))
                .fetchOne();


        Result<Record> chatMemberRecords =
        dsl.select()
                .from(CHAT_MEMBER)
                .leftOuterJoin(MYCHAT_USER).on(CHAT_MEMBER.CHAT_MEMBER_MYCHAT_USER_ID.eq(MYCHAT_USER.MYCHAT_USER_ID))
                .leftOuterJoin(PROFILE).on(PROFILE.MYCHAT_USER_ID.eq(MYCHAT_USER.MYCHAT_USER_ID))
                .where(CHAT_MEMBER.CHAT_ID.eq(req.chatId))
                .fetch();


        Result<Record> chatMessageRecords =
        dsl.select()
                .from(CHAT_MESSAGE)
                .leftOuterJoin(CHAT_MEMBER).on(CHAT_MEMBER.CHAT_MEMBER_ID.eq(CHAT_MESSAGE.CHAT_MEMBER_ID))
                .leftOuterJoin(MYCHAT_USER).on(MYCHAT_USER.MYCHAT_USER_ID.eq(CHAT_MEMBER.CHAT_MEMBER_MYCHAT_USER_ID))
                .leftOuterJoin(PROFILE).on(PROFILE.MYCHAT_USER_ID.eq(MYCHAT_USER.MYCHAT_USER_ID))
                .where(CHAT_MESSAGE.CHAT_ID.eq(req.chatId))
                .limit(req.limit)
                .offset(req.offset)
                .fetch();

        return ChatDetails.Res.success(chatRecord, chatMemberRecords, chatMessageRecords);
    }
}
