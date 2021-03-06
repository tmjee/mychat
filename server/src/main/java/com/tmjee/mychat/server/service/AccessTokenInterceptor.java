package com.tmjee.mychat.server.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tmjee.mychat.server.exception.InvalidAccessTokenException;
import com.tmjee.mychat.server.rest.V1;
import com.tmjee.mychat.server.service.annotations.UserPreferencesAnnotation;
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
    public void setUserPreferences(@UserPreferencesAnnotation Provider<UserPreferences> userPreferencesProvider) {
        this.userPreferencesProvider = userPreferencesProvider;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            Object argument0 = invocation.getArguments()[0];
            Field f = argument0.getClass().getField("accessToken");
            f.setAccessible(true);


            //V1.Req rq = V1.Req.class.cast(argument0);
            String accessToken = f.get(argument0).toString();

            LOG.log(Level.INFO, ()->format("accessToken read %s", accessToken));

            LOG.log(Level.INFO, format("Provider %s User preference %s, access token from user preference %s",
                    userPreferencesProvider, userPreferencesProvider.get(), userPreferencesProvider.get().getAccessToken()));
            if (!accessToken.equals(userPreferencesProvider.get().getAccessToken())) {
                throw new InvalidAccessTokenException(format("Invalid access token %s vs %s", userPreferencesProvider.get().getAccessToken(), accessToken));
            }
        }catch(Exception e) {
            throw new InvalidAccessTokenException(format("unable to get access token field"), e);
        }
        return invocation.proceed();
    }
}
