package com.offer18.sdk;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class Offer18ClientUnitTest {

    @Test
    public void sdk_init_does_not_throws_exceptions() throws Exception {
        Offer18.init(null, new HashMap<>());
    }

    @Test
    public void client_endpoint_returns_valid_url_when_global_pixel_is_disabled() throws Exception {
        Offer18.init(null, new HashMap<>());
        Map<String, String> args = new HashMap<>();
    }
}

