package com.offer18.sdk.constant;

import org.jetbrains.annotations.NotNull;

public enum Env {
    DEBUG {
        @NotNull
        @Override public String toString() {
            return "debug";
        }
    },
    PRODUCTION {
        @NotNull
        @Override public String toString() {
            return "production";
        }
    }
}
