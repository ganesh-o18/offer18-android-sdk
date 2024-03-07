package com.offer18.sdk.worker;

import android.util.Log;

import com.offer18.sdk.Exception.Offer18FormFieldDataTypeException;
import com.offer18.sdk.Exception.Offer18FormFieldRequiredException;
import com.offer18.sdk.Exception.Offer18SSLVerifcationException;
import com.offer18.sdk.constant.Constant;
import com.offer18.sdk.contract.Configuration;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TrackConversionWorker implements Runnable {
    CountDownLatch remoteConfigDownloadSignal;
    OkHttpClient httpClient;
    Configuration configuration;
    Map<String, String> args;

    public TrackConversionWorker(CountDownLatch remoteConfigDownloadSignal, OkHttpClient okHttpClient, Configuration configuration, Map<String, String> args) {
        this.remoteConfigDownloadSignal = remoteConfigDownloadSignal;
        this.httpClient = okHttpClient;
        this.configuration = configuration;
        this.args = args;
    }

    @Override
    public void run() {
        try {
            this.remoteConfigDownloadSignal.await();
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
        } catch (InterruptedException | Offer18SSLVerifcationException |
                 Offer18FormFieldRequiredException | Offer18FormFieldDataTypeException e) {
            throw new RuntimeException(e);
        }
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