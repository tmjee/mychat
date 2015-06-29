package com.tmjee.mychat.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tmjee
 */
public class MyChatClient {

    public static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");

    private final String hostConnectionUrl;
    private final String applicationToken;
    private ObjectMapper objectMapper;
    private OkHttpClient okHttpClient;

    public MyChatClient(String hostConnectionUrl, String applicationToken) {
        this.hostConnectionUrl = hostConnectionUrl;
        this.applicationToken = applicationToken;
        objectMapper = new ObjectMapper();
        okHttpClient = new OkHttpClient();
    }


    public MyChatClient logon(String username, String password) throws IOException, URISyntaxException {
        Map<String, String> variables = new HashMap<>();
        variables.put("username", username);
        variables.put("password", password);
        variables.put("applicationToken", applicationToken);

        RequestBody b = RequestBody.create(JSON_MEDIA_TYPE, readResourceAsString("logon_1.json", variables));
        Request req =  new Request.Builder()
                .url(hostConnectionUrl+"/logon")
                .post(b)
                .build();
        Response resp = okHttpClient.newCall(req).execute();



        return this;
    }




    public static String readResourceAsString(String jsonFileResource, Map<String, String> variables) throws URISyntaxException, IOException {
        String r = new String(Files.readAllBytes(Paths.get(Thread.currentThread().getContextClassLoader().getResource(jsonFileResource).toURI())));
        for(Map.Entry<String, String> e : variables.entrySet()) {
            r = r.replace("${"+e.getKey()+"}", e.getValue());
        }
        return r;
    }
}
