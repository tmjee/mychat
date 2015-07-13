package com.tmjee.mychat.server.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tmjee.mychat.server.jooq.generated.tables.records.ChannelRecord;
import com.tmjee.mychat.server.jooq.generated.tables.records.MychatUserRecord;
import com.tmjee.mychat.server.jooq.generated.tables.records.ProfileRecord;
import com.tmjee.mychat.common.domain.*;

import com.tmjee.mychat.server.rest.Register;
import com.tmjee.mychat.server.service.annotations.ApplicationTokenAnnotation;
import com.tmjee.mychat.server.service.annotations.DSLContextAnnotation;
import com.tmjee.mychat.server.service.annotations.TransactionAnnotation;
import com.tmjee.mychat.server.utils.DigestUtils;
import org.jooq.DSLContext;
import org.jooq.Record1;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.tmjee.mychat.server.jooq.generated.Tables.*;
import static java.lang.String.format;

/**
 * @author tmjee
 */
public class RegisterServices {

    private static final Logger LOG = Logger.getLogger(RegisterServices.class.getName());

    private final Provider<DSLContext> dslProvider;

    @Inject
    public RegisterServices(@DSLContextAnnotation Provider<DSLContext> dslProvider) {
        this.dslProvider = dslProvider;
    }


    @TransactionAnnotation
    @ApplicationTokenAnnotation
    public Register.Res register(Register.Req req) throws NoSuchAlgorithmException {

        DSLContext dsl = dslProvider.get();

        Record1<Integer> count = dsl.selectCount()
                .from(MYCHAT_USER)
                .where(MYCHAT_USER.IDENTIFICATION_TYPE.eq(MyChatUserIdentificationTypeEnum.EMAIL.name()))
                .and(MYCHAT_USER.IDENTIFICATION.eq(req.email))
                .and(MYCHAT_USER.STATUS.eq(MyChatUserStatusEnum.ACTIVE.name()))
                .fetchOne();

        if (count.value1() > 0) {
            return Register.Res.alreadyExists(req);
        }



        String salt = DigestUtils.toHex(DigestUtils.randomizeNumber());
        String passwd = DigestUtils.hashPassword(req.password, salt);

        MychatUserRecord mychatUserRecord =
            dsl.insertInto(MYCHAT_USER,
                MYCHAT_USER.IDENTIFICATION_TYPE,
                MYCHAT_USER.IDENTIFICATION,
                MYCHAT_USER.CREATION_DATE,
                MYCHAT_USER.MODIFICATION_DATE,
                MYCHAT_USER.PASSWORD,
                MYCHAT_USER.SALT,
                MYCHAT_USER.STATUS,
                MYCHAT_USER.ACTIVATION_TOKEN)
            .values(
                    MyChatUserIdentificationTypeEnum.EMAIL.name(),
                    req.email,
                    new Timestamp(System.currentTimeMillis()),
                    new Timestamp(System.currentTimeMillis()),
                    passwd,
                    salt,
                    MyChatUserStatusEnum.PENDING.name(),
                    DigestUtils.randomizedId())
            .returning()
            .fetchOne();


        if (mychatUserRecord != null) {
            ProfileRecord profileRecord =
                    dsl.insertInto(PROFILE,
                            PROFILE.WHATSUP,
                            PROFILE.CREATION_DATE,
                            PROFILE.FULLNAME,
                            PROFILE.GENDER,
                            PROFILE.MYCHAT_USER_ID)
                    .values(
                            "",
                            new Timestamp(System.currentTimeMillis()),
                            req.fullname,
                            req.gender.name(),
                            mychatUserRecord.getMychatUserId()
                    ).returning().fetchOne();



            ChannelRecord channelRecord =
                dsl.insertInto(CHANNEL,
                    CHANNEL.CREATION_DATE,
                    CHANNEL.MYCHAT_USER_ID,
                    CHANNEL.TYPE,
                    CHANNEL.TYPE_ID)
                    .values(
                            new Timestamp(System.currentTimeMillis()),
                            mychatUserRecord.getMychatUserId(),
                            ChannelTypeEnum.MYCHAT.name(),
                            String.valueOf(mychatUserRecord.getMychatUserId()))
                    .returning()
                    .fetchOne();

            if (channelRecord != null && profileRecord != null) {
                return Register.Res.success(mychatUserRecord, channelRecord);
            } else {
                if (channelRecord == null) {
                    LOG.log(Level.SEVERE, format("no record inserted into CHANNEL table for MYCHAT_USER_ID %s", mychatUserRecord.getMychatUserId()));
                }
                if (profileRecord == null) {
                    LOG.log(Level.SEVERE, format("no record inserted into PROFILE table for MYCHAT_USER_ID %s", mychatUserRecord.getMychatUserId()));
                }
            }
        } else {
            LOG.log(Level.SEVERE, format("no record inserted into MYCHAT_USER table for identification %s", req.email));
        }
        return Register.Res.failed(req);
    }
}
