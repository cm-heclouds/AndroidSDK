package com.chinamobile.iot.onenet.apikey;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class EditApiKey extends BaseStringRequest {

    private static final String URL = BASE_URL + "/keys/";

    private String mMasterKey;
    private String mTitle;
    private String mDeviceId;
    private String mDatastreamId;

    public EditApiKey(String masterKey, String keyString, String title, String deviceId, String datastreamId,
            ResponseListener listener) {
        super(Method.PUT, URL + keyString, listener);
        mMasterKey = masterKey;
        mTitle = title;
        mDeviceId = deviceId;
        mDatastreamId = datastreamId;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("api-key", mMasterKey);
        return headers;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        byte[] body = null;
        try {
            JSONObject resource = new JSONObject();
            resource.put("dev_id", mDeviceId);
            if (mDatastreamId != null) resource.put("ds_id", mDatastreamId);

            JSONArray resources = new JSONArray();
            resources.put(resource);

            JSONObject resourcesObj = new JSONObject();
            resourcesObj.put("resources", resources);

            JSONArray permissions = new JSONArray();
            permissions.put(resourcesObj);

            JSONObject obj = new JSONObject();
            obj.put("title", mTitle);
            obj.put("permissions", permissions);

            body = obj.toString().getBytes("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return body;
    }

}
