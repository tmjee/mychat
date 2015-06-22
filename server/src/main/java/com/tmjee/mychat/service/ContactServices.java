package com.tmjee.mychat.service;

import com.google.inject.Inject;
import static com.tmjee.jooq.generated.Tables.*;

import com.tmjee.jooq.generated.tables.Contact;
import com.tmjee.mychat.rest.AddContacts;
import com.tmjee.mychat.service.annotations.DSLContextAnnotation;
import org.jooq.DSLContext;

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


        dsl.selectCount()
                .from(CONTACT)
                .where(CONTACT.MYCHAT_USER_ID.eq(req.myChatUserId))
                .and(CONTACT.CONTACT_MYCHAT_USER_ID.eq(req.contactMyChatUserId))
                .and(CONTACT.STATUS)
                .fetch();


        return null;
    }
}
