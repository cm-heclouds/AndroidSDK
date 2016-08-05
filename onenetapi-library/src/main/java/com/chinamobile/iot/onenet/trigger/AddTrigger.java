package com.chinamobile.iot.onenet.trigger;

import com.android.volley.AuthFailureError;
import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddTrigger extends BaseStringRequest {

    private static final String URL = BASE_URL + "/triggers";

    private String mUrl;
    private String mType;
    private Object mThreshold;
    private String mDatastreamId;
    private ArrayList<String> mDevicesIds;
    private ArrayList<String> mDsUUIDs;

    public AddTrigger(String apiKey, String url, String type, Object threshold,
            String datastreamId, ArrayList<String> deviceIds, ArrayList<String> dsUUIDs,
            ResponseListener listener) {
        super(Method.POST, URL, apiKey, listener);
        mUrl = url;
        mType = type;
        mThreshold = threshold;
        mDatastreamId = datastreamId;
        mDevicesIds = deviceIds;
        mDsUUIDs = dsUUIDs;
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
