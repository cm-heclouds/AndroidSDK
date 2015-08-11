package com.chinamobile.iot.onenet.datastream;

import com.android.volley.AuthFailureError;
import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

import org.json.JSONObject;

public class EditDatastream extends BaseStringRequest {

    private String mUnit;
    private String mUnitSymbol;

    public EditDatastream(String apiKey, String deviceId, String streamId, String unit, String unitSymbol,
            ResponseListener listener) {
        super(Method.PUT, BASE_URL + "/devices/" + deviceId + "/datastreams/" + streamId, apiKey, listener);

        mUnit = unit;
        mUnitSymbol = unitSymbol;
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
