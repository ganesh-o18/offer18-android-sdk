package com.offer18.sdk;

import android.util.Log;

import com.offer18.sdk.constant.Constant;
import com.offer18.sdk.contract.Client;
import com.offer18.sdk.contract.Configuration;

import java.io.IOException;
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
            Constant.EVENT, Constant.COUPON, Constant.TID,
            Constant.ADV_SUB_1, Constant.ADV_SUB_2, Constant.ADV_SUB_3, Constant.ADV_SUB_4,
            Constant.ADV_SUB_5, Constant.SALE, Constant.PAYOUT, Constant.CURRENCY
    };
    private final OkHttpClient httpClient;

    public Offer18Client() {
        this.httpClient = new OkHttpClient();
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
        if (args.containsKey(Constant.IS_GLOBAL_PIXEL) && Objects.requireNonNull(args.get(Constant.IS_GLOBAL_PIXEL)).length() > 0) {
            args.put(Constant.IS_GLOBAL_PIXEL, "1");
            args.remove(Constant.OFFER_ID);
        }
        for (String param : this.params) {
            if (args.containsKey(param) && Objects.requireNonNull(args.get(param)).length() > 0) {
                url.addQueryParameter(param, args.get(param));
            }
        }

        return url.build();
    }
}
