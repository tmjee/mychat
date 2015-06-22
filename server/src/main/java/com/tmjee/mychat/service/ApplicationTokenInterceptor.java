package com.tmjee.mychat.service;

import com.google.inject.Inject;
import static com.tmjee.jooq.generated.Tables.*;
import static java.lang.String.format;

import com.tmjee.jooq.generated.tables.records.ApplicationRecord;
import com.tmjee.mychat.exception.InvalidApplicationTokenException;
import com.tmjee.mychat.service.annotations.DSLContextAnnotation;
import com.tmjee.mychat.service.annotations.UserPreferencesAnnotation;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jooq.DSLContext;

import java.lang.reflect.Field;

/**
 * @author tmjee
 */
public class ApplicationTokenInterceptor implements MethodInterceptor {

    private DSLContext dsl;
    private volatile UserPreferences userPreferences;

    @Inject
    public void setDsl(@DSLContextAnnotation DSLContext dslContext) {
        this.dsl = dslContext;
    }

    @Inject
    public void setUserPreferences(@UserPreferencesAnnotation UserPreferences userPreferences) {
        this.userPreferences = userPreferences;
    }


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        ApplicationRecord applicationRecord = null;
        try {
            Field f = invocation.getMethod().getParameters()[0].getClass().getField("applicationToken");
            Object argument0 = invocation.getArguments()[0];

            String applicationToken = f.get(argument0).toString();

            applicationRecord =
                    dsl.selectFrom(APPLICATION)
                            .where(APPLICATION.KEY.eq(applicationToken))
                            .fetchOne();

            if (applicationRecord == null) {
                throw new InvalidApplicationTokenException(format("application token passed in [%s]", argument0));
            }
        } catch(Exception e) {
            throw new InvalidApplicationTokenException(format("no field in parameters to identify application token"));
        }

        userPreferences.setApplicationToken(applicationRecord.getKey());

        return invocation.proceed();
    }
}
