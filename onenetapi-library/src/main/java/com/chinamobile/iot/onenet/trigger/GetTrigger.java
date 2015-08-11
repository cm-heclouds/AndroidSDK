package com.chinamobile.iot.onenet.trigger;

import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class GetTrigger extends BaseStringRequest {

    private static final String URL = BASE_URL + "/triggers/";

    public GetTrigger(String apiKey, String triggerId, ResponseListener listener) {
        super(Method.GET, URL + triggerId, apiKey, listener);
    }

}
