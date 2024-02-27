package com.offer18.sdk;

import com.offer18.sdk.Exception.Offer18ClientNotInitialiseException;
import com.offer18.sdk.Exception.Offer18InvalidCredentialException;
import com.offer18.sdk.constant.Constant;
import com.offer18.sdk.contract.Client;
import com.offer18.sdk.contract.Configuration;
import com.offer18.sdk.contract.CredentialManager;

import java.util.Map;
import java.util.Objects;

public class Offer18 {
    protected static CredentialManager credentialManager;
    protected static Configuration configuration;
    /**
     * Offer18 client
     */
    protected static Client client;

    protected static String env = Constant.ENV_DEBUG;

    /**
     * Init SDK
     */
    public static void init(Map<String, String> credentials) throws Exception {
//        credentialManager = validateCredentials(credentials);
//        configuration = new Offer18Configuration(credentialManager);
        client = initSDK();
    }

    /**
     * Track conversion
     */
    public static void trackConversion(Map<String, String> args) throws Offer18ClientNotInitialiseException {
//        if (client == null) {
//            throw new Offer18ClientNotInitialiseException("Offer18 sdk not initialised");
//        }
        client.trackConversion(args, configuration);
    }

    public static String getEnv() {
        return env;
    }

    public static String enableDebugMode() {
        env = Constant.ENV_DEBUG;
        return env;
    }

    public static String enableProductionMode() {
        env = Constant.ENV_PRODUCTION;
        return env;
    }

    public static boolean isDebugModeEnabled() {
        return Objects.equals(env, Constant.ENV_DEBUG);
    }

    public static boolean isProductionModeEnabled() {
        return Objects.equals(env, Constant.ENV_PRODUCTION);
    }

    /**
     * Validate the provided credentials
     */
    private static CredentialManager validateCredentials(Map<String, String> credentials) throws Offer18InvalidCredentialException {
        if (credentials == null || credentials.isEmpty()) {
            throw new Offer18InvalidCredentialException("There is no api key and secret key in provided credentials");
        }
        String apiKey = (String) credentials.get("api-key");
        String apiSecret = (String) credentials.get("api-secret");
        if ((apiKey == null || apiKey.length() == 0) && (apiSecret == null || apiSecret.length() == 0)) {
            throw new Offer18InvalidCredentialException("There is no api key and secret key in provided credentials");
        }
        if (apiKey == null || apiKey.length() == 0) {
            throw new Offer18InvalidCredentialException("There is no api key in provided credentials");
        }
        if (apiSecret == null || apiSecret.length() == 0) {
            throw new Offer18InvalidCredentialException("There is no api secret in provided credentials");
        }
        return new Offer18CredentialManager(apiKey, apiSecret);
    }

    /**
     * Initialise SDK
     */
    private static Client initSDK() throws Exception {
        return new Offer18Client();
    }
}
