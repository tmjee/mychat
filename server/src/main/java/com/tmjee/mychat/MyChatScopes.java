package com.tmjee.mychat;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import org.apache.log4j.helpers.ThreadLocalMap;

import java.lang.annotation.Annotation;

/**
 * @author tmjee
 */
public class MyChatScopes {


    public static final Scope ThreadLocalScope = new ThreadLocalScope();


    private static class ThreadLocalScope implements Scope {
        @Override
        public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped) {
            return new Provider<T>(){

                final ThreadLocal<T> threadLocal =  ThreadLocal.withInitial(()->unscoped.get());

                @Override
                public T get() {
                    return threadLocal.get();
                }
            };
        }
    }
}
