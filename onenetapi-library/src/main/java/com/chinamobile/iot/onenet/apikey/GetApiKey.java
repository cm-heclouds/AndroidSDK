package com.chinamobile.iot.onenet.apikey;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class GetApiKey extends BaseStringRequest {

    private static final String URL = BASE_URL + "/keys";

    private String mMasterKey;

    public GetApiKey(String masterKey, String params, ResponseListener listener) {
        super(Method.GET, URL + params, listener);
        mMasterKey = masterKey;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("api-key", mMasterKey);
        return headers;
    }

}
