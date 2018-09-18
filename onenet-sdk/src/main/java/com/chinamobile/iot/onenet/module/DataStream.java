package com.chinamobile.iot.onenet.module;

import com.chinamobile.iot.onenet.http.Urls;

import okhttp3.HttpUrl;

public class DataStream {

    public static String urlForAdding(String deviceId) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("devices")
                .addPathSegment(deviceId).addPathSegment("datastreams").toString();
    }

    public static String urlForUpdating(String deviceId, String dataStreamId) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("devices")
                .addPathSegment(deviceId).addPathSegment("datastreams")
                .addEncodedPathSegment(dataStreamId).toString();
    }

    public static String urlForQueryingSingle(String deviceId, String dataStreamId) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("devices")
                .addPathSegment(deviceId).addPathSegment("datastreams")
                .addEncodedPathSegment(dataStreamId).toString();
    }

    public static String urlForQueryingMulti(String deviceId, String[] dataStreamIds) {
        HttpUrl.Builder builder = new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("devices")
                .addPathSegment(deviceId).addPathSegment("datastreams");
        if (dataStreamIds != null && dataStreamIds.length > 0) {
            StringBuilder sb = new StringBuilder(dataStreamIds[0]);
            for (int i = 1; i < dataStreamIds.length; i++) {
                sb.append(",");
                sb.append(dataStreamIds[i]);
            }
            builder.addEncodedQueryParameter("datastream_ids", sb.toString());
        }
        return builder.toString();
    }

    public static String urlForDeleting(String deviceId, String dataStreamId) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("devices")
                .addPathSegment(deviceId).addPathSegment("datastreams")
                .addEncodedPathSegment(dataStreamId).toString();
    }

}
