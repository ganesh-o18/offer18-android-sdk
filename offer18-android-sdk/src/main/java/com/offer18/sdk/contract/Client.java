package com.offer18.sdk.contract;

import java.util.Map;

public interface Client {
    String trackConversion(Map<String, String> args, Configuration configuration) throws Exception;
    Configuration getConfiguration();
}
