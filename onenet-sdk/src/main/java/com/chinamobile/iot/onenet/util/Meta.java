package com.chinamobile.iot.onenet.util;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class Meta {

    public static final String META_APIKEY = "api-key";

    public static String readApiKey(Application application) throws NameNotFoundException {
        ApplicationInfo applicationInfo = application.getPackageManager()
                .getApplicationInfo(application.getPackageName(), PackageManager.GET_META_DATA);
        return applicationInfo.metaData.getString(META_APIKEY);
    }

}
