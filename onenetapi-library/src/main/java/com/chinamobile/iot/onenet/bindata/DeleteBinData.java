package com.chinamobile.iot.onenet.bindata;

import com.android.volley.AuthFailureError;
import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

import java.util.HashMap;
import java.util.Map;

public class DeleteBinData extends BaseStringRequest {

    private static final String URL = BASE_URL + "/bindata/";

    private String mApiKey;

    public DeleteBinData(String apiKey, String index, ResponseListener listener) {
        super(Method.DELETE, URL + index, listener);
        mApiKey = apiKey;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        if (mApiKey != null) headers.put("api-key", mApiKey);
        return headers;
    }
}
