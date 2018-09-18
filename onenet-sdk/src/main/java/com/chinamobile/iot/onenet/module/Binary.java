package com.chinamobile.iot.onenet.module;

import com.chinamobile.iot.onenet.http.Urls;

import okhttp3.HttpUrl;

public class Binary {

    public static String urlForAdding(String deviceId, String dataStreamId) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("bindata")
                .addQueryParameter("device_id", deviceId)
                .addEncodedQueryParameter("datastream_id", dataStreamId).toString();
    }

    public static String urlForQuerying(String index) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("bindata")
                .addEncodedPathSegment(index).toString();
    }

}
