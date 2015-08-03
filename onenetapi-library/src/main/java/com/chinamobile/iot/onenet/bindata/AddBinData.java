package com.chinamobile.iot.onenet.bindata;

import com.chinamobile.iot.onenet.BaseBinRequest;
import com.chinamobile.iot.onenet.ResponseListener;
import com.loopj.android.http.AsyncHttpClient;

import java.io.File;
import java.io.InputStream;

public class AddBinData extends BaseBinRequest {

    private static final String URL = BASE_URL + "/bindata";

    private String mParams;
    private AsyncHttpClient mAsyncHttpClient;

    public AddBinData(String apiKey, String params, ResponseListener listener) {
        super(listener);

        mParams = params;
        mAsyncHttpClient = new AsyncHttpClient();
        mAsyncHttpClient.addHeader("api-key", apiKey);
    }

    public void send(File file) {
        execute(mAsyncHttpClient, URL + mParams, file);
    }

    public void send(InputStream stream) {
        execute(mAsyncHttpClient, URL + mParams, stream);
    }

    public void send(String text) {
        execute(mAsyncHttpClient, URL + mParams, text);
    }
}
