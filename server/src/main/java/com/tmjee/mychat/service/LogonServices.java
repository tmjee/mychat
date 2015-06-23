package com.tmjee.mychat.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tmjee.jooq.generated.tables.records.AccessTokenRecord;
import com.tmjee.jooq.generated.tables.records.MychatUserRecord;
import com.tmjee.mychat.domain.IdentificationTypeEnum;
import com.tmjee.mychat.domain.TokenStateEnum;
import com.tmjee.mychat.rest.Logon;
import com.tmjee.mychat.service.annotations.ApplicationTokenAnnotation;
import com.tmjee.mychat.service.annotations.DSLContextAnnotation;
import com.tmjee.mychat.service.annotations.TransactionAnnotation;
import com.tmjee.mychat.service.annotations.UserPreferencesAnnotation;
import com.tmjee.mychat.utils.DigestUtils;
import org.jooq.DSLContext;
import org.jooq.Result;

import static com.tmjee.jooq.generated.Tables.*;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author tmjee
 */
public class LogonServices {

    private final Provider<DSLContext> dslContextProvider;
    private final Provider<UserPreferences> userPreferencesProvider;

    @Inject
    public LogonServices(@DSLContextAnnotation Provider<DSLContext> dslContextProvider,
                         @UserPreferencesAnnotation Provider<UserPreferences> userPreferencesProvider) {
        this.dslContextProvider = dslContextProvider;
        this.userPreferencesProvider = userPreferencesProvider;
    }


    @TransactionAnnotation
    @ApplicationTokenAnnotation
    public Logon.Res logon(Logon.Req req) throws NoSuchAlgorithmException {
        System.out.println("**** userPreferences="+userPreferencesProvider);
        System.out.println("**** userPreferences="+userPreferencesProvider.get());
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

                Result<AccessTokenRecord> tokenRecord =
                    context.insertInto(ACCESS_TOKEN,
                            ACCESS_TOKEN.MYCHAT_USER_ID,
                            ACCESS_TOKEN.ACCESS_TOKEN_,
                            ACCESS_TOKEN.STATE,
                            ACCESS_TOKEN.CREATION_DATE)
                        .values(myChatUserId,
                                accessToken,
                                TokenStateEnum.ACTIVE.name(),
                                new Timestamp(System.currentTimeMillis()))
                        .returning()
                        .fetch();

                userPreferencesProvider.get().setAccessToken(accessToken);

                System.out.println("****************************** success");
                return Logon.Res.success(accessToken, myChatUserRecord);
            } else {
                return Logon.Res.failedBadUsernamePasswordCombination();
            }
        }
        System.out.println("****************************** failed");
        return Logon.Res.failed();
    }


}
