package com.chinamobile.iot.onenet.module;

import com.chinamobile.iot.onenet.http.Urls;

import java.util.Iterator;
import java.util.Map;

import okhttp3.HttpUrl;

public class Device {

    public static String urlForRegistering(String registerCode) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("register_de")
                .addQueryParameter("register_code", registerCode).toString();
    }

    public static String urlForAdding() {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("devices")
                .toString();
    }

    public static String urlForUpdating(String deviceId) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("devices")
                .addPathSegment(deviceId).toString();
    }

    public static String urlForQueryingSingle(String deviceId) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("devices")
                .addPathSegment(deviceId).toString();
    }

    public static String urlForfuzzyQuerying(Map<String, String> params) {
        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("devices");
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

    public static String urlForDeleting(String deviceId) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("devices")
                .addPathSegment(deviceId).toString();
    }

}
