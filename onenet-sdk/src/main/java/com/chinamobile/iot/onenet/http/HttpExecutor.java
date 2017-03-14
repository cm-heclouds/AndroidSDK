package com.chinamobile.iot.onenet.http;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpExecutor {

    private OkHttpClient mOkHttpClient;

    public HttpExecutor(OkHttpClient okHttpClient) {
        mOkHttpClient = okHttpClient;
    }

    public void get(String url, Callback callback) {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        requestBuilder.get();
        execute(requestBuilder.build(), callback);
    }

    public void post(String url, RequestBody requestBody, Callback callback) {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        requestBuilder.post(requestBody);
        execute(requestBuilder.build(), callback);
    }

    public void put(String url, RequestBody requestBody, Callback callback) {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        requestBuilder.put(requestBody);
        execute(requestBuilder.build(), callback);
    }

    public void delete(String url, Callback callback) {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        requestBuilder.delete();
        execute(requestBuilder.build(), callback);
    }

    public void delete(String url, RequestBody requestBody, Callback callback) {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        requestBuilder.delete(requestBody);
        execute(requestBuilder.build(), callback);
    }

    public void patch(String url, RequestBody requestBody, Callback callback) {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        requestBuilder.patch(requestBody);
        execute(requestBuilder.build(), callback);
    }

    public void head(String url, Callback callback) {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        requestBuilder.head();
        execute(requestBuilder.build(), callback);
    }

    private void execute(Request request, Callback callback) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }

}
