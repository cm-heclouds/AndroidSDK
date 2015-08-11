package com.chinamobile.iot.onenet.datapoint;

import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class GetDatapoints extends BaseStringRequest {

    public GetDatapoints(String apiKey, String deviceId, String params, ResponseListener listener) {
        super(Method.GET, BASE_URL + "/devices/" + deviceId + "/datapoints" + params, apiKey, listener);
    }

}
