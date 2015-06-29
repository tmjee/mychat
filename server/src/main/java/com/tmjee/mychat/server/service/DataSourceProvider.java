package com.tmjee.mychat.server.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;

import javax.sql.DataSource;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tmjee
 */
public class DataSourceProvider implements Provider<DataSource>{

    private static final Logger LOG = Logger.getLogger(DataSourceProvider.class.getName());

    private final String dbUsername;
    private final String dbPassword;
    private final String dbUrl;
    private final String dbDriver;

    @Inject
    public DataSourceProvider(@Named("db.username") String dbUsername,
                              @Named("db.password") String dbPassword,
                              @Named("db.url") String dbUrl,
                              @Named("db.driver") String dbDriver) {
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.dbUrl = dbUrl;
        this.dbDriver = dbDriver;
    }

    @Override
    public DataSource get() {
        try {
            Class.forName(dbDriver);

            BoneCPConfig config = new BoneCPConfig();
            config.setUsername(dbUsername);
            config.setPassword(dbPassword);
            config.setJdbcUrl(dbUrl);

            BoneCPDataSource dataSource = new BoneCPDataSource(config);

            return dataSource;
        } catch (ClassNotFoundException e) {
            LOG.log(Level.SEVERE, e.toString(), e);
        }
        return null;
    }
}
