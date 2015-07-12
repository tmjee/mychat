package com.tmjee.mychat.server.service;

import com.google.inject.Inject;
import static com.tmjee.mychat.server.jooq.generated.Tables.*;
import static java.lang.String.format;

import com.google.inject.Provider;
import com.tmjee.mychat.server.jooq.generated.tables.records.ApplicationTokenRecord;
import com.tmjee.mychat.server.exception.InvalidApplicationTokenException;
import com.tmjee.mychat.server.rest.V1;
import com.tmjee.mychat.server.service.annotations.DSLContextAnnotation;
import com.tmjee.mychat.server.service.annotations.UserPreferencesAnnotation;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jooq.DSLContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tmjee
 */
public class ApplicationTokenInterceptor implements MethodInterceptor {

    private static final Logger LOG = Logger.getLogger(ApplicationTokenInterceptor.class.getName());

    private Provider<DSLContext> dslProvider;
    private volatile Provider<UserPreferences> userPreferencesProvider;

    @Inject
    public void setDsl(@DSLContextAnnotation Provider<DSLContext> dslProvider) {
        this.dslProvider = dslProvider;
    }

    @Inject
    public void setUserPreferences(@UserPreferencesAnnotation Provider<UserPreferences> userPreferencesProvider) {
        this.userPreferencesProvider = userPreferencesProvider;
    }


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        ApplicationTokenRecord applicationRecord = null;
        String applicationToken = null;
        try {

            Object argument0 = invocation.getArguments()[0];
            Field f = argument0.getClass().getField("applicationToken");
            f.setAccessible(true);

            //V1.Req rq = V1.Req.class.cast(argument0);

            applicationToken = f.get(argument0).toString();
            String _applicationToken = applicationToken;
            LOG.log(Level.FINEST, ()->format("applicationToken read %s", _applicationToken));

        } catch(Exception e) {
            throw new InvalidApplicationTokenException(format("no field in parameters to identify application token"), e);
        }
        applicationRecord =
                dslProvider.get().selectFrom(APPLICATION_TOKEN)
                        .where(APPLICATION_TOKEN.APPLICATION_TOKEN_.eq(applicationToken))
                        .fetchOne();

        if (applicationRecord == null) {
            throw new InvalidApplicationTokenException(format("no matching application token for the one passed in [%s]", applicationToken));
        }

        userPreferencesProvider.get().setApplicationToken(applicationRecord.getApplicationToken());

        return invocation.proceed();
    }
}
