package com.chinamobile.iot.onenet.util;

import android.util.Log;

import com.chinamobile.iot.onenet.OneNetApi;

import okhttp3.logging.HttpLoggingInterceptor;

public class OneNetLogger implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(String message) {
        Log.i(OneNetApi.LOG_TAG, message);
    }
}
