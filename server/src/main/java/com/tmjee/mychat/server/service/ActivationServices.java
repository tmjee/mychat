package com.tmjee.mychat.server.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tmjee.mychat.common.domain.ActivationStatusEnum;
import com.tmjee.mychat.common.domain.ActivationTypeEnum;
import com.tmjee.mychat.server.jooq.generated.Tables;
import com.tmjee.mychat.server.jooq.generated.tables.records.ActivationRecord;
import com.tmjee.mychat.server.rest.CompleteActivation;
import com.tmjee.mychat.server.service.annotations.DSLContextAnnotation;
import org.jooq.DSLContext;
import org.jooq.Result;

import java.sql.Timestamp;

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
        ActivationRecord activationRecord =
        dsl.selectFrom(Tables.ACTIVATION)
                .where(Tables.ACTIVATION.ACTIVATION_TOKEN.eq(req.activationToken))
                .fetchOne();
        if (activationRecord != null &&
                activationRecord.getStatus().equals(ActivationStatusEnum.PENDING.name())) {

            activationRecord =
                dsl.update(Tables.ACTIVATION)
                    .set(Tables.ACTIVATION.STATUS, ActivationStatusEnum.DONE.name())
                    .set(Tables.ACTIVATION.MODIFICATION_DATE, new Timestamp(System.currentTimeMillis()))
                    .where(Tables.ACTIVATION.ACTIVATION_ID.eq(activationRecord.getActivationId()))
                    .returning()
                    .fetchOne();

            return CompleteActivation.Res.success(activationRecord);
        } else {
            return CompleteActivation.Res.failed(activationRecord);
        }
    }
}
