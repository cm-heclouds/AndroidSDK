package com.chinamobile.iot.onenet.module;

import com.chinamobile.iot.onenet.http.Urls;

import okhttp3.HttpUrl;

public class Mqtt {

    public static String urlForSendingCmdByTopic(String topic) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("mqtt")
                .addEncodedQueryParameter("topic", topic).toString();
    }

    public static String urlForQueryingDevicesByTopic(String topic, int page, int perPage) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("mqtt")
                .addPathSegment("topic_device").addEncodedQueryParameter("topic", topic)
                .addQueryParameter("page", String.valueOf(page))
                .addQueryParameter("per_page", String.valueOf(perPage)).toString();
    }

    public static String urlForQueryingDeviceTopics(String deviceId) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("mqtt")
                .addPathSegment("device_topic").addPathSegment(deviceId).toString();
    }

    public static String urlForAddingTopic() {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("mqtt")
                .addPathSegment("topic").toString();
    }

    public static String urlForDeletingTopic(String topic) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("mqtt")
                .addPathSegment("topic").addEncodedQueryParameter("name", topic).toString();
    }

    public static String urlForQueryingTopics() {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("mqtt")
                .addPathSegment("topic").toString();
    }

}
