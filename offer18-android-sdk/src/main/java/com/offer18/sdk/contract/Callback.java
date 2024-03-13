package com.offer18.sdk.contract;

public interface Callback {
    void onSuccess(Response response);
    void onError(Response response);
}
