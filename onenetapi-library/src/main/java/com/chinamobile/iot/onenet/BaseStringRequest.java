package com.chinamobile.iot.onenet;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public abstract class BaseStringRequest extends StringRequest implements IRequest {

    public BaseStringRequest(int method, String url, ResponseListener listener) {
        super(method, url, ListenerWrapper.wrapListener(url, listener), ListenerWrapper.wrapError(url, listener));
    }

    private static class ListenerWrapper {

        public static Listener<String> wrapListener(final String url, final ResponseListener listener) {
            return new Listener<String>() {

                @Override
                public void onResponse(String response) {
                    if (OneNetApi.mDebugEable) {
                        Log.i("OneNetApi", "request url = " + url);
                        Log.i("OneNetApi", "raw response = " + response);
                    }
                    if (listener != null) {
                        OneNetResponse oneNetResponse = new OneNetResponse();
                        oneNetResponse.setRawResponse(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            oneNetResponse.setErrno(getErrno(jsonObject));
                            oneNetResponse.setError(getError(jsonObject));
                            oneNetResponse.setData(getData(jsonObject));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            oneNetResponse.setErrno(-1);
                            oneNetResponse.setError("");
                        }
                        listener.onResponse(oneNetResponse);
                    }
                }

            };
        }

        public static ErrorListener wrapError(final String url, final ResponseListener errorListener) {
            return new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (OneNetApi.mDebugEable) {
                        Log.e("OneNetApi", "request url = " + url);
                        Log.e("OneNetApi", error.toString());
                    }
                    if (errorListener != null) {
                        OneNetError oneNetError = new OneNetError(error.toString(), error.getCause());
                        errorListener.onError(oneNetError);
                    }
                }
            };
        }

    }

    private static int getErrno(JSONObject jsonObject) {
        return jsonObject.optInt("errno", -1);
    }

    private static String getError(JSONObject jsonObject) {
        return jsonObject.optString("error");
    }

    private static String getData(JSONObject jsonObject) {
        return jsonObject.optString("data");
    }

}
