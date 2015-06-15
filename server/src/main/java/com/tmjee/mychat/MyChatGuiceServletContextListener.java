package com.tmjee.mychat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.tmjee.mychat.rest.MyChatResourceConfig;
import com.tmjee.mychat.service.LogonServiceAnnotation;
import com.tmjee.mychat.service.LogonServices;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tmjee
 */
public class MyChatGuiceServletContextListener extends GuiceServletContextListener {

    private static volatile Injector injectorV1;

    @Override
    protected Injector getInjector() {
        injectorV1 = Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {

                bind(ObjectMapper.class);
                bind(LogonServices.class)
                        .annotatedWith(LogonServiceAnnotation.class)
                        .to(LogonServices.class);

            }
        });
        return injectorV1;
    }

    public static Injector getV1Injector() {
        return injectorV1;
    }
}
