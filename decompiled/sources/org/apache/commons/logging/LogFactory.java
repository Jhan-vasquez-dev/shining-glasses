package org.apache.commons.logging;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

/* JADX INFO: loaded from: classes.dex */
public abstract class LogFactory {
    public static final String DIAGNOSTICS_DEST_PROPERTY = "org.apache.commons.logging.diagnostics.dest";
    public static final String FACTORY_DEFAULT = "org.apache.commons.logging.impl.LogFactoryImpl";
    public static final String FACTORY_PROPERTIES = "commons-logging.properties";
    public static final String FACTORY_PROPERTY = "org.apache.commons.logging.LogFactory";
    public static final String HASHTABLE_IMPLEMENTATION_PROPERTY = "org.apache.commons.logging.LogFactory.HashtableImpl";
    public static final String PRIORITY_KEY = "priority";
    protected static final String SERVICE_ID = "META-INF/services/org.apache.commons.logging.LogFactory";
    public static final String TCCL_KEY = "use_tccl";
    private static final String WEAK_HASHTABLE_CLASSNAME = "org.apache.commons.logging.impl.WeakHashtable";
    static /* synthetic */ Class class$org$apache$commons$logging$LogFactory;
    private static final String diagnosticPrefix;
    private static PrintStream diagnosticsStream;
    protected static Hashtable factories;
    protected static volatile LogFactory nullClassLoaderFactory;
    private static final ClassLoader thisClassLoader;

    public abstract Object getAttribute(String str);

    public abstract String[] getAttributeNames();

    public abstract Log getInstance(Class cls) throws LogConfigurationException;

    public abstract Log getInstance(String str) throws LogConfigurationException;

    public abstract void release();

    public abstract void removeAttribute(String str);

    public abstract void setAttribute(String str, Object obj);

    protected LogFactory() {
    }

