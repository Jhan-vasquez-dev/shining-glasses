package com.alibaba.fastjson2.support;

import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaders;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriters;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/* JADX INFO: loaded from: classes.dex */
public class LambdaMiscCodec {
    static volatile Throwable errorLast;
    static volatile boolean hppcError;

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public static ObjectWriter getObjectWriter(Type type, Class cls) {
        if (hppcError) {
            return null;
        }
        String name = cls.getName();
        name.hashCode();
        byte b = -1;
        switch (name.hashCode()) {
            case -1757049669:
                if (name.equals("com.carrotsearch.hppc.LongHashSet")) {
                    b = 0;
                }
                break;
            case -1682705914:
                if (name.equals("gnu.trove.set.hash.TShortHashSet")) {
                    b = 1;
                }
                break;
            case -1670613343:
                if (name.equals("com.carrotsearch.hppc.CharHashSet")) {
                    b = 2;
                }
                break;
            case -864935548:
                if (name.equals("com.carrotsearch.hppc.CharArrayList")) {
                    b = 3;
                }
                break;
            case -848095899:
                if (name.equals("com.carrotsearch.hppc.IntArrayList")) {
                    b = 4;
                }
                break;
            case -808573634:
                if (name.equals("gnu.trove.list.array.TLongArrayList")) {
                    b = 5;
                }
                break;
            case -702521390:
                if (name.equals("com.carrotsearch.hppc.BitSet")) {
                    b = 6;
                }
                break;
            case -448666600:
                if (name.equals("gnu.trove.list.array.TShortArrayList")) {
                    b = 7;
                }
                break;
            case -342082893:
                if (name.equals("gnu.trove.set.hash.TIntHashSet")) {
                    b = 8;
                }
                break;
            case -240096200:
                if (name.equals("com.carrotsearch.hppc.ShortArrayList")) {
                    b = 9;
                }
                break;
            case -127813975:
                if (name.equals("com.carrotsearch.hppc.DoubleArrayList")) {
                    b = 10;
                }
                break;
            case 100244498:
                if (name.equals("com.carrotsearch.hppc.ByteArrayList")) {
                    b = 11;
                }
                break;
            case 217956074:
                if (name.equals("gnu.trove.set.hash.TLongHashSet")) {
                    b = 12;
                }
                break;
            case 652357028:
                if (name.equals("gnu.trove.list.array.TCharArrayList")) {
                    b = 13;
                }
                break;
            case 1138418232:
                if (name.equals("gnu.trove.list.array.TFloatArrayList")) {
                    b = 14;
                }
                break;
            case 1195384194:
                if (name.equals("gnu.trove.stack.array.TByteArrayStack")) {
                    b = 15;
                }
                break;
            case 1346988632:
                if (name.equals("com.carrotsearch.hppc.FloatArrayList")) {
                    b = JSONB.Constants.BC_INT32_NUM_16;
                }
                break;
            case 1395322562:
                if (name.equals("com.carrotsearch.hppc.IntHashSet")) {
                    b = 17;
                }
                break;
            case 1556153669:
                if (name.equals("gnu.trove.list.array.TIntArrayList")) {
                    b = 18;
                }
                break;
            case 1617537074:
                if (name.equals("gnu.trove.list.array.TByteArrayList")) {
                    b = 19;
                }
                break;
            case 1643140783:
                if (name.equals("org.bson.types.Decimal128")) {
                    b = 20;
                }
                break;
            case 1891987166:
                if (name.equals("gnu.trove.set.hash.TByteHashSet")) {
                    b = 21;
                }
                break;
            case 1969101086:
                if (name.equals("com.carrotsearch.hppc.LongArrayList")) {
                    b = 22;
                }
                break;
            case 1996438217:
                if (name.equals("gnu.trove.list.array.TDoubleArrayList")) {
                    b = 23;
                }
                break;
        }
        switch (b) {
            case 0:
            case 5:
            case 12:
            case 22:
                try {
                    return ObjectWriters.ofToLongArray(createFunction(cls.getMethod("toArray", new Class[0])));
                } catch (NoSuchMethodException | SecurityException e) {
                    throw new JSONException("illegal state", e);
                }
            case 1:
            case 7:
            case 9:
                try {
                    return ObjectWriters.ofToShortArray(createFunction(cls.getMethod("toArray", new Class[0])));
                } catch (NoSuchMethodException | SecurityException e2) {
                    throw new JSONException("illegal state", e2);
                }
            case 2:
            case 3:
            case 13:
                try {
                    return ObjectWriters.ofToCharArray(createFunction(cls.getMethod("toArray", new Class[0])));
                } catch (NoSuchMethodException | SecurityException e3) {
                    throw new JSONException("illegal state", e3);
                }
            case 4:
            case 8:
            case 17:
            case 18:
                try {
                    return ObjectWriters.ofToIntArray(createFunction(cls.getMethod("toArray", new Class[0])));
                } catch (NoSuchMethodException | SecurityException e4) {
                    throw new JSONException("illegal state", e4);
                }
            case 6:
                MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(cls);
                try {
                    return ObjectWriters.ofToBooleanArray(createToLongFunction(cls.getMethod("size", new Class[0])), (BiFunction<Object, Integer, Boolean>) (BiFunction) LambdaMetafactory.metafactory(lookupTrustedLookup, "apply", MethodType.methodType(BiFunction.class), MethodType.methodType(Object.class, Object.class, Object.class), lookupTrustedLookup.findVirtual(cls, "get", MethodType.methodType((Class<?>) Boolean.TYPE, (Class<?>) Integer.TYPE)), MethodType.methodType(Boolean.class, cls, Integer.class)).getTarget().invokeExact());
                } catch (Throwable unused) {
                    hppcError = true;
                }
                break;
            case 10:
            case 23:
                try {
                    return ObjectWriters.ofToDoubleArray(createFunction(cls.getMethod("toArray", new Class[0])));
                } catch (NoSuchMethodException | SecurityException e5) {
                    throw new JSONException("illegal state", e5);
                }
            case 11:
            case 15:
            case 19:
            case 21:
                try {
                    return ObjectWriters.ofToByteArray(createFunction(cls.getMethod("toArray", new Class[0])));
                } catch (NoSuchMethodException | SecurityException e6) {
                    throw new JSONException("illegal state", e6);
                }
            case 14:
            case 16:
                try {
                    return ObjectWriters.ofToFloatArray(createFunction(cls.getMethod("toArray", new Class[0])));
                } catch (NoSuchMethodException | SecurityException e7) {
                    throw new JSONException("illegal state", e7);
                }
            case 20:
                try {
                    return ObjectWriters.ofToBigDecimal(createFunction(cls.getMethod("bigDecimalValue", new Class[0])));
                } catch (NoSuchMethodException | SecurityException e8) {
                    throw new JSONException("illegal state", e8);
                }
            default:
                return null;
        }
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public static ObjectReader getObjectReader(Class cls) {
        if (hppcError) {
            return null;
        }
        String name = cls.getName();
        name.hashCode();
        byte b = -1;
        switch (name.hashCode()) {
            case -1757049669:
                if (name.equals("com.carrotsearch.hppc.LongHashSet")) {
                    b = 0;
                }
                break;
            case -1682705914:
                if (name.equals("gnu.trove.set.hash.TShortHashSet")) {
                    b = 1;
                }
                break;
            case -1670613343:
                if (name.equals("com.carrotsearch.hppc.CharHashSet")) {
                    b = 2;
                }
                break;
            case -864935548:
                if (name.equals("com.carrotsearch.hppc.CharArrayList")) {
                    b = 3;
                }
                break;
            case -848095899:
                if (name.equals("com.carrotsearch.hppc.IntArrayList")) {
                    b = 4;
                }
                break;
            case -808573634:
                if (name.equals("gnu.trove.list.array.TLongArrayList")) {
                    b = 5;
                }
                break;
            case -448666600:
                if (name.equals("gnu.trove.list.array.TShortArrayList")) {
                    b = 6;
                }
                break;
            case -342082893:
                if (name.equals("gnu.trove.set.hash.TIntHashSet")) {
                    b = 7;
                }
                break;
            case -240096200:
                if (name.equals("com.carrotsearch.hppc.ShortArrayList")) {
                    b = 8;
                }
                break;
            case -127813975:
                if (name.equals("com.carrotsearch.hppc.DoubleArrayList")) {
                    b = 9;
                }
                break;
            case 100244498:
                if (name.equals("com.carrotsearch.hppc.ByteArrayList")) {
                    b = 10;
                }
                break;
            case 217956074:
                if (name.equals("gnu.trove.set.hash.TLongHashSet")) {
                    b = 11;
                }
                break;
            case 652357028:
                if (name.equals("gnu.trove.list.array.TCharArrayList")) {
                    b = 12;
                }
                break;
            case 1138418232:
                if (name.equals("gnu.trove.list.array.TFloatArrayList")) {
                    b = 13;
                }
                break;
            case 1195384194:
                if (name.equals("gnu.trove.stack.array.TByteArrayStack")) {
                    b = 14;
                }
                break;
            case 1346988632:
                if (name.equals("com.carrotsearch.hppc.FloatArrayList")) {
                    b = 15;
                }
                break;
            case 1395322562:
                if (name.equals("com.carrotsearch.hppc.IntHashSet")) {
                    b = JSONB.Constants.BC_INT32_NUM_16;
                }
                break;
            case 1556153669:
                if (name.equals("gnu.trove.list.array.TIntArrayList")) {
                    b = 17;
                }
                break;
            case 1617537074:
                if (name.equals("gnu.trove.list.array.TByteArrayList")) {
                    b = 18;
                }
                break;
            case 1643140783:
                if (name.equals("org.bson.types.Decimal128")) {
                    b = 19;
                }
                break;
            case 1891987166:
                if (name.equals("gnu.trove.set.hash.TByteHashSet")) {
                    b = 20;
                }
                break;
            case 1969101086:
                if (name.equals("com.carrotsearch.hppc.LongArrayList")) {
                    b = 21;
                }
                break;
            case 1996438217:
                if (name.equals("gnu.trove.list.array.TDoubleArrayList")) {
                    b = 22;
                }
                break;
        }
        switch (b) {
            case 0:
            case 21:
                try {
                    return ObjectReaders.fromLongArray(createFunction(cls.getMethod("from", long[].class)));
                } catch (NoSuchMethodException | SecurityException e) {
                    throw new JSONException("illegal state", e);
                }
            case 1:
            case 6:
                try {
                    return ObjectReaders.fromShortArray(createFunction(cls.getConstructor(short[].class)));
                } catch (NoSuchMethodException | SecurityException e2) {
                    throw new JSONException("illegal state", e2);
                }
            case 2:
            case 3:
                try {
                    return ObjectReaders.fromCharArray(createFunction(cls.getMethod("from", char[].class)));
                } catch (NoSuchMethodException | SecurityException e3) {
                    throw new JSONException("illegal state", e3);
                }
            case 4:
            case 16:
                try {
                    return ObjectReaders.fromIntArray(createFunction(cls.getMethod("from", int[].class)));
                } catch (NoSuchMethodException | SecurityException e4) {
                    throw new JSONException("illegal state", e4);
                }
            case 5:
            case 11:
                try {
                    return ObjectReaders.fromLongArray(createFunction(cls.getConstructor(long[].class)));
                } catch (NoSuchMethodException | SecurityException e5) {
                    throw new JSONException("illegal state", e5);
                }
            case 7:
            case 17:
                try {
                    return ObjectReaders.fromIntArray(createFunction(cls.getConstructor(int[].class)));
                } catch (NoSuchMethodException | SecurityException e6) {
                    throw new JSONException("illegal state", e6);
                }
            case 8:
                try {
                    return ObjectReaders.fromShortArray(createFunction(cls.getMethod("from", short[].class)));
                } catch (NoSuchMethodException | SecurityException e7) {
                    throw new JSONException("illegal state", e7);
                }
            case 9:
                try {
                    return ObjectReaders.fromDoubleArray(createFunction(cls.getMethod("from", double[].class)));
                } catch (NoSuchMethodException | SecurityException e8) {
                    throw new JSONException("illegal state", e8);
                }
            case 10:
                try {
                    return ObjectReaders.fromByteArray(createFunction(cls.getMethod("from", byte[].class)));
                } catch (NoSuchMethodException | SecurityException e9) {
                    throw new JSONException("illegal state", e9);
                }
            case 12:
                try {
                    return ObjectReaders.fromCharArray(createFunction(cls.getConstructor(char[].class)));
                } catch (NoSuchMethodException | SecurityException e10) {
                    throw new JSONException("illegal state", e10);
                }
            case 13:
                try {
                    return ObjectReaders.fromFloatArray(createFunction(cls.getConstructor(float[].class)));
                } catch (NoSuchMethodException | SecurityException e11) {
                    throw new JSONException("illegal state", e11);
                }
            case 14:
            case 18:
            case 20:
                try {
                    return ObjectReaders.fromByteArray(createFunction(cls.getConstructor(byte[].class)));
                } catch (NoSuchMethodException | SecurityException e12) {
                    throw new JSONException("illegal state", e12);
                }
            case 15:
                try {
                    return ObjectReaders.fromFloatArray(createFunction(cls.getMethod("from", float[].class)));
                } catch (NoSuchMethodException | SecurityException e13) {
                    throw new JSONException("illegal state", e13);
                }
            case 19:
                try {
                    return ObjectReaders.fromBigDecimal(createFunction(cls.getConstructor(BigDecimal.class)));
                } catch (NoSuchMethodException | SecurityException e14) {
                    throw new JSONException("illegal state", e14);
                }
            case 22:
                try {
                    return ObjectReaders.fromDoubleArray(createFunction(cls.getConstructor(double[].class)));
                } catch (NoSuchMethodException | SecurityException e15) {
                    throw new JSONException("illegal state", e15);
                }
            default:
                return null;
        }
    }

    public static LongFunction createLongFunction(Constructor constructor) {
        try {
            Class<?> declaringClass = constructor.getDeclaringClass();
            MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(declaringClass);
            return (LongFunction) LambdaMetafactory.metafactory(lookupTrustedLookup, "apply", TypeUtils.METHOD_TYPE_LONG_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_LONG, lookupTrustedLookup.findConstructor(declaringClass, TypeUtils.METHOD_TYPE_VOID_LONG), MethodType.methodType(declaringClass, (Class<?>) Long.TYPE)).getTarget().invokeExact();
        } catch (Throwable th) {
            errorLast = th;
            return new ReflectLongFunction(constructor);
        }
    }

    public static ToIntFunction createToIntFunction(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        try {
            MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(declaringClass);
            return (ToIntFunction) LambdaMetafactory.metafactory(lookupTrustedLookup, "applyAsInt", TypeUtils.METHOD_TYPE_TO_INT_FUNCTION, TypeUtils.METHOD_TYPE_INT_OBJECT, lookupTrustedLookup.findVirtual(declaringClass, method.getName(), MethodType.methodType(Integer.TYPE)), MethodType.methodType((Class<?>) Integer.TYPE, declaringClass)).getTarget().invokeExact();
        } catch (Throwable th) {
            errorLast = th;
            return new ReflectToIntFunction(method);
        }
    }

    public static ToLongFunction createToLongFunction(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        try {
            MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(declaringClass);
            return (ToLongFunction) LambdaMetafactory.metafactory(lookupTrustedLookup, "applyAsLong", TypeUtils.METHOD_TYPE_TO_LONG_FUNCTION, TypeUtils.METHOD_TYPE_LONG_OBJECT, lookupTrustedLookup.findVirtual(declaringClass, method.getName(), MethodType.methodType(Long.TYPE)), MethodType.methodType((Class<?>) Long.TYPE, declaringClass)).getTarget().invokeExact();
        } catch (Throwable th) {
            errorLast = th;
            return new ReflectToLongFunction(method);
        }
    }

    public static Function createFunction(Constructor constructor) {
        try {
            Class<?> declaringClass = constructor.getDeclaringClass();
            MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(declaringClass);
            Class<?> cls = constructor.getParameterTypes()[0];
            return (Function) LambdaMetafactory.metafactory(lookupTrustedLookup, "apply", TypeUtils.METHOD_TYPE_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_OBJECT, lookupTrustedLookup.findConstructor(declaringClass, MethodType.methodType((Class<?>) Void.TYPE, cls)), MethodType.methodType(declaringClass, box(cls))).getTarget().invokeExact();
        } catch (Throwable th) {
            errorLast = th;
            return new ConstructorFunction(constructor);
        }
    }

    public static Supplier createSupplier(Constructor constructor) {
        try {
            Class<?> declaringClass = constructor.getDeclaringClass();
            MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(declaringClass);
            return (Supplier) LambdaMetafactory.metafactory(lookupTrustedLookup, "get", TypeUtils.METHOD_TYPE_SUPPLIER, TypeUtils.METHOD_TYPE_OBJECT, lookupTrustedLookup.findConstructor(declaringClass, MethodType.methodType(Void.TYPE)), MethodType.methodType(declaringClass)).getTarget().invokeExact();
        } catch (Throwable th) {
            errorLast = th;
            return new ConstructorSupplier(constructor);
        }
    }

    public static Supplier createSupplier(Method method) {
        try {
            Class<?> declaringClass = method.getDeclaringClass();
            Class<?> returnType = method.getReturnType();
            MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(declaringClass);
            return (Supplier) LambdaMetafactory.metafactory(lookupTrustedLookup, "get", TypeUtils.METHOD_TYPE_SUPPLIER, TypeUtils.METHOD_TYPE_OBJECT, lookupTrustedLookup.findStatic(declaringClass, method.getName(), MethodType.methodType(returnType)), MethodType.methodType(returnType)).getTarget().invokeExact();
        } catch (Throwable th) {
            errorLast = th;
            return new ReflectSupplier(method);
        }
    }

    public static BiFunction createBiFunction(Method method) {
        MethodType methodType;
        MethodHandle methodHandle;
        try {
            Class<?> declaringClass = method.getDeclaringClass();
            Class<?> returnType = method.getReturnType();
            MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(declaringClass);
            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?> cls = parameterTypes[0];
            if (Modifier.isStatic(method.getModifiers())) {
                Class<?> cls2 = parameterTypes[1];
                MethodHandle methodHandleFindStatic = lookupTrustedLookup.findStatic(declaringClass, method.getName(), MethodType.methodType(returnType, cls, cls2));
                methodType = MethodType.methodType(returnType, cls, cls2);
                methodHandle = methodHandleFindStatic;
            } else {
                MethodHandle methodHandleFindVirtual = lookupTrustedLookup.findVirtual(declaringClass, method.getName(), MethodType.methodType(returnType, cls));
                methodType = MethodType.methodType(returnType, declaringClass, box(cls));
                methodHandle = methodHandleFindVirtual;
            }
            return (BiFunction) LambdaMetafactory.metafactory(lookupTrustedLookup, "apply", TypeUtils.METHOD_TYPE_BI_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_OBJECT_OBJECT, methodHandle, methodType).getTarget().invokeExact();
        } catch (Throwable th) {
            errorLast = th;
            return new ReflectBiFunction(method);
        }
    }

    public static BiFunction createBiFunction(Constructor constructor) {
        try {
            Class<?> declaringClass = constructor.getDeclaringClass();
            MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(declaringClass);
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Class<?> cls = parameterTypes[0];
            Class<?> cls2 = parameterTypes[1];
            return (BiFunction) LambdaMetafactory.metafactory(lookupTrustedLookup, "apply", TypeUtils.METHOD_TYPE_BI_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_OBJECT_OBJECT, lookupTrustedLookup.findConstructor(declaringClass, MethodType.methodType(Void.TYPE, cls, cls2)), MethodType.methodType(declaringClass, box(cls), box(cls2))).getTarget().invokeExact();
        } catch (Throwable th) {
            errorLast = th;
            return new ConstructorBiFunction(constructor);
        }
    }

    static Class<?> box(Class cls) {
        if (cls == Integer.TYPE) {
            return Integer.class;
        }
        if (cls == Long.TYPE) {
            return Long.class;
        }
        if (cls == Boolean.TYPE) {
            return Boolean.class;
        }
        if (cls == Short.TYPE) {
            return Short.class;
        }
        if (cls == Byte.TYPE) {
            return Byte.class;
        }
        if (cls == Character.TYPE) {
            return Character.class;
        }
        if (cls == Float.TYPE) {
            return Float.class;
        }
        return cls == Double.TYPE ? Double.class : cls;
    }

    static final class ConstructorSupplier implements Supplier {
        final Constructor constructor;

        ConstructorSupplier(Constructor constructor) {
            this.constructor = constructor;
        }

        @Override // java.util.function.Supplier
        public Object get() {
            try {
                return this.constructor.newInstance(new Object[0]);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new JSONException("invoke error", e);
            }
        }
    }

    static final class ConstructorFunction implements Function {
        final Constructor constructor;

        ConstructorFunction(Constructor constructor) {
            this.constructor = constructor;
        }

        @Override // java.util.function.Function
        public Object apply(Object obj) {
            try {
                return this.constructor.newInstance(obj);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new JSONException("invoke error", e);
            }
        }
    }

    static final class ConstructorBiFunction implements BiFunction {
        final Constructor constructor;

        ConstructorBiFunction(Constructor constructor) {
            this.constructor = constructor;
        }

        @Override // java.util.function.BiFunction
        public Object apply(Object obj, Object obj2) {
            try {
                return this.constructor.newInstance(obj, obj2);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new JSONException("invoke error", e);
            }
        }
    }

    static final class ReflectBiFunction implements BiFunction {
        final Method method;

        ReflectBiFunction(Method method) {
            this.method = method;
        }

        @Override // java.util.function.BiFunction
        public Object apply(Object obj, Object obj2) {
            try {
                if (Modifier.isStatic(this.method.getModifiers())) {
                    return this.method.invoke(null, obj, obj2);
                }
                return this.method.invoke(obj, obj2);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new JSONException("invoke error", e);
            }
        }
    }

    static final class ReflectSupplier implements Supplier {
        final Method method;

        ReflectSupplier(Method method) {
            this.method = method;
        }

        @Override // java.util.function.Supplier
        public Object get() {
            try {
                return this.method.invoke(null, new Object[0]);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new JSONException("invoke error", e);
            }
        }
    }

    public static Function createFunction(Method method) {
        Class<?> cls;
        MethodHandle methodHandleFindVirtual;
        Class<?> declaringClass = method.getDeclaringClass();
        int modifiers = method.getModifiers();
        Class<?>[] parameterTypes = method.getParameterTypes();
        boolean zIsStatic = Modifier.isStatic(modifiers);
        Class<?> returnType = method.getReturnType();
        if (parameterTypes.length == 1 && zIsStatic) {
            cls = parameterTypes[0];
        } else {
            if (parameterTypes.length != 0 || zIsStatic) {
                throw new JSONException("not support parameters " + method);
            }
            cls = declaringClass;
        }
        try {
            MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(declaringClass);
            if (zIsStatic) {
                methodHandleFindVirtual = lookupTrustedLookup.findStatic(declaringClass, method.getName(), MethodType.methodType(returnType, cls));
            } else {
                methodHandleFindVirtual = lookupTrustedLookup.findVirtual(declaringClass, method.getName(), MethodType.methodType(returnType));
            }
            return (Function) LambdaMetafactory.metafactory(lookupTrustedLookup, "apply", TypeUtils.METHOD_TYPE_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_OBJECT, methodHandleFindVirtual, MethodType.methodType(returnType, cls)).getTarget().invokeExact();
        } catch (Throwable th) {
            errorLast = th;
            if (!Modifier.isStatic(method.getModifiers())) {
                return new GetterFunction(method);
            }
            return new FactoryFunction(method);
        }
    }

    public static ObjIntConsumer createObjIntConsumer(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        try {
            MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(declaringClass);
            return (ObjIntConsumer) LambdaMetafactory.metafactory(lookupTrustedLookup, "accept", TypeUtils.METHOD_TYPE_OBJECT_INT_CONSUMER, TypeUtils.METHOD_TYPE_VOID_OBJECT_INT, lookupTrustedLookup.findVirtual(declaringClass, method.getName(), MethodType.methodType((Class<?>) Void.TYPE, (Class<?>) Integer.TYPE)), MethodType.methodType(Void.TYPE, declaringClass, Integer.TYPE)).getTarget().invokeExact();
        } catch (Throwable th) {
            errorLast = th;
            return new ReflectObjIntConsumer(method);
        }
    }

    static final class ReflectObjIntConsumer implements ObjIntConsumer {
        final Method method;

        public ReflectObjIntConsumer(Method method) {
            this.method = method;
        }

        @Override // java.util.function.ObjIntConsumer
        public void accept(Object obj, int i) {
            try {
                this.method.invoke(obj, Integer.valueOf(i));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new JSONException("invoke error", e);
            }
        }
    }

    static final class FactoryFunction implements Function {
        final Method method;

        FactoryFunction(Method method) {
            this.method = method;
        }

        @Override // java.util.function.Function
        public Object apply(Object obj) {
            try {
                return this.method.invoke(null, obj);
            } catch (Exception e) {
                throw new JSONException("createInstance error", e);
            }
        }
    }

    static final class GetterFunction implements Function {
        final Method method;

        GetterFunction(Method method) {
            this.method = method;
        }

        @Override // java.util.function.Function
        public Object apply(Object obj) {
            try {
                return this.method.invoke(obj, new Object[0]);
            } catch (Exception e) {
                throw new JSONException("createInstance error", e);
            }
        }
    }

    static final class ReflectLongFunction implements LongFunction {
        final Constructor constructor;

        public ReflectLongFunction(Constructor constructor) {
            this.constructor = constructor;
        }

        @Override // java.util.function.LongFunction
        public Object apply(long j) {
            try {
                return this.constructor.newInstance(Long.valueOf(j));
            } catch (Exception e) {
                throw new JSONException("createInstance error", e);
            }
        }
    }

    static final class ReflectToIntFunction implements ToIntFunction {
        final Method method;

        public ReflectToIntFunction(Method method) {
            this.method = method;
        }

        @Override // java.util.function.ToIntFunction
        public int applyAsInt(Object obj) {
            try {
                return ((Integer) this.method.invoke(obj, new Object[0])).intValue();
            } catch (Exception e) {
                throw new JSONException("applyAsInt error", e);
            }
        }
    }

    static final class ReflectToLongFunction implements ToLongFunction {
        final Method method;

        public ReflectToLongFunction(Method method) {
            this.method = method;
        }

        @Override // java.util.function.ToLongFunction
        public long applyAsLong(Object obj) {
            try {
                return ((Long) this.method.invoke(obj, new Object[0])).longValue();
            } catch (Exception e) {
                throw new JSONException("applyAsLong error", e);
            }
        }
    }
}
