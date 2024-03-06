package com.offer18.sdk.contract;

import com.offer18.sdk.constant.Env;

public interface Configuration {
    String getLoggingMode();

    Env getEnv();

    String getApiKey();

    String getApiSecret();

    ServiceDiscovery getServiceDiscovery();

    Storage getStorage();

    void setStorage(Storage storage);

    void enableDebugMode();

    void enableProductionMode();
}
