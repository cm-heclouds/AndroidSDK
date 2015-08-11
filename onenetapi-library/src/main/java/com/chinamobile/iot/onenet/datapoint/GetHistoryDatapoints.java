package com.chinamobile.iot.onenet.datapoint;

import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class GetHistoryDatapoints extends BaseStringRequest {

    private static final String URL = BASE_URL + "/datapoints";

    public GetHistoryDatapoints(String apikey, String params, ResponseListener listener) {
        super(Method.GET, URL + params, apikey, listener);
    }

}
