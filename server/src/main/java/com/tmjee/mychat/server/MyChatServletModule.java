package com.tmjee.mychat.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import com.google.inject.servlet.ServletScopes;
import com.tmjee.mychat.server.jooq.generated.tables.Activation;
import com.tmjee.mychat.server.service.*;
import com.tmjee.mychat.server.service.annotations.*;
import org.aopalliance.intercept.MethodInterceptor;
import org.jooq.DSLContext;
import org.objectweb.jotm.Jotm;

import javax.sql.DataSource;
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




        // user preferences
        bind(UserPreferences.class)
                .annotatedWith(UserPreferencesAnnotation.class)
                .toProvider(UserPreferencesProvider.class)
                .in(ServletScopes.SESSION);


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
                .in(MyChatScopes.ThreadLocalScope);


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
        bind(RegisterServices.class)
                .annotatedWith(RegisterServiceAnnotation.class)
                .to(RegisterServices.class)
                .in(Singleton.class);
        bind(ContactServices.class)
                .annotatedWith(ContactServiceAnnotation.class)
                .to(ContactServices.class)
                .in(Singleton.class);
        bind(ChatServices.class)
                .annotatedWith(ChatServiceAnnotation.class)
                .to(ChatServices.class)
                .in(Singleton.class);
        bind(PersonalServices.class)
                .annotatedWith(PersonalServicesAnnotation.class)
                .to(PersonalServices.class)
                .in(Singleton.class);
        bind(LocationServices.class)
                .annotatedWith(LocationServicesAnnotation.class)
                .to(LocationServices.class)
                .in(Singleton.class);
        bind(ProfileServices.class)
                .annotatedWith(ProfileServicesAnnotation.class)
                .to(ProfileServices.class)
                .in(Singleton.class);
        bind(ActivationServices.class)
                .annotatedWith(ActivationServicesAnnotation.class)
                .to(ActivationServices.class)
                .in(Singleton.class);



        // TransactionInterceptor
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

        // ApplicationTokenInterceptor
        ApplicationTokenInterceptor applicationTokenInterceptor = new ApplicationTokenInterceptor();
        requestInjection(applicationTokenInterceptor);

        bindInterceptor(
                Matchers.annotatedWith(ApplicationTokenAnnotation.class),
                Matchers.any(),
                applicationTokenInterceptor);

        bindInterceptor(
                Matchers.any(),
                Matchers.annotatedWith(ApplicationTokenAnnotation.class),
                applicationTokenInterceptor);


        // AccessTokenInterceptor
        AccessTokenInterceptor accessTokenInterceptor = new AccessTokenInterceptor();
        requestInjection(accessTokenInterceptor);

        bindInterceptor(
                Matchers.annotatedWith(AccessTokenAnnotation.class),
                Matchers.any(),
                accessTokenInterceptor);
        bindInterceptor(
                Matchers.any(),
                Matchers.annotatedWith(AccessTokenAnnotation.class),
                accessTokenInterceptor);

        // RolesInterceptor
        RolesInterceptor rolesInterceptor = new RolesInterceptor();
        requestInjection(rolesInterceptor);

        bindInterceptor(
                Matchers.annotatedWith(RolesAnnotation.class),
                Matchers.any(),
                rolesInterceptor);
        bindInterceptor(
                Matchers.any(),
                Matchers.annotatedWith(RolesAnnotation.class),
                rolesInterceptor);



        // servlets
        bind(MyChatImageServlet.class).in(Singleton.class);
        serve("/mychat/images/*").with(MyChatImageServlet.class);

    }
}
