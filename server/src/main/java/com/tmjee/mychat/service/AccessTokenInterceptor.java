package com.tmjee.mychat.service;

import com.google.inject.Inject;
import com.tmjee.mychat.exception.InvalidAccessTokenException;
import com.tmjee.mychat.service.annotations.UserPreferencesAnnotation;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Field;

import static java.lang.String.format;

/**
 * @author tmjee
 */
public class AccessTokenInterceptor implements MethodInterceptor {


    private volatile UserPreferences userPreferences;

    @Inject
    public void setUserPreferences(@UserPreferencesAnnotation UserPreferences userPreferences) {
        this.userPreferences = userPreferences;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            Field f = invocation.getMethod().getParameters()[0].getClass().getField("accessToken");
            Object o = invocation.getArguments()[0];
            String accessToken = f.get(o).toString();

            if (!accessToken.equals(userPreferences.getAccessToken())) {
                throw new InvalidAccessTokenException(format("Invalid access token %s vs %s", userPreferences.getAccessToken(), accessToken));
            }
        }catch(Exception e) {
            throw new InvalidAccessTokenException(format("unable to get access token field"));
        }
        return invocation.proceed();
    }
}
