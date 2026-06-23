package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.http.client.methods.HttpTrace;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/* JADX INFO: loaded from: classes.dex */
public class Log4JLogger implements Log, Serializable {
    private static final String FQCN;
    static /* synthetic */ Class class$org$apache$commons$logging$impl$Log4JLogger = null;
    static /* synthetic */ Class class$org$apache$log4j$Level = null;
    static /* synthetic */ Class class$org$apache$log4j$Priority = null;
    private static final long serialVersionUID = 5160705895411730424L;
    private static final Priority traceLevel;
    private volatile transient Logger logger;
    private final String name;

    static {
        Priority priority;
        Class clsClass$ = class$org$apache$commons$logging$impl$Log4JLogger;
        if (clsClass$ == null) {
            clsClass$ = class$("org.apache.commons.logging.impl.Log4JLogger");
            class$org$apache$commons$logging$impl$Log4JLogger = clsClass$;
        }
        FQCN = clsClass$.getName();
        Class clsClass$2 = class$org$apache$log4j$Priority;
        if (clsClass$2 == null) {
            clsClass$2 = class$("org.apache.log4j.Priority");
            class$org$apache$log4j$Priority = clsClass$2;
        }
        Class<?> clsClass$3 = class$org$apache$log4j$Level;
        if (clsClass$3 == null) {
            clsClass$3 = class$("org.apache.log4j.Level");
            class$org$apache$log4j$Level = clsClass$3;
        }
        if (!clsClass$2.isAssignableFrom(clsClass$3)) {
            throw new InstantiationError("Log4J 1.2 not available");
        }
        try {
            Class clsClass$4 = class$org$apache$log4j$Level;
            if (clsClass$4 == null) {
                clsClass$4 = class$("org.apache.log4j.Level");
                class$org$apache$log4j$Level = clsClass$4;
            }
            priority = (Priority) clsClass$4.getDeclaredField(HttpTrace.METHOD_NAME).get(null);
        } catch (Exception unused) {
            priority = Level.DEBUG;
        }
        traceLevel = priority;
    }

    static /* synthetic */ Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    public Log4JLogger() {
        this.logger = null;
        this.name = null;
    }

    public Log4JLogger(String str) {
        this.logger = null;
        this.name = str;
        this.logger = getLogger();
    }

    public Log4JLogger(Logger logger) {
        this.logger = null;
        if (logger == null) {
            throw new IllegalArgumentException("Warning - null logger in constructor; possible log4j misconfiguration.");
        }
        this.name = logger.getName();
        this.logger = logger;
    }

    @Override // org.apache.commons.logging.Log
    public void trace(Object obj) {
        getLogger().log(FQCN, traceLevel, obj, (Throwable) null);
    }

    @Override // org.apache.commons.logging.Log
    public void trace(Object obj, Throwable th) {
        getLogger().log(FQCN, traceLevel, obj, th);
    }

    @Override // org.apache.commons.logging.Log
    public void debug(Object obj) {
        getLogger().log(FQCN, Level.DEBUG, obj, (Throwable) null);
    }

    @Override // org.apache.commons.logging.Log
    public void debug(Object obj, Throwable th) {
        getLogger().log(FQCN, Level.DEBUG, obj, th);
    }

    @Override // org.apache.commons.logging.Log
    public void info(Object obj) {
        getLogger().log(FQCN, Level.INFO, obj, (Throwable) null);
    }

    @Override // org.apache.commons.logging.Log
    public void info(Object obj, Throwable th) {
        getLogger().log(FQCN, Level.INFO, obj, th);
    }

    @Override // org.apache.commons.logging.Log
    public void warn(Object obj) {
        getLogger().log(FQCN, Level.WARN, obj, (Throwable) null);
    }

    @Override // org.apache.commons.logging.Log
    public void warn(Object obj, Throwable th) {
        getLogger().log(FQCN, Level.WARN, obj, th);
    }

    @Override // org.apache.commons.logging.Log
    public void error(Object obj) {
        getLogger().log(FQCN, Level.ERROR, obj, (Throwable) null);
    }

    @Override // org.apache.commons.logging.Log
    public void error(Object obj, Throwable th) {
        getLogger().log(FQCN, Level.ERROR, obj, th);
    }

    @Override // org.apache.commons.logging.Log
    public void fatal(Object obj) {
        getLogger().log(FQCN, Level.FATAL, obj, (Throwable) null);
    }

    @Override // org.apache.commons.logging.Log
    public void fatal(Object obj, Throwable th) {
        getLogger().log(FQCN, Level.FATAL, obj, th);
    }

    public Logger getLogger() {
        Logger logger;
        Logger logger2 = this.logger;
        if (logger2 != null) {
            return logger2;
        }
        synchronized (this) {
            logger = this.logger;
            if (logger == null) {
                logger = Logger.getLogger(this.name);
                this.logger = logger;
            }
        }
        return logger;
    }

    @Override // org.apache.commons.logging.Log
    public boolean isDebugEnabled() {
        return getLogger().isDebugEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isErrorEnabled() {
        return getLogger().isEnabledFor(Level.ERROR);
    }

    @Override // org.apache.commons.logging.Log
    public boolean isFatalEnabled() {
        return getLogger().isEnabledFor(Level.FATAL);
    }

    @Override // org.apache.commons.logging.Log
    public boolean isInfoEnabled() {
        return getLogger().isInfoEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isTraceEnabled() {
        return getLogger().isEnabledFor(traceLevel);
    }

    @Override // org.apache.commons.logging.Log
    public boolean isWarnEnabled() {
        return getLogger().isEnabledFor(Level.WARN);
    }
}
