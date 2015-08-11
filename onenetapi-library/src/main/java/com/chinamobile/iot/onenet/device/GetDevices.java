package com.chinamobile.iot.onenet.device;

import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class GetDevices extends BaseStringRequest {

    private static final String URL = BASE_URL + "/devices";

    public GetDevices(String apiKey, String params, ResponseListener listener) {
        super(Method.GET, URL + params, apiKey, listener);
    }

}
