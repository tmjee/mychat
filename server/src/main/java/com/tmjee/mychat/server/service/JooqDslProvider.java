package com.tmjee.mychat.server.service;

import org.jooq.impl.DSL;

import javax.inject.Provider;
import java.util.Properties;

/**
 * Created by tmjee on 6/06/15.
 */
public class JooqDslProvider implements Provider<DSL> {

    private static Properties properties;
    static {
    }



    @Override
    public DSL get() {
        return null;
    }
}
