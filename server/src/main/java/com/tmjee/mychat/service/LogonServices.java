package com.tmjee.mychat.service;

import com.google.inject.Inject;
import com.tmjee.mychat.domain.IdentificationTypeEnum;
import com.tmjee.mychat.domain.LogonResult;
import com.tmjee.mychat.domain.TokenStateEnum;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

import static org.jooq.impl.DSL.*;
import static com.tmjee.jooq.generated.Tables.*;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author tmjee
 */
public class LogonServices {


    private DSL dsl;

    @Inject
    public LogonServices(DSL dsl) {
        this.dsl = dsl;
    }


    public LogonResult logon(String email, String password) throws NoSuchAlgorithmException {
        Result<Record> result =
            select()
                .from(MYCHAT_USER)
                .where(MYCHAT_USER.IDENTIFICATION_TYPE.eq(IdentificationTypeEnum.EMAIL.name()))
                .and(MYCHAT_USER.IDENTIFICATION_TYPE.isNotNull())
                .and(MYCHAT_USER.IDENTIFICATION.isNotNull())
                .and(MYCHAT_USER.IDENTIFICATION.eq(email))
                .fetch();
        if (result.isNotEmpty()) {
            Record record = result.get(0);
            String salt = record.getValue(MYCHAT_USER.SALT);
            String pwd = record.getValue(MYCHAT_USER.PASSWORD);

            String hashedPassword = DigestUtils.hashPassword(password, salt);
            if (pwd.equalsIgnoreCase(hashedPassword)) {
                Integer myChatUserId = record.getValue(MYCHAT_USER.MYCHAT_USER_ID);

                String accessToken = UUID.randomUUID().toString();

                insertInto(TOKEN,
                        TOKEN.MYCHAT_USER_ID,
                        TOKEN.TOKEN_,
                        TOKEN.STATE,
                        TOKEN.CREATION_DATE)
                        .values(myChatUserId,
                                accessToken,
                                TokenStateEnum.ACTIVE.name(),
                                new Timestamp(System.currentTimeMillis()))
                        .execute();

                return LogonResult.success(accessToken, record);
            }
        }
        return LogonResult.failed();
    }


}
