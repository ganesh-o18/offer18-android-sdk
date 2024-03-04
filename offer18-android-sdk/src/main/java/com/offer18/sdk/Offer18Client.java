package com.offer18.sdk;

import android.util.Log;

import com.offer18.sdk.constant.Constant;
import com.offer18.sdk.contract.Client;
import com.offer18.sdk.contract.Configuration;
import com.offer18.sdk.contract.Storage;

import org.jetbrains.annotations.NotNull;
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

public class Offer18Client implements Client {
    private final String[] params = {Constant.OFFER_ID, Constant.ACCOUNT_ID, Constant.POSTBACK_TYPE,
            Constant.IS_GLOBAL_PIXEL, Constant.EVENT, Constant.COUPON, Constant.TID,
            Constant.ADV_SUB_1, Constant.ADV_SUB_2, Constant.ADV_SUB_3, Constant.ADV_SUB_4,
            Constant.ADV_SUB_5, Constant.SALE, Constant.PAYOUT, Constant.CURRENCY
    };
    private final OkHttpClient httpClient;

    public Offer18Client(Configuration configuration) {
        long currentUnixTimeStamp = Calendar.getInstance().getTimeInMillis() / 1000;
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
//        clientBuilder.addInterceptor(new ServiceDiscoveryInterceptor());
        this.httpClient = clientBuilder.build();
        if (configuration.getServiceDiscovery().serviceDiscoveryTimeout() - currentUnixTimeStamp <= 0) {
            // Service discovery document is stale, update it.
            String url = "https://ganesh-local-dev.o18-test.live/m/files/cron-jobs/service_discovery.php";
            Request request = new Request.Builder().url(url).build();
            this.httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        Log.d("offer18-sd-http", Integer.toString(response.code()));
                    }
                    Storage storage = configuration.getStorage();
                    if (Objects.isNull(storage)) {
                        Log.d("offer18-sd-storage", "storage not available");
                    }
                    String json = response.body() != null ? response.body().string() : null;
                    if (Objects.isNull(json) || Objects.requireNonNull(json).length() == 0) {
                        Log.d("offer18-sd-response", "response is not valid json");
                    }
                    JSONObject serviceDocument, services, http, serviceDiscovery, conversion = null;
                    try {
                        serviceDocument = new JSONObject(json);
                        String digest = serviceDocument.getString("digest");
                        if (Objects.equals(storage.get("digest"), digest)) {
                            return;
                        }
                        storage.set("digest", digest);
                        http = serviceDocument.getJSONObject("http");
                        serviceDiscovery = serviceDocument.getJSONObject("service_discovery");
                        storage.set("service_document_updated_at", Long.toString(currentUnixTimeStamp));
                        storage.set("time_out", http.getString("time_out"));
                        storage.set("ssl_verification", http.getString("ssl_verification"));
                        storage.set("expires_in", serviceDiscovery.getString("expires_in"));
                        services = serviceDocument.getJSONObject("services");
                    } catch (JSONException e) {
                        Log.d("offer18-sd-response", "json parse error, response is not valid json");
                    }
                }
            });
        }
    }

    @Override
    public String trackConversion(Map<String, String> args, Configuration configuration) {
        HttpUrl url = this.buildEndpoint(args);
        Request request = new Request.Builder().url(url).build();
        Log.println(Log.INFO, "offer18-url", request.url().toString());
        Call call = this.httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.println(Log.INFO, "offer18-error", Objects.requireNonNull(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.println(Log.INFO, "offer18-response", response.toString());
            }
        });
        return null;
    }

    public HttpUrl buildEndpoint(Map<String, String> args) {
        HttpUrl.Builder url = new HttpUrl.Builder()
                .scheme("https")
                .host("ganesh-local-dev.o18-test.live")
                .addPathSegments("tracking/p.php");
        if (!args.containsKey(Constant.POSTBACK_TYPE) || Objects.isNull(args.get(Constant.POSTBACK_TYPE))) {
            args.put(Constant.POSTBACK_TYPE, Constant.POSTBACK_TYPE_PIXEL);
        }
        if (args.containsKey(Constant.IS_GLOBAL_PIXEL)) {
            if (Objects.requireNonNull(args.get(Constant.IS_GLOBAL_PIXEL)).equals("true")) {
                args.put(Constant.IS_GLOBAL_PIXEL, "1");
                args.remove(Constant.OFFER_ID);
            } else {
                args.remove(Constant.IS_GLOBAL_PIXEL);
            }
        }
        for (String param : this.params) {
            if (args.containsKey(param) && Objects.requireNonNull(args.get(param)).length() > 0) {
                url.addQueryParameter(param, args.get(param));
            }
        }

        return url.build();
    }
}
