package com.chinamobile.iot.onenet.module;

import com.chinamobile.iot.onenet.http.Urls;

import okhttp3.HttpUrl;

public class Command {

    public enum CommandType {
        TYPE_CMD_REQ, TYPE_PUSH_DATA
    }

    public static String urlForSending(String deviceId) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("cmds")
                .addQueryParameter("device_id", deviceId).toString();
    }

    public static String urlForSending(String deviceId, boolean needResponse, int timeout, CommandType type) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("cmds")
                .addQueryParameter("device_id", deviceId).addQueryParameter("qos", needResponse ? "1" : "0")
                .addQueryParameter("timeout", String.valueOf(timeout))
                .addQueryParameter("type", String.valueOf(type.ordinal())).toString();
    }

    public static String urlForQueryingStatus(String cmdUuid) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("cmds")
                .addPathSegment(cmdUuid).toString();
    }

    public static String urlForQueryingResponse(String cmdUuid) {
        return new HttpUrl.Builder().scheme(Urls.sScheme).host(Urls.sHost).addPathSegment("cmds")
                .addPathSegment(cmdUuid).addPathSegment("resp").toString();
    }

}
