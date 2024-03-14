package com.offer18.sdk;

import com.offer18.sdk.util.Offer18Util;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class Offer18UtilUnitTest {

    @Test
    public void generated_md5_are_correct() throws NoSuchAlgorithmException {
        String msg = "hello";
        String md5 = "5d41402abc4b2a76b9719d911017c592";
        org.junit.Assert.assertEquals(md5, Offer18Util.getMD5(msg));
    }
}

