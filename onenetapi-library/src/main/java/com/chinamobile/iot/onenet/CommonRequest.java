package com.chinamobile.iot.onenet;

import com.android.volley.AuthFailureError;

import java.util.Map;

/**
 * 通用请求，可自定义请求方法、url、header和body 
 * 
 * @author chenglei
 *
 */
public class CommonRequest extends BaseStringRequest {

    private Map<String, String> mHeaders;
    private byte[] mBody;

    public CommonRequest(int method, String url, Map<String, String> headers, byte[] body, ResponseListener listener) {
        super(method, url, null, listener);
        mHeaders = headers;
        mBody = body;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (null == mHeaders) {
            return super.getHeaders();
        }
        return mHeaders;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return mBody;
    }

}
