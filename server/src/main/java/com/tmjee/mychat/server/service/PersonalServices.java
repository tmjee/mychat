package com.tmjee.mychat.server.service;

import com.google.inject.Inject;
import com.tmjee.mychat.server.rest.WhatsUp;
import com.tmjee.mychat.server.service.annotations.AccessTokenAnnotation;
import com.tmjee.mychat.server.service.annotations.ApplicationTokenAnnotation;
import com.tmjee.mychat.server.service.annotations.DSLContextAnnotation;
import com.tmjee.mychat.server.service.annotations.TransactionAnnotation;
import org.jooq.DSLContext;
import org.jooq.Record1;

import javax.inject.Provider;

import java.sql.Timestamp;

import static com.tmjee.mychat.server.jooq.generated.Tables.*;

/**
 * @author tmjee
 */
public class PersonalServices {

    private final Provider<DSLContext> dslProvider;

    @Inject
    public PersonalServices(@DSLContextAnnotation Provider<DSLContext> dslProvider) {
        this.dslProvider = dslProvider;
    }


    @TransactionAnnotation
    @ApplicationTokenAnnotation
    @AccessTokenAnnotation
    public WhatsUp.Res whatsUp(WhatsUp.Req req) {
        DSLContext dsl = dslProvider.get();
        Record1<Integer> count = dsl.selectCount()
                .from(PROFILE)
                .where(PROFILE.MYCHAT_USER_ID.eq(req.myChatUserId))
                .fetchOne();

        if (count.value1() > 0) { // update
            dsl.update(PROFILE)
                    .set(PROFILE.WHATSUP, req.whatsUp)
                    .set(PROFILE.MODIFICATION_DATE, new Timestamp(System.currentTimeMillis()))
                    .where(PROFILE.MYCHAT_USER_ID.eq(req.myChatUserId))
                    .returning()
                    .fetchOne();

        }else { // insert
            dsl.insertInto(PROFILE, PROFILE.MYCHAT_USER_ID, PROFILE.WHATSUP, PROFILE.CREATION_DATE)
                    .values(req.myChatUserId, req.whatsUp, new Timestamp(System.currentTimeMillis()))
                    .returning()
                    .fetchOne();
        }
        return WhatsUp.Res.success();
    }



}
