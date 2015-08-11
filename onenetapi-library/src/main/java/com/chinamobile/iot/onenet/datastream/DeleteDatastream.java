package com.chinamobile.iot.onenet.datastream;

import com.chinamobile.iot.onenet.BaseStringRequest;
import com.chinamobile.iot.onenet.ResponseListener;

public class DeleteDatastream extends BaseStringRequest {

    public DeleteDatastream(String apiKey, String deviceId, String streamId,
            ResponseListener listener) {
        super(Method.DELETE, BASE_URL + "/devices/" + deviceId + "/datastreams/" + streamId, apiKey, listener);
    }

}
