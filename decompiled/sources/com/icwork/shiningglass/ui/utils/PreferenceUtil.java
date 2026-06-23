package com.icwork.shiningglass.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/* JADX INFO: loaded from: classes.dex */
public class PreferenceUtil {
    private static SharedPreferences.Editor mEditor;
    private static SharedPreferences mSharedPreferences;

    public static void init(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public static void removeKey(String str) {
        SharedPreferences.Editor editorEdit = mSharedPreferences.edit();
        mEditor = editorEdit;
        editorEdit.remove(str);
        mEditor.commit();
    }

    public static void removeAll() {
        SharedPreferences.Editor editorEdit = mSharedPreferences.edit();
        mEditor = editorEdit;
        editorEdit.clear();
        mEditor.commit();
    }

    public static void commitString(String str, String str2) {
        SharedPreferences.Editor editorEdit = mSharedPreferences.edit();
        mEditor = editorEdit;
        editorEdit.putString(str, str2);
        mEditor.commit();
    }

    public static String getString(String str, String str2) {
        return mSharedPreferences.getString(str, str2);
    }

    public static void commitInt(String str, int i) {
        SharedPreferences.Editor editorEdit = mSharedPreferences.edit();
        mEditor = editorEdit;
        editorEdit.putInt(str, i);
        mEditor.commit();
    }

    public static int getInt(String str, int i) {
        return mSharedPreferences.getInt(str, i);
    }

    public static void commitLong(String str, long j) {
        SharedPreferences.Editor editorEdit = mSharedPreferences.edit();
        mEditor = editorEdit;
        editorEdit.putLong(str, j);
        mEditor.commit();
    }

    public static long getLong(String str, long j) {
        return mSharedPreferences.getLong(str, j);
    }

    public static void commitBoolean(String str, boolean z) {
        SharedPreferences.Editor editorEdit = mSharedPreferences.edit();
        mEditor = editorEdit;
        editorEdit.putBoolean(str, z);
        mEditor.commit();
    }

    public static Boolean getBoolean(String str, boolean z) {
        try {
            return Boolean.valueOf(mSharedPreferences.getBoolean(str, z));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
