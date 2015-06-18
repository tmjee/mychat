package com.tmjee.mychat.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tmjee.mychat.service.annotations.JotmAnnotation;
import org.aopalliance.intercept.Interceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.objectweb.jotm.Jotm;

import javax.transaction.Status;
import javax.transaction.UserTransaction;

/**
 * @author tmjee
 */
public class TransactionInterceptor implements MethodInterceptor {

    private final Jotm jotm;

    public TransactionInterceptor(@JotmAnnotation Jotm jotm) {
        this.jotm = jotm;
    }


    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        UserTransaction userTransaction = jotm.getUserTransaction();
        userTransaction.begin();

        if (isGoodToCommit(jotm.getUserTransaction().getStatus()) {

        }
        Object ret = methodInvocation.proceed();

        return null;
    }


    private boolean isGoodToCommit(UserTransaction userTransaction) {
        return true;
    }
}
