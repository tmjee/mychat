package com.tmjee.mychat.client;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

/**
 * @author tmjee
 */
public class MyChatClientBuilder {
    private String applicationToken;
    private String hostConnectionUrl;

    public Injector injector;

    public MyChatClientBuilder withHostConnectionUrl(String hostConnectionUrl) {
        this.hostConnectionUrl = hostConnectionUrl;
        return this;
    }

    public MyChatClientBuilder withApplicationToken(String applicationToken) {
        this.applicationToken = applicationToken;
        return this;
    }

    public MyChatClient build() {
        final Configuration configuration = new Configuration(
                applicationToken,
                hostConnectionUrl
        );
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Configuration.class).toInstance(configuration);
                bind(MyChatClient.class).in(Singleton.class);
            }
        });
        return injector.getInstance(MyChatClient.class);
    }



    static class Configuration {

        public final String applicationToken;
        public final String hostConnectionUrl;

        Configuration(String applicationToken, String hostConnectionUrl){
            this.applicationToken = applicationToken;
            this.hostConnectionUrl = hostConnectionUrl;
        }
    }
}
