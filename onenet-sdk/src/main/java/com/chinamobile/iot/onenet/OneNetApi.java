package com.chinamobile.iot.onenet;

import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.chinamobile.iot.onenet.http.HttpExecutor;
import com.chinamobile.iot.onenet.http.Urls;
import com.chinamobile.iot.onenet.util.Assertions;
import com.chinamobile.iot.onenet.util.Meta;
import com.chinamobile.iot.onenet.util.OneNetLogger;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OneNetApi {

    public static final String LOG_TAG = "OneNetApi";

    private static String sApiKey;
    static boolean sDebug;
    private static HttpExecutor sHttpExecutor;

    public static void init(Application application, boolean debug) {
        try {
            sApiKey = Meta.readApiKey(application);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        sDebug = debug;
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        if (sDebug) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new OneNetLogger());
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addNetworkInterceptor(loggingInterceptor);
        }
        okHttpClientBuilder.addInterceptor(sApiKeyInterceptor);
        sHttpExecutor = new HttpExecutor(okHttpClientBuilder.build());
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

    private static boolean isInitialized() {
        return sHttpExecutor != null;
    }

    private static void assertInitialized() {
        Assertions.assertCondition(isInitialized(), "You should call OneNetApi.init() in your Application!");
    }

    public static void registerDevice(String registerCode, String requestBodyString, OneNetApiCallback callback) {
        assertInitialized();
        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme(Urls.SCHEME).host(Urls.HOST).addPathSegment("register_de")
                .addQueryParameter("register_code", registerCode);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBodyString);
        sHttpExecutor.post(builder.toString(), requestBody, new OneNetApiCallbackAdapter(callback));
    }

    public static void addDevice(String requestBodyString, OneNetApiCallback callback) {
        assertInitialized();
        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme(Urls.SCHEME).host(Urls.HOST).addPathSegment("devices");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBodyString);
        sHttpExecutor.post(builder.toString(), requestBody, new OneNetApiCallbackAdapter(callback));
    }

    public static void fuzzyQueryDevices(Map<String, String> params, OneNetApiCallback callback) {
        assertInitialized();
        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme(Urls.SCHEME).host(Urls.HOST).addPathSegment("devices");
        if (params != null) {
            Iterator iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                builder.addQueryParameter(key, value);
            }
        }
        sHttpExecutor.get(builder.toString(), new OneNetApiCallbackAdapter(callback));
    }

}
