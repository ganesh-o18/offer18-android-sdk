package com.offer18.sdk;

import com.offer18.sdk.constant.Env;
import com.offer18.sdk.contract.Configuration;
import com.offer18.sdk.contract.CredentialManager;
import com.offer18.sdk.contract.ServiceDiscovery;
import com.offer18.sdk.contract.Storage;

import java.util.Map;

public class Offer18Configuration implements Configuration {
    protected CredentialManager credentialManager;
    protected boolean loggingEnabled = false;
    protected Env env = Env.PRODUCTION;
    protected Storage storage;

    public Offer18Configuration(CredentialManager credentialManager, Map<String, String> config) {
        this.credentialManager = credentialManager;
    }

    @Override
    public String getLoggingMode() {
        return null;
    }

    @Override
    public Env getEnv() {
        return this.env;
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
        return new Offer18ServiceDiscovery(this.storage);
    }

    @Override
    public Storage getStorage() {
        return this.storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void enableDebugMode() {
        this.env = Env.DEBUG;
    }

    @Override
    public void enableProductionMode() {
        this.env = Env.PRODUCTION;
    }
}
