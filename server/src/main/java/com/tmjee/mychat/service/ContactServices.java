package com.tmjee.mychat.service;

import com.google.inject.Inject;
import static com.tmjee.jooq.generated.Tables.*;

import com.tmjee.jooq.generated.tables.Contact;
import com.tmjee.jooq.generated.tables.MychatUser;
import com.tmjee.jooq.generated.tables.Profile;
import com.tmjee.jooq.generated.tables.records.ContactRecord;
import com.tmjee.jooq.generated.tables.records.MychatUserRecord;
import com.tmjee.mychat.domain.ContactStatusEnum;
import com.tmjee.mychat.rest.AddContacts;
import com.tmjee.mychat.service.annotations.DSLContextAnnotation;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import java.sql.Timestamp;

/**
 * @author tmjee
 */
public class ContactServices {

    private final DSLContext dsl;

    @Inject
    public ContactServices(@DSLContextAnnotation DSLContext dsl) {
        this.dsl = dsl;
    }


    public AddContacts.Res addContact(AddContacts.Req req) {

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
            Profile profile = PROFILE.as("p");
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
        Profile profile = PROFILE.as("p");
        Record contactMyChatUser = dsl
                .select(profile.fields())
                .from(myChatUser)
                .leftOuterJoin(profile)
                .on(PROFILE.MYCHAT_USER_ID.eq(MYCHAT_USER.MYCHAT_USER_ID))
                .where(MYCHAT_USER.MYCHAT_USER_ID.eq(result.get(0).getContactMychatUserId()))
                .fetchOne();
        return AddContacts.Res.failed(result.get(0), contactMyChatUser);
    }
}
