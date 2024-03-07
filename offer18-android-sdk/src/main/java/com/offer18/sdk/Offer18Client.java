package com.offer18.sdk;

import android.util.Log;

import com.offer18.sdk.Exception.Offer18FormFieldDataTypeException;
import com.offer18.sdk.Exception.Offer18FormFieldRequiredException;
import com.offer18.sdk.Exception.Offer18SSLVerifcationException;
import com.offer18.sdk.constant.Constant;
import com.offer18.sdk.constant.Env;
import com.offer18.sdk.contract.Client;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class Offer18Client implements Client {
    protected Configuration configuration;
    private final OkHttpClient httpClient;

    public Offer18Client(Configuration configuration) {
        this.configuration = configuration;
        String currentDigest = this.configuration.get(Constant.DIGEST);
        long currentUnixTimeStamp = Calendar.getInstance().getTimeInMillis() / 1000;
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        this.httpClient = clientBuilder.build();
        this.httpClient.dispatcher().setMaxRequests(1);
        String url = "https://ganesh-local-dev.o18-test.live/m/files/cron-jobs/service_discovery.php";
        Request request = new Request.Builder().url(url).build();
        this.httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

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
                } catch (JSONException e) {
                    if (configuration.getEnv() == Env.DEBUG) {
                        Log.d("o18", "json parse error, response is not valid json");
                    }
                }
            }
        });
    }

    @Override
    public String trackConversion(Map<String, String> args, Configuration configuration) throws Offer18SSLVerifcationException, Offer18FormFieldRequiredException, Offer18FormFieldDataTypeException {
        HttpUrl url = this.buildEndpoint(args);
        Request request = new Request.Builder().url(url).build();
        Log.println(Log.INFO, "offer18-url", request.url().toString());
        Call call = this.httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("o18", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // log response if logging is enabled
                Log.d("o18", response.toString());
            }
        });
        return null;
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }

    public HttpUrl buildEndpoint(Map<String, String> args) throws Offer18SSLVerifcationException, Offer18FormFieldRequiredException, Offer18FormFieldDataTypeException {
        HttpUrl.Builder url = new HttpUrl.Builder()
                .scheme("https")
                .host("ganesh-local-dev.o18-test.live")
                .addPathSegments("tracking/p.php");
        String doesSSLVerificationRequire = this.configuration.get("http_ssl_verification");
        if (Objects.equals(doesSSLVerificationRequire, "true")) {
            if (!Objects.equals(url.getScheme$okhttp(), "https")) {
                throw new Offer18SSLVerifcationException("HTTPS scheme is required");
            }
        }
        if (!args.containsKey(Constant.POSTBACK_TYPE) || Objects.isNull(args.get(Constant.POSTBACK_TYPE))) {
            args.put(Constant.POSTBACK_TYPE, Constant.POSTBACK_TYPE_PIXEL);
        }
        for (String key : args.keySet()) {
            String formName = this.configuration.get("conversion." + key + ".form_name");
            String required = this.configuration.get("conversion." + key + ".required");
            String dataType = this.configuration.get("conversion." + key + ".data_type");
            if (Objects.equals(required, "true")) {
                if (!args.containsKey(formName)) {
                    throw new Offer18FormFieldRequiredException("Postback type is required");
                }
            }
            if (args.containsKey(formName) && !Objects.isNull(args.get(formName)) && !args.get(formName).isEmpty()) {
                if (dataType.equals("number")) {
                    try {
                        Float.parseFloat(args.get(formName));
                    } catch (NumberFormatException | NullPointerException e) {
                        throw new Offer18FormFieldDataTypeException(formName + " must be a number");
                    }
                }
            }
            url.addQueryParameter(key, args.get(key));
        }
        return url.build();
    }
}
