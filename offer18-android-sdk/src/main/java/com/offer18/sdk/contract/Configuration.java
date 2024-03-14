package com.offer18.sdk.contract;

import com.offer18.sdk.constant.Env;

public interface Configuration {
    String getLoggingMode();

    Env getEnv();

    String getApiKey();

    String getApiSecret();

    Storage getStorage();

    void setStorage(Storage storage);

    void enableDebugMode();

    void enableProductionMode();

    String get(String key);

    boolean set(String key, String value);

    boolean remove(String key);

    long getHttpDefaultTimeout();

    boolean isRemoteConfigOutdated();

    Logger getLogger();

    void setLogger(Logger logger);

    boolean isLoggingEnabled();
}
