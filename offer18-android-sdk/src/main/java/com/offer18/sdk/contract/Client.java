package com.offer18.sdk.contract;

import java.util.Map;

public interface Client {
    String trackConversion(Map<String, String> args, Configuration configuration) throws Exception;
    String trackConversion(Map<String, String> args, Configuration configuration, Callback callback) throws Exception;
    Configuration getConfiguration();
}
