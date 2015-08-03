package com.chinamobile.iot.onenet.datapoint;

import com.android.volley.AuthFailureError;
import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

import java.util.HashMap;
import java.util.Map;

public class GetHistoryDatapoints extends BaseStringRequest {

    private static final String URL = BASE_URL + "/datapoints";

    private String mApiKey;

    public GetHistoryDatapoints(String apikey, String params, ResponseListener listener) {
        super(Method.GET, URL + params, listener);
        mApiKey = apikey;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        if (mApiKey != null) headers.put("api-key", mApiKey);
        return headers;
    }
}
