package com.chinamobile.iot.onenet;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class BaseBinRequest implements IRequest {

    private ResponseListener mListener;

    public BaseBinRequest(ResponseListener listener) {
        mListener = listener;
    }

    protected void execute(AsyncHttpClient client, String url, File file) {

        RequestParams params = new RequestParams();
        try {
            params.put("file", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        post(client, url, params);
    }

    protected void execute(AsyncHttpClient client, String url, InputStream stream) {
        RequestParams params = new RequestParams();
        params.put("file", stream);
        post(client, url, params);
    }

    protected void execute(AsyncHttpClient client, String url, String value) {
        RequestParams params = new RequestParams();
        params.put("text", value);
        post(client, url, params);
    }

    protected void execute(AsyncHttpClient client, String url, byte[] value) {
        RequestParams params = new RequestParams();
        params.put("text", value);
        post(client, url, params);
    }

    private void post(AsyncHttpClient client, final String url, RequestParams params) {
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                OneNetResponse response = new OneNetResponse();
                if (bytes != null) {
                    try {
                        String responseString = new String(bytes, "utf-8");

                        if (OneNetApi.mDebugEable) {
                            Log.i("OneNetApi", "request url = " + url);
                            Log.i("OneNetApi", "raw response = " + responseString);
                        }

                        response.setRawResponse(responseString);
                        JSONObject obj = new JSONObject(responseString);
                        response.setErrno(obj.optInt("errno", -1));
                        response.setError(obj.optString("error"));
                        response.setData(obj.optString("data"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        response.setErrno(-1);
                        response.setError("");
                    }
                }

                if (mListener != null) {
                    mListener.onResponse(response);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                String responseString = "";
                if (bytes != null) {
                    try {
                        responseString = new String(bytes, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                if (OneNetApi.mDebugEable) {
                    Log.e("OneNetApi", "request url = " + url);
                    Log.e("OneNetApi", responseString + ", " + throwable.toString());
                }

                if (mListener != null) {
                    OneNetError oneNetError = new OneNetError(responseString, throwable.getCause());
                    mListener.onError(oneNetError);
                }
            }
        });
    }

}
