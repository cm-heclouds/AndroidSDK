package com.chinamobile.iot.onenet.datapoint;

import com.android.volley.AuthFailureError;
import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

import org.json.JSONArray;
import org.json.JSONObject;

public class AddDatapoint extends BaseStringRequest {

    private String mDatastreamId;
    private JSONArray mDatapoints;
    private JSONObject mData;

    public AddDatapoint(String apiKey, String deviceId, String datastreamId, JSONArray datapoints, ResponseListener listener) {
        super(Method.POST, BASE_URL + "/devices/" + deviceId + "/datapoints", apiKey, listener);
        mDatastreamId = datastreamId;
        mDatapoints = datapoints;
    }

    public AddDatapoint(String apiKey, String deviceId, JSONObject data, ResponseListener listener) {
        super(Method.POST, BASE_URL + "/devices/" + deviceId + "/datapoints", apiKey, listener);
        mData = data;
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
