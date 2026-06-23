package com.cdbwsoft.library;

import android.content.Context;
import android.os.Environment;
import androidx.core.app.NotificationCompat;
import com.cdbwsoft.library.net.entity.BaseResponseFactory;
import com.cdbwsoft.library.net.entity.Response;
import com.cdbwsoft.library.net.entity.ResponseFactory;
import java.io.File;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class AppConfig {
    public static String CACHE_DAILY = "yyyy-MM-dd";
    public static final String CACHE_FILE = "cache";
    public static String CACHE_PATH = null;
    public static String CACHE_TIMELY = "HH:mm:ss";
    public static boolean DEBUG = false;
    public static String LOG_EXT = ".log";
    public static String LOG_FILE_NAME = "yyyy-MM-dd";
    public static String LOG_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String META_CHANNEL = "HEATON_CHANNEL";
    public static String PATH_LOGS = null;
    public static String PATH_NAME = "heaton";
    public static String PATH_ROOT = null;
    public static final String PLATFORM = "Android";
    public static String SERVER = "http://api.e-toys.cn/api/";
    public static final String SETTING_FILE = "setting";
    public static final String UUID_BLE_CHARACTERISTIC_TEXT = "d44bc439-abfd-45a2-b575-925416129600";
    public static final String UUID_BLE_DESCRIPTOR_TEXT = "00002902-0000-1000-8000-00805f9b34fb";
    public static final String UUID_BLE_SERVICE_TEXT = "0000fee9-0000-1000-8000-00805f9b34fb";
    public static final String UUID_INSECURE_TEXT = "00001101-0000-1000-8000-00805F9B34FB";
    public static final String UUID_SECURE_TEXT = "00001101-0000-1000-8000-00805F9B34FB";
    public static int OTA_DIALOG_LAYOUT = R.layout.update_layout;
    public static int UPDATE_DIALOG_LAYOUT = R.layout.update_layout;
    public static int UPDATE_DIALOG_THEME = 0;
    public static int UPDATE_DIALOG_TITLE = R.string.update_title;
    public static int UPDATE_DIALOG_CANCEL = R.string.update_cancel;
    public static int UPDATE_DIALOG_SURE = R.string.update_sure;
    public static int UPDATE_DIALOG_AFTER = R.string.update_after;
    public static int UPDATE_DIALOG_RETRY = R.string.update_retry;
    public static int UPDATE_DIALOG_URL_INVALID = R.string.download_url_invalid;
    public static int UPDATE_DIALOG_WRITE_PERMISSION = R.string.need_write_permission;
    public static int UPDATE_DIALOG_NOT_AMOUNT = R.string.not_mount;
    public static int UPDATE_DIALOG_NO_PERMISSION = R.string.no_permission;
    public static int UPDATE_DIALOG_DOWNLOAD_ERROR = R.string.download_error;
    public static int UPDATE_DIALOG_TOTAL_SIZE = R.string.total_size;
    public static int MAX_CONNECTED_DEVICE = 6;
    public static boolean CENTER_SINGLE_BUTTON = false;
    public static boolean DIALOG_BUTTON_REVERSAL = false;
    public static String RESPONSE_CODE_KEY = NotificationCompat.CATEGORY_STATUS;
    public static int RESPONSE_SUCCESS_CODE = 0;
    public static String RESPONSE_DATA_KEY = "data";
    public static String RESPONSE_MSG_KEY = NotificationCompat.CATEGORY_MESSAGE;
    public static final UUID UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static final UUID UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static Class<?> RESPONSE_CLASS = Response.class;
    public static BaseResponseFactory<?> RESPONSE_FACTORY = new ResponseFactory();
    public static boolean BIND_DEVICE = true;
    public static boolean UPLOAD_ERROR_LOGS = true;
    public static boolean UPLOAD_RUNNING_LOGS = true;
    public static boolean UPLOAD_APP_INSTALLS = true;

    public static void init(Context context) {
        PATH_ROOT = Environment.getExternalStorageDirectory().getPath() + File.separator + PATH_NAME + File.separator;
        PATH_LOGS = PATH_ROOT + context.getPackageName() + File.separator + "logs" + File.separator;
        CACHE_PATH = PATH_ROOT + context.getPackageName() + File.separator + CACHE_FILE + File.separator;
    }
}
