package com.cdbwsoft.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class SharedPreferenceUtils {
    public static final String FILE_NAME = "common";
    protected Context mContext;
    protected String mFileName;

    public SharedPreferenceUtils(Context context, String str) {
        this.mContext = context;
        this.mFileName = str;
    }

    public <T> void put(String str, T t) {
        put(this.mContext, str, t, this.mFileName);
    }

    public static <T> void put(Context context, String str, T t) {
        put(context, str, t, FILE_NAME);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> void put(Context context, String str, T t, String str2) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences(str2, 0).edit();
        if (t instanceof String) {
            editorEdit.putString(str, (String) t);
        } else if (t instanceof Integer) {
            editorEdit.putInt(str, ((Integer) t).intValue());
        } else if (t instanceof Boolean) {
            editorEdit.putBoolean(str, ((Boolean) t).booleanValue());
        } else if (t instanceof Float) {
            editorEdit.putFloat(str, ((Float) t).floatValue());
        } else if (t instanceof Long) {
            editorEdit.putLong(str, ((Long) t).longValue());
        } else {
            editorEdit.putString(str, t.toString());
        }
        editorEdit.apply();
    }

    public void put(String str, Set<String> set) {
        put(this.mContext, str, set, this.mFileName);
    }

    public static void put(Context context, String str, Set<String> set) {
        put(context, str, set, FILE_NAME);
    }

    public static void put(Context context, String str, Set<String> set, String str2) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences(str2, 0).edit();
        editorEdit.putStringSet(str, set);
        editorEdit.apply();
    }

    public String get(String str) {
        return (String) get(str, (Object) null);
    }

    public static String get(Context context, String str) {
        return (String) get(context, str, null);
    }

    public <T> T get(String str, T t) {
        return (T) get(this.mContext, str, t, this.mFileName);
    }

    public static <T> T get(Context context, String str, T t) {
        return (T) get(context, str, t, FILE_NAME);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T get(Context context, String str, T t, String str2) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(str2, 0);
        if (t == 0 || (t instanceof String)) {
            return (T) sharedPreferences.getString(str, (String) t);
        }
        if (t instanceof Integer) {
            return (T) Integer.valueOf(sharedPreferences.getInt(str, ((Integer) t).intValue()));
        }
        if (t instanceof Boolean) {
            return (T) Boolean.valueOf(sharedPreferences.getBoolean(str, ((Boolean) t).booleanValue()));
        }
        if (t instanceof Float) {
            return (T) Float.valueOf(sharedPreferences.getFloat(str, ((Float) t).floatValue()));
        }
        if (t instanceof Long) {
            return (T) Long.valueOf(sharedPreferences.getLong(str, ((Long) t).longValue()));
        }
        if (t instanceof Set) {
            return (T) sharedPreferences.getStringSet(str, (Set) t);
        }
        return null;
    }

    public void remove(String str) {
        remove(this.mContext, str, this.mFileName);
    }

    public static void remove(Context context, String str) {
        remove(context, str, FILE_NAME);
    }

    public static void remove(Context context, String str, String str2) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences(str2, 0).edit();
        editorEdit.remove(str);
        editorEdit.apply();
    }

    public void clear() {
        clear(this.mContext, this.mFileName);
    }

    public static void clear(Context context) {
        clear(context, FILE_NAME);
    }

    public static void clear(Context context, String str) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences(str, 0).edit();
        editorEdit.clear();
        editorEdit.apply();
    }

    public boolean contains(String str) {
        return contains(this.mContext, str, this.mFileName);
    }

    public static boolean contains(Context context, String str) {
        return contains(context, str, FILE_NAME);
    }

    public static boolean contains(Context context, String str, String str2) {
        return context.getSharedPreferences(str2, 0).contains(str);
    }

    public Map<String, ?> getAll() {
        return getAll(this.mContext, this.mFileName);
    }

    public static Map<String, ?> getAll(Context context) {
        return getAll(context, FILE_NAME);
    }

    public static Map<String, ?> getAll(Context context, String str) {
        return context.getSharedPreferences(str, 0).getAll();
    }
}
