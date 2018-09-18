package com.chinamobile.iot.onenet.module;

import com.chinamobile.iot.onenet.http.Urls;

import okhttp3.HttpUrl;

public class ApiKey {

    public static String urlForAdding() {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("keys").toString();
    }

    public static String urlForUpdating(String key) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("keys")
                .addPathSegment(key).toString();
    }

    public static String urlForQuerying(String key, int page, int perPage, String deviceId) {
        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("keys");
        if (key != null) {
            builder.addQueryParameter("key", key);
        }
        if (page > 0) {
            builder.addQueryParameter("page", String.valueOf(page));
        }
        if (perPage > 0) {
            builder.addQueryParameter("per_page", String.valueOf(perPage));
        }
        if (deviceId != null) {
            builder.addQueryParameter("device_id", deviceId);
        }
        return builder.toString();
    }

    public static String urlForDeleting(String key) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("keys")
                .addPathSegment(key).toString();
    }

}
