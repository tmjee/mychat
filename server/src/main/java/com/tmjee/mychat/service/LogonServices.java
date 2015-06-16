package com.tmjee.mychat.service;

import com.google.inject.Inject;
import com.tmjee.mychat.rest.Logon;
import org.jooq.impl.DSL;

import static org.jooq.impl.DSL.*;
import static com.tmjee.jooq.generated.Tables.*;

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
        select()
            .from(MYCHAT_USER)
            .where(MYCHAT_USER.IDENTIFICATION_TYPE.eq())
            .fetch()
                ;

    }


}
