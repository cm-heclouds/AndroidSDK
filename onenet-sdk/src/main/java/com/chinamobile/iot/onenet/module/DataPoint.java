package com.chinamobile.iot.onenet.module;

import android.text.TextUtils;

import com.chinamobile.iot.onenet.http.Urls;

import java.util.Iterator;
import java.util.Map;

import okhttp3.HttpUrl;

public class DataPoint {

    public static final String DEFAULT_TYPE = "3";

    public static String urlForAdding(String deviceId, String type) {
        return new HttpUrl.Builder().scheme(Urls.SCHEME).host(Urls.HOST).addPathSegment("devices")
                .addPathSegment(deviceId).addPathSegment("datapoints")
                .addQueryParameter("type", TextUtils.isEmpty(type) ? DEFAULT_TYPE : type).toString();
    }

    public static String urlForQuerying(String deviceId, Map<String, String> params) {
        HttpUrl.Builder builder = new HttpUrl.Builder().scheme(Urls.SCHEME).host(Urls.HOST)
                .addPathSegment("devices");
        if (params != null) {
            Iterator iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                builder.addEncodedQueryParameter(key, value);
            }
        }
        return builder.toString();
    }

}
