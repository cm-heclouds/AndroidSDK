package com.chinamobile.iot.onenet.module;

import com.chinamobile.iot.onenet.http.Urls;

import okhttp3.HttpUrl;

public class DataStream {

    public static String urlForAdding(String deviceId) {
        return new HttpUrl.Builder().scheme(Urls.SCHEME).host(Urls.HOST).addPathSegment("devices")
                .addPathSegment(deviceId).addPathSegment("datastreams").toString();
    }

    public static String urlForUpdating(String deviceId, String dataStreamId) {
        return new HttpUrl.Builder().scheme(Urls.SCHEME).host(Urls.HOST).addPathSegment("devices")
                .addPathSegment(deviceId).addPathSegment("datastreams")
                .addEncodedPathSegment(dataStreamId).toString();
    }

    public static String urlForQueryingSingle(String deviceId, String dataStreamId) {
        return new HttpUrl.Builder().scheme(Urls.SCHEME).host(Urls.HOST).addPathSegment("devices")
                .addPathSegment(deviceId).addPathSegment("datastreams")
                .addEncodedPathSegment(dataStreamId).toString();
    }

    public static String urlForQueryingMulti(String deviceId) {
        return new HttpUrl.Builder().scheme(Urls.SCHEME).host(Urls.HOST).addPathSegment("devices")
                .addPathSegment(deviceId).addPathSegment("datastreams").toString();
    }

    public static String urlForDeleting(String deviceId, String dataStreamId) {
        return new HttpUrl.Builder().scheme(Urls.SCHEME).host(Urls.HOST).addPathSegment("devices")
                .addPathSegment(deviceId).addPathSegment("datastreams")
                .addEncodedPathSegment(dataStreamId).toString();
    }

}
