package com.chinamobile.iot.onenet.module;

import com.chinamobile.iot.onenet.http.Urls;

import okhttp3.HttpUrl;

public class Trigger {

    public static String urlForAdding() {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("triggers")
                .toString();
    }

    public static String urlForUpdating(String triggerId) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("triggers")
                .addPathSegment(triggerId).toString();
    }

    public static String urlForQueryingSingle(String triggerId) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("triggers")
                .addPathSegment(triggerId).toString();
    }

    public static String urlForfuzzyQuerying(String name, int page, int perPage) {
        HttpUrl.Builder builder = new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("triggers");
        if (name != null && name.length() > 0) {
            builder.addEncodedQueryParameter("title", name);
        }
        if (page > 0) {
            builder.addQueryParameter("page", String.valueOf(page));
        }
        if (perPage > 0) {
            builder.addQueryParameter("per_page", String.valueOf(perPage));
        }
        return builder.toString();
    }

    public static String urlForDeleting(String triggerId) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("triggers")
                .addPathSegment(triggerId).toString();
    }

}
