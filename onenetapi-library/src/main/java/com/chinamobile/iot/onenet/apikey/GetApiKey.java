package com.chinamobile.iot.onenet.apikey;

import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class GetApiKey extends BaseStringRequest {

    private static final String URL = BASE_URL + "/keys";

    public GetApiKey(String masterKey, String params, ResponseListener listener) {
        super(Method.GET, URL + params, masterKey, listener);
    }

}
