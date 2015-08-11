package com.chinamobile.iot.onenet.datastream;

import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class GetDatastream extends BaseStringRequest {

    public GetDatastream(String apiKey, String deviceId, String streamId,
            ResponseListener listener) {
        super(Method.GET, BASE_URL + "/devices/" + deviceId + "/datastreams/" + streamId, apiKey, listener);
    }

}
