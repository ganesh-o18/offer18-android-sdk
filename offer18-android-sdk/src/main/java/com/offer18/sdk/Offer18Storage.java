package com.offer18.sdk;

import android.content.Context;
import android.content.SharedPreferences;

import com.offer18.sdk.constant.Constant;
import com.offer18.sdk.contract.Storage;

class Offer18Storage implements Storage {
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;

    public Offer18Storage(Context context) {
        this.sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREFERENCE, Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
    }

    @Override
    public boolean set(String key, String value) {
        this.editor.putString(key, value);
        return this.editor.commit();
    }

    @Override
    public String get(String key) {
        return this.sharedPreferences.getString(key, "");
    }

    @Override
    public boolean has(String key) {
        return this.sharedPreferences.contains(key);
    }

    @Override
    public boolean remove(String key) {
        this.editor.remove(key);
        return this.editor.commit();
    }
}
