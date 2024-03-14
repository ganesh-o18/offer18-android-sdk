package com.offer18.sdk;

import com.offer18.sdk.contract.Callback;
import com.offer18.sdk.contract.Client;
import com.offer18.sdk.contract.Configuration;
import com.offer18.sdk.worker.ServiceDiscoveryWorker;
import com.offer18.sdk.worker.TrackConversionWorker;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

class Offer18Client implements Client {
    protected Configuration configuration;
    private final OkHttpClient httpClient;
    protected CountDownLatch remoteConfigDownloadSignal = new CountDownLatch(1);

    public Offer18Client(Configuration configuration) {
        this.configuration = configuration;
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS);
        this.httpClient = clientBuilder.build();
        new Thread(new ServiceDiscoveryWorker(remoteConfigDownloadSignal, this.httpClient, this.configuration, null)).start();
    }

    @Override
    public String trackConversion(Map<String, String> args, Configuration configuration) throws Exception {
        try {
            new Thread(new TrackConversionWorker(remoteConfigDownloadSignal, this.httpClient, this.configuration, args)).start();
        } catch (RuntimeException e) {
            throw new Exception(e.getMessage());
        }
        return null;
    }

    /**
     * @param args
     * @param configuration
     * @param callback
     * @return
     * @throws Exception
     */
    @Override
    public String trackConversion(Map<String, String> args, Configuration configuration, Callback callback) throws Exception {
        try {
            new Thread(new TrackConversionWorker(remoteConfigDownloadSignal, this.httpClient, this.configuration, args, callback)).start();
        } catch (RuntimeException e) {
            throw new Exception(e.getMessage());
        }
        return null;
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }
}
