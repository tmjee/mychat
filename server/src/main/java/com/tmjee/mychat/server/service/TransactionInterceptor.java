package com.tmjee.mychat.server.service;

import com.google.inject.Inject;
import com.tmjee.mychat.server.service.annotations.JotmAnnotation;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.objectweb.jotm.Jotm;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * @author tmjee
 */
public class TransactionInterceptor implements MethodInterceptor {

    private Jotm jotm;

    @Inject
    public void setJotm(@JotmAnnotation Jotm jotm) {
        this.jotm = jotm;
    }


    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        UserTransaction userTransaction = jotm.getUserTransaction();

        boolean isNew = isNewTransaction(userTransaction);
        if (isNew) {
            userTransaction.begin();
        }

        try {
            Object ret = methodInvocation.proceed();

            if (isNew) {
                userTransaction.commit();
            }
            return ret;
        }catch(Exception e) {
            if (isNew) {
                userTransaction.rollback();
            }
            throw e;
        }
    }


    private boolean isNewTransaction(UserTransaction userTransaction) throws SystemException {
        return (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION);
    }

}
