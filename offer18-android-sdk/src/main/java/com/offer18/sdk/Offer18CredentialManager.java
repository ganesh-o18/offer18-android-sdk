package com.offer18.sdk;

import com.offer18.sdk.contract.CredentialManager;

public class Offer18CredentialManager implements CredentialManager {
    private String apiKey;
    private String apiSecret;

    public Offer18CredentialManager() {
    }

    public Offer18CredentialManager(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    @Override
    public String getApiKey() {
        return this.apiKey;
    }

    @Override
    public String getApiSecret() {
        return this.apiSecret;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }
}
