package com.offer18.sdk.worker;

import android.util.Log;

import com.offer18.sdk.constant.Constant;
import com.offer18.sdk.constant.Env;
import com.offer18.sdk.contract.Configuration;
import com.offer18.sdk.contract.Storage;
import com.offer18.sdk.utils.Offer18Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

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
        boolean isRemoteConfigOutdated = this.configuration.isRemoteConfigOutdated();
        if (!isRemoteConfigOutdated) {
            Log.d("o18", "remote config is up-to-date");
            this.remoteConfigDownloadSignal.countDown();
            return;
        }
        Log.d("o18", "remote config is outdated, updating");
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
                this.onResponse(response, isRemoteConfigOutdated);
            } else {
                this.remoteConfigDownloadSignal.countDown();
            }
        } catch (IOException e) {
            Log.d("o18", e.getMessage());
            this.remoteConfigDownloadSignal.countDown();
        }
    }

    public void onResponse(Response response, boolean isRemoteConfigOutdated) throws IOException {
        JSONObject serviceDocument, services, http, serviceDiscovery, conversion, conversionFields;
        JSONArray conversionFieldsArray = new JSONArray();
        long currentUnixTimeStamp = Calendar.getInstance().getTimeInMillis() / 1000;
        boolean shouldUpdateLocalConfig;
        String digest = null;
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
        try {
            serviceDocument = new JSONObject(json);
            try {
                String remoteDigest = Offer18Util.getMD5(json);
                Log.d("o18", "Remote digest " + remoteDigest);
                String localDigest = this.configuration.get(Constant.DIGEST);
                Log.d("o18", "Local digest " + localDigest);
                shouldUpdateLocalConfig = !remoteDigest.equals(localDigest);
                if (shouldUpdateLocalConfig) {
                    digest = remoteDigest;
                }
            } catch (Exception exception) {
                shouldUpdateLocalConfig = true;
            }
            Log.d("o18", "Updating local config: " + shouldUpdateLocalConfig);
            if (shouldUpdateLocalConfig) {
                if (!Objects.isNull(digest)) {
                    this.configuration.set(Constant.DIGEST, digest);
                }
                http = serviceDocument.getJSONObject("http");
                serviceDiscovery = serviceDocument.getJSONObject("service_discovery");
                storage.set("http_time_out", http.getString("time_out"));
                storage.set("http_ssl_verification", http.getString("ssl_verification"));
                try {
                    long expiresIn = serviceDiscovery.getLong(Constant.EXPIRES_AT);
                    long expiresAt = currentUnixTimeStamp + expiresIn;
                    storage.set(Constant.EXPIRES_AT, Long.toString(expiresAt));
                } catch (Exception e) {
                    Log.d("o18", "failed to update expires in " + e.getMessage());
                }
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
                    storage.set("conversion." + param + "." + "form_name", formName);
                    storage.set("conversion." + param + "." + "required", Boolean.toString(required));
                    storage.set("conversion." + param + "." + "type", dataType);
                    conversionFieldsArray.put(param);
                }
                storage.set(Constant.CONVERSION_PARAMS, conversionFieldsArray.toString());
            }
            if (isRemoteConfigOutdated) {
                try {
                    long expiresIn = serviceDocument.getJSONObject(Constant.SERVICE_DISCOVERY).getLong(Constant.EXPIRES_AT);
                    long expiresAt = currentUnixTimeStamp + expiresIn;
                    storage.set(Constant.EXPIRES_AT, Long.toString(expiresAt));
                } catch (Exception e) {
                    Log.d("o18", "failed to update expires in " + e.getMessage());
                }
            }
        } catch (JSONException e) {
            if (configuration.getEnv() == Env.DEBUG) {
                Log.d("o18", "json parse error, response is not valid json");
            }
        }
        remoteConfigDownloadSignal.countDown();
    }
}
