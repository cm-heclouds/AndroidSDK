package com.chinamobile.iot.onenet.sample;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class Utils {

    public static boolean isNetworkAvaliable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public static boolean checkNetwork(final Context context) {
        if (!isNetworkAvaliable(context)) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
                }
            });
            return false;
        }
        return true;
    }

}
