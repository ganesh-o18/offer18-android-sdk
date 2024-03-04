package com.offer18.sdk;

import com.offer18.sdk.contract.ServiceDiscovery;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * ServiceDiscovery interceptor will first check for the http request and will try to
 * use latest service configurations.
 */
class ServiceDiscoveryInterceptor implements Interceptor {
    public ServiceDiscoveryInterceptor() {
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {


        return null;
    }
}
