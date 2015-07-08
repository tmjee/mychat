package com.tmjee.mychat.server.rest;

import static com.tmjee.mychat.server.jooq.generated.Tables.*;

import com.tmjee.mychat.server.jooq.generated.tables.records.ContactRecord;
import com.tmjee.mychat.server.jooq.generated.tables.records.MychatUserRecord;
import com.tmjee.mychat.server.jooq.generated.tables.records.ProfileRecord;
import com.tmjee.mychat.server.service.ContactServices;
import org.jooq.Record;
import org.jooq.Result;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.*;

/**
 * @author tmjee
 */
@Provider
public class ListContacts extends V1<ListContacts.Req, ListContacts.Res>{


    @POST
    @Path("/contacts/{myChatUserId}/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response listContacts(Req req,
                                 @PathParam("myChatUserId") Integer myChatUserId) {
        req.myChatUserId = myChatUserId;
        return action(req, this::f);
    }

    private Res f(Req req) {
        ContactServices contactServices = getInstance(ContactServices.class);
        return contactServices.listContacts(req);
    }


    public static class Req extends V1.Req {

        public Integer myChatUserId;
        public Integer limit;
        public Integer offset;

        @Override
        protected void validate() {

        }
    }

    public static class Res extends V1.Res {

        public List<Map<String, String>> contacts;
        public int total;
        public int limit;
        public int offset;

        public static Res success(Req req, int total, Result<Record> resultOfRecords) {
            Res res = new Res();
            res.total = total;
            res.limit = req.limit;
            res.offset = req.offset;
            res.addMessage("Contacts retrieved successfully");
            res.contacts = new ArrayList<>();
            Iterator<Record> i = resultOfRecords.iterator();
            while(i.hasNext()) {
                Record r = i.next();
                ContactRecord contactRecord = r.into(CONTACT);
                MychatUserRecord mychatUserRecord = r.into(MYCHAT_USER);
                ProfileRecord profileRecord = r.into(PROFILE);

                Map<String, String> m = new HashMap<>();
                m.put("name", profileRecord.getFullname());
                m.put("myChatUserId", contactRecord.getContactMychatUserId().toString());

                res.contacts.add(m);
            }
            return res;
        }
    }
}
