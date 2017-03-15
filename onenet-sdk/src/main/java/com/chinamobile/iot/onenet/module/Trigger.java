package com.chinamobile.iot.onenet.module;

import com.chinamobile.iot.onenet.http.Urls;

import okhttp3.HttpUrl;

public class Trigger {

    public static String urlForAdding() {
        return new HttpUrl.Builder().scheme(Urls.SCHEME).host(Urls.HOST).addPathSegment("triggers")
                .toString();
    }

    public static String urlForUpdating(String triggerId) {
        return new HttpUrl.Builder().scheme(Urls.SCHEME).host(Urls.HOST).addPathSegment("triggers")
                .addPathSegment(triggerId).toString();
    }

    public static String urlForQueryingSingle(String triggerId) {
        return new HttpUrl.Builder().scheme(Urls.SCHEME).host(Urls.HOST).addPathSegment("triggers")
                .addPathSegment(triggerId).toString();
    }

    public static String urlForfuzzyQuerying(String name, int page, int perPage) {
        return new HttpUrl.Builder().scheme(Urls.SCHEME).host(Urls.HOST).addPathSegment("triggers")
                .addEncodedQueryParameter("title", name).addQueryParameter("page", String.valueOf(page))
                .addQueryParameter("per_page", String.valueOf(perPage)).toString();
    }

    public static String urlForDeleting(String triggerId) {
        return new HttpUrl.Builder().scheme(Urls.SCHEME).host(Urls.HOST).addPathSegment("triggers")
                .addPathSegment(triggerId).toString();
    }

}
