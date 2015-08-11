package com.chinamobile.iot.onenet.logs;

import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class GetRestAPILogs extends BaseStringRequest {

    private static final String URL = BASE_URL + "/logs/";

    public GetRestAPILogs(String apiKey, String deviceId, ResponseListener listener) {
        super(Method.GET, URL + deviceId, apiKey, listener);
    }

}
