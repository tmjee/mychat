package com.tmjee.mychat.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tmjee.mychat.exception.InvalidAccessTokenException;
import com.tmjee.mychat.service.annotations.UserPreferencesAnnotation;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;

/**
 * @author tmjee
 */
public class AccessTokenInterceptor implements MethodInterceptor {

    private static final Logger LOG = Logger.getLogger(AccessTokenInterceptor.class.getName());

    private volatile Provider<UserPreferences> userPreferencesProvider;

    @Inject
    public void setUserPreferences(Provider<UserPreferences> userPreferencesProvider) {
        this.userPreferencesProvider = userPreferencesProvider;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            Field f = invocation.getMethod().getParameters()[0].getType().getField("accessToken");
            Object o = invocation.getArguments()[0];
            String accessToken = f.get(o).toString();

            LOG.log(Level.FINEST, ()->format("accessToken read %s", accessToken));



            if (!accessToken.equals(userPreferencesProvider.get().getAccessToken())) {
                throw new InvalidAccessTokenException(format("Invalid access token %s vs %s", userPreferencesProvider.get().getAccessToken(), accessToken));
            }
        }catch(Exception e) {
            throw new InvalidAccessTokenException(format("unable to get access token field"));
        }
        return invocation.proceed();
    }
}
