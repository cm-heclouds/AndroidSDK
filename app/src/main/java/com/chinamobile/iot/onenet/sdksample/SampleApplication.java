package com.chinamobile.iot.onenet.sdksample;

import android.app.Application;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.sdksample.utils.Preferences;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化SDK（必须）
        OneNetApi.init(this, true);

        String savedApiKey = Preferences.getInstance(this).getString(Preferences.API_KEY, null);
        if (savedApiKey != null) {
            OneNetApi.setAppKey(savedApiKey);
        }
    }
}
