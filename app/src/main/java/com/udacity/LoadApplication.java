package com.udacity;

import android.app.Application;

import timber.log.Timber;

public class LoadApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
