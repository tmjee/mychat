package com.tmjee.mychat.service;

import com.google.inject.Inject;
import com.tmjee.jooq.generated.tables.Token;
import com.tmjee.jooq.generated.tables.records.MychatUserRecord;
import com.tmjee.jooq.generated.tables.records.TokenRecord;
import com.tmjee.mychat.domain.IdentificationTypeEnum;
import com.tmjee.mychat.domain.TokenStateEnum;
import com.tmjee.mychat.rest.Logon;
import com.tmjee.mychat.service.annotations.UserPreferencesAnnotation;
import com.tmjee.mychat.utils.DigestUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import static org.jooq.impl.DSL.*;
import static com.tmjee.jooq.generated.Tables.*;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author tmjee
 */
public class LogonServices {

    private final DSLContextProvider dslContextProvider;
    private final UserPreferences userPreferences;

    @Inject
    public LogonServices(DSLContextProvider dslContextProvider,
                         @UserPreferencesAnnotation UserPreferences userPreferences) {
        this.dslContextProvider = dslContextProvider;
        this.userPreferences = userPreferences;
    }


    public Logon.Res logon(Logon.Req req) throws NoSuchAlgorithmException {
        DSLContext context = dslContextProvider.get();
        System.out.println("******************************");
        Result<MychatUserRecord> result =
            context.selectFrom(MYCHAT_USER)
                .where(MYCHAT_USER.IDENTIFICATION_TYPE.eq(IdentificationTypeEnum.EMAIL.name()))
                .and(MYCHAT_USER.IDENTIFICATION_TYPE.isNotNull())
                .and(MYCHAT_USER.IDENTIFICATION.isNotNull())
                .and(MYCHAT_USER.IDENTIFICATION.eq(req.email))
                .fetch();

        if (result.isNotEmpty()) {
            MychatUserRecord myChatUserRecord = result.get(0);
            String salt = myChatUserRecord.getValue(MYCHAT_USER.SALT);
            String pwd = myChatUserRecord.getValue(MYCHAT_USER.PASSWORD);

            String hashedPassword = DigestUtils.hashPassword(req.password, salt);
            if (pwd.equalsIgnoreCase(hashedPassword)) {
                Integer myChatUserId = myChatUserRecord.getValue(MYCHAT_USER.MYCHAT_USER_ID);

                String accessToken = UUID.randomUUID().toString();

                Result<TokenRecord> tokenRecord =
                    insertInto(TOKEN,
                        TOKEN.MYCHAT_USER_ID,
                        TOKEN.TOKEN_,
                        TOKEN.STATE,
                        TOKEN.CREATION_DATE)
                        .values(myChatUserId,
                                accessToken,
                                TokenStateEnum.ACTIVE.name(),
                                new Timestamp(System.currentTimeMillis()))
                        .returning()
                        .fetch();

                userPreferences.setAccessToken(accessToken);

                System.out.println("****************************** success");
                return Logon.Res.success(accessToken, myChatUserRecord);
            }
        }
        System.out.println("****************************** failed");
        return Logon.Res.failed();
    }


}
