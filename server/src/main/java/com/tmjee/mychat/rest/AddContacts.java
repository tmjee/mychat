package com.tmjee.mychat.rest;

import com.tmjee.jooq.generated.tables.records.ContactRecord;
import com.tmjee.mychat.domain.ContactStatusEnum;
import com.tmjee.mychat.service.ContactServices;
import org.jooq.Record;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static java.lang.String.format;
import static com.tmjee.jooq.generated.Tables.*;

/**
 * @author tmjee
 */
public class AddContacts extends V1<AddContacts.Req, AddContacts.Res> {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addContacts(Req req) {
        return action(req, this::f);
    }


    private Res f(Req req) {
        ContactServices contactServices = getInstance(ContactServices.class);
        return contactServices.addContact(req);
    }


    public static class Req extends V1.Req {

        public Integer myChatUserId;
        public Integer contactMyChatUserId;

        @Override
        protected void validate() {

        }
    }


    public static class Res extends V1.Res {

        public static Res success(Record contactMyChatUserProfile) {
            System.out.println("******8 fullname="+contactMyChatUserProfile.getValue(PROFILE.FULLNAME));
            Res res = new Res();
            res.addMessage(format("Contact added for %s, waiting acceptance",
                    contactMyChatUserProfile.getValue(PROFILE.FULLNAME)));
            return res;
        }

        public static Res failed(ContactRecord contactRecord, Record contactMyChatUserProfile) {
            Res res = new Res();
            switch (ContactStatusEnum.valueOf(contactRecord.getStatus())) {
                case PENDING_ACCEPTANCE:
                    res.addMessage(format("Contact %s pending acceptance", contactMyChatUserProfile.getValue(PROFILE.FULLNAME)));
                    break;
                case PENDING_CONFIRMATION:
                    res.addMessage(format("Contact %s pending confirmation", contactMyChatUserProfile.getValue(PROFILE.FULLNAME)));
                    break;
            }
            res.addMessage(format("Contact %s already exists", contactMyChatUserProfile.getValue(PROFILE.FULLNAME)));
            return res;
        }
    }
}
