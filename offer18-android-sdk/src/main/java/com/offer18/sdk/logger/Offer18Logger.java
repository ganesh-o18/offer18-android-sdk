package com.offer18.sdk.logger;

import com.offer18.sdk.contract.Logger;
import com.offer18.sdk.util.Offer18Util;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Offer18Logger implements Logger {
    OkHttpClient httpClient;
    String accessToken;

    public Offer18Logger(String accessToken) {
        this.httpClient = new OkHttpClient.Builder().connectTimeout(2000, TimeUnit.MILLISECONDS).build();
        this.accessToken = accessToken;
    }

    /**
     * @param info
     */
    @Override
    public void log(String info) {
        try {
            Request.Builder requestBuilder = new Request.Builder().url(buildEndpoint());
            requestBuilder.setHeaders$okhttp(this.getHeaders());
            requestBuilder.post(this.buildRequestBody(info));
            Request request = requestBuilder.build();
            this.httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                }
            });
        } catch (Exception e) {

        }
    }

    public Headers.Builder getHeaders() {
        return new Headers.Builder().set("Authorization", "Bearer " + this.accessToken);
    }

    protected String buildEndpoint() {
        return "https://in.logs.betterstack.com";
    }

    protected RequestBody buildRequestBody(String body) throws JSONException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("message_string", body);
        Map<String, String> metadata = Offer18Util.getMetadata();
        for(String key : metadata.keySet()) {
            jsonBody.put(key, metadata.get(key));
        }
        return RequestBody.create(jsonBody.toString().getBytes(StandardCharsets.UTF_8));
    }
}
