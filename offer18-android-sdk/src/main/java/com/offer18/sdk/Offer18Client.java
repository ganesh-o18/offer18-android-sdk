package com.offer18.sdk;

import com.offer18.sdk.Exception.Offer18FormFieldDataTypeException;
import com.offer18.sdk.Exception.Offer18FormFieldRequiredException;
import com.offer18.sdk.Exception.Offer18SSLVerifcationException;
import com.offer18.sdk.constant.Constant;
import com.offer18.sdk.contract.Client;
import com.offer18.sdk.contract.Configuration;
import com.offer18.sdk.worker.ServiceDiscoveryWorker;
import com.offer18.sdk.worker.TrackConversionWorker;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import okhttp3.OkHttpClient;

class Offer18Client implements Client {
    protected Configuration configuration;
    private final OkHttpClient httpClient;
    protected CountDownLatch remoteConfigDownloadSignal = new CountDownLatch(1);

    public Offer18Client(Configuration configuration) {
        this.configuration = configuration;
        String currentDigest = this.configuration.get(Constant.DIGEST);
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        this.httpClient = clientBuilder.build();
        new Thread(new ServiceDiscoveryWorker(remoteConfigDownloadSignal, this.httpClient, this.configuration, null)).start();
    }

    @Override
    public String trackConversion(Map<String, String> args, Configuration configuration) throws Offer18SSLVerifcationException, Offer18FormFieldRequiredException, Offer18FormFieldDataTypeException {
        new Thread(new TrackConversionWorker(remoteConfigDownloadSignal, this.httpClient, this.configuration, args)).start();
        return null;
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }
}
