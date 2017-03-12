package com.chinamobile.iot.onenet;

import android.app.Application;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.chinamobile.iot.onenet.http.HttpExecutor;
import com.chinamobile.iot.onenet.util.Meta;
import com.chinamobile.iot.onenet.util.OneNetLogger;
import com.chinamobile.iot.onenet.util.RequestBodyBuilder;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class OneNetApi {

    public static final String LOG_TAG = "OneNetApi";

    private static String sApiKey;
    private static boolean sDebug;
    private static HttpExecutor sHttpExecutor;

    public static void init(Application application, boolean debug) {
        try {
            sApiKey = Meta.readApiKey(application);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        sDebug = debug;
        OkHttpClient okHttpClient = new OkHttpClient();
        if (sDebug) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new OneNetLogger());
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient.networkInterceptors().add(loggingInterceptor);
        }
        okHttpClient.interceptors().add(sApiKeyInterceptor);
        sHttpExecutor = new HttpExecutor(okHttpClient);
    }

    private static Interceptor sApiKeyInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("api-key", sApiKey);
            if (TextUtils.isEmpty(sApiKey)) {
                Log.e(LOG_TAG, "api-key is messing, please config in the meta-data or call setApiKey()");
            }
            return chain.proceed(builder.build());
        }
    };

    public static void setApiKey(String apiKey) {
        sApiKey = apiKey;
    }

    public static void registerDevice(String registerCode, String body) {
        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme("http").host("api.heclouds.com").addPathSegment("register_de")
                .addQueryParameter("register_code", registerCode);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body);
        sHttpExecutor.post(builder.toString(), requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    public static void addDevice() {

    }

}
