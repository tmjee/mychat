package com.tmjee.mychat.server;

import com.tmjee.mychat.server.exception.MyChatException;

import java.util.function.Function;

/**
 * @author tmjee
 */
@FunctionalInterface
public interface MyChatFunction<T,R> extends Function<T,R> {
    @Override
    default R apply(T t) throws MyChatException {
        try {
            return applyWithThrowableException(t);
        }catch(Exception e) {
            throw new MyChatException(e);
        }
    }


    R applyWithThrowableException(T t) throws Exception;
}
