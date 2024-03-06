package com.offer18.sdk.contract;

import com.offer18.sdk.Exception.Offer18SSLVerifcationException;

import java.util.Map;

public interface Client {
    String trackConversion(Map<String, String> args, Configuration configuration) throws Offer18SSLVerifcationException;
}
