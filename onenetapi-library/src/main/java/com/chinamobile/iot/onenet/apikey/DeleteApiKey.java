package com.chinamobile.iot.onenet.apikey;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class DeleteApiKey extends BaseStringRequest {

    private static final String URL = BASE_URL + "/keys/";

    private String mMasterKey;

    public DeleteApiKey(String masterKey, String keyString, ResponseListener listener) {
        super(Method.DELETE, URL + keyString, listener);
        mMasterKey = masterKey;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("api-key", mMasterKey);
        return headers;
    }

}
