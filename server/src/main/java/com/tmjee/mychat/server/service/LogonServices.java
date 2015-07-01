package com.tmjee.mychat.server.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tmjee.jooq.generated.tables.records.AccessTokenRecord;
import com.tmjee.jooq.generated.tables.records.MychatUserRecord;
import com.tmjee.jooq.generated.tables.records.RoleRecord;
import com.tmjee.mychat.common.domain.MyChatUserIdentificationTypeEnum;
import com.tmjee.mychat.common.domain.AccessTokenStateEnum;
import com.tmjee.mychat.common.domain.RolesEnum;
import com.tmjee.mychat.server.rest.Logon;
import com.tmjee.mychat.server.service.annotations.ApplicationTokenAnnotation;
import com.tmjee.mychat.server.service.annotations.DSLContextAnnotation;
import com.tmjee.mychat.server.service.annotations.TransactionAnnotation;
import com.tmjee.mychat.server.service.annotations.UserPreferencesAnnotation;
import com.tmjee.mychat.server.utils.DigestUtils;
import org.jooq.DSLContext;
import org.jooq.Result;

import static com.tmjee.jooq.generated.Tables.*;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.EnumSet;
import java.util.Iterator;
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
        DSLContext context = dslContextProvider.get();
        Result<MychatUserRecord> result =
            context.selectFrom(MYCHAT_USER)
                .where(MYCHAT_USER.IDENTIFICATION_TYPE.eq(MyChatUserIdentificationTypeEnum.EMAIL.name()))
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

                // handle access token record
                Result<AccessTokenRecord> tokenRecord =
                    context.insertInto(ACCESS_TOKEN,
                            ACCESS_TOKEN.MYCHAT_USER_ID,
                            ACCESS_TOKEN.ACCESS_TOKEN_,
                            ACCESS_TOKEN.STATE,
                            ACCESS_TOKEN.CREATION_DATE)
                        .values(myChatUserId,
                                accessToken,
                                AccessTokenStateEnum.ACTIVE.name(),
                                new Timestamp(System.currentTimeMillis()))
                        .returning()
                        .fetch();

                String oldAccessToken = userPreferencesProvider.get().getAccessToken();
                if (oldAccessToken != null) {
                    context.update(ACCESS_TOKEN)
                            .set(ACCESS_TOKEN.STATE, AccessTokenStateEnum.LOGOUT.name())
                            .set(ACCESS_TOKEN.MODIFICATION_DATE, new Timestamp(System.currentTimeMillis()))
                            .execute();
                }
                userPreferencesProvider.get().setAccessToken(accessToken);

                // handle roles
                Result<RoleRecord> roleRecordsResult = context.selectFrom(ROLE).where(ROLE.MYCHAT_USER_ID.eq(myChatUserId)).fetch();
                EnumSet<RolesEnum> roles = EnumSet.noneOf(RolesEnum.class);
                Iterator<RoleRecord> i = roleRecordsResult.iterator();
                while(i.hasNext()) {
                    roles.add(RolesEnum.valueOf(i.next().getRole()));
                }
                userPreferencesProvider.get().setRoles(roles);


                return Logon.Res.success(accessToken, myChatUserRecord);
            } else {
                return Logon.Res.failedBadUsernamePasswordCombination();
            }
        }
        return Logon.Res.failed();
    }


}
