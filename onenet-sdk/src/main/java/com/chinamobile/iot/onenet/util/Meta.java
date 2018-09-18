package com.chinamobile.iot.onenet.util;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class Meta {

    public static final String META_APIKEY = "com.chinamobile.iot.onenet.APP-KEY";
    public static final String META_SCHEME = "com.chinamobile.iot.onenet.SCHEME";
    public static final String META_HOST = "com.chinamobile.iot.onenet.HOST";

    public static String readAppKey(Application application) throws Exception {
        ApplicationInfo applicationInfo = application.getPackageManager()
                .getApplicationInfo(application.getPackageName(), PackageManager.GET_META_DATA);
        return applicationInfo.metaData.getString(META_APIKEY);
    }

    public static String readScheme(Application application) throws Exception {
        ApplicationInfo applicationInfo = application.getPackageManager()
                .getApplicationInfo(application.getPackageName(), PackageManager.GET_META_DATA);
        return applicationInfo.metaData.getString(META_SCHEME);
    }

    public static String readHost(Application application) throws Exception {
        ApplicationInfo applicationInfo = application.getPackageManager()
                .getApplicationInfo(application.getPackageName(), PackageManager.GET_META_DATA);
        return applicationInfo.metaData.getString(META_HOST);
    }

}
