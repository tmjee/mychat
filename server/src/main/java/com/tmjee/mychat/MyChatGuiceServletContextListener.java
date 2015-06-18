package com.tmjee.mychat;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import java.util.logging.Logger;

/**
 * @author tmjee
 */
public class MyChatGuiceServletContextListener extends GuiceServletContextListener {

    private static volatile Injector injectorV1;

    private static final Logger LOG = Logger.getLogger(MyChatGuiceServletContextListener.class.getName());


    @Override
    protected Injector getInjector() {
        injectorV1 = Guice.createInjector(new MyChatServletModule());
        return injectorV1;
    }

    public static Injector getV1Injector() {
        return injectorV1;
    }
}
