package com.offer18.sdk.response;

import com.offer18.sdk.contract.Response;

public class TrackConversionResponse implements Response {
    protected boolean status;
    protected String message;

    public TrackConversionResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public boolean isSuccessful() {
        return this.status;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
