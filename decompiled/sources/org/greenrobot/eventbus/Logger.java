package org.greenrobot.eventbus;

import java.util.logging.Level;
import org.greenrobot.eventbus.android.AndroidComponents;

/* JADX INFO: loaded from: classes2.dex */
public interface Logger {
    void log(Level level, String str);

    void log(Level level, String str, Throwable th);

    public static class JavaLogger implements Logger {
        protected final java.util.logging.Logger logger;

        public JavaLogger(String str) {
            this.logger = java.util.logging.Logger.getLogger(str);
        }

        @Override // org.greenrobot.eventbus.Logger
        public void log(Level level, String str) {
            this.logger.log(level, str);
        }

        @Override // org.greenrobot.eventbus.Logger
        public void log(Level level, String str, Throwable th) {
            this.logger.log(level, str, th);
        }
    }

    public static class SystemOutLogger implements Logger {
        @Override // org.greenrobot.eventbus.Logger
        public void log(Level level, String str) {
            System.out.println("[" + level + "] " + str);
        }

        @Override // org.greenrobot.eventbus.Logger
        public void log(Level level, String str, Throwable th) {
            System.out.println("[" + level + "] " + str);
            th.printStackTrace(System.out);
        }
    }

    public static class Default {
        public static Logger get() {
            if (AndroidComponents.areAvailable()) {
                return AndroidComponents.get().logger;
            }
            return new SystemOutLogger();
        }
    }
}
