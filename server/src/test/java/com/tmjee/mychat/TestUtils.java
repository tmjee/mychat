package com.tmjee.mychat;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @author tmjee
 */
public class TestUtils {

    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final OkHttpClient okHttpClient = new OkHttpClient();


    public static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");

    public static final String URL_PREFIX = "http://localhost:8080/v1/";


    public static String readResourceAsString(String jsonFileResource) throws URISyntaxException, IOException {
        return new String(Files.readAllBytes(Paths.get(Thread.currentThread().getContextClassLoader().getResource(jsonFileResource).toURI())));
    }


    public static Map<String, Object> fromJson(String json) throws IOException {
        return objectMapper.readValue(json, Map.class);
    }

    public static Map<String, Object> logon(String jsonFileResource) throws IOException, URISyntaxException {
        String json = readResourceAsString(jsonFileResource);
        RequestBody b = RequestBody.create(JSON_MEDIA_TYPE, json);
        Request req = new Request.Builder()
                .url(URL_PREFIX+"logon")
                .post(b)
                .build();
        Response resp = okHttpClient.newCall(req).execute();
        return objectMapper.readValue(resp.body().string(), Map.class);
    }

}
