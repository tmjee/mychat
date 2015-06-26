package com.tmjee.mychat.rest;

import com.tmjee.jooq.generated.Tables;
import com.tmjee.jooq.generated.tables.records.MomentRecord;
import com.tmjee.jooq.generated.tables.records.MychatUserRecord;
import com.tmjee.jooq.generated.tables.records.ProfileRecord;
import com.tmjee.mychat.service.MomentServices;
import org.glassfish.jersey.message.internal.MediaTypes;
import org.jooq.Record;
import org.jooq.Result;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.*;

/**
 * @author tmjee
 */
@Provider
public class ListMoments  extends V1<ListMoments.Req, ListMoments.Res> {



    @POST
    @Path("/moments/{myChatUserId}/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response listMoments(Req req) {
        return action(req, this::f);
    }

    private Res f(Req req) {
        MomentServices momentServices = getInstance(MomentServices.class);
        return momentServices.listMoments(req);
    }


    public static class Req extends V1.Req {

        public Integer myChatUserId;
        public Integer offset;
        public Integer limit;

        @Override
        protected void validate() {

        }
    }

    public static class Res extends V1.Res {

        public Integer total;
        public List<Map<String, String>> moments;


        public static Res success(Result<Record> r) {
            Res res = new Res();
            res.moments = new ArrayList<>();
            Iterator<Record> i = r.iterator();
            while(i.hasNext()) {
                Map<String, String> m = new HashMap<>();
                Record rec = i.next();

                MomentRecord momentRecord = rec.into(Tables.MOMENT);
                MychatUserRecord mychatUserRecord = rec.into(Tables.MYCHAT_USER);
                ProfileRecord profileRecord = rec.into(Tables.PROFILE);

                m.put("myChatUserId", mychatUserRecord.getMychatUserId());
                m.put("momentId", momentRecord.getMomentId());
                m.put("message", momentRecord.getMessage());
                m.put("image", momentRecord.getEncoded());





            }
            return res;
        }
    }
}
