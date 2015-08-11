package com.chinamobile.iot.onenet.device;

import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class GetDevice extends BaseStringRequest {

    private static final String URL = BASE_URL + "/devices/";

    public GetDevice(String apiKey, String deviceId, ResponseListener listener) {
        super(Method.GET, URL + deviceId, apiKey, listener);
    }

}
