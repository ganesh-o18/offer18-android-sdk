package com.offer18.sdk.contract;

public interface Configuration {
    String getLoggingMode();

    String getEnv();

    String getApiKey();

    String getApiSecret();

    ServiceDiscovery getServiceDiscovery();

    Storage getStorage();

    void setStorage(Storage storage);
}
