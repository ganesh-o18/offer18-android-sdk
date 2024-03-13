package com.offer18.sdk.response;

import com.offer18.sdk.contract.Response;

public class TrackConversionResponse implements Response {
    protected boolean success = false;
    protected String error;

    public TrackConversionResponse() {
        this.success = true;
    }

    public TrackConversionResponse(String error) {
        this.error = error;
    }

    @Override
    public boolean isSuccessful() {
        return false;
    }

    @Override
    public String getError() {
        return this.error;
    }
}
