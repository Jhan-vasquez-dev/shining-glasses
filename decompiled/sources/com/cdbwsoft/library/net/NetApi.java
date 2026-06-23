package com.cdbwsoft.library.net;

import android.text.TextUtils;
import com.cdbwsoft.library.AppConfig;
import com.cdbwsoft.library.BaseApplication;
import com.cdbwsoft.library.net.entity.Response;
import com.cdbwsoft.library.net.entity.ResponseList;
import com.cdbwsoft.library.net.entity.ResponseVo;
import com.cdbwsoft.library.vo.AppVO;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.http.cookie.ClientCookie;

/* JADX INFO: loaded from: classes.dex */
public class NetApi implements Api {
    protected static <T> void attachParams(BaseRequest<T> baseRequest, String[]... strArr) {
        baseRequest.setAttachParams(strArr);
    }

    protected static String[][] parseArrayToParams(List<?> list, String str, String[]... strArr) {
        if (list == null || list.size() == 0) {
            return strArr;
        }
        ArrayList arrayList = new ArrayList();
        if (strArr != null && strArr.length > 0) {
            Collections.addAll(arrayList, strArr);
        }
        Iterator<?> it = list.iterator();
        int i = 0;
        while (it.hasNext()) {
            int i2 = i + 1;
            String[][] toParams = parseToParams(it.next(), str + "[" + i + "][$key]", new String[0][]);
            if (toParams != null && toParams.length >= 2) {
                Collections.addAll(arrayList, toParams);
            }
            i = i2;
        }
        return (String[][]) arrayList.toArray((String[][]) Array.newInstance((Class<?>) String.class, arrayList.size(), 2));
    }

    protected static String[][] parseToParams(Object obj, String[]... strArr) {
        return parseToParams(obj, null, strArr);
    }

    protected static String[][] parseToParams(Object obj, String str, String[]... strArr) {
        String strValueOf;
        if (obj == null) {
            return strArr;
        }
        ArrayList arrayList = new ArrayList();
        if (strArr != null && strArr.length > 0) {
            Collections.addAll(arrayList, strArr);
        }
        for (Class<?> superclass = obj.getClass(); superclass != null && !superclass.isAssignableFrom(Object.class); superclass = superclass.getSuperclass()) {
            for (Field field : superclass.getDeclaredFields()) {
                if ((field.getModifiers() & 8) != 8) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    try {
                        Object obj2 = field.get(obj);
                        if (obj2 != null) {
                            if (obj2 instanceof Date) {
                                strValueOf = String.valueOf(((Date) obj2).getTime());
                            } else {
                                strValueOf = String.valueOf(obj2);
                            }
                            String name = field.getName();
                            if (!TextUtils.isEmpty(str)) {
                                name = str.replace("$key", name);
                            }
                            arrayList.add(new String[]{name, strValueOf});
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return (String[][]) arrayList.toArray((String[][]) Array.newInstance((Class<?>) String.class, arrayList.size(), 2));
    }

    public static <T> void executeListRequest(String str, ResponseListener<ResponseList<T>> responseListener, String[]... strArr) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        ListRequest listRequest = new ListRequest(str, responseListener);
        attachParams(listRequest, strArr);
        BaseApplication.getInstance().getRequestQueue().add(listRequest);
    }

    public static <T> void executeBeanRequest(String str, ResponseListener<ResponseVo<T>> responseListener, String[]... strArr) {
        BeanRequest beanRequest = new BeanRequest(str, responseListener);
        attachParams(beanRequest, strArr);
        BaseApplication.getInstance().getRequestQueue().add(beanRequest);
    }

    public static <T> void executeCommonRequest(String str, ResponseListener<T> responseListener, String[]... strArr) {
        CommonRequest commonRequest = new CommonRequest(str, responseListener);
        attachParams(commonRequest, strArr);
        BaseApplication.getInstance().getRequestQueue().add(commonRequest);
    }

    public static void executeSimpleRequest(String str, ResponseListener<Response> responseListener, String[]... strArr) {
        SimpleRequest simpleRequest = new SimpleRequest(str, responseListener);
        attachParams(simpleRequest, strArr);
        BaseApplication.getInstance().getRequestQueue().add(simpleRequest);
    }

    public static void executeFileRequest(String str, FileListener fileListener, String[]... strArr) {
        FileRequest fileRequest = new FileRequest(str, fileListener);
        attachParams(fileRequest, strArr);
        BaseApplication.getInstance().getRequestQueue().add(fileRequest);
    }

    protected static String getUrl(String str) {
        return AppConfig.SERVER + str;
    }

    public static class App {
        public static <T> void getAdInfo(String[][] strArr, ResponseListener<ResponseVo<T>> responseListener) {
            NetApi.executeBeanRequest(NetApi.getUrl(Api.APP_GETADINFO), responseListener, strArr);
        }

        public static void uploadInfo(String[][] strArr, ResponseListener<Response> responseListener) {
            NetApi.executeSimpleRequest(NetApi.getUrl("app/count"), responseListener, strArr);
        }

        public static <T> void lastUpdate(String str, ResponseListener<ResponseVo<T>> responseListener) {
            NetApi.executeBeanRequest(NetApi.getUrl("app/lastUpdate"), responseListener, new String[]{"app_id", str}, new String[]{"platform", AppConfig.PLATFORM});
        }

        public static <T> void lastOtaUpdate(String str, ResponseListener<ResponseVo<T>> responseListener) {
            NetApi.executeBeanRequest(NetApi.getUrl(Api.APP_LAST_OTA_UPDATE), responseListener, new String[]{"app_id", str});
        }

        public static void uploadLog(String str, int i, String str2, String str3, FileListener fileListener) {
            NetApi.executeFileRequest(NetApi.getUrl(Api.APP_UPLOAD_LOG), fileListener, new String[]{"app_package", str}, new String[]{"app_version_code", String.valueOf(i)}, new String[]{"app_version_name", str2}, new String[]{"unique_id", str3});
        }

        public static void bindDevice(String str, String str2, String str3, String str4, String str5, int i, ResponseListener<Response> responseListener) {
            NetApi.executeSimpleRequest(NetApi.getUrl(Api.APP_BIND_DEVICE), responseListener, new String[]{"device_source", str}, new String[]{"unique_id", str2}, new String[]{"device_brand", str3}, new String[]{"device_model", str4}, new String[]{"device_platform", str5}, new String[]{ClientCookie.VERSION_ATTR, String.valueOf(i)});
        }

        public static void runningLog(String str, String str2, int i, String str3, FileListener fileListener) {
            NetApi.executeFileRequest(NetApi.getUrl(Api.APP_RUNNING_LOG), fileListener, new String[]{"unique_id", str}, new String[]{"app_package", str2}, new String[]{"app_version_code", String.valueOf(i)}, new String[]{"app_version_name", str3});
        }

        public static void appInstalled(String str, List<AppVO> list, ResponseListener<Response> responseListener) {
            NetApi.executeSimpleRequest(NetApi.getUrl(Api.APP_INSTALLED), responseListener, NetApi.parseArrayToParams(list, "apps", new String[]{"unique_id", str}));
        }
    }
}
