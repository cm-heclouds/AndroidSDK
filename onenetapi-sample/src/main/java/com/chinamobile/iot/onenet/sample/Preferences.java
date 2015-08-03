package com.chinamobile.iot.onenet.sample;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    // Preferences file name
    private static final String PREFERENCES_FILE = "one_net_api_sample_pref";

    private static Preferences sPreferences;

    private final SharedPreferences mShareferences;
    private final SharedPreferences.Editor mEditor;

    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    private Preferences(Context context) {
        mShareferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        mEditor = mShareferences.edit();
    }

    public static synchronized Preferences getInstance(Context context) {
        if (null == sPreferences) {
            sPreferences = new Preferences(context);
        }
        return sPreferences;
    }

    public void setUsername(String username) {
        mEditor.putString(KEY_USERNAME, username);
        mEditor.commit();
    }

    public String getUsername() {
        return mShareferences.getString(KEY_USERNAME, "");
    }

    public void setPassword(String password) {
        mEditor.putString(KEY_PASSWORD, password);
        mEditor.commit();
    }

    public String getPassword() {
        return mShareferences.getString(KEY_PASSWORD, "");
    }
}
