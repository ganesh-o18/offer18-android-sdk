package com.offer18.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;

public class Offer18ClientUnitTest {

    @Test
    public void sdk_init_does_not_throws_exceptions() throws Exception {
        Offer18.init(new HashMap<>());
        Map<String, String> args = new HashMap<>();
        String domain = "ganesh-local-dev.o18-test.live";
        String accountId = "18962";
        String offerId = "20520286";
        args.put("domain", domain);
        args.put("accountId", accountId);
        args.put("offerId", offerId);
        HttpUrl url = new Offer18Client().buildEndpoint(args);
        assertNotNull(url.toString());
        System.out.println(url.url());
        assertEquals(url.url().getHost(), domain);
        assertEquals(url.queryParameter("m"), accountId);
        assertEquals(url.queryParameter("o"), offerId);
    }
}

