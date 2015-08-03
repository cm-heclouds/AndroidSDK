package com.chinamobile.iot.onenet.cmds;

import com.chinamobile.iot.onenet.BaseBinRequest;
import com.chinamobile.iot.onenet.ResponseListener;
import com.loopj.android.http.AsyncHttpClient;

import java.io.File;
import java.io.InputStream;

public class SendToEdp extends BaseBinRequest {

    private static final String URL = BASE_URL + "/cmds/";

    private String mDeviceId;
    private AsyncHttpClient mAsyncHttpClient;

    public SendToEdp(String apiKey, String deviceId, ResponseListener listener) {
        super(listener);

        mDeviceId = deviceId;
        mAsyncHttpClient = new AsyncHttpClient();
        mAsyncHttpClient.addHeader("api-key", apiKey);
    }

    public void send(File file) {
        execute(mAsyncHttpClient, URL + mDeviceId, file);
    }

    public void send(InputStream stream) {
        execute(mAsyncHttpClient, URL + mDeviceId, stream);
    }

    public void send(String text) {
        execute(mAsyncHttpClient, URL + mDeviceId, text);
    }

}
