package com.chinamobile.iot.onenet.datastream;

import com.android.volley.AuthFailureError;
import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

import org.json.JSONObject;

public class AddDatastream extends BaseStringRequest {

    private String mStreamId;
    private String mUnit;
    private String mUnitSymbol;

    public AddDatastream(String apiKey, String deviceId, String streamId, String unit, String unitSymbol,
            ResponseListener listener) {
        super(Method.POST, BASE_URL + "/devices/" + deviceId + "/datastreams", apiKey, listener);

        mStreamId = streamId;
        mUnit = unit;
        mUnitSymbol = unitSymbol;
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
