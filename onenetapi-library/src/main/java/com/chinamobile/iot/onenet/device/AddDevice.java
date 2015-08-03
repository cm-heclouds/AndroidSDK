package com.chinamobile.iot.onenet.device;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class AddDevice extends BaseStringRequest {

    private static final String URL = BASE_URL + "/devices";

    private String mApiKey;
    private String mTitle;
    private String mDesc;
    private boolean mIsPrivate;
    private String mRouteTo;
    private JSONObject mAuthInfo;

    private JSONObject mBody;

    public AddDevice(String apiKey, String title, String desc,
            boolean isPrivate, String routeTo, JSONObject authInfo,
            ResponseListener listener) {
        super(Method.POST, URL, listener);

        mApiKey = apiKey;
        mTitle = title;
        mDesc = desc;
        mIsPrivate = isPrivate;
        mRouteTo = routeTo;
        mAuthInfo = authInfo;
    }

    public AddDevice(String apiKey, JSONObject body,
            ResponseListener listener) {
        super(Method.POST, URL, listener);

        mApiKey = apiKey;
        mBody = body;
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
        try {
            if (mBody != null) {
                body = mBody.toString().getBytes("utf-8");
            } else {
                JSONObject obj = new JSONObject();
                obj.put("title", mTitle);
                if (mDesc != null) {
                    obj.put("desc", mDesc);
                }
                obj.put("private", mIsPrivate);
                if (mRouteTo != null) {
                    obj.put("route_to", mRouteTo);
                }
                if (mAuthInfo != null) {
                    obj.put("auth_info", mAuthInfo);
                }
                body = obj.toString().getBytes("utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }

}