    private static final Hashtable createFactoryStore() {
        String systemProperty;
        Hashtable hashtable = null;
        try {
            systemProperty = getSystemProperty(HASHTABLE_IMPLEMENTATION_PROPERTY, null);
        } catch (SecurityException unused) {
            systemProperty = null;
        }
        if (systemProperty == null) {
            systemProperty = WEAK_HASHTABLE_CLASSNAME;
        }
        try {
            hashtable = (Hashtable) Class.forName(systemProperty).newInstance();
        } catch (Throwable th) {
            handleThrowable(th);
            if (!WEAK_HASHTABLE_CLASSNAME.equals(systemProperty)) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic("[ERROR] LogFactory: Load of custom hashtable failed");
                } else {
                    System.err.println("[ERROR] LogFactory: Load of custom hashtable failed");
                }
            }
        }
        return hashtable == null ? new Hashtable() : hashtable;
    }

    private static String trim(String str) {
        if (str == null) {
            return null;
        }
        return str.trim();
    }

    protected static void handleThrowable(Throwable th) {
        if (th instanceof ThreadDeath) {
            throw ((ThreadDeath) th);
        }
        if (th instanceof VirtualMachineError) {
            throw ((VirtualMachineError) th);
        }
    }

    public static LogFactory getFactory() throws LogConfigurationException {
        BufferedReader bufferedReader;
        String property;
        ClassLoader contextClassLoaderInternal = getContextClassLoaderInternal();
        if (contextClassLoaderInternal == null && isDiagnosticsEnabled()) {
            logDiagnostic("Context classloader is null.");
        }
        LogFactory cachedFactory = getCachedFactory(contextClassLoaderInternal);
        if (cachedFactory != null) {
            return cachedFactory;
        }
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer("[LOOKUP] LogFactory implementation requested for the first time for context classloader ").append(objectId(contextClassLoaderInternal)).toString());
            logHierarchy("[LOOKUP] ", contextClassLoaderInternal);
        }
        Properties configurationFile = getConfigurationFile(contextClassLoaderInternal, FACTORY_PROPERTIES);
        ClassLoader classLoader = (configurationFile == null || (property = configurationFile.getProperty(TCCL_KEY)) == null || Boolean.valueOf(property).booleanValue()) ? contextClassLoaderInternal : thisClassLoader;
        if (isDiagnosticsEnabled()) {
            logDiagnostic("[LOOKUP] Looking for system property [org.apache.commons.logging.LogFactory] to define the LogFactory subclass to use...");
        }
        try {
            String systemProperty = getSystemProperty(FACTORY_PROPERTY, null);
            if (systemProperty != null) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer("[LOOKUP] Creating an instance of LogFactory class '").append(systemProperty).append("' as specified by system property org.apache.commons.logging.LogFactory").toString());
                }
                cachedFactory = newFactory(systemProperty, classLoader, contextClassLoaderInternal);
            } else if (isDiagnosticsEnabled()) {
                logDiagnostic("[LOOKUP] No system property [org.apache.commons.logging.LogFactory] defined.");
            }
        } catch (SecurityException e) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer("[LOOKUP] A security exception occurred while trying to create an instance of the custom factory class: [").append(trim(e.getMessage())).append("]. Trying alternative implementations...").toString());
            }
        } catch (RuntimeException e2) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer("[LOOKUP] An exception occurred while trying to create an instance of the custom factory class: [").append(trim(e2.getMessage())).append("] as specified by a system property.").toString());
            }
            throw e2;
        }
        if (cachedFactory == null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic("[LOOKUP] Looking for a resource file of name [META-INF/services/org.apache.commons.logging.LogFactory] to define the LogFactory subclass to use...");
            }
            try {
                InputStream resourceAsStream = getResourceAsStream(contextClassLoaderInternal, SERVICE_ID);
                if (resourceAsStream != null) {
                    try {
                        bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream, "UTF-8"));
                    } catch (UnsupportedEncodingException unused) {
                        bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
                    }
                    String line = bufferedReader.readLine();
                    bufferedReader.close();
                    if (line != null && !"".equals(line)) {
                        if (isDiagnosticsEnabled()) {
                            logDiagnostic(new StringBuffer("[LOOKUP]  Creating an instance of LogFactory class ").append(line).append(" as specified by file 'META-INF/services/org.apache.commons.logging.LogFactory' which was present in the path of the context classloader.").toString());
                        }
                        cachedFactory = newFactory(line, classLoader, contextClassLoaderInternal);
                    }
                } else if (isDiagnosticsEnabled()) {
                    logDiagnostic("[LOOKUP] No resource file with name 'META-INF/services/org.apache.commons.logging.LogFactory' found.");
                }
            } catch (Exception e3) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer("[LOOKUP] A security exception occurred while trying to create an instance of the custom factory class: [").append(trim(e3.getMessage())).append("]. Trying alternative implementations...").toString());
                }
            }
        }
        if (cachedFactory == null) {
            if (configurationFile != null) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic("[LOOKUP] Looking in properties file for entry with key 'org.apache.commons.logging.LogFactory' to define the LogFactory subclass to use...");
                }
                String property2 = configurationFile.getProperty(FACTORY_PROPERTY);
                if (property2 != null) {
                    if (isDiagnosticsEnabled()) {
                        logDiagnostic(new StringBuffer("[LOOKUP] Properties file specifies LogFactory subclass '").append(property2).append("'").toString());
                    }
                    cachedFactory = newFactory(property2, classLoader, contextClassLoaderInternal);
                } else if (isDiagnosticsEnabled()) {
                    logDiagnostic("[LOOKUP] Properties file has no entry specifying LogFactory subclass.");
                }
            } else if (isDiagnosticsEnabled()) {
                logDiagnostic("[LOOKUP] No properties file available to determine LogFactory subclass from..");
            }
        }
        if (cachedFactory == null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic("[LOOKUP] Loading the default LogFactory implementation 'org.apache.commons.logging.impl.LogFactoryImpl' via the same classloader that loaded this LogFactory class (ie not looking in the context classloader).");
            }
            cachedFactory = newFactory(FACTORY_DEFAULT, thisClassLoader, contextClassLoaderInternal);
        }
        if (cachedFactory != null) {
            cacheFactory(contextClassLoaderInternal, cachedFactory);
            if (configurationFile != null) {
                Enumeration<?> enumerationPropertyNames = configurationFile.propertyNames();
                while (enumerationPropertyNames.hasMoreElements()) {
                    String str = (String) enumerationPropertyNames.nextElement();
                    cachedFactory.setAttribute(str, configurationFile.getProperty(str));
                }
            }
        }
        return cachedFactory;
    }

    public static Log getLog(Class cls) throws LogConfigurationException {
        return getFactory().getInstance(cls);
    }

    public static Log getLog(String str) throws LogConfigurationException {
        return getFactory().getInstance(str);
    }

    public static void release(ClassLoader classLoader) {
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer("Releasing factory for classloader ").append(objectId(classLoader)).toString());
        }
        Hashtable hashtable = factories;
        synchronized (hashtable) {
            if (classLoader == null) {
                if (nullClassLoaderFactory != null) {
                    nullClassLoaderFactory.release();
                    nullClassLoaderFactory = null;
                }
            } else {
                LogFactory logFactory = (LogFactory) hashtable.get(classLoader);
                if (logFactory != null) {
                    logFactory.release();
                    hashtable.remove(classLoader);
                }
            }
        }
    }

    public static void releaseAll() {
        if (isDiagnosticsEnabled()) {
            logDiagnostic("Releasing factory for all classloaders.");
        }
        Hashtable hashtable = factories;
        synchronized (hashtable) {
            Enumeration enumerationElements = hashtable.elements();
            while (enumerationElements.hasMoreElements()) {
                ((LogFactory) enumerationElements.nextElement()).release();
            }
            hashtable.clear();
            if (nullClassLoaderFactory != null) {
                nullClassLoaderFactory.release();
                nullClassLoaderFactory = null;
            }
        }
    }

    protected static ClassLoader getClassLoader(Class cls) {
        try {
            return cls.getClassLoader();
        } catch (SecurityException e) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer("Unable to get classloader for class '").append(cls).append("' due to security restrictions - ").append(e.getMessage()).toString());
            }
            throw e;
        }
    }

    protected static ClassLoader getContextClassLoader() throws LogConfigurationException {
        return directGetContextClassLoader();
    }

    private static ClassLoader getContextClassLoaderInternal() throws LogConfigurationException {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: org.apache.commons.logging.LogFactory.1
            @Override // java.security.PrivilegedAction
            public Object run() {
                return LogFactory.directGetContextClassLoader();
            }
        });
    }

    protected static ClassLoader directGetContextClassLoader() throws LogConfigurationException {
        try {
            return Thread.currentThread().getContextClassLoader();
        } catch (SecurityException unused) {
            return null;
        }
    }

    private static LogFactory getCachedFactory(ClassLoader classLoader) {
        if (classLoader == null) {
            return nullClassLoaderFactory;
        }
        return (LogFactory) factories.get(classLoader);
    }

    private static void cacheFactory(ClassLoader classLoader, LogFactory logFactory) {
        if (logFactory != null) {
            if (classLoader == null) {
                nullClassLoaderFactory = logFactory;
            } else {
                factories.put(classLoader, logFactory);
            }
        }
    }

    protected static LogFactory newFactory(final String str, final ClassLoader classLoader, ClassLoader classLoader2) throws LogConfigurationException {
        Object objDoPrivileged = AccessController.doPrivileged((PrivilegedAction<Object>) new PrivilegedAction() { // from class: org.apache.commons.logging.LogFactory.2
            @Override // java.security.PrivilegedAction
            public Object run() {
                return LogFactory.createFactory(str, classLoader);
            }
        });
        if (objDoPrivileged instanceof LogConfigurationException) {
            LogConfigurationException logConfigurationException = (LogConfigurationException) objDoPrivileged;
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer("An error occurred while loading the factory class:").append(logConfigurationException.getMessage()).toString());
                throw logConfigurationException;
            }
            throw logConfigurationException;
        }
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer("Created object ").append(objectId(objDoPrivileged)).append(" to manage classloader ").append(objectId(classLoader2)).toString());
        }
        return (LogFactory) objDoPrivileged;
    }

    protected static LogFactory newFactory(String str, ClassLoader classLoader) {
        return newFactory(str, classLoader, null);
    }

    protected static Object createFactory(String str, ClassLoader classLoader) {
        Class<?> clsLoadClass = null;
        try {
            if (classLoader != null) {
                try {
                    try {
                        clsLoadClass = classLoader.loadClass(str);
                        Class clsClass$ = class$org$apache$commons$logging$LogFactory;
                        if (clsClass$ == null) {
                            clsClass$ = class$(FACTORY_PROPERTY);
                            class$org$apache$commons$logging$LogFactory = clsClass$;
                        }
                        if (clsClass$.isAssignableFrom(clsLoadClass)) {
                            if (isDiagnosticsEnabled()) {
                                logDiagnostic(new StringBuffer("Loaded class ").append(clsLoadClass.getName()).append(" from classloader ").append(objectId(classLoader)).toString());
                            }
                        } else if (isDiagnosticsEnabled()) {
                            StringBuffer stringBufferAppend = new StringBuffer("Factory class ").append(clsLoadClass.getName()).append(" loaded from classloader ").append(objectId(clsLoadClass.getClassLoader())).append(" does not extend '");
                            Class clsClass$2 = class$org$apache$commons$logging$LogFactory;
                            if (clsClass$2 == null) {
                                clsClass$2 = class$(FACTORY_PROPERTY);
                                class$org$apache$commons$logging$LogFactory = clsClass$2;
                            }
                            logDiagnostic(stringBufferAppend.append(clsClass$2.getName()).append("' as loaded by this classloader.").toString());
                            logHierarchy("[BAD CL TREE] ", classLoader);
                        }
                        return (LogFactory) clsLoadClass.newInstance();
                    } catch (NoClassDefFoundError e) {
                        if (classLoader == thisClassLoader) {
                            if (isDiagnosticsEnabled()) {
                                logDiagnostic(new StringBuffer("Class '").append(str).append("' cannot be loaded via classloader ").append(objectId(classLoader)).append(" - it depends on some other class that cannot be found.").toString());
                            }
                            throw e;
                        }
                    }
                } catch (ClassCastException unused) {
                    if (classLoader == thisClassLoader) {
                        boolean zImplementsLogFactory = implementsLogFactory(clsLoadClass);
                        StringBuffer stringBuffer = new StringBuffer("The application has specified that a custom LogFactory implementation should be used but Class '");
                        stringBuffer.append(str);
                        stringBuffer.append("' cannot be converted to '");
                        Class clsClass$3 = class$org$apache$commons$logging$LogFactory;
                        if (clsClass$3 == null) {
                            clsClass$3 = class$(FACTORY_PROPERTY);
                            class$org$apache$commons$logging$LogFactory = clsClass$3;
                        }
                        stringBuffer.append(clsClass$3.getName());
                        stringBuffer.append("'. ");
                        if (zImplementsLogFactory) {
                            stringBuffer.append("The conflict is caused by the presence of multiple LogFactory classes in incompatible classloaders. Background can be found in http://commons.apache.org/logging/tech.html. If you have not explicitly specified a custom LogFactory then it is likely that the container has set one without your knowledge. In this case, consider using the commons-logging-adapters.jar file or specifying the standard LogFactory from the command line. ");
                        } else {
                            stringBuffer.append("Please check the custom implementation. ");
                        }
                        stringBuffer.append("Help can be found @http://commons.apache.org/logging/troubleshooting.html.");
                        if (isDiagnosticsEnabled()) {
                            logDiagnostic(stringBuffer.toString());
                        }
                        throw new ClassCastException(stringBuffer.toString());
                    }
                } catch (ClassNotFoundException e2) {
                    if (classLoader == thisClassLoader) {
                        if (isDiagnosticsEnabled()) {
                            logDiagnostic(new StringBuffer("Unable to locate any class called '").append(str).append("' via classloader ").append(objectId(classLoader)).toString());
                        }
                        throw e2;
                    }
                }
            }
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer("Unable to load factory class via classloader ").append(objectId(classLoader)).append(" - trying the classloader associated with this LogFactory.").toString());
            }
            return (LogFactory) Class.forName(str).newInstance();
        } catch (Exception e3) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic("Unable to create LogFactory instance.");
            }
            if (clsLoadClass != null) {
                Class clsClass$4 = class$org$apache$commons$logging$LogFactory;
                if (clsClass$4 == null) {
                    clsClass$4 = class$(FACTORY_PROPERTY);
                    class$org$apache$commons$logging$LogFactory = clsClass$4;
                }
                if (!clsClass$4.isAssignableFrom(clsLoadClass)) {
                    return new LogConfigurationException("The chosen LogFactory implementation does not extend LogFactory. Please check your configuration.", e3);
                }
            }
            return new LogConfigurationException(e3);
        }
    }

    static /* synthetic */ Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    private static boolean implementsLogFactory(Class cls) {
        if (cls != null) {
            try {
                ClassLoader classLoader = cls.getClassLoader();
                if (classLoader == null) {
                    logDiagnostic("[CUSTOM LOG FACTORY] was loaded by the boot classloader");
                    return false;
                }
                logHierarchy("[CUSTOM LOG FACTORY] ", classLoader);
                boolean zIsAssignableFrom = Class.forName(FACTORY_PROPERTY, false, classLoader).isAssignableFrom(cls);
                if (zIsAssignableFrom) {
                    logDiagnostic(new StringBuffer("[CUSTOM LOG FACTORY] ").append(cls.getName()).append(" implements LogFactory but was loaded by an incompatible classloader.").toString());
                    return zIsAssignableFrom;
                }
                logDiagnostic(new StringBuffer("[CUSTOM LOG FACTORY] ").append(cls.getName()).append(" does not implement LogFactory.").toString());
                return zIsAssignableFrom;
            } catch (ClassNotFoundException unused) {
                logDiagnostic("[CUSTOM LOG FACTORY] LogFactory class cannot be loaded by classloader which loaded the custom LogFactory implementation. Is the custom factory in the right classloader?");
            } catch (LinkageError e) {
                logDiagnostic(new StringBuffer("[CUSTOM LOG FACTORY] LinkageError thrown whilst trying to determine whether the compatibility was caused by a classloader conflict: ").append(e.getMessage()).toString());
            } catch (SecurityException e2) {
                logDiagnostic(new StringBuffer("[CUSTOM LOG FACTORY] SecurityException thrown whilst trying to determine whether the compatibility was caused by a classloader conflict: ").append(e2.getMessage()).toString());
            }
        }
        return false;
    }

    private static InputStream getResourceAsStream(final ClassLoader classLoader, final String str) {
        return (InputStream) AccessController.doPrivileged(new PrivilegedAction() { // from class: org.apache.commons.logging.LogFactory.3
            @Override // java.security.PrivilegedAction
            public Object run() {
                ClassLoader classLoader2 = classLoader;
                if (classLoader2 != null) {
                    return classLoader2.getResourceAsStream(str);
                }
                return ClassLoader.getSystemResourceAsStream(str);
            }
        });
    }

    private static Enumeration getResources(final ClassLoader classLoader, final String str) {
        return (Enumeration) AccessController.doPrivileged(new PrivilegedAction() { // from class: org.apache.commons.logging.LogFactory.4
            @Override // java.security.PrivilegedAction
            public Object run() {
                try {
                    ClassLoader classLoader2 = classLoader;
                    if (classLoader2 != null) {
                        return classLoader2.getResources(str);
                    }
                    return ClassLoader.getSystemResources(str);
                } catch (IOException e) {
                    if (LogFactory.isDiagnosticsEnabled()) {
                        LogFactory.logDiagnostic(new StringBuffer("Exception while trying to find configuration file ").append(str).append(":").append(e.getMessage()).toString());
                    }
                    return null;
                } catch (NoSuchMethodError unused) {
                    return null;
                }
            }
        });
    }

    private static Properties getProperties(final URL url) {
        return (Properties) AccessController.doPrivileged(new PrivilegedAction() { // from class: org.apache.commons.logging.LogFactory.5
            /* JADX WARN: Removed duplicated region for block: B:39:0x0072 A[EXC_TOP_SPLITTER, SYNTHETIC] */
            @Override // java.security.PrivilegedAction
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public java.lang.Object run() throws java.lang.Throwable {
                /*
                    r5 = this;
                    java.lang.String r0 = "Unable to close stream for URL "
                    java.lang.String r1 = "Unable to read URL "
                    r2 = 0
                    java.net.URL r3 = r1     // Catch: java.lang.Throwable -> L40 java.io.IOException -> L42
                    java.net.URLConnection r3 = r3.openConnection()     // Catch: java.lang.Throwable -> L40 java.io.IOException -> L42
                    r4 = 0
                    r3.setUseCaches(r4)     // Catch: java.lang.Throwable -> L40 java.io.IOException -> L42
                    java.io.InputStream r3 = r3.getInputStream()     // Catch: java.lang.Throwable -> L40 java.io.IOException -> L42
                    if (r3 == 0) goto L21
                    java.util.Properties r4 = new java.util.Properties     // Catch: java.io.IOException -> L43 java.lang.Throwable -> L6e
                    r4.<init>()     // Catch: java.io.IOException -> L43 java.lang.Throwable -> L6e
                    r4.load(r3)     // Catch: java.io.IOException -> L43 java.lang.Throwable -> L6e
                    r3.close()     // Catch: java.io.IOException -> L43 java.lang.Throwable -> L6e
                    return r4
                L21:
                    if (r3 == 0) goto L6d
                    r3.close()     // Catch: java.io.IOException -> L27
                    goto L6d
                L27:
                    boolean r1 = org.apache.commons.logging.LogFactory.isDiagnosticsEnabled()
                    if (r1 == 0) goto L6d
                    java.lang.StringBuffer r1 = new java.lang.StringBuffer
                    r1.<init>(r0)
                L32:
                    java.net.URL r0 = r1
                    java.lang.StringBuffer r0 = r1.append(r0)
                    java.lang.String r0 = r0.toString()
                    org.apache.commons.logging.LogFactory.access$000(r0)
                    goto L6d
                L40:
                    r1 = move-exception
                    goto L70
                L42:
                    r3 = r2
                L43:
                    boolean r4 = org.apache.commons.logging.LogFactory.isDiagnosticsEnabled()     // Catch: java.lang.Throwable -> L6e
                    if (r4 == 0) goto L5b
                    java.lang.StringBuffer r4 = new java.lang.StringBuffer     // Catch: java.lang.Throwable -> L6e
                    r4.<init>(r1)     // Catch: java.lang.Throwable -> L6e
                    java.net.URL r1 = r1     // Catch: java.lang.Throwable -> L6e
                    java.lang.StringBuffer r1 = r4.append(r1)     // Catch: java.lang.Throwable -> L6e
                    java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> L6e
                    org.apache.commons.logging.LogFactory.access$000(r1)     // Catch: java.lang.Throwable -> L6e
                L5b:
                    if (r3 == 0) goto L6d
                    r3.close()     // Catch: java.io.IOException -> L61
                    goto L6d
                L61:
                    boolean r1 = org.apache.commons.logging.LogFactory.isDiagnosticsEnabled()
                    if (r1 == 0) goto L6d
                    java.lang.StringBuffer r1 = new java.lang.StringBuffer
                    r1.<init>(r0)
                    goto L32
                L6d:
                    return r2
                L6e:
                    r1 = move-exception
                    r2 = r3
                L70:
                    if (r2 == 0) goto L8e
                    r2.close()     // Catch: java.io.IOException -> L76
                    goto L8e
                L76:
                    boolean r2 = org.apache.commons.logging.LogFactory.isDiagnosticsEnabled()
                    if (r2 == 0) goto L8e
                    java.lang.StringBuffer r2 = new java.lang.StringBuffer
                    r2.<init>(r0)
                    java.net.URL r0 = r1
                    java.lang.StringBuffer r0 = r2.append(r0)
                    java.lang.String r0 = r0.toString()
                    org.apache.commons.logging.LogFactory.access$000(r0)
                L8e:
                    throw r1
                */
                throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.logging.LogFactory.AnonymousClass5.run():java.lang.Object");
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x0105  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static final java.util.Properties getConfigurationFile(java.lang.ClassLoader r14, java.lang.String r15) {
        /*
            Method dump skipped, instruction units count: 323
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.logging.LogFactory.getConfigurationFile(java.lang.ClassLoader, java.lang.String):java.util.Properties");
    }

    private static String getSystemProperty(final String str, final String str2) throws SecurityException {
        return (String) AccessController.doPrivileged(new PrivilegedAction() { // from class: org.apache.commons.logging.LogFactory.6
            @Override // java.security.PrivilegedAction
            public Object run() {
                return System.getProperty(str, str2);
            }
        });
    }

    private static PrintStream initDiagnostics() {
        try {
            String systemProperty = getSystemProperty(DIAGNOSTICS_DEST_PROPERTY, null);
            if (systemProperty == null) {
                return null;
            }
            if (systemProperty.equals("STDOUT")) {
                return System.out;
            }
            if (systemProperty.equals("STDERR")) {
                return System.err;
            }
            return new PrintStream(new FileOutputStream(systemProperty, true));
        } catch (IOException | SecurityException unused) {
            return null;
        }
    }

    protected static boolean isDiagnosticsEnabled() {
        return diagnosticsStream != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void logDiagnostic(String str) {
        PrintStream printStream = diagnosticsStream;
        if (printStream != null) {
            printStream.print(diagnosticPrefix);
            diagnosticsStream.println(str);
            diagnosticsStream.flush();
        }
    }

    protected static final void logRawDiagnostic(String str) {
        PrintStream printStream = diagnosticsStream;
        if (printStream != null) {
            printStream.println(str);
            diagnosticsStream.flush();
        }
    }

    private static void logClassLoaderEnvironment(Class cls) {
        if (isDiagnosticsEnabled()) {
            try {
                logDiagnostic(new StringBuffer("[ENV] Extension directories (java.ext.dir): ").append(System.getProperty("java.ext.dir")).toString());
                logDiagnostic(new StringBuffer("[ENV] Application classpath (java.class.path): ").append(System.getProperty("java.class.path")).toString());
            } catch (SecurityException unused) {
                logDiagnostic("[ENV] Security setting prevent interrogation of system classpaths.");
            }
            String name = cls.getName();
            try {
                ClassLoader classLoader = getClassLoader(cls);
                logDiagnostic(new StringBuffer("[ENV] Class ").append(name).append(" was loaded via classloader ").append(objectId(classLoader)).toString());
                logHierarchy(new StringBuffer("[ENV] Ancestry of classloader which loaded ").append(name).append(" is ").toString(), classLoader);
            } catch (SecurityException unused2) {
                logDiagnostic(new StringBuffer("[ENV] Security forbids determining the classloader for ").append(name).toString());
            }
        }
    }

    private static void logHierarchy(String str, ClassLoader classLoader) {
        if (isDiagnosticsEnabled()) {
            if (classLoader != null) {
                logDiagnostic(new StringBuffer().append(str).append(objectId(classLoader)).append(" == '").append(classLoader.toString()).append("'").toString());
            }
            try {
                ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
                if (classLoader != null) {
                    StringBuffer stringBuffer = new StringBuffer(new StringBuffer().append(str).append("ClassLoader tree:").toString());
                    do {
                        stringBuffer.append(objectId(classLoader));
                        if (classLoader == systemClassLoader) {
                            stringBuffer.append(" (SYSTEM) ");
                        }
                        try {
                            classLoader = classLoader.getParent();
                            stringBuffer.append(" --> ");
                        } catch (SecurityException unused) {
                            stringBuffer.append(" --> SECRET");
                        }
                    } while (classLoader != null);
                    stringBuffer.append("BOOT");
                    logDiagnostic(stringBuffer.toString());
                }
            } catch (SecurityException unused2) {
                logDiagnostic(new StringBuffer().append(str).append("Security forbids determining the system classloader.").toString());
            }
        }
    }

    public static String objectId(Object obj) {
        if (obj == null) {
            return "null";
        }
        return new StringBuffer().append(obj.getClass().getName()).append("@").append(System.identityHashCode(obj)).toString();
    }

    static {
        String strObjectId;
        Class clsClass$ = class$org$apache$commons$logging$LogFactory;
        if (clsClass$ == null) {
            clsClass$ = class$(FACTORY_PROPERTY);
            class$org$apache$commons$logging$LogFactory = clsClass$;
        }
        ClassLoader classLoader = getClassLoader(clsClass$);
        thisClassLoader = classLoader;
        if (classLoader == null) {
            strObjectId = "BOOTLOADER";
        } else {
            try {
                strObjectId = objectId(classLoader);
            } catch (SecurityException unused) {
                strObjectId = "UNKNOWN";
            }
        }
        diagnosticPrefix = new StringBuffer("[LogFactory from ").append(strObjectId).append("] ").toString();
        diagnosticsStream = initDiagnostics();
        Class clsClass$2 = class$org$apache$commons$logging$LogFactory;
        if (clsClass$2 == null) {
            clsClass$2 = class$(FACTORY_PROPERTY);
            class$org$apache$commons$logging$LogFactory = clsClass$2;
        }
        logClassLoaderEnvironment(clsClass$2);
        factories = createFactoryStore();
        if (isDiagnosticsEnabled()) {
            logDiagnostic("BOOTSTRAP COMPLETED");
        }
    }
}
