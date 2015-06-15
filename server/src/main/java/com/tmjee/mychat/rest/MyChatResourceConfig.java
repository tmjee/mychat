package com.tmjee.mychat.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ext.ContextResolver;


/**
 * @author tmjee
 */
public class MyChatResourceConfig extends ResourceConfig {

    public MyChatResourceConfig() {
        packages("com.tmjee.mychat.rest");
        register(JacksonFeature.class);
    }


    public static class MyChatObjectMapperProvider implements ContextResolver<ObjectMapper> {

        ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public ObjectMapper getContext(Class<?> type) {
           return objectMapper;
        }
    }
}
