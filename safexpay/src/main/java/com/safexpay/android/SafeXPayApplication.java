package com.safexpay.android;

import android.app.Application;

public class SafeXPayApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        SafeXPay.getInstance().initialize(this, SafeXPay.Environment.TEST);
    }
}
