package com.offer18.sdk;

import com.offer18.sdk.contract.ServiceDiscovery;
import com.offer18.sdk.contract.Storage;

import java.util.HashMap;
import java.util.Map;

 class Offer18ServiceDiscovery implements ServiceDiscovery {
    protected Storage storage;

     public Offer18ServiceDiscovery(Storage storage) {
        this.storage = storage;
     }

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
     public boolean isOutDated() {
         return false;
     }

     @Override
     public boolean isExists() {
         return false;
     }

     @Override
    public Map<String, HashMap<String, String>> getServices() {
        return null;
    }
}
