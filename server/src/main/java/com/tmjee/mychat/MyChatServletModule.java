package com.tmjee.mychat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import com.tmjee.mychat.service.*;
import com.tmjee.mychat.service.annotations.*;
import org.aopalliance.intercept.MethodInterceptor;
import org.jooq.DSLContext;
import org.objectweb.jotm.Jotm;

import javax.sql.DataSource;
import javax.transaction.UserTransaction;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tmjee
 */
public class MyChatServletModule extends ServletModule {

    private static final Logger LOG = Logger.getLogger(MyChatServletModule.class.getName());

    @Override
    protected void configureServlets() {

        // bind properties
        try {
            Properties prop = new Properties();
            prop.load(getClass().getResourceAsStream("/config.properties"));

            System.out.println(getClass().getResourceAsStream("/config.properties"));
            prop.forEach((k,v)->
                    bindConstant()
                        .annotatedWith(Names.named((k.toString())))
                        .to(v.toString()));
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "unable to locate config.properties");
        }

        MethodInterceptor transactionInterceptor = new TransactionInterceptor();
        requestInjection(transactionInterceptor);

        bindInterceptor(
                Matchers.any(),
                Matchers.annotatedWith(TransactionAnnotation.class),
                transactionInterceptor);

        bindInterceptor(
                Matchers.annotatedWith(TransactionAnnotation.class),
                Matchers.any(),
                transactionInterceptor);


        // jotm
        bind(Jotm.class)
                .annotatedWith(JotmAnnotation.class)
                .toProvider(JotmProvider.class)
                .in(Singleton.class);


        // datasource
        bind(DataSource.class)
                .annotatedWith(DataSourceAnnotation.class)
                .toProvider(DataSourceProvider.class)
                .in(Singleton.class);


        // DSLContext
        bind(DSLContext.class)
                .annotatedWith(DSLContextAnnotation.class)
                .toProvider(DSLContextProvider.class)
                .in(Singleton.class);

        // ObjectMapper
        bind(ObjectMapper.class)
            .annotatedWith(ObjectMapperAnnotation.class)
            .toProvider(ObjectMapperProvider.class)
            .in(Singleton.class);

        // Services
        bind(LogonServices.class)
                .annotatedWith(LogonServiceAnnotation.class)
                .to(LogonServices.class)
                .in(Singleton.class);
    }
}
