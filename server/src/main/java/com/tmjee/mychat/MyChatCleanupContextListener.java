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

/**
 * @author tmjee
 */
public class MyChatCleanupContextListener implements ServletContextListener {

    private final DataSource dataSource;
    private final Jotm jotm;

    @Inject
    public MyChatCleanupContextListener(@JotmAnnotation Jotm jotm,
                                        @DataSourceAnnotation DataSource dataSource)
    {
        this.dataSource = dataSource;
        this.jotm = jotm;
    }



    @Override
    public void contextInitialized(ServletContextEvent sce) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ((BoneCPDataSource)dataSource).close();
        jotm.stop();
    }
}
