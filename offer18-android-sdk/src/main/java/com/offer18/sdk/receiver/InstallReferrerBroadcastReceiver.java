package com.offer18.sdk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class InstallReferrerBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String pkg = intent.getPackage();
        SharedPreferences sharedPreferences = context.getSharedPreferences("offer18-service_discover", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("install.pkg", pkg);
        editor.apply();
    }
}
