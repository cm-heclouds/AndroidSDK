package com.chinamobile.iot.onenet.device;

import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class DeleteDevice extends BaseStringRequest {

    private static final String URL = BASE_URL + "/devices/";

    public DeleteDevice(String apiKey, String deviceId, ResponseListener listener) {
        super(Method.DELETE, URL + deviceId, apiKey, listener);
    }

}
