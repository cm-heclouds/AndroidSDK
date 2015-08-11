package com.chinamobile.iot.onenet.trigger;

import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class DeleteTrigger extends BaseStringRequest {

    private static final String URL = BASE_URL + "/triggers/";

    public DeleteTrigger(String apiKey, String triggerId, ResponseListener listener) {
        super(Method.DELETE, URL + triggerId, apiKey, listener);
    }

}
