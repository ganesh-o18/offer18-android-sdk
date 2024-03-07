package com.offer18.sdk;

import com.offer18.sdk.constant.Env;
import com.offer18.sdk.contract.Configuration;
import com.offer18.sdk.contract.CredentialManager;
import com.offer18.sdk.contract.Storage;

import java.util.Map;

class Offer18Configuration implements Configuration {
    protected CredentialManager credentialManager;
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

    @Override
    public String get(String key) {
        return this.getStorage().get(key);
    }

    @Override
    public boolean set(String key, String value) {
       return this.storage.set(key, value);
    }

    @Override
    public boolean remove(String key) {
       return this.storage.remove(key);
    }
}
