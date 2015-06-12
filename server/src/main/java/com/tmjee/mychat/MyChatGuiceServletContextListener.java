package com.tmjee.mychat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.tmjee.mychat.service.LogonServiceAnnotation;
import com.tmjee.mychat.service.LogonServices;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author tmjee
 */
public class MyChatGuiceServletContextListener implements ServletContextListener {

    private static Injector injectorV1;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        injectorV1 = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(ObjectMapper.class);
                bind(LogonServices.class)
                        .annotatedWith(LogonServiceAnnotation.class)
                        .to(LogonServices.class);

            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        injectorV1 = null;
    }

    public static Injector getV1Injector() {
        return injectorV1;
    }
}
