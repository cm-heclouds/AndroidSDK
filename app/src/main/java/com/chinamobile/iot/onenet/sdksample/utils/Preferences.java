package com.chinamobile.iot.onenet.sdksample.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.content.SharedPreferencesCompat;

import java.util.Set;

public class Preferences {

    public static final String API_KEY = "api_key";
    
    private static Preferences sPreferences;
    private final SharedPreferences mShareferences;
    private final SharedPreferences.Editor mEditor;
    
    private Preferences(Context context) {
        mShareferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        mEditor = mShareferences.edit();
    }
    
    public static synchronized Preferences getInstance(Context context) {
        if (null == sPreferences) {
            sPreferences = new Preferences(context.getApplicationContext());
        }
        return sPreferences;
    }

    public void putInt(final String key, final int value) {
        mEditor.putInt(key, value);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(mEditor);
    }

    public void putLong(final String key, final long value) {
        mEditor.putLong(key, value);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(mEditor);
    }

    public void putString(final String key, final String value) {
        mEditor.putString(key, value);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(mEditor);
    }

    public void putFloat(final String key, final float value) {
        mEditor.putFloat(key, value);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(mEditor);
    }

    public void putBoolean(final String key, final boolean value) {
        mEditor.putBoolean(key, value);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(mEditor);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void putStringSet(final String key, final Set<String> values) {
        mEditor.putStringSet(key, values);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(mEditor);
    }

    public int getInt(final String key, final int defaultValue) {
        return mShareferences.getInt(key, defaultValue);
    }

    public long getLong(final String key, final long defaultValue) {
        return mShareferences.getLong(key, defaultValue);
    }

    public String getString(final String key, final String defaultValue) {
        return mShareferences.getString(key, defaultValue);
    }

    public float getFloat(final String key, final float defaultValue) {
        return mShareferences.getFloat(key, defaultValue);
    }

    public boolean getBoolean(final String key, final boolean defaultValue) {
        return mShareferences.getBoolean(key, defaultValue);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(final String key) {
        return mShareferences.getStringSet(key, null);
    }

}
