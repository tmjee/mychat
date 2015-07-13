package com.tmjee.mychat.client;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author tmjee
 */
public class RequiresAccessTokenInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // TODO:
        return invocation.proceed();
    }
}
