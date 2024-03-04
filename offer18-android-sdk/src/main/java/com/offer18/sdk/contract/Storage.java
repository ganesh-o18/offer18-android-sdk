package com.offer18.sdk.contract;

/**
 * --------------------------------------------
 * A key values based storage, keys and values
 * are limited to string
 * ----------------------------------------------
 */
public interface Storage {
    boolean set(String key, String value);

    String get(String key);

    boolean has(String key);

    boolean remove(String key);
}
