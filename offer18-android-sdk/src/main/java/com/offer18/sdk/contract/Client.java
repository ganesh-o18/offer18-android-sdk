package com.offer18.sdk.contract;

import com.offer18.sdk.Exception.Offer18FormFieldDataTypeException;
import com.offer18.sdk.Exception.Offer18FormFieldRequiredException;
import com.offer18.sdk.Exception.Offer18SSLVerifcationException;

import java.util.Map;

public interface Client {
    String trackConversion(Map<String, String> args, Configuration configuration) throws Exception;
    Configuration getConfiguration();
}
