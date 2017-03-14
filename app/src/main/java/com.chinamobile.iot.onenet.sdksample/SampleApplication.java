package com.chinamobile.iot.onenet.sdksample;

import android.app.Application;

import com.chinamobile.iot.onenet.OneNetApi;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OneNetApi.init(this, true);
    }
}
