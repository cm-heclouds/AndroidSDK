package com.chinamobile.iot.onenet.datapoint;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class AddDatapoint extends BaseStringRequest {

    private String mApiKey;
    private String mDatastreamId;
    private JSONArray mDatapoints;
    private JSONObject mData;

    public AddDatapoint(String apiKey, String deviceId, String datastreamId, JSONArray datapoints, ResponseListener listener) {
        super(Method.POST, BASE_URL + "/devices/" + deviceId + "/datapoints", listener);
        mApiKey = apiKey;
        mDatastreamId = datastreamId;
        mDatapoints = datapoints;
    }

    public AddDatapoint(String apiKey, String deviceId, JSONObject data, ResponseListener listener) {
        super(Method.POST, BASE_URL + "/devices/" + deviceId + "/datapoints", listener);
        mApiKey = apiKey;
        mData = data;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        if (mApiKey != null) headers.put("api-key", mApiKey);
        return headers;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        byte[] body = null;
        try {
            if (mData != null) {
                body = mData.toString().getBytes("utf-8");
            } else {
                JSONObject obj = new JSONObject();

                JSONArray datastreams = new JSONArray();

                JSONObject datastream = new JSONObject();
                datastream.put("id", mDatastreamId);
                datastream.put("datapoints", mDatapoints);

                datastreams.put(datastream);

                obj.put("datastreams", datastreams);
                body = obj.toString().getBytes("utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }

}
