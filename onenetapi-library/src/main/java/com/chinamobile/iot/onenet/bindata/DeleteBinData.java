package com.chinamobile.iot.onenet.bindata;

import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class DeleteBinData extends BaseStringRequest {

    private static final String URL = BASE_URL + "/bindata/";

    public DeleteBinData(String apiKey, String index, ResponseListener listener) {
        super(Method.DELETE, URL + index, apiKey, listener);
    }

}
