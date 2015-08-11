package com.chinamobile.iot.onenet.apikey;

import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class DeleteApiKey extends BaseStringRequest {

    private static final String URL = BASE_URL + "/keys/";

    public DeleteApiKey(String masterKey, String keyString, ResponseListener listener) {
        super(Method.DELETE, URL + keyString, masterKey, listener);
    }

}
