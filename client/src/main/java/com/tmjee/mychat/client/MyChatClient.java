package com.tmjee.mychat.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.squareup.okhttp.*;
import com.tmjee.mychat.common.domain.GenderEnum;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;

/**
 * @author tmjee
 */
public class MyChatClient implements AccessTokenAware, ApplicationTokenAware {

    private static final Logger LOG = Logger.getLogger(MyChatClient.class.getName());

    public static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");

    private final MyChatClientBuilder.Configuration configuration;
    private ObjectMapper objectMapper;
    private OkHttpClient okHttpClient;

    private String accessToken;

    @Inject
    public MyChatClient(MyChatClientBuilder.Configuration configuration) {
        this.configuration = configuration;
        objectMapper = new ObjectMapper();
        okHttpClient = new OkHttpClient();
    }


    @RequiresApplicationTokenAnnotation
    public Map<String, Object> logon(String email, String password) throws IOException, URISyntaxException {

       Map<String, Object> mappedResponse = new Command()
                .pathed("/logon")
                .withJsonResource("logon_1.json")
                .bindVariables("email", email)
                .bindVariables("passwprd", password)
                .bindVariables("applicationToken", configuration.applicationToken)
                .build()
                .executeWith(this)
                .mapped();
        return mappedResponse;
    }

    @RequiresApplicationTokenAnnotation
    public Map<String, Object> register(String email, String password, String fullname, GenderEnum genderEnum) throws IOException, URISyntaxException {

        Map<String, Object> mappedResponse = new Command()
                .pathed("/register")
                .withJsonResource("register_1.json")
                .bindVariables("applicationToken", getApplicationToken())
                .bindVariables("email", email)
                .bindVariables("password", password)
                .bindVariables("fullname", fullname)
                .bindVariables("gender", genderEnum.name())
                .build()
                .executeWith(this)
                .mapped();
        return mappedResponse;
    }






    public static String readResourceAsString(String jsonFileResource, Map<String, String> variables) throws URISyntaxException, IOException {
        String r = new String(Files.readAllBytes(Paths.get(Thread.currentThread().getContextClassLoader().getResource(jsonFileResource).toURI())));
        for(Map.Entry<String, String> e : variables.entrySet()) {
            r = r.replace("${"+e.getKey()+"}", e.getValue());
        }
        return r;
    }


    /**
     * @author tmjee
     */
    static class Command {
        String path;

        Command pathed(String path) {
            this.path = path;
            return this;
        }
        JsonResourceCommand withJsonResource(String jsonResource) {
            return new JsonResourceCommand(path, jsonResource);
        }
    }

    /**
     * @author tmjee
     */
    static class JsonResourceCommand {
        final String path;
        final String jsonResource;
        final Map<String, String> vars;

        JsonResourceCommand(String path, String jsonResource) {
            this.path = path;
            this.jsonResource = jsonResource;
            this.vars = new HashMap<>(10);
        }

        JsonResourceCommand bindVariables(String var, String val) {
            vars.put(var, val);
            return this;
        }

        ExecuteCommand build() {
            return new ExecuteCommand(path, jsonResource, vars);
        }
    }


    /**
     * @author tmjee
     */
    static class ExecuteCommand {
        final String path;
        final String jsonResource;
        final Map<String, String> vars;

        ExecuteCommand(String path, String jsonResource, Map<String, String> vars) {
            this.path = path;
            this.jsonResource = jsonResource;
            this.vars = vars;
        }

        ResponseCommand executeWith(MyChatClient c) throws IOException, URISyntaxException {
            String json = MyChatClient.readResourceAsString(jsonResource, vars);
            LOG.log(Level.INFO, format("request for %s - %n%s", path, json));
            RequestBody b = RequestBody.create(c.JSON_MEDIA_TYPE, json);
            Request req = new Request.Builder()
                    .url(c.configuration.hostConnectionUrl+path)
                    .post(b)
                    .build();
            Response resp = c.okHttpClient.newCall(req).execute();
            return new ResponseCommand(c, path, resp);
        }
    }


    /**
     * @author tmjee
     */
    static class ResponseCommand {
        final Response response;
        final MyChatClient c;
        final String path;

        String body;

        ResponseCommand(MyChatClient c, String path, Response response) {
            this.c = c;
            this.path = path;
            this.response = response;
        }

        Map<String, Object> mapped() throws IOException {
            if (body == null) {
                body = response.body().string();
                LOG.log(Level.INFO, format("response for %s - %n%s", path, body));
            }
            return c.objectMapper.readValue(body, Map.class);
        }
    }
















    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String getApplicationToken() {
        return configuration.applicationToken;
    }
}
