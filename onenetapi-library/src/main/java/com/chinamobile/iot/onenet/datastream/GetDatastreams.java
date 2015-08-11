package com.chinamobile.iot.onenet.datastream;

import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class GetDatastreams extends BaseStringRequest {

    public GetDatastreams(String apiKey, String deviceId, String params, ResponseListener listener) {
        super(Method.GET, BASE_URL + "/devices/" + deviceId + "/datastreams" + params, apiKey, listener);
    }

}
