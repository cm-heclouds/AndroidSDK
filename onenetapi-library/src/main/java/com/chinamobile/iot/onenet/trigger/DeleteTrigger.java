package com.chinamobile.iot.onenet.trigger;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class DeleteTrigger extends BaseStringRequest {

    private static final String URL = BASE_URL + "/triggers/";

    private String mApiKey;

    public DeleteTrigger(String apiKey, String triggerId, ResponseListener listener) {
        super(Method.DELETE, URL + triggerId, listener);
        mApiKey = apiKey;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("api-key", mApiKey);
        return headers;
    }

}
