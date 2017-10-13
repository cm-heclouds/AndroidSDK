package com.chinamobile.iot.onenet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

class OneNetApiCallbackAdapter implements Callback {

    private static final int MSG_SUCCESS = 0;
    private static final int MSG_FAILED = 1;

    private OneNetApiCallback mOneNetApiCallback;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SUCCESS:
                    if (mOneNetApiCallback != null) {
                        Bundle b = msg.getData();
                        String response = b.getString("response");
                        mOneNetApiCallback.onSuccess(response);
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
            sendSuccessMessage(responseString);
        } else {
            if (OneNetApi.sDebug) {
                Log.e(OneNetApi.LOG_TAG, response.toString());
            }
            sendFailedMessage(new Exception(response.toString()));
        }
    }

    private void sendSuccessMessage(String response) {
        Message msg = mHandler.obtainMessage(MSG_SUCCESS);
        Bundle b = new Bundle();
        b.putString("response", response);
        msg.setData(b);
        mHandler.sendMessage(msg);
    }

    private void sendFailedMessage(Exception e) {
        Message msg = mHandler.obtainMessage(MSG_FAILED);
        Bundle b = new Bundle();
        b.putSerializable("exception", e);
        msg.setData(b);
        mHandler.sendMessage(msg);
    }

}
