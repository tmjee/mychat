package com.tmjee.mychat.server.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ext.ContextResolver;


/**
 * @author tmjee
 */
public class MyChatResourceConfig extends ResourceConfig {

    public MyChatResourceConfig() {
        packages("com.tmjee.mychat.rest");
        register(JacksonFeature.class);
        register(MultiPartFeature.class);
    }
}
