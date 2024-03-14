package com.offer18.sdk.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
}
