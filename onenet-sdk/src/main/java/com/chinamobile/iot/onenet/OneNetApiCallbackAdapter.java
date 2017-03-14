package com.chinamobile.iot.onenet;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

class OneNetApiCallbackAdapter implements Callback {

    private OneNetApiCallback mOneNetApiCallback;

    public OneNetApiCallbackAdapter(OneNetApiCallback oneNetApiCallback) {
        mOneNetApiCallback = oneNetApiCallback;
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        if (mOneNetApiCallback != null) {
            mOneNetApiCallback.onFailed(e);
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (mOneNetApiCallback != null) {
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                String responseString = responseBody.string();
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    final int errno = jsonObject.optInt("errno");
                    final String error = jsonObject.optString("error");
                    final String data = jsonObject.optString("data");
                    mOneNetApiCallback.onSuccess(errno, error, data);
                } catch (final JSONException e) {
                    e.printStackTrace();
                    mOneNetApiCallback.onFailed(e);
                }
            } else {
                if (OneNetApi.sDebug) {
                    Log.e(OneNetApi.LOG_TAG, response.toString());
                }
                mOneNetApiCallback.onFailed(new Exception(response.toString()));
            }
        }
    }

}
