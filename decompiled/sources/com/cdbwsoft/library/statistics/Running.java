package com.cdbwsoft.library.statistics;

import com.cdbwsoft.library.AppConfig;
import com.cdbwsoft.library.cache.CacheManager;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class Running implements Runnable {
    public static final int ACTION_ACTIVE_APP = 4;
    public static final int ACTION_APP_ENTER = 1;
    public static final int ACTION_APP_EXIT = 2;
    public static final int ACTION_CONNECT_DEVICE = 3;
    public static final int ACTION_LOGIN_APP = 5;
    private static final String ACTIVITY_FILE;
    private static final String ACTIVITY_FILE_NAME = "$date$.log";
    private static final String ACTIVITY_PATH;

    @Override // java.lang.Runnable
    public void run() {
    }

    static {
        String str = "running" + File.separator;
        ACTIVITY_PATH = str;
        ACTIVITY_FILE = str + ACTIVITY_FILE_NAME;
    }

    public static void app(int i, String str, String str2) {
        CacheManager.saveCacheDaily(ACTIVITY_FILE, System.currentTimeMillis() + "," + str + "," + i + "," + str2 + "\n", true);
    }

    public static List<File> getFiles() {
        File[] fileArrListFiles;
        try {
            File file = new File(AppConfig.CACHE_PATH, ACTIVITY_PATH);
            if (file.exists() && file.isDirectory() && (fileArrListFiles = file.listFiles(new FileFilter() { // from class: com.cdbwsoft.library.statistics.Running.1
                @Override // java.io.FileFilter
                public boolean accept(File file2) {
                    Calendar calendar = Calendar.getInstance();
                    int i = calendar.get(6);
                    calendar.setTimeInMillis(file2.lastModified());
                    return i != calendar.get(6);
                }
            })) != null) {
                return Arrays.asList(fileArrListFiles);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
