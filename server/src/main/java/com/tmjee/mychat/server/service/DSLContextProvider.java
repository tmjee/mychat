package com.tmjee.mychat.server.service;

import com.google.inject.Inject;
import com.tmjee.mychat.server.service.annotations.DataSourceAnnotation;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.inject.Provider;
import javax.sql.DataSource;

/**
 * @author tmjee
 */
public class DSLContextProvider implements Provider<DSLContext> {

    private final DataSource dataSource;

    @Inject
    public DSLContextProvider(@DataSourceAnnotation DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DSLContext get() {
        return DSL.using(dataSource, SQLDialect.POSTGRES);
    }
}
