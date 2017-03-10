package com.chinamobile.iot.onenet;

import android.app.Application;
import android.content.pm.PackageManager;

import com.chinamobile.iot.onenet.util.Meta;
import com.chinamobile.iot.onenet.util.OneNetLogger;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class OneNetApi {

    public static final String LOG_TAG = "OneNetApi";

    private static String sApiKey;
    private static boolean sDebug;
    private static OkHttpClient sOkHttpClient;

    public static void init(Application application, boolean debug) {
        try {
            sApiKey = Meta.readApiKey(application);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        sDebug = debug;
        sOkHttpClient = new OkHttpClient();
        if (sDebug) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new OneNetLogger());
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            sOkHttpClient.networkInterceptors().add(loggingInterceptor);
        }
    }

    public static void setApiKey(String apiKey) {
        sApiKey = apiKey;
    }

    public static void registerDevice(String registerCode) {
        Request.Builder requestBuilder = new Request.Builder().url("");
        requestBuilder.method("GET",null);
        Call call = sOkHttpClient.newCall(requestBuilder.build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
            }
        });
    }

    public static void addDevice() {

    }

}
