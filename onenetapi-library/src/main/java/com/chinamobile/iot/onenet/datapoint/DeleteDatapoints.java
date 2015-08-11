package com.chinamobile.iot.onenet.datapoint;

import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class DeleteDatapoints extends BaseStringRequest {

    public DeleteDatapoints(String apiKey, String deviceId, String params, ResponseListener listener) {
        super(Method.DELETE, BASE_URL + "/devices/" + deviceId + "/datapoints" + params, apiKey, listener);
    }

}
