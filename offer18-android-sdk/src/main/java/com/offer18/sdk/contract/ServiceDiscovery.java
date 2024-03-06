package com.offer18.sdk.contract;

import java.util.HashMap;
import java.util.Map;

/**
 * --------------------------------------------------------------------------
 * ServiceDiscovery intent to provide configurations for the SDK.
 * These configurations are discoverable using REST interface
 * and persists on client. Since these are subjected to
 * changes over period time, these values will be
 * updated periodically.
 * <p>
 * The configurations includes
 * 1. Endpoints for the apis
 * 2. Parameter's name & their respective data types & default values.
 * --------------------------------------------------------------------------
 */
public interface ServiceDiscovery {
    Long getHttpTimeout();

    String doesSSLVerificationRequired();

    Long serviceDiscoveryTimeout();

    /**
     * Check if service document is outdated
     * @return
     */
    boolean isOutDated();

    /**
     * Check if service document exists
     * @return
     */
    boolean isExists();

    Map<String, HashMap<String, String>> getServices();
}
