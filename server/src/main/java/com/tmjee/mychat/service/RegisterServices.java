package com.tmjee.mychat.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tmjee.jooq.generated.tables.records.ChannelRecord;
import com.tmjee.jooq.generated.tables.records.MychatUserRecord;
import com.tmjee.mychat.domain.ChannelTypeEnum;
import com.tmjee.mychat.domain.IdentificationTypeEnum;
import com.tmjee.mychat.domain.MyChatUserStatusEnum;
import com.tmjee.mychat.rest.Register;
import com.tmjee.mychat.service.annotations.ApplicationTokenAnnotation;
import com.tmjee.mychat.service.annotations.TransactionAnnotation;
import com.tmjee.mychat.utils.DigestUtils;
import org.jooq.DSLContext;
import org.jooq.Result;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.tmjee.jooq.generated.Tables.*;
import static java.lang.String.format;

/**
 * @author tmjee
 */
public class RegisterServices {

    private static final Logger LOG = Logger.getLogger(RegisterServices.class.getName());

    private final Provider<DSLContext> dslProvider;

    @Inject
    public RegisterServices(Provider<DSLContext> dslProvider) {
        this.dslProvider = dslProvider;
    }


    @TransactionAnnotation
    @ApplicationTokenAnnotation
    public Register.Res register(Register.Req req) throws NoSuchAlgorithmException {

        String salt = DigestUtils.toHex(DigestUtils.randomizeNumber());
        String passwd = DigestUtils.hashPassword(req.password, salt);

        Result<MychatUserRecord> record =
            dslProvider.get().insertInto(MYCHAT_USER,
                MYCHAT_USER.IDENTIFICATION_TYPE,
                MYCHAT_USER.IDENTIFICATION,
                MYCHAT_USER.CREATION_DATE,
                MYCHAT_USER.MODIFICATION_DATE,
                MYCHAT_USER.PASSWORD,
                MYCHAT_USER.SALT,
                MYCHAT_USER.STATUS)
            .values(
                    IdentificationTypeEnum.EMAIL.name(),
                    req.email,
                    new Timestamp(System.currentTimeMillis()),
                    new Timestamp(System.currentTimeMillis()),
                    passwd,
                    salt,
                    MyChatUserStatusEnum.PENDING.name())
            .returning()
            .fetch();


        if (record.isNotEmpty()) {
            Result<ChannelRecord> channelRecord =
                dslProvider.get().insertInto(CHANNEL,
                    CHANNEL.CREATION_DATE,
                    CHANNEL.MYCHAT_USER_ID,
                    CHANNEL.TYPE,
                    CHANNEL.TYPE_ID)
                    .values(
                            new Timestamp(System.currentTimeMillis()),
                            record.get(0).getMychatUserId(),
                            ChannelTypeEnum.MYCHAT.name(),
                            String.valueOf(record.get(0).getMychatUserId()))
                    .returning()
                    .fetch();

            if (channelRecord.isNotEmpty()) {
                return Register.Res.success(record.get(0), channelRecord.get(0));
            }
            LOG.log(Level.SEVERE, format("no record inserted into CHANNEL table for MYCHAT_USER_ID %s", record.get(0).getMychatUserId()));
        } else {
            return Register.Res.alreadyExists(req);
        }
        return Register.Res.failed(req);
    }
}
