package com.tmjee.mychat.service;

import com.google.inject.Inject;
import static com.tmjee.jooq.generated.Tables.*;
import static java.lang.String.format;

import com.google.inject.Provider;
import com.tmjee.jooq.generated.tables.MychatUser;
import com.tmjee.jooq.generated.tables.records.ContactRecord;
import com.tmjee.mychat.domain.ContactStatusEnum;
import com.tmjee.mychat.domain.MyChatUserStatusEnum;
import com.tmjee.mychat.rest.AcceptContacts;
import com.tmjee.mychat.rest.AddContacts;
import com.tmjee.mychat.rest.Profile;
import com.tmjee.mychat.rest.ListContacts;
import com.tmjee.mychat.service.annotations.AccessTokenAnnotation;
import com.tmjee.mychat.service.annotations.ApplicationTokenAnnotation;
import com.tmjee.mychat.service.annotations.DSLContextAnnotation;
import com.tmjee.mychat.service.annotations.TransactionAnnotation;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;

import java.sql.Timestamp;
import java.util.logging.Logger;

/**
 * @author tmjee
 */
public class ContactServices {

    private static final Logger LOG = Logger.getLogger(ContactServices.class.getName());

    private final Provider<DSLContext> dslProvider;

    @Inject
    public ContactServices(@DSLContextAnnotation Provider<DSLContext> dslProvider) {
        this.dslProvider = dslProvider;
    }


    @TransactionAnnotation
    @ApplicationTokenAnnotation
    @AccessTokenAnnotation
    public AddContacts.Res addContact(AddContacts.Req req) {
        DSLContext dsl = dslProvider.get();

        Result<ContactRecord> result =
            dsl.selectFrom(CONTACT)
                .where(CONTACT.MYCHAT_USER_ID.eq(req.myChatUserId))
                .and(CONTACT.CONTACT_MYCHAT_USER_ID.eq(req.contactMyChatUserId))
                .fetch();

        if (result.isEmpty()) {
            ContactRecord myContactRecord = dsl.insertInto(CONTACT,
                    CONTACT.MYCHAT_USER_ID,
                    CONTACT.CONTACT_MYCHAT_USER_ID,
                    CONTACT.STATUS,
                    CONTACT.CREATION_DATE)
                    .values(
                            req.myChatUserId,
                            req.contactMyChatUserId,
                            ContactStatusEnum.PENDING_CONFIRMATION.name(),
                            new Timestamp(System.currentTimeMillis())
                    ).returning()
                    .fetchOne();

            ContactRecord otherSideContactRecord = dsl.insertInto(CONTACT,
                    CONTACT.MYCHAT_USER_ID,
                    CONTACT.CONTACT_MYCHAT_USER_ID,
                    CONTACT.STATUS,
                    CONTACT.CREATION_DATE)
                    .values(
                            req.contactMyChatUserId,
                            req.myChatUserId,
                            ContactStatusEnum.PENDING_ACCEPTANCE.name(),
                            new Timestamp(System.currentTimeMillis())
                    ).returning()
                    .fetchOne();

            MychatUser myChatUser = MYCHAT_USER.as("m");
            com.tmjee.jooq.generated.tables.Profile profile = PROFILE.as("p");
            Record contactMyChatUser = dsl
                    .select(profile.fields())
                    .from(myChatUser)
                    .leftOuterJoin(profile)
                    .on(PROFILE.MYCHAT_USER_ID.eq(MYCHAT_USER.MYCHAT_USER_ID))
                    .where(MYCHAT_USER.MYCHAT_USER_ID.eq(req.contactMyChatUserId))
                    .fetchOne();

            return AddContacts.Res.success(contactMyChatUser);
        }

        MychatUser myChatUser = MYCHAT_USER.as("m");
        com.tmjee.jooq.generated.tables.Profile profile = PROFILE.as("p");
        Record contactMyChatUser = dsl
                .select(profile.fields())
                .from(myChatUser)
                .leftOuterJoin(profile)
                .on(PROFILE.MYCHAT_USER_ID.eq(MYCHAT_USER.MYCHAT_USER_ID))
                .where(MYCHAT_USER.MYCHAT_USER_ID.eq(result.get(0).getContactMychatUserId()))
                .fetchOne();
        return AddContacts.Res.failed(result.get(0), contactMyChatUser);
    }

    @TransactionAnnotation
    @ApplicationTokenAnnotation
    @AccessTokenAnnotation
    public AcceptContacts.Res acceptContact(AcceptContacts.Req req) {
        DSLContext context = dslProvider.get();

        ContactRecord acceptorRecord = context.update(CONTACT)
                .set(CONTACT.STATUS, ContactStatusEnum.ACCEPTED.name())
                .where(CONTACT.MYCHAT_USER_ID.eq(req.myChatUserId))
                .and(CONTACT.CONTACT_MYCHAT_USER_ID.eq(req.contactMyChatUserId))
                .and(CONTACT.STATUS.eq(ContactStatusEnum.PENDING_ACCEPTANCE.name()))
                .returning()
                .fetchOne();

        if (acceptorRecord != null) {
            ContactRecord pendingAcceptRecord = context.update(CONTACT)
                    .set(CONTACT.STATUS, ContactStatusEnum.ACCEPTED.name())
                    .where(CONTACT.MYCHAT_USER_ID.eq(req.contactMyChatUserId))
                    .and(CONTACT.CONTACT_MYCHAT_USER_ID.eq(req.myChatUserId))
                    .and(CONTACT.STATUS.eq(ContactStatusEnum.PENDING_CONFIRMATION.name()))
                    .returning()
                    .fetchOne();
            if (pendingAcceptRecord != null) {
                return AcceptContacts.Res.success();
            } else {
                return AcceptContacts.Res.failureNoPendingConfirmation();
            }
        } else {
            return AcceptContacts.Res.failureNoPendingAccept();
        }
    }


    @TransactionAnnotation
    @ApplicationTokenAnnotation
    @AccessTokenAnnotation
    public ListContacts.Res listContacts(ListContacts.Req req) {
        DSLContext dsl = dslProvider.get();
            Record1<Integer> total = dsl.selectCount()
                    .from(CONTACT)
                    .leftOuterJoin(MYCHAT_USER).on(MYCHAT_USER.MYCHAT_USER_ID.eq(CONTACT.CONTACT_MYCHAT_USER_ID))
                    .where(CONTACT.MYCHAT_USER_ID.eq(req.myChatUserId))
                    .and(MYCHAT_USER.STATUS.eq(MyChatUserStatusEnum.ACTIVE.name()))
                    .fetchOne();

            Result<Record> resultOfRecords = dsl.select()
                .from(CONTACT)
                .leftOuterJoin(MYCHAT_USER).on(MYCHAT_USER.MYCHAT_USER_ID.eq(CONTACT.CONTACT_MYCHAT_USER_ID))
                .leftOuterJoin(PROFILE).on(PROFILE.MYCHAT_USER_ID.eq(CONTACT.CONTACT_MYCHAT_USER_ID))
                .where(CONTACT.MYCHAT_USER_ID.eq(req.myChatUserId))
                .and(MYCHAT_USER.STATUS.eq(MyChatUserStatusEnum.ACTIVE.name()))
                .limit(req.limit)
                .offset(req.offset)
                .fetch();

        return ListContacts.Res.success(req, total.value1(), resultOfRecords);
    }

}
