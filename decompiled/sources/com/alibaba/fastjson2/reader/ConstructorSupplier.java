package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.function.Supplier;

/* JADX INFO: loaded from: classes.dex */
final class ConstructorSupplier implements Supplier {
    final Constructor constructor;
    final Class objectClass;
    final boolean useClassNewInstance;

    public ConstructorSupplier(Constructor constructor) {
        constructor.setAccessible(true);
        this.constructor = constructor;
        Class declaringClass = constructor.getDeclaringClass();
        this.objectClass = declaringClass;
        this.useClassNewInstance = constructor.getParameterCount() == 0 && Modifier.isPublic(constructor.getModifiers()) && Modifier.isPublic(declaringClass.getModifiers());
    }

    @Override // java.util.function.Supplier
    public Object get() {
        try {
            if (this.useClassNewInstance) {
                return this.objectClass.newInstance();
            }
            if (this.constructor.getParameterCount() == 1) {
                return this.constructor.newInstance(new Object[1]);
            }
            return this.constructor.newInstance(new Object[0]);
        } catch (Throwable th) {
            throw new JSONException("create instance error", th);
        }
    }
}
