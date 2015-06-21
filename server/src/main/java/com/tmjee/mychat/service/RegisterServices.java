package com.tmjee.mychat.service;

import com.google.inject.Inject;
import com.tmjee.mychat.domain.IdentificationTypeEnum;
import com.tmjee.mychat.domain.MyChatUserStatusEnum;
import com.tmjee.mychat.rest.Register;
import com.tmjee.mychat.service.annotations.DSLContextAnnotation;
import com.tmjee.mychat.utils.DigestUtils;
import org.jooq.DSLContext;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

import static com.tmjee.jooq.generated.Tables.*;

/**
 * @author tmjee
 */
public class RegisterServices {
    private final DSLContext dsl;

    @Inject
    public RegisterServices(@DSLContextAnnotation DSLContext dsl) {
        this.dsl = dsl;
    }


    public Register.Res register(Register.Req req) throws NoSuchAlgorithmException {

        String salt = DigestUtils.toHex(DigestUtils.randomizeNumber());

        String passwd = DigestUtils.hashPassword(req.password, salt);

        dsl.insertInto(MYCHAT_USER,
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
                    MyChatUserStatusEnum.PENDING.name());
        return null;
    }
}
