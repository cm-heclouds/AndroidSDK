package com.chinamobile.iot.onenet;

public interface OneNetApiCallback {

    /**
     * Called when the HTTP response code was 2xx.
     */
    void onSuccess(String response);

    /**
     * Called when the request could not be executed due to cancellation, a connectivity problem,
     * timeout or the HTTP response code was not 2xx.
     */
    void onFailed(Exception e);

}
