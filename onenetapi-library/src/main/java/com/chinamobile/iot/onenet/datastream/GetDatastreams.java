package com.chinamobile.iot.onenet.datastream;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class GetDatastreams extends BaseStringRequest {

    private String mApiKey;

    public GetDatastreams(String apiKey, String deviceId, String params, ResponseListener listener) {
        super(Method.GET, BASE_URL + "/devices/" + deviceId + "datastreams" + params, listener);
        mApiKey = apiKey;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        if (mApiKey != null) headers.put("api-key", mApiKey);
        return headers;
    }

}
