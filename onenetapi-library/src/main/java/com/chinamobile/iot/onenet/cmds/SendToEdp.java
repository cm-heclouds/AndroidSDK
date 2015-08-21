package com.chinamobile.iot.onenet.cmds;

import com.android.volley.AuthFailureError;
import com.chinamobile.iot.onenet.BaseBinRequest;
import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;
import com.loopj.android.http.AsyncHttpClient;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class SendToEdp extends BaseStringRequest {

    private static final String URL = BASE_URL + "/cmds?device_id=";

    private byte[] mBytes;

    public SendToEdp(String apiKey, String deviceId, String text, ResponseListener listener) {
        super(Method.POST, URL + deviceId, apiKey, listener);
        try {
            mBytes = text.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public SendToEdp(String apiKey, String deviceId, byte[] bytes, ResponseListener listener) {
        super(Method.POST, URL + deviceId, apiKey, listener);
        mBytes = bytes;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return mBytes;
    }
}
