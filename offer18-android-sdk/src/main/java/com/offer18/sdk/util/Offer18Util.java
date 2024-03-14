package com.offer18.sdk.util;

import android.os.Build;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Offer18Util {
    public static String getMD5(String msg) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] messageDigest = digest.digest(msg.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : messageDigest) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public static Map<String, String> getMetadata() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("brand_name", Build.BRAND);
        metadata.put("manufacturer_name", Build.MANUFACTURER);
        metadata.put("product_name", Build.PRODUCT);
        metadata.put("sdk_version", Integer.toString(Build.VERSION.SDK_INT));
        metadata.put("base_os_name", Build.VERSION.BASE_OS);
        metadata.put("os_code_name", Build.VERSION.CODENAME);
        return metadata;
    }
}
