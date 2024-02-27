package com.offer18.sdk;

import com.offer18.sdk.contract.Configuration;
import com.offer18.sdk.contract.CredentialManager;

public class Offer18Configuration implements Configuration {
    protected CredentialManager credentialManager;

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
}
