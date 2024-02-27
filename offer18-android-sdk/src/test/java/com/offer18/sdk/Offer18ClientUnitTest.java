package com.offer18.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.offer18.sdk.constant.Constant;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;

public class Offer18ClientUnitTest {

    @Test
    public void sdk_init_does_not_throws_exceptions() throws Exception {
        Offer18.init(new HashMap<>());
    }

    @Test
    public void client_endpoint_returns_valid_url_when_global_pixel_is_disabled() throws Exception {
        Offer18.init(new HashMap<>());
        Map<String, String> args = new HashMap<>();
        String domain = "ganesh-local-dev.o18-test.live";
        String accountId = "18962";
        String offerId = "20520286";
        String advSub1 = "1";
        String coupon = "coupon_1";
        String currency = "USD";
        args.put(Constant.DOMAIN, domain);
        args.put(Constant.ACCOUNT_ID, accountId);
        args.put(Constant.OFFER_ID, offerId);
        args.put(Constant.ADV_SUB_1, advSub1);
        args.put(Constant.COUPON, coupon);
        args.put(Constant.POSTBACK_TYPE, Constant.POSTBACK_TYPE_IFRAME);
        args.put(Constant.CURRENCY, currency);
        args.put(Constant.IS_GLOBAL_PIXEL, "false");
        HttpUrl url = new Offer18Client().buildEndpoint(args);
        assertNotNull(url.toString());
        System.out.println(url.url());
        assertEquals(url.url().getHost(), domain);
        assertEquals(url.queryParameter(Constant.ACCOUNT_ID), accountId);
        assertEquals(url.queryParameter(Constant.OFFER_ID), offerId);
        assertEquals(url.queryParameter(Constant.ADV_SUB_1), advSub1);
        assertEquals(url.queryParameter(Constant.COUPON), coupon);
        assertEquals(url.queryParameter(Constant.POSTBACK_TYPE), Constant.POSTBACK_TYPE_IFRAME);
    }

    @Test
    public void client_endpoint_returns_valid_url_when_global_pixel_is_enabled() throws Exception {
        Offer18.init(new HashMap<>());
        Map<String, String> args = new HashMap<>();
        String domain = "ganesh-local-dev.o18-test.live";
        String accountId = "18962";
        String offerId = "20520286";
        String advSub1 = "1";
        String coupon = "coupon_1";
        String currency = "USD";
        args.put(Constant.DOMAIN, domain);
        args.put(Constant.ACCOUNT_ID, accountId);
        args.put(Constant.OFFER_ID, offerId);
        args.put(Constant.ADV_SUB_1, advSub1);
        args.put(Constant.COUPON, coupon);
        args.put(Constant.POSTBACK_TYPE, Constant.POSTBACK_TYPE_IFRAME);
        args.put(Constant.CURRENCY, currency);
        args.put(Constant.IS_GLOBAL_PIXEL, "true");
        HttpUrl url = new Offer18Client().buildEndpoint(args);
        assertNotNull(url.toString());
        System.out.println(url.url());
        assertEquals(url.url().getHost(), domain);
        assertEquals(url.queryParameter(Constant.ACCOUNT_ID), accountId);
        assertNull(url.queryParameter(Constant.OFFER_ID));
        assertEquals(url.queryParameter(Constant.ADV_SUB_1), advSub1);
        assertEquals(url.queryParameter(Constant.COUPON), coupon);
        assertEquals(url.queryParameter(Constant.POSTBACK_TYPE), Constant.POSTBACK_TYPE_IFRAME);
        assertEquals(url.queryParameter(Constant.IS_GLOBAL_PIXEL), "1");
    }
}

