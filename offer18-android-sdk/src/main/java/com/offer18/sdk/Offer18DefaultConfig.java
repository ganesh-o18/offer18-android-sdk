package com.offer18.sdk;

import com.offer18.sdk.contract.Storage;

import java.util.Objects;

public class Offer18DefaultConfig {

    public static boolean loadDefaultConfig(Storage storage) {
        if (Objects.isNull(storage)) {
            return false;
        }
        storage.set("http_ssl_verification", "false");
        storage.set("http_time_out", "2000");
        return true;
    }
}
