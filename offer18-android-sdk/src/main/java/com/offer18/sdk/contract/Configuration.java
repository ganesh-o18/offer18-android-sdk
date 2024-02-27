package com.offer18.sdk.contract;

public interface Configuration {
    /**
     * Get logging mode
     *
     * @return String
     */
    String getLoggingMode();

    /**
     * Get current env
     *
     * @return String
     */
    String getEnv();

    String getApiKey();

    String getApiSecret();
}
