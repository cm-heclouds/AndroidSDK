package com.chinamobile.iot.onenet;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class BaseBinRequest implements IRequest {

    private ResponseListener mListener;

    public BaseBinRequest(ResponseListener listener) {
        mListener = listener;
    }

    @Deprecated
    protected void execute(AsyncHttpClient client, String url, File file) {
        HttpEntity entity = new FileEntity(file, "application/octet-stream");
        post(client, url, entity, "application/octet-stream");
    }

    @Deprecated
    protected void execute(AsyncHttpClient client, String url, InputStream stream) {
        byte[] bytes = new byte[1024 * 64];
        try {
            stream.read(bytes, 0, stream.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity = new ByteArrayEntity(bytes);
        post(client, url, entity, "application/octet-stream");
    }

    protected void execute(AsyncHttpClient client, String url, String value) {
        HttpEntity entity = null;
        try {
            entity = new StringEntity(value, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post(client, url, entity, "text/plain");
    }

    protected void execute(AsyncHttpClient client, String url, byte[] value) {
        ByteArrayEntity entity = new ByteArrayEntity(value);
        entity.setContentEncoding("utf-8");
        post(client, url, entity, "application/octet-stream");
    }

    private void post(AsyncHttpClient client, final String url, HttpEntity entity, String contentType) {
        client.post(null, url, entity, contentType, new AsyncHttpResponseHandler() {
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
