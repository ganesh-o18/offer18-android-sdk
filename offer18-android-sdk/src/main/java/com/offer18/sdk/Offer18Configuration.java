package com.offer18.sdk;

import com.offer18.sdk.contract.Configuration;
import com.offer18.sdk.contract.CredentialManager;
import com.offer18.sdk.contract.ServiceDiscovery;
import com.offer18.sdk.contract.Storage;

public class Offer18Configuration implements Configuration {
    protected CredentialManager credentialManager;
    protected Storage storage;
    public Offer18Configuration(CredentialManager credentialManager) {
        this.credentialManager = credentialManager;
    }

    @Override
    public String getLoggingMode() {
        return null;
    }

    @Override
    public String getEnv() {
        return null;
    }

    @Override
    public String getApiKey() {
        return this.credentialManager.getApiKey();
    }

    @Override
    public String getApiSecret() {
        return this.credentialManager.getApiSecret();
    }

    @Override
    public ServiceDiscovery getServiceDiscovery() {
        return new Offer18ServiceDiscovery();
    }

    @Override
    public Storage getStorage() {
        return this.storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }
}
