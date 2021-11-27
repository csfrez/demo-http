package com.csfrez.http;

import com.sun.net.httpserver.BasicAuthenticator;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.IOException;

public class OKHttpClientTest {



    @Test
    public void testGet() throws Exception{
        OkHttpClient client = new OkHttpClient();
        String url = "https://www.baidu.com";
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
        }
    }

    @Test
    public void testPost() throws Exception{
        String json = "";
        String url = "https://www.baidu.com";
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
        }
    }

    @Test
    public void testIntercept() throws Exception{
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request request = chain.request();

            long t1 = System.nanoTime();
            System.out.println(String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            System.out.println(String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }).build();
        String url = "https://www.baidu.com";
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
        }
    }

}
