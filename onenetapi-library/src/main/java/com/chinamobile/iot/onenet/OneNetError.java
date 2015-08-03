package com.chinamobile.iot.onenet;

import com.android.volley.VolleyError;

/**
 * 网络或服务器错误
 * 
 * @author chenglei
 *
 */
@SuppressWarnings("serial")
public class OneNetError extends VolleyError {

    public OneNetError(String exceptionMessage, Throwable reason) {
        super(exceptionMessage, reason);
    }

}
