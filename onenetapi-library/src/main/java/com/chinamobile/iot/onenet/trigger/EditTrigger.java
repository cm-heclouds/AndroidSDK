package com.chinamobile.iot.onenet.trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class EditTrigger extends BaseStringRequest {

    private static final String URL = BASE_URL + "/triggers/";

    private String mApiKey;
    private String mUrl;
    private String mType;
    private double mThreshold;
    private String mDatastreamId;
    private ArrayList<String> mDevicesIds;
    private ArrayList<String> mDsUUIDs;

    public EditTrigger(String apiKey, String triggerId, String url, String type, double threshold,
            String datastreamId, ArrayList<String> deviceIds, ArrayList<String> dsUUIDs,
            ResponseListener listener) {
        super(Method.PUT, URL + triggerId, listener);
        mApiKey = apiKey;
        mUrl = url;
        mType = type;
        mThreshold = threshold;
        mDatastreamId = datastreamId;
        mDevicesIds = deviceIds;
        mDsUUIDs = dsUUIDs;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("api-key", mApiKey);
        return headers;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        byte[] body = null;
        JSONObject obj = new JSONObject();
            try {
                obj.put("url", mUrl);
                obj.put("type", mType);
                obj.put("threshold", mThreshold);
                if (mDatastreamId != null) obj.put("ds_id", mDatastreamId);
                if (mDevicesIds != null && !mDevicesIds.isEmpty()) {
                    JSONArray devIds = new JSONArray(mDevicesIds);
                    obj.put("dev_ids", devIds);
                }
                if (mDsUUIDs != null && !mDsUUIDs.isEmpty()) {
                    JSONArray dsUUIDs = new JSONArray(mDsUUIDs);
                    obj.put("ds_uuids", dsUUIDs);
                }
                body = obj.toString().getBytes("utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }

        return body;
    }

}
