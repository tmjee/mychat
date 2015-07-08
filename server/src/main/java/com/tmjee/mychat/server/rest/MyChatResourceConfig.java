package com.tmjee.mychat.server.rest;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;


/**
 * @author tmjee
 */
public class MyChatResourceConfig extends ResourceConfig {

    public MyChatResourceConfig() {
        packages("com.tmjee.mychat.server.rest");
        register(JacksonFeature.class);
        register(MultiPartFeature.class);
    }
}
