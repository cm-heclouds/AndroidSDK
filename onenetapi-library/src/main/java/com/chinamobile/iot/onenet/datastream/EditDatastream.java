package com.chinamobile.iot.onenet.datastream;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class EditDatastream extends BaseStringRequest {

    private String mApiKey;
    private String mUnit;
    private String mUnitSymbol;

    public EditDatastream(String apiKey, String deviceId, String streamId, String unit, String unitSymbol,
            ResponseListener listener) {
        super(Method.PUT, BASE_URL + "/devices/" + deviceId + "/datastreams/" + streamId, listener);

        mApiKey = apiKey;
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
            obj.put("unit", mUnit);
            obj.put("unit_symbol", mUnitSymbol);
            body = obj.toString().getBytes("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }

}
