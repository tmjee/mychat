package com.tmjee.mychat.client;

/**
 * @author tmjee
 */
public class Static {
    public final static MyChatClient MYCHAT_CLIENT =
            new MyChatClientBuilder()
                .withApplicationToken("75b92637-2750-4eb9-8da2-7df486fbfb23")
                .withHostConnectionUrl("http://localhost:8080/v1")
                .build();
}
