package com.chinamobile.iot.onenet.device;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class GetDevices extends BaseStringRequest {

    private static final String URL = BASE_URL + "/devices";

    private String mApiKey;

    public GetDevices(String apiKey, String params,
            ResponseListener listener) {

        super(Method.GET, URL + params, listener);
        mApiKey = apiKey;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        if (mApiKey != null) headers.put("api-key", mApiKey);
        return headers;
    }

}
