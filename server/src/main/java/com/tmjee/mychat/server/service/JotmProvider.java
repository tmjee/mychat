package com.tmjee.mychat.server.service;

import org.objectweb.jotm.Jotm;

import javax.inject.Provider;
import javax.naming.NamingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tmjee
 */
public class JotmProvider implements Provider<Jotm> {

    private static final Logger LOG = Logger.getLogger(JotmProvider.class.getName());

    @Override
    public Jotm get() {
        try {
            return new Jotm(true, false);
        } catch (NamingException e) {
            LOG.log(Level.SEVERE, e.toString(), e);
        }
        return null;
    }
}
