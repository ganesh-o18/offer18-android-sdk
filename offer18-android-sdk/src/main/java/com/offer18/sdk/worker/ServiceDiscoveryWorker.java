package com.offer18.sdk.worker;

import android.util.Log;

import com.offer18.sdk.constant.Constant;
import com.offer18.sdk.constant.Env;
import com.offer18.sdk.contract.Configuration;
import com.offer18.sdk.contract.Storage;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ServiceDiscoveryWorker implements Runnable {
    CountDownLatch remoteConfigDownloadSignal;
    OkHttpClient httpClient;
    Configuration configuration;
    Map<String, String> args;

    public ServiceDiscoveryWorker(CountDownLatch remoteConfigDownloadSignal, OkHttpClient okHttpClient, Configuration configuration, Map<String, String> args) {
        this.remoteConfigDownloadSignal = remoteConfigDownloadSignal;
        this.httpClient = okHttpClient;
        this.configuration = configuration;
        this.args = args;
    }

    @Override
    public void run() {
        String url = "https://ganesh-local-dev.o18-test.live/m/files/cron-jobs/service_discovery.php";
        Request request = new Request.Builder().url(url).build();
        this.httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                remoteConfigDownloadSignal.countDown();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    if (configuration.getEnv() == Env.DEBUG) {
                        Log.d("o18", Integer.toString(response.code()));
                    }
                }
                Storage storage = configuration.getStorage();
                if (Objects.isNull(storage)) {
                    if (configuration.getEnv() == Env.DEBUG) {
                        Log.d("o18", "storage not available");
                    }
                }
                String json = response.body() != null ? response.body().string() : null;
                if (Objects.isNull(json) || json.isEmpty()) {
                    if (configuration.getEnv() == Env.DEBUG) {
                        Log.d("o18", "response is not valid json");
                    }
                }
                JSONObject serviceDocument, services, http, serviceDiscovery, conversion;
                long currentUnixTimeStamp = Calendar.getInstance().getTimeInMillis() / 1000;
                try {
                    serviceDocument = new JSONObject(json);
                    String digest = serviceDocument.getString(Constant.DIGEST);
                    storage.set(Constant.DIGEST, digest);
                    http = serviceDocument.getJSONObject("http");
                    serviceDiscovery = serviceDocument.getJSONObject("service_discovery");
                    storage.set("service_document_updated_at", Long.toString(currentUnixTimeStamp));
                    storage.set("http_time_out", http.getString("time_out"));
                    storage.set("http_ssl_verification", http.getString("ssl_verification"));
                    storage.set("service_document_expires_in", serviceDiscovery.getString("expires_in"));
                    services = serviceDocument.getJSONObject("services");
                    conversion = services.getJSONObject("conversion");
                    JSONArray fields = conversion.getJSONArray("fields");
                    JSONObject fieldValidations = conversion.getJSONObject("fields_validation");
                    for (int i = 0; i < fields.length(); i++) {
                        String field = fields.getString(i);
                        JSONObject fieldValidation = fieldValidations.getJSONObject(field);
                        String formName = fieldValidation.getString("form_name");
                        boolean required = fieldValidation.getBoolean("required");
                        String dataType = fieldValidation.getString("type");
                        storage.set("conversion." + formName + "." + "form_name", formName);
                        storage.set("conversion." + formName + "." + "required", Boolean.toString(required));
                        storage.set("conversion." + formName + "." + "type", dataType);
                    }
                    remoteConfigDownloadSignal.countDown();
                } catch (JSONException e) {
                    if (configuration.getEnv() == Env.DEBUG) {
                        Log.d("o18", "json parse error, response is not valid json");
                    }
                }
            }
        });
    }
}
