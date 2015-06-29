package com.tmjee.mychat.server.rest;

import com.squareup.okhttp.*;
import com.tmjee.mychat.server.TestUtils;

import java.util.Map;

/**
 * @author tmjee
 */
public class LogonTest {

    public static final MediaType JSON = MediaType.parse("application/json");

    public static void main(String[] args) throws Exception {

        String json = TestUtils.readResourceAsString("logon_1.json");

        OkHttpClient c = new OkHttpClient();
        RequestBody b = RequestBody.create(JSON, json);
        Request req = new Request.Builder()
                .url("http://localhost:8080/v1/logon")
                .post(b)
                .build();
        Response res = c.newCall(req).execute();
        String body = res.body().string();

        Map<String, Object> data = TestUtils.fromJson(body);

        System.out.println(data);
    }
}
