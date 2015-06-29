package com.tmjee.mychat.server;

import com.google.inject.Inject;
import com.jolbox.bonecp.BoneCPDataSource;
import com.tmjee.mychat.server.service.annotations.DataSourceAnnotation;
import com.tmjee.mychat.server.service.annotations.JotmAnnotation;
import org.objectweb.jotm.Jotm;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tmjee
 */
public class MyChatCleanupContextListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(MyChatCleanupContextListener.class.getName());

    private DataSource dataSource;
    private Jotm jotm;

    @Inject
    public void setJotm(@JotmAnnotation Jotm jotm) {
        this.jotm = jotm;
    }

    @Inject
    public void setDataSource(@DataSourceAnnotation DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void contextInitialized(ServletContextEvent sce) {
        LOG.log(Level.FINEST, "test...... FINEST");
        LOG.log(Level.FINER, "test...... FINNER");
        LOG.log(Level.FINE, "test...... FINE");
        LOG.log(Level.CONFIG, "test...... CONFIG");
        LOG.log(Level.INFO, "test...... INFO");
        LOG.log(Level.WARNING, "test...... WARNING");
        LOG.log(Level.SEVERE, "test...... SEVERE ");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (Objects.nonNull(dataSource)) {
            LOG.log(Level.INFO, "Shutting down dataSource");
            ((BoneCPDataSource) dataSource).close();
        }
        if (Objects.nonNull(jotm)) {
            LOG.log(Level.INFO, "Shutting down jotm");
            jotm.stop();
        }
    }
}
