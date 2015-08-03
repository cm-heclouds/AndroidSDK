package com.chinamobile.iot.onenet;

public interface ResponseListener {

    void onResponse(OneNetResponse response);

    void onError(OneNetError error);

}
