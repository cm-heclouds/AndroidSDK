package com.chinamobile.iot.onenet.datastream;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class DeleteDatastream extends BaseStringRequest {

    private String mApiKey;

    public DeleteDatastream(String apiKey, String deviceId, String streamId,
            ResponseListener listener) {
        super(Method.DELETE, BASE_URL + "/devices/" + deviceId + "/datastreams/" + streamId, listener);

        mApiKey = apiKey;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("api-key", mApiKey);
        return headers;
    }

}
