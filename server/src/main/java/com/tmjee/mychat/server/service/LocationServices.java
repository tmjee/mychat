package com.tmjee.mychat.server.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tmjee.mychat.server.rest.UpdateLocation;
import com.tmjee.mychat.server.service.annotations.DSLContextAnnotation;
import org.jooq.DSLContext;
import org.jooq.Record1;

import static com.tmjee.mychat.server.jooq.generated.Tables.*;

/**
 * @author tmjee
 */
public class LocationServices {

    private final Provider<DSLContext> dslProvider;

    @Inject
    public LocationServices(@DSLContextAnnotation Provider<DSLContext> dslProvider) {
        this.dslProvider = dslProvider;
    }


    public UpdateLocation.Res contributeLocation(UpdateLocation.Req req) {
        DSLContext dsl = dslProvider.get();
        Record1<Integer> count =
            dsl.selectCount()
                .from(LOCATION)
                .where(LOCATION.MYCHAT_USER_ID.eq(req.myChatUserId))
                .fetchOne();
        if (count.value1() > 0) { // update
            dsl.update(LOCATION)
                    .set(LOCATION.LATITUDE, req.latitude)
                    .set(LOCATION.LONGITUDE, req.longitude)
                    .set(LOCATION.NAME, req.name)
                    .where(LOCATION.MYCHAT_USER_ID.eq(req.myChatUserId))
                    .returning()
                    .fetchOne();
        } else { // insert
           dsl.insertInto(LOCATION)
                   .columns(LOCATION.MYCHAT_USER_ID, LOCATION.NAME, LOCATION.LONGITUDE, LOCATION.LATITUDE)
                   .values(req.myChatUserId, req.name, req.latitude, req.longitude)
                   .returning()
                   .fetchOne();
        }
        return UpdateLocation.Res.success();
    }
}
