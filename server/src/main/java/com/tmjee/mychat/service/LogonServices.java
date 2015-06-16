package com.tmjee.mychat.service;

import com.google.inject.Inject;
import com.tmjee.jooq.generated.tables.MychatUser;
import com.tmjee.mychat.rest.Logon;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

import static org.jooq.impl.DSL.*;
import static com.tmjee.jooq.generated.tables.MychatUser.*;

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


    public void logon(String email, String password) {
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
            record.getValue(MYCHAT_USER.SALT);
        }
    }


}
