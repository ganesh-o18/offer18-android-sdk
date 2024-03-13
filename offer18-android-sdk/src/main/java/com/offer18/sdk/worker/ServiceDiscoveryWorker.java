package com.offer18.sdk.worker;

import android.util.Log;

import com.offer18.sdk.constant.Constant;
import com.offer18.sdk.constant.Env;
import com.offer18.sdk.contract.Configuration;
import com.offer18.sdk.contract.Storage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Timeout;

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
        Log.d("o18", "calling service discovery");
        String url = Constant.SERVICE_DISCOVERY_ENDPOINT + "?digest=" + this.configuration.get(Constant.DIGEST);
        Log.d("o18", url);
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = this.httpClient.newCall(request).execute();
            Log.d("o18", response.toString());
            Log.d("o18", "" + response.code());
            if (response.code() == 204) {
                Log.d("o18", "no change in remote config");
                this.remoteConfigDownloadSignal.countDown();
                return;
            }
            if (response.isSuccessful()) {
                this.onResponse(response);
            } else {
                this.remoteConfigDownloadSignal.countDown();
            }
        } catch (IOException e) {
            Log.d("o18", e.getMessage());
            this.remoteConfigDownloadSignal.countDown();
        }
    }

    public void onResponse(Response response) throws IOException {
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
        JSONObject serviceDocument, services, http, serviceDiscovery, conversion, conversionFields;
        JSONArray conversionFieldsArray = new JSONArray();
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
            conversionFields = conversion.getJSONObject("fields");
            Iterator<String> params = conversionFields.keys();
            while (params.hasNext()) {
                String param = params.next();
                JSONObject fieldValidation = conversionFields.getJSONObject(param);
                String formName = fieldValidation.getString("form_name");
                boolean required = fieldValidation.getBoolean("required");
                String dataType = fieldValidation.getString("type");
                storage.set("conversion." + formName + "." + "form_name", formName);
                storage.set("conversion." + formName + "." + "required", Boolean.toString(required));
                storage.set("conversion." + formName + "." + "type", dataType);
                conversionFieldsArray.put(param);
            }
            storage.set(Constant.CONVERSION_PARAMS, conversionFieldsArray.toString());
        } catch (JSONException e) {
            if (configuration.getEnv() == Env.DEBUG) {
                Log.d("o18", "json parse error, response is not valid json");
            }
        }
        remoteConfigDownloadSignal.countDown();
    }
}
