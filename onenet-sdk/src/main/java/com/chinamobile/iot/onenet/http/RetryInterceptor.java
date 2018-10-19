package com.chinamobile.iot.onenet.http;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RetryInterceptor implements Interceptor {
    public final int MAX_RETRY_COUNT;
    private int mRetried = 0;

    public RetryInterceptor() {
        this.MAX_RETRY_COUNT = 2;
    }

    public RetryInterceptor(int maxRetryCount) {
        this.MAX_RETRY_COUNT = maxRetryCount;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return process(chain);
    }

    private Response process(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        try {
            response = chain.proceed(request);
            if (response != null && response.isSuccessful()) {
                return response;
            }
        } catch (IOException e) {
            if (mRetried >= MAX_RETRY_COUNT) {
                throw e;
            }
        }
        if (mRetried < MAX_RETRY_COUNT) {
            mRetried++;
            Log.e("chenglei", "retried " + mRetried + " times");
            response = process(chain);
        }
        return response;
    }
}
