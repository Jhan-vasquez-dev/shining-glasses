package org.apache.commons.logging.impl;

import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.LogFactory;

/* JADX INFO: loaded from: classes.dex */
public class ServletContextCleaner implements ServletContextListener {
    private static final Class[] RELEASE_SIGNATURE;
    static /* synthetic */ Class class$java$lang$ClassLoader;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
    }

    static {
        Class[] clsArr = new Class[1];
        Class clsClass$ = class$java$lang$ClassLoader;
        if (clsClass$ == null) {
            clsClass$ = class$("java.lang.ClassLoader");
            class$java$lang$ClassLoader = clsClass$;
        }
        clsArr[0] = clsClass$;
        RELEASE_SIGNATURE = clsArr;
    }

    static /* synthetic */ Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Object[] objArr = {contextClassLoader};
        ClassLoader parent = contextClassLoader;
        while (parent != null) {
            try {
                Class<?> clsLoadClass = parent.loadClass(LogFactory.FACTORY_PROPERTY);
                clsLoadClass.getMethod("release", RELEASE_SIGNATURE).invoke(null, objArr);
                parent = clsLoadClass.getClassLoader().getParent();
            } catch (ClassNotFoundException unused) {
                parent = null;
            } catch (IllegalAccessException unused2) {
                System.err.println("LogFactory instance found which is not accessable!");
                parent = null;
            } catch (NoSuchMethodException unused3) {
                System.err.println("LogFactory instance found which does not support release method!");
                parent = null;
            } catch (InvocationTargetException unused4) {
                System.err.println("LogFactory instance release method failed!");
                parent = null;
            }
        }
        LogFactory.release(contextClassLoader);
    }
}
