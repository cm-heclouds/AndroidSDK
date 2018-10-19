package com.chinamobile.iot.onenet.sdksample;

import android.app.Application;
import android.text.TextUtils;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.http.Config;
import com.chinamobile.iot.onenet.sdksample.utils.Preferences;

import java.util.concurrent.TimeUnit;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化SDK（必须）
        Config config = Config.newBuilder()
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .writeTimeout(60000, TimeUnit.MILLISECONDS)
                .retryCount(2)
                .build();
        OneNetApi.init(this, true, config);

        String savedApiKey = Preferences.getInstance(this).getString(Preferences.API_KEY, null);
        if (!TextUtils.isEmpty(savedApiKey)) {
            OneNetApi.setAppKey(savedApiKey);
        }
    }
}
