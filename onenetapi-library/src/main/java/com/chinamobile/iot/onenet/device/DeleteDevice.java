package com.chinamobile.iot.onenet.device;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class DeleteDevice extends BaseStringRequest {

    private static final String URL = BASE_URL + "/devices/";

    private String mApiKey;

    public DeleteDevice(String apiKey, String deviceId,
            ResponseListener listener) {
        super(Method.DELETE, URL + deviceId, listener);
        mApiKey = apiKey;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("api-key", mApiKey);
        return headers;
    }

}
