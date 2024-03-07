package com.offer18.sdk.worker;

import java.util.concurrent.CountDownLatch;

import okhttp3.OkHttpClient;

public class TrackConversionWorker implements Runnable {
    CountDownLatch remoteConfigDownloadSignal;
    OkHttpClient httpClient;
    
    public TrackConversionWorker(CountDownLatch remoteConfigDownloadSignal, OkHttpClient okHttpClient) {
        this.remoteConfigDownloadSignal = remoteConfigDownloadSignal;
        this.httpClient = okHttpClient;
    }

    @Override
    public void run() {

    }
}
