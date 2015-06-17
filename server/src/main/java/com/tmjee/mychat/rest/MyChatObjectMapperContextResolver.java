package com.tmjee.mychat.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmjee.mychat.MyChatGuiceServletContextListener;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * @author tmjee
 */
@Provider
public class MyChatObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    private ObjectMapper objectMapper;


    @Override
    public ObjectMapper getContext(Class<?> type) {
        if (null == objectMapper) {
            objectMapper = getObjectMapper();
        }
        return objectMapper;
    }

    protected ObjectMapper getObjectMapper() {
        return MyChatGuiceServletContextListener.getV1Injector().getInstance(ObjectMapper.class);
    }
}
