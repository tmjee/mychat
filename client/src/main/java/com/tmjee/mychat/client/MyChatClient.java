package com.tmjee.mychat.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.squareup.okhttp.*;
import com.tmjee.mychat.common.domain.GenderEnum;
import com.tmjee.mychat.common.domain.MyChatUserIdentificationTypeEnum;
import com.tmjee.mychat.common.domain.MyChatUserStatusEnum;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;

/**
 * @author tmjee
 */
public class MyChatClient implements AccessTokenAware, ApplicationTokenAware, MySelfAware {

    private static final Logger LOG = Logger.getLogger(MyChatClient.class.getName());

    public static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");

    private final MyChatClientBuilder.Configuration configuration;
    private ObjectMapper objectMapper;
    private OkHttpClient okHttpClient;

    private String accessToken;
    private Integer myChatUserId;

    private MySelf myself;

    @Inject
    public MyChatClient(MyChatClientBuilder.Configuration configuration) {
        this.configuration = configuration;
        objectMapper = new ObjectMapper();
        okHttpClient = new OkHttpClient();
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        okHttpClient.setCookieHandler(cookieManager);
    }


    @RequiresApplicationTokenAnnotation
    public Map<String, Object> logon(String email, String password) throws IOException, URISyntaxException {

       Map<String, Object> mappedResponse = new Command()
                .pathed("/logon")
                .withJsonResource("logon_1.json")
                .bindVariables("email", email)
                .bindVariables("password", password)
                .bindVariables("applicationToken", configuration.applicationToken)
                .build()
                .executeWith(this)
                .mapped();
        if ((Boolean) mappedResponse.get("valid")) {
            accessToken = (String) mappedResponse.get("accessToken");
            myChatUserId = (Integer) mappedResponse.get("myChatUserId");
        }
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


    @RequiresApplicationTokenAnnotation
    public Map<String, Object> activate(String activationToken) throws IOException, URISyntaxException {
        Map<String, Object> mappedResponse = new Command()
                .pathed(format("/activation/%s", activationToken))
                .withJsonResource("activate_1.json")
                .bindVariables("applicationToken", getApplicationToken())
                .build()
                .executeWith(this)
                .mapped();
        return mappedResponse;
    }

    @RequiresApplicationTokenAnnotation
    @RequiresAccessTokenAnnotation
    public Map<String, Object> listContacts(Integer myChatUserId, int limit, int offset) throws IOException, URISyntaxException {
        return new Command()
                .pathed(format("/contacts/%s/list", myChatUserId))
                .withJsonResource("list_contacts_1.json")
                .bindVariables("applicationToken", getApplicationToken())
                .bindVariables("accessToken", getAccessToken())
                .bindVariables("limit", limit)
                .bindVariables("offset", offset)
                .build()
                .executeWith(this)
                .mapped();
    }


    @RequiresApplicationTokenAnnotation
    @RequiresAccessTokenAnnotation
    public Map<String, Object> postAvatar(Integer myChatUserId, String mimeType, byte[] bytes) throws IOException, URISyntaxException {
        return new Command()
                .pathed(format("/avatar/%s/post", myChatUserId))
                .withJsonResource("post_avatar_1.json")
                .bindVariables("applicationToken", getApplicationToken())
                .bindVariables("accessToken", getApplicationToken())
                .bindVariables("avatar", mimeType, bytes)
                .build()
                .executeWith(this)
                .mapped();
    }







    public static String readResourceAsString(String jsonFileResource, Map<String, Object> variables) throws URISyntaxException, IOException {
        String r = new String(Files.readAllBytes(Paths.get(Thread.currentThread().getContextClassLoader().getResource(jsonFileResource).toURI())));
        for(Map.Entry<String, Object> e : variables.entrySet()) {
            r = r.replace("${"+e.getKey()+"}", e.getValue().toString());
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
        final Map<String, Object> vars;

        JsonResourceCommand(String path, String jsonResource) {
            this.path = path;
            this.jsonResource = jsonResource;
            this.vars = new HashMap<>(10);
        }

        JsonResourceCommand bindVariables(String var, String val) {
            vars.put(var, val);
            return this;
        }

        JsonResourceCommand bindVariables(String var, int val) {
            vars.put(var, String.valueOf(val));
            return this;
        }

        JsonResourceCommand bindVariables(String var, String mimeType, byte[] bytes) {
            return bindVariables(var, Part.newPart(mimeType, bytes));
        }

        JsonResourceCommand bindVariables(String var, Part part) {
            vars.put(var, part);
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
        final Map<String, Object> vars;

        ExecuteCommand(String path, String jsonResource, Map<String, Object> vars) {
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

        ResponseCommand executeAsMultipartWith(MyChatClient c) throws IOException {
            MultipartBuilder builder = new MultipartBuilder()
                    .type(MultipartBuilder.FORM);
            for(Map.Entry<String, Object>  entry : vars.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof Part) {
                    Part part = (Part)value;
                    builder.addPart(
                            Headers.of("Content-Disposition", "form-data; name=\""+key+"\""),
                            RequestBody.create(MediaType.parse(part.getMediaType()), part.getBytes())
                    );
                } else {
                    builder.addPart(
                            Headers.of("Content-Disposition", "form-data; name=\""+key+"\""),
                            RequestBody.create(MediaType.parse("text/plain"), value.toString())
                    );
                }
            }
            RequestBody b = builder.build();
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





    public static class Part {
        private final String mediaType;
        private final byte[] b;

        private Part(String mediaType, byte[] b) {
            this.mediaType = mediaType;
            this.b = b;
        }

        public static Part newPart(String mediaType, byte[] b) {
            return new Part(mediaType, b);
        }

        public String getMediaType() { return mediaType; }
        public byte[] getBytes() { return b; }
    }






    // ===== Aware Interfaces implementations =========================================================


    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String getApplicationToken() {
        return configuration.applicationToken;
    }


    @Override
    @RequiresApplicationTokenAnnotation
    @RequiresAccessTokenAnnotation
    public MySelf getMySelf() throws IOException, URISyntaxException {
        if (myself == null) {

            Map<String, Object> mapped = new Command()
                    .pathed(format("/profile/%s", myChatUserId))
                    .withJsonResource("profile_1.json")
                    .bindVariables("applicationToken", getApplicationToken())
                    .bindVariables("accessToken", accessToken)
                    .build()
                    .executeWith(this)
                    .mapped();


            Integer myChatUserId = (Integer) mapped.get("myChatUserId");
            String name = (String) mapped.get("name");
            GenderEnum genderEnum = GenderEnum.valueOf((String) mapped.get("gender"));
            Timestamp dateCreated = new Timestamp((Long)mapped.get("dateCreated"));
            String whatsup = (String) mapped.get("whatsup");
            MyChatUserStatusEnum statusEnum = MyChatUserStatusEnum.valueOf((String) mapped.get("status"));
            String identification = (String) mapped.get("identification");
            MyChatUserIdentificationTypeEnum identificationTypeEnum =
                    MyChatUserIdentificationTypeEnum.valueOf((String)mapped.get("identificationType"));


            myself = new MySelf(myChatUserId, identificationTypeEnum, identification,
                    statusEnum, dateCreated, whatsup, genderEnum, name);
        }
        return myself;
    }


}
