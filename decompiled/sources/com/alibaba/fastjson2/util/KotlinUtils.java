package com.alibaba.fastjson2.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class KotlinUtils {
    public static final int STATE;
    private static volatile Map<Class, String[]> kotlinIgnores;
    private static volatile boolean kotlinIgnores_error;
    private static volatile boolean kotlin_class_klass_error;
    private static volatile boolean kotlin_error;
    private static volatile Constructor kotlin_kclass_constructor;
    private static volatile Method kotlin_kclass_getConstructors;
    private static volatile Method kotlin_kfunction_getParameters;
    private static volatile Method kotlin_kparameter_getName;
    private static volatile Class kotlin_metadata;
    private static volatile boolean kotlin_metadata_error;

    static {
        int i = 0;
        try {
            Class.forName("kotlin.Metadata");
            i = 1;
            Class.forName("kotlin.reflect.jvm.ReflectJvmMapping");
            i = 2;
        } catch (Throwable unused) {
        }
        STATE = i;
    }

    private KotlinUtils() {
        throw new IllegalStateException();
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0034  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void getConstructor(java.lang.Class<?> r13, com.alibaba.fastjson2.codec.BeanInfo r14) {
        /*
            java.lang.String[] r0 = r14.createParameterNames
            java.lang.reflect.Constructor[] r1 = com.alibaba.fastjson2.util.BeanUtils.getConstructor(r13)
            int r2 = r1.length
            r3 = 0
            r4 = 0
            r5 = r3
            r6 = r5
            r7 = r4
        Lc:
            r8 = 2
            if (r5 >= r2) goto L3d
            r9 = r1[r5]
            int r10 = r9.getParameterCount()
            if (r0 == 0) goto L1b
            int r11 = r0.length
            if (r10 == r11) goto L1b
            goto L3a
        L1b:
            if (r10 <= r8) goto L34
            java.lang.Class[] r8 = r9.getParameterTypes()
            int r11 = r10 + (-2)
            r11 = r8[r11]
            java.lang.Class r12 = java.lang.Integer.TYPE
            if (r11 != r12) goto L34
            int r11 = r10 + (-1)
            r8 = r8[r11]
            java.lang.Class<kotlin.jvm.internal.DefaultConstructorMarker> r11 = kotlin.jvm.internal.DefaultConstructorMarker.class
            if (r8 != r11) goto L34
            r14.markerConstructor = r9
            goto L3a
        L34:
            if (r7 == 0) goto L38
            if (r6 >= r10) goto L3a
        L38:
            r7 = r9
            r6 = r10
        L3a:
            int r5 = r5 + 1
            goto Lc
        L3d:
            if (r6 == 0) goto L84
            int r0 = com.alibaba.fastjson2.util.KotlinUtils.STATE
            if (r0 != r8) goto L84
            kotlin.reflect.KClass r13 = kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(r13)     // Catch: java.lang.Throwable -> L84
            java.util.Collection r13 = r13.getConstructors()     // Catch: java.lang.Throwable -> L84
            java.util.Iterator r13 = r13.iterator()     // Catch: java.lang.Throwable -> L84
        L4f:
            boolean r0 = r13.hasNext()     // Catch: java.lang.Throwable -> L84
            if (r0 == 0) goto L69
            java.lang.Object r0 = r13.next()     // Catch: java.lang.Throwable -> L84
            kotlin.reflect.KFunction r0 = (kotlin.reflect.KFunction) r0     // Catch: java.lang.Throwable -> L84
            java.util.List r0 = r0.getParameters()     // Catch: java.lang.Throwable -> L84
            if (r4 == 0) goto L67
            int r1 = r0.size()     // Catch: java.lang.Throwable -> L84
            if (r6 != r1) goto L4f
        L67:
            r4 = r0
            goto L4f
        L69:
            if (r4 == 0) goto L84
            int r13 = r4.size()     // Catch: java.lang.Throwable -> L84
            java.lang.String[] r0 = new java.lang.String[r13]     // Catch: java.lang.Throwable -> L84
        L71:
            if (r3 >= r13) goto L82
            java.lang.Object r1 = r4.get(r3)     // Catch: java.lang.Throwable -> L84
            kotlin.reflect.KParameter r1 = (kotlin.reflect.KParameter) r1     // Catch: java.lang.Throwable -> L84
            java.lang.String r1 = r1.getName()     // Catch: java.lang.Throwable -> L84
            r0[r3] = r1     // Catch: java.lang.Throwable -> L84
            int r3 = r3 + 1
            goto L71
        L82:
            r14.createParameterNames = r0     // Catch: java.lang.Throwable -> L84
        L84:
            r14.creatorConstructor = r7
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.KotlinUtils.getConstructor(java.lang.Class, com.alibaba.fastjson2.codec.BeanInfo):void");
    }

    public static boolean isKotlin(Class cls) {
        if (kotlin_metadata == null && !kotlin_metadata_error) {
            try {
                kotlin_metadata = Class.forName("kotlin.Metadata");
            } catch (Throwable unused) {
                kotlin_metadata_error = true;
            }
        }
        return kotlin_metadata != null && cls.isAnnotationPresent(kotlin_metadata);
    }

    public static Constructor getKotlinConstructor(Constructor[] constructorArr) {
        return getKotlinConstructor(constructorArr, null);
    }

    public static Constructor getKotlinConstructor(Constructor[] constructorArr, String[] strArr) {
        Constructor constructor = null;
        for (Constructor constructor2 : constructorArr) {
            Class<?>[] parameterTypes = constructor2.getParameterTypes();
            if ((strArr == null || parameterTypes.length == strArr.length) && ((parameterTypes.length <= 0 || !"kotlin.jvm.internal.DefaultConstructorMarker".equals(parameterTypes[parameterTypes.length - 1].getName())) && (constructor == null || constructor.getParameterTypes().length < parameterTypes.length))) {
                constructor = constructor2;
            }
        }
        return constructor;
    }

    public static String[] getKoltinConstructorParameters(Class cls) {
        if (kotlin_kclass_constructor == null && !kotlin_class_klass_error) {
            try {
                kotlin_kclass_constructor = Class.forName("kotlin.reflect.jvm.internal.KClassImpl").getConstructor(Class.class);
            } catch (Throwable unused) {
                kotlin_class_klass_error = true;
            }
        }
        if (kotlin_kclass_constructor == null) {
            return null;
        }
        if (kotlin_kclass_getConstructors == null && !kotlin_class_klass_error) {
            try {
                kotlin_kclass_getConstructors = Class.forName("kotlin.reflect.jvm.internal.KClassImpl").getMethod("getConstructors", new Class[0]);
            } catch (Throwable unused2) {
                kotlin_class_klass_error = true;
            }
        }
        if (kotlin_kfunction_getParameters == null && !kotlin_class_klass_error) {
            try {
                kotlin_kfunction_getParameters = Class.forName("kotlin.reflect.KFunction").getMethod("getParameters", new Class[0]);
            } catch (Throwable unused3) {
                kotlin_class_klass_error = true;
            }
        }
        if (kotlin_kparameter_getName == null && !kotlin_class_klass_error) {
            try {
                kotlin_kparameter_getName = Class.forName("kotlin.reflect.KParameter").getMethod("getName", new Class[0]);
            } catch (Throwable unused4) {
                kotlin_class_klass_error = true;
            }
        }
        if (kotlin_error) {
            return null;
        }
        try {
            Object obj = null;
            for (Object obj2 : (Iterable) kotlin_kclass_getConstructors.invoke(kotlin_kclass_constructor.newInstance(cls), new Object[0])) {
                List list = (List) kotlin_kfunction_getParameters.invoke(obj2, new Object[0]);
                if (obj == null || list.size() != 0) {
                    obj = obj2;
                }
            }
            if (obj == null) {
                return null;
            }
            List list2 = (List) kotlin_kfunction_getParameters.invoke(obj, new Object[0]);
            String[] strArr = new String[list2.size()];
            for (int i = 0; i < list2.size(); i++) {
                strArr[i] = (String) kotlin_kparameter_getName.invoke(list2.get(i), new Object[0]);
            }
            return strArr;
        } catch (Throwable th) {
            th.printStackTrace();
            kotlin_error = true;
            return null;
        }
    }

    public static boolean isKotlinIgnore(Class cls, String str) {
        if (kotlinIgnores == null && !kotlinIgnores_error) {
            try {
                HashMap map = new HashMap();
                map.put(Class.forName("kotlin.ranges.CharRange"), new String[]{"getEndInclusive", "isEmpty"});
                map.put(Class.forName("kotlin.ranges.IntRange"), new String[]{"getEndInclusive", "isEmpty"});
                map.put(Class.forName("kotlin.ranges.LongRange"), new String[]{"getEndInclusive", "isEmpty"});
                map.put(Class.forName("kotlin.ranges.ClosedFloatRange"), new String[]{"getEndInclusive", "isEmpty"});
                map.put(Class.forName("kotlin.ranges.ClosedDoubleRange"), new String[]{"getEndInclusive", "isEmpty"});
                kotlinIgnores = map;
            } catch (Throwable unused) {
                kotlinIgnores_error = true;
            }
        }
        if (kotlinIgnores == null) {
            return false;
        }
        String[] strArr = kotlinIgnores.get(cls);
        return strArr != null && Arrays.binarySearch(strArr, str) >= 0;
    }
}
