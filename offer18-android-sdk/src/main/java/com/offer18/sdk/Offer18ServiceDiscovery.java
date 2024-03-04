package com.offer18.sdk;

import com.offer18.sdk.contract.ServiceDiscovery;

import java.util.HashMap;
import java.util.Map;

 class Offer18ServiceDiscovery implements ServiceDiscovery {

    private static final String url = "https://ganesh-local-dev.o18-test.live/m/files/cron-jobs/service_discovery.php";

    @Override
    public Long getHttpTimeout() {
        return null;
    }

    @Override
    public boolean doesSSLVerificationRequired() {
        return false;
    }

    @Override
    public Long serviceDiscoveryTimeout() {
        return 1000L;
    }

    @Override
    public Map<String, HashMap<String, String>> getServices() {
        return null;
    }
}
