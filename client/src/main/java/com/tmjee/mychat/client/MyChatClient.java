package com.tmjee.mychat.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.squareup.okhttp.*;
import com.tmjee.mychat.common.domain.GenderEnum;
import com.tmjee.mychat.common.domain.MyChatUserIdentificationTypeEnum;
import com.tmjee.mychat.common.domain.MyChatUserStatusEnum;
import okio.Buffer;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
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
        cleanup();

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
    public Map<String, Object> postAvatar(Integer myChatUserId, String fileName, String mimeType, byte[] bytes) throws IOException, URISyntaxException {
        return new Command()
                .pathed(format("/avatar/%s/post", myChatUserId))
                .withMultipart()
                .bindVariables("applicationToken", getApplicationToken())
                .bindVariables("accessToken", getAccessToken())
                .bindVariables("avatar", fileName, mimeType, bytes)
                .build()
                .executeWith(this)
                .mapped();
    }



    @RequiresApplicationTokenAnnotation
    public Map<String, Object> getAvatar(Integer myChatUserId) throws IOException, URISyntaxException {
        return new Command()
                .pathed(format("/avatar/%s/get", myChatUserId))
                .withJsonResource("get_avatar_1.json")
                .bindVariables("applicationToken", getApplicationToken())
                .build()
                .executeWith(this)
                .mapped();
    }

    @RequiresApplicationTokenAnnotation
    @RequiresAccessTokenAnnotation
    public Map<String, Object> addContact(Integer myChatUserId, Integer contactMyChatUserId) throws IOException, URISyntaxException {
        return new Command()
                .pathed(format("/contacts/%s/add", myChatUserId))
                .withJsonResource("add_contacts_1.json")
                .bindVariables("applicationToken", getApplicationToken())
                .bindVariables("accessToken", getAccessToken())
                .bindVariables("contactMyChatUserId", contactMyChatUserId)
                .build()
                .executeWith(this)
                .mapped();
    }


    @RequiresApplicationTokenAnnotation
    @RequiresAccessTokenAnnotation
    public Map<String, Object> acceptContact(Integer myChatUserId, Integer contactMyChatUserId) throws IOException, URISyntaxException {
        return new Command()
                .pathed(format("/contacts/%s/accept", myChatUserId))
                .withJsonResource("accept_contacts_1.json")
                .bindVariables("applicationToken", getApplicationToken())
                .bindVariables("accessToken", getAccessToken())
                .bindVariables("contactMyChatUserId", contactMyChatUserId)
                .build()
                .executeWith(this)
                .mapped();
    }

    @RequiresApplicationTokenAnnotation
    @RequiresAccessTokenAnnotation
    public Map<String, Object> whatsUp(Integer myChatUserId, String whatsup) throws IOException, URISyntaxException {
        return new Command()
                .pathed(format("/whatsup/%s/update", myChatUserId))
                .withJsonResource("whatsup_1.json")
                .bindVariables("applicationToken", getApplicationToken())
                .bindVariables("accessToken", getAccessToken())
                .bindVariables("whatsUp", whatsup)
                .build()
                .executeWith(this)
                .mapped();
    }


    @RequiresApplicationTokenAnnotation
    @RequiresAccessTokenAnnotation
    public Map<String, Object> updateLocation(Integer myChatUserId, String name, Float latitude, Float longitude) throws IOException, URISyntaxException {
        return new Command()
                .pathed(format("/location/%s/update", myChatUserId))
                .withJsonResource("location_1.json")
                .bindVariables("applicationToken", getApplicationToken())
                .bindVariables("accessToken", getAccessToken())
                .bindVariables("name", name)
                .bindVariables("latitude", latitude)
                .bindVariables("longitude", longitude)
                .build()
                .executeWith(this)
                .mapped();
    }


    @RequiresApplicationTokenAnnotation
    @RequiresAccessTokenAnnotation
    public Map<String, Object> createMyChat(Integer myChatUserId, List<Integer> membersChatUserId, String chatName) throws IOException, URISyntaxException {
        return new Command()
                .pathed(format("/mychats/%s/create", myChatUserId))
                .withJsonResource("create_mychat_1.json")
                .bindVariables("applicationToken", getApplicationToken())
                .bindVariables("accessToken", getAccessToken())
                .bindVariables("chatName", chatName)
                .bindVariables("membersMyChatUserIds", membersChatUserId)
                .build()
                .executeWith(this)
                .mapped();
    }

    @RequiresApplicationTokenAnnotation
    @RequiresAccessTokenAnnotation
    public Map<String, Object> listMyChats(Integer myChatUserId, Integer offset, Integer limit) throws IOException, URISyntaxException {
        return new Command()
                .pathed(format("/mychats/%s/list", myChatUserId))
                .withJsonResource("list_mychats_1.json")
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
    public Map<String, Object> joinChat(Integer chatId, Integer myChatUserId, List<Integer> membersMyChatUserIds) throws IOException, URISyntaxException {
        return new Command()
                .pathed(format("/chats/%s/join", chatId))
                .withJsonResource("join_chat_1.json")
                .bindVariables("applicationToken", getApplicationToken())
                .bindVariables("accessToken", getAccessToken())
                .bindVariables("membersMyChatUserIds", membersMyChatUserIds)
                .bindVariables("myChatUserId", myChatUserId)
                .build()
                .executeWith(this)
                .mapped();
    }


    @RequiresApplicationTokenAnnotation
    @RequiresAccessTokenAnnotation
    public Map<String, Object> sendChat(Integer chatId, Integer myChatUserId, String chat) throws IOException, URISyntaxException {
        return new Command()
                .pathed(format("/chats/%s/send", chatId))
                .withJsonResource("send_chat_1.json")
                .bindVariables("applicationToken", getApplicationToken())
                .bindVariables("accessToken", getAccessToken())
                .bindVariables("message", chat)
                .bindVariables("myChatUserId", myChatUserId)
                .build()
                .executeWith(this)
                .mapped();
    }

    @RequiresApplicationTokenAnnotation
    @RequiresAccessTokenAnnotation
    public Map<String, Object> chatDetails(Integer chatId, Integer myChatUserId, Integer limit, Integer offset) throws IOException, URISyntaxException {
        return new Command()
                .pathed(format("/chats/%s/details", chatId))
                .withJsonResource("chat_details_1.json")
                .bindVariables("applicationToken", getApplicationToken())
                .bindVariables("accessToken", getAccessToken())
                .bindVariables("myChatUserId", myChatUserId)
                .bindVariables("limit", limit)
                .bindVariables("offset", offset)
                .build()
                .executeWith(this)
                .mapped();

    }


    @RequiresApplicationTokenAnnotation
    @RequiresAccessTokenAnnotation
    public Map<String, Object> leaveChat(Integer chatId, Integer myChatUserId) throws IOException, URISyntaxException {
        return new Command()
                .pathed(format("/chats/%s/leave", chatId))
                .withJsonResource("leave_chat_1.json")
                .bindVariables("applicationToken", getApplicationToken())
                .bindVariables("accessToken", getAccessToken())
                .bindVariables("myChatUserId", myChatUserId)
                .build()
                .executeWith(this)
                .mapped();
    }


    @RequiresApplicationTokenAnnotation
    @RequiresAccessTokenAnnotation
    public Map<String, Object> postMoment(Integer myChatUserId, String message, String fileName,
                                          String mimeType, byte[] b) throws IOException {
        MultipartFormCommand c = new Command()
                .pathed(format("/moments/%s/post", myChatUserId))
                .withMultipart()
                .bindVariables("applicationToken", getApplicationToken())
                .bindVariables("accessToken", getAccessToken())
                .bindVariables("message", message);

        if (fileName != null && mimeType != null && b != null) {
            c = c.bindVariables("media", fileName, mimeType, b);
        }

        return c.build()
                .executeWith(this)
                .mapped();
    }

    @RequiresApplicationTokenAnnotation
    @RequiresAccessTokenAnnotation
    public Map<String, Object> listMoments(Integer myChatUserId, Integer offset, Integer limit)
                                            throws IOException, URISyntaxException {
        return new Command()
                .pathed(format("/moments/%s/list", myChatUserId))
                .withJsonResource("list_moments_1.json")
                .bindVariables("applicationToken", getApplicationToken())
                .bindVariables("accessToken", getAccessToken())
                .bindVariables("limit", limit)
                .bindVariables("offset", offset)
                .build()
                .executeWith(this)
                .mapped();
    }


    private void cleanup() {
        myChatUserId = null;
        accessToken = null;
        myself = null;
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
        MultipartFormCommand withMultipart() {
            return new MultipartFormCommand(path);
        }
    }


    /**
     * @author tmjee
     * @param <T>
     * @param <U>
     */
    static abstract class BindingCommand<T, U> {

        final Map<String, Object> vars;

        BindingCommand() {
            this.vars = new HashMap<>(10);
        }

        T bindVariables(String var, String val) {
            vars.put(var, val);
            return (T) this;
        }

        T bindVariables(String var, int val) {
            vars.put(var, String.valueOf(val));
            return (T) this;
        }

        T bindVariables(String var, float val) {
            vars.put(var, String.valueOf(val));
            return (T) this;
        }

        T bindVariables(String var, List<?> vals) {
            StringBuilder b = new StringBuilder();
            b.append("[");
            int length = vals.size()-1;
            for (int a=0; a< vals.size(); a++) {
                b.append(vals.get(a).toString());
                if (a<length) {
                    b.append(",");
                }
            }
            b.append("]");
            vars.put(var, b.toString());
            return (T) this;
        }

        abstract U build();
    }

    /**
     * @author tmjee
     */
    static class JsonResourceCommand extends BindingCommand<JsonResourceCommand, ExecuteCommand> {
        final String path;
        final String jsonResource;

        JsonResourceCommand(String path, String jsonResource) {
            this.path = path;
            this.jsonResource = jsonResource;
        }

        @Override
        ExecuteCommand build() {
            return new ExecuteCommand(path, jsonResource, vars);
        }
    }


    /**
     * @author tmjee
     */
    static class MultipartFormCommand extends BindingCommand<MultipartFormCommand, MultipartExecuteCommand> {

        final String path;

        MultipartFormCommand(String path) {
            this.path = path;
        }

        MultipartFormCommand bindVariables(String var, String fileName, String mimeType, byte[] bytes) {
            return bindVariables(var, Part.newPart(fileName, mimeType, bytes));
        }

        MultipartFormCommand bindVariables(String var, Part part) {
            vars.put(var, part);
            return this;
        }

        @Override
        MultipartExecuteCommand build() {
            return new MultipartExecuteCommand(path, vars);
        }
    }


    /**
     * @author tmjee
     */
    static class MultipartExecuteCommand {

        final String path;
        final Map<String, Object> vars;

        MultipartExecuteCommand(String path, Map<String, Object> vars) {
            this.path = path;
            this.vars = vars;
        }

        ResponseCommand executeWith(MyChatClient c) throws IOException {
            MultipartBuilder builder = new MultipartBuilder()
                    .type(MultipartBuilder.FORM);
            for(Map.Entry<String, Object>  entry : vars.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof Part) {
                    Part part = (Part)value;
                    builder.addPart(
                            Headers.of("Content-Disposition", format("form-data; name=\"%s\"; filename=\"%s\"", key, part.getFileName())),
                            RequestBody.create(MediaType.parse(part.getMediaType()), part.getBytes())
                    );
                } else {
                    builder.addPart(
                            Headers.of("Content-Disposition", format("form-data; name=\"%s\"", key)),
                            RequestBody.create(null, value.toString())
                    );
                }
            }
            RequestBody b = builder.build();
            Request req = new Request.Builder()
                    .url(c.configuration.hostConnectionUrl+path)
                    .post(b)
                    .build();
            if (LOG.isLoggable(Level.INFO)) {
                Buffer buffer = new Buffer();
                b.writeTo(buffer);
                LOG.log(Level.INFO, format("request for %s - %n%s", path, buffer.readUtf8()));
            }
            Response resp = c.okHttpClient.newCall(req).execute();
            return new ResponseCommand(c, path, resp);
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
        private final String fileName;
        private final String mediaType;
        private final byte[] b;

        private Part(String fileName, String mediaType, byte[] b) {
            this.fileName = fileName;
            this.mediaType = mediaType;
            this.b = b;
        }

        public static Part newPart(String fileName, String mediaType, byte[] b) {
            return new Part(fileName, mediaType, b);
        }

        public String getMediaType() { return mediaType; }
        public byte[] getBytes() { return b; }
        public String getFileName() { return fileName; }
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
