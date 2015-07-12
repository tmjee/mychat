package com.tmjee.mychat.server.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tmjee.mychat.common.domain.MyChatUserStatusEnum;
import com.tmjee.mychat.server.jooq.generated.Tables;
import com.tmjee.mychat.server.jooq.generated.tables.records.MychatUserRecord;
import com.tmjee.mychat.server.jooq.generated.tables.records.ProfileRecord;
import com.tmjee.mychat.server.rest.CompleteActivation;
import com.tmjee.mychat.server.service.annotations.DSLContextAnnotation;
import org.jooq.DSLContext;

/**
 * @author tmjee
 */
public class ActivationServices {

    private final Provider<DSLContext> dslProvider;

    @Inject
    public ActivationServices(@DSLContextAnnotation Provider<DSLContext> dslProvider) {
        this.dslProvider = dslProvider;
    }

    public CompleteActivation.Res completeActivation(CompleteActivation.Req req) {
        DSLContext dsl = dslProvider.get();

        MychatUserRecord mychatUserRecord =
            dsl.selectFrom(Tables.MYCHAT_USER)
                .where(Tables.MYCHAT_USER.ACTIVATION_TOKEN.eq(req.activationToken))
                .fetchOne();

        if (mychatUserRecord.getStatus().equals(MyChatUserStatusEnum.PENDING.name())) {
            mychatUserRecord =
                dsl.update(Tables.MYCHAT_USER)
                    .set(Tables.MYCHAT_USER.STATUS, MyChatUserStatusEnum.ACTIVE.name())
                    .where(Tables.MYCHAT_USER.MYCHAT_USER_ID.eq(mychatUserRecord.getMychatUserId()))
                    .returning()
                    .fetchOne();
            return CompleteActivation.Res.success(mychatUserRecord);
        } else {
            return CompleteActivation.Res.failed(mychatUserRecord);
        }
    }
}
