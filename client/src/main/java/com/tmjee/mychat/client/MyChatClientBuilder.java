package com.tmjee.mychat.client;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;

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
        final RequiresApplicationTokenInterceptor requiresApplicationTokenInterceptor = new RequiresApplicationTokenInterceptor();
        final RequiresAccessTokenInterceptor requiresAccessTokenInterceptor = new RequiresAccessTokenInterceptor();
        final Configuration configuration = new Configuration(
                applicationToken,
                hostConnectionUrl
        );
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Configuration.class).toInstance(configuration);
                bind(MyChatClient.class).in(Singleton.class);

                /*
                bindInterceptor(
                        Matchers.any(),
                        Matchers.annotatedWith(RequiresApplicationTokenAnnotation.class),
                        requiresApplicationTokenInterceptor);

                bindInterceptor(
                        Matchers.annotatedWith(RequiresApplicationTokenAnnotation.class),
                        Matchers.any(),
                        requiresApplicationTokenInterceptor);

                bindInterceptor(
                        Matchers.any(),
                        Matchers.annotatedWith(RequiresAccessTokenAnnotation.class),
                        requiresAccessTokenInterceptor);

                bindInterceptor(
                        Matchers.annotatedWith(RequiresAccessTokenAnnotation.class),
                        Matchers.any(),
                        requiresAccessTokenInterceptor);
                        */
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
