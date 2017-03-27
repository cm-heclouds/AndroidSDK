package com.chinamobile.iot.onenet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

class OneNetApiCallbackAdapter implements Callback {

    private static final int MSG_SUCCESS = 0;
    private static final int MSG_FAILED = 1;

    private OneNetApiCallback mOneNetApiCallback;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SUCCESS:
                    if (mOneNetApiCallback != null) {
                        Bundle b = msg.getData();
                        int errno = b.getInt("errno");
                        String error = b.getString("error");
                        String data = b.getString("data");
                        mOneNetApiCallback.onSuccess(errno, error, data);
                    }
                    break;

                case MSG_FAILED:
                    if (mOneNetApiCallback != null) {
                        Bundle b = msg.getData();
                        Exception e = (Exception) b.getSerializable("exception");
                        mOneNetApiCallback.onFailed(e);
                    }
                    break;
            }
        }

    };

    public OneNetApiCallbackAdapter(OneNetApiCallback oneNetApiCallback) {
        mOneNetApiCallback = oneNetApiCallback;
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        sendFailedMessage(e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (response.isSuccessful()) {
            ResponseBody responseBody = response.body();
            String responseString = responseBody.string();
            try {
                JSONObject jsonObject = new JSONObject(responseString);
                final int errno = jsonObject.optInt("errno");
                final String error = jsonObject.optString("error");
                JSONObject dataObject = jsonObject.optJSONObject("data");
                final String data = dataObject != null ? dataObject.toString() : null;
                sendSuccessMessage(errno, error, data);
            } catch (final JSONException e) {
                e.printStackTrace();
                sendFailedMessage(e);
            }
        } else {
            if (OneNetApi.sDebug) {
                Log.e(OneNetApi.LOG_TAG, response.toString());
            }
            sendFailedMessage(new Exception(response.toString()));
        }
    }

    private void sendSuccessMessage(int errno, String error, String data) {
        Message msg = mHandler.obtainMessage(MSG_SUCCESS);
        Bundle b = new Bundle();
        b.putInt("errno", errno);
        b.putString("error", error);
        b.putString("data", data);
        msg.setData(b);
        mHandler.sendMessage(msg);
    }

    private void sendFailedMessage(Exception e) {
        Message msg = mHandler.obtainMessage(MSG_SUCCESS);
        Bundle b = new Bundle();
        b.putSerializable("exception", e);
        msg.setData(b);
        mHandler.sendMessage(msg);
    }

}
