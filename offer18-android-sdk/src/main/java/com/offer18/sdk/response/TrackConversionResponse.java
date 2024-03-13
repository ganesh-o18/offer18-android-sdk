package com.offer18.sdk.response;

import com.offer18.sdk.contract.Response;

public class TrackConversionResponse implements Response {
    protected boolean status;
    protected String error;

    public TrackConversionResponse(boolean status, String error) {
        this.status = status;
        this.error = error;
    }

    @Override
    public boolean isSuccessful() {
        return this.status;
    }

    @Override
    public String getMessage() {
        return this.error;
    }
}
