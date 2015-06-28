package com.tmjee.mychat.rest;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

/**
 * @author tmjee
 */
public class LogonTest {

    public static void main(String[] args) throws Exception {
        OkHttpClient c = new OkHttpClient();
        RequestBody b = RequestBody.create(MediaType.parse("application/json"), "");
        Request req = new Request.Builder()
                .url("http://localhost:8080/v1/logon")
                .post(RequestBody.create(JSON))
    }
}
