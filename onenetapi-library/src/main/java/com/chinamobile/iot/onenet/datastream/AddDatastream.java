package com.chinamobile.iot.onenet.datastream;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class AddDatastream extends BaseStringRequest {

    private String mApiKey;
    private String mStreamId;
    private String mUnit;
    private String mUnitSymbol;

    public AddDatastream(String apiKey, String deviceId, String streamId, String unit, String unitSymbol,
            ResponseListener listener) {
        super(Method.POST, BASE_URL + "/devices/" + deviceId + "/datastreams", listener);

        mApiKey = apiKey;
        mStreamId = streamId;
        mUnit = unit;
        mUnitSymbol = unitSymbol;
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
            obj.put("id", mStreamId);
            obj.put("unit", mUnit);
            obj.put("unit_symbol", mUnitSymbol);
            body = obj.toString().getBytes("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }

}
