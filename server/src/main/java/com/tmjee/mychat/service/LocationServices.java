package com.tmjee.mychat.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tmjee.mychat.rest.Location;
import com.tmjee.mychat.service.annotations.DSLContextAnnotation;
import org.jooq.DSLContext;
import org.jooq.Record1;

import static com.tmjee.jooq.generated.Tables.*;

/**
 * @author tmjee
 */
public class LocationServices {

    private final Provider<DSLContext> dslProvider;

    @Inject
    public LocationServices(@DSLContextAnnotation Provider<DSLContext> dslProvider) {
        this.dslProvider = dslProvider;
    }


    public Location.Res contributeLocation(Location.Req req) {
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
                    .where(LOCATION.MYCHAT_USER_ID.eq(req.myChatUserId))
                    .returning()
                    .fetchOne();
        } else { // insert
           dsl.insertInto(LOCATION)
                   .columns(LOCATION.MYCHAT_USER_ID, LOCATION.LONGITUDE, LOCATION.LATITUDE)
                   .values(req.myChatUserId, req.latitude, req.longitude)
                   .returning()
                   .fetchOne();
        }
        return Location.Res.success();
    }
}
