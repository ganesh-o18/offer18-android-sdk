package com.offer18.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.Instrumentation;
import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.offer18.sdk.Exception.Offer18ClientNotInitialiseException;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

@RunWith(AndroidJUnit4.class)
public class Offer18ClientInstrumentedTest {
    @Test
    public void sdk_does_throw_exception_when_context_is_not_provided() {
        try {
            Offer18.init(null);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    @Test
    public void sdk_does_throw_exception_when_services_are_called_before_sdk_init() throws Exception {
        try {
            Offer18.trackConversion(new HashMap<>());
        } catch (Exception e) {
            assertTrue(e instanceof Offer18ClientNotInitialiseException);
        }
    }

    @Test
    public void sdk_does_not_throw_exception_when_context_is_provide() throws Exception {
        Instrumentation instrumentationRegistry = InstrumentationRegistry.getInstrumentation();
        Context context = instrumentationRegistry.getContext();
        Offer18.init(context);
    }

    @Test
    public void sdk_default_config_for_http_time_out_is_2000() throws Exception {
        Instrumentation instrumentationRegistry = InstrumentationRegistry.getInstrumentation();
        Context context = instrumentationRegistry.getContext();
        Offer18.init(context);
        Offer18Configuration configuration = new Offer18Configuration(new HashMap<>());
        configuration.setStorage(new Offer18Storage(context));
        assertEquals(2000, configuration.getHttpDefaultTimeout());
    }

    @Test
    public void sdk_configuration_can_retrieve_what_was_set_before() throws Exception {
        Instrumentation instrumentationRegistry = InstrumentationRegistry.getInstrumentation();
        Context context = instrumentationRegistry.getContext();
        Offer18.init(context);
        Offer18Configuration configuration = new Offer18Configuration(new HashMap<>());
        configuration.setStorage(new Offer18Storage(context));
        configuration.set("hello", "world");
        assertEquals("world", configuration.get("hello"));
    }

    @Test
    public void sdk_configuration_cannot_retrieve_what_was_deleted() throws Exception {
        Instrumentation instrumentationRegistry = InstrumentationRegistry.getInstrumentation();
        Context context = instrumentationRegistry.getContext();
        Offer18.init(context);
        Offer18Configuration configuration = new Offer18Configuration(new HashMap<>());
        configuration.setStorage(new Offer18Storage(context));
        configuration.set("hello", "world");
        configuration.remove("hello");
        assertEquals("", configuration.get("hello"));
    }
}