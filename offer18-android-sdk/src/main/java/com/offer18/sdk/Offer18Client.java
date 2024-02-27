package com.offer18.sdk;

import android.util.Log;

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

    private final OkHttpClient httpClient;

    public Offer18Client() {
        this.httpClient = new OkHttpClient();
    }

    @Override
    public String trackConversion(Map<String, String> args, Configuration configuration) {
//        String url = "https://ganesh-local-dev.o18-test.live/tracking/p.php?m=18962&o=21015810&tid=D-20522077-1708511957--IHKQA3356";
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
        if (args.containsKey("accountId") && Objects.requireNonNull(args.get("accountId")).length() > 0) {
            url.addQueryParameter("m", args.get("accountId"));
        }
        if (args.containsKey("tid") && Objects.requireNonNull(args.get("tid")).length() > 0) {
            url.addQueryParameter("tid", args.get("tid"));
        }
        if (args.containsKey("advSub1") && Objects.requireNonNull(args.get("advSub1")).length() > 0) {
            url.addQueryParameter("adv_sub1", args.get("advSub1"));
        }
        if (args.containsKey("advSub2") && Objects.requireNonNull(args.get("advSub2")).length() > 0) {
            url.addQueryParameter("adv_sub2", args.get("advSub2"));
        }
        if (args.containsKey("advSub3") && Objects.requireNonNull(args.get("advSub3")).length() > 0) {
            url.addQueryParameter("adv_sub3", args.get("advSub3"));
        }
        if (args.containsKey("advSub4") && Objects.requireNonNull(args.get("advSub4")).length() > 0) {
            url.addQueryParameter("adv_sub4", args.get("advSub4"));
        }
        if (args.containsKey("advSub5") && Objects.requireNonNull(args.get("advSub5")).length() > 0) {
            url.addQueryParameter("adv_sub5", args.get("advSub5"));
        }
        if (args.containsKey("coupon") && Objects.requireNonNull(args.get("coupon")).length() > 0) {
            url.addQueryParameter("coupon", args.get("coupon"));
        }
        if (args.containsKey("event") && Objects.requireNonNull(args.get("event")).length() > 0) {
            url.addQueryParameter("event", args.get("event"));
        }
        if (args.containsKey("sale") && Objects.requireNonNull(args.get("sale")).length() > 0) {
            url.addQueryParameter("sale", args.get("sale"));
        }
        if (args.containsKey("payout") && Objects.requireNonNull(args.get("payout")).length() > 0) {
            url.addQueryParameter("payout", args.get("payout"));
        }
        if (args.containsKey("allowMultiConversion") && Objects.requireNonNull(args.get("allowMultiConversion")).length() > 0) {
            url.addQueryParameter("allow_multi_conversion", args.get("allowMultiConversion"));
        }
        if (args.containsKey("postbackType") && Objects.requireNonNull(args.get("postbackType")).length() > 0) {
            String postbackType = args.get("postbackType");
            if (Objects.equals(postbackType, "pixel")) {
                url.addQueryParameter("t", "i");
                if (args.containsKey("isGlobalPixel") && Objects.requireNonNull(args.get("isGlobalPixel")).length() > 0 && Objects.equals(args.get("isGlobalPixel"), "true")) {
                    url.addQueryParameter("gb", "1");
                } else {
                    if (args.containsKey("offerId") && Objects.requireNonNull(args.get("offerId")).length() > 0) {
                        url.addQueryParameter("o", args.get("offerId"));
                    }
                }
            } else if (Objects.equals(postbackType, "iframe")) {
                url.addQueryParameter("t", "f");
                if (args.containsKey("isGlobalPixel") && Objects.requireNonNull(args.get("isGlobalPixel")).length() > 0 && Objects.equals(args.get("isGlobalPixel"), "true")) {
                    url.addQueryParameter("gb", "1");
                } else {
                    if (args.containsKey("offerId") && Objects.requireNonNull(args.get("offerId")).length() > 0) {
                        url.addQueryParameter("o", args.get("offerId"));
                    }
                }
            }
        } else {
            url.addQueryParameter("t", "f");
            if (args.containsKey("isGlobalPixel") && Objects.requireNonNull(args.get("isGlobalPixel")).length() > 0 && Objects.equals(args.get("isGlobalPixel"), "true")) {
                url.addQueryParameter("gb", "1");
            } else {
                if (args.containsKey("offerId") && Objects.requireNonNull(args.get("offerId")).length() > 0) {
                    url.addQueryParameter("o", args.get("offerId"));
                }
            }
        }
        return url.build();
    }
}
