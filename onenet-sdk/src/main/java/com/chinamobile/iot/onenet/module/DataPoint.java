package com.chinamobile.iot.onenet.module;

import com.chinamobile.iot.onenet.http.Urls;

import java.util.Iterator;
import java.util.Map;

import okhttp3.HttpUrl;

public class DataPoint {

    public static String urlForAdding(String deviceId, String type) {
        HttpUrl.Builder builder = new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost)
                .addPathSegment("devices").addPathSegment(deviceId).addPathSegment("datapoints");
        if (type != null) {
            builder.addQueryParameter("type", type);
        }
        return builder.toString();
    }

    public static String urlForQuerying(String deviceId, Map<String, String> params) {
        HttpUrl.Builder builder = new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost)
                .addPathSegment("devices").addPathSegment(deviceId).addPathSegment("datapoints");
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
