package com.tmjee.mychat;

import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.jolbox.bonecp.BoneCPDataSource;
import com.tmjee.mychat.service.annotations.DataSourceAnnotation;
import com.tmjee.mychat.service.annotations.JotmAnnotation;
import org.objectweb.jotm.Jotm;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import java.util.Objects;

/**
 * @author tmjee
 */
public class MyChatCleanupContextListener implements ServletContextListener {

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
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (Objects.nonNull(dataSource)) {
            ((BoneCPDataSource) dataSource).close();
        }
        if (Objects.nonNull(jotm)) {
            jotm.stop();
        }
    }
}
