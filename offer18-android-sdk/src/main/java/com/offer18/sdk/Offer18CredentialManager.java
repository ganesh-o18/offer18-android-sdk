package com.offer18.sdk;

import com.offer18.sdk.Exception.Offer18InvalidCredentialException;
import com.offer18.sdk.contract.CredentialManager;

import java.util.Map;

public class Offer18CredentialManager implements CredentialManager {
    private String apiKey;
    private String apiSecret;

    public Offer18CredentialManager() {
    }

    public Offer18CredentialManager(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public Offer18CredentialManager(Map<String, String> credentials) throws Offer18InvalidCredentialException {
        String apiKey = credentials.get("api-key");
        String apiSecret = credentials.get("api-secret");
        if ((apiKey == null || apiKey.isEmpty()) && (apiSecret == null || apiSecret.isEmpty())) {
            throw new Offer18InvalidCredentialException("There is no api key and secret key in provided credentials");
        }
        if (apiKey == null || apiKey.isEmpty()) {
            throw new Offer18InvalidCredentialException("There is no api key in provided credentials");
        }
        if (apiSecret == null || apiSecret.isEmpty()) {
            throw new Offer18InvalidCredentialException("There is no api secret in provided credentials");
        }
       this.setApiKey(apiKey);
        this.setApiSecret(apiSecret);
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
