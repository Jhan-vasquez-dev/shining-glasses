package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.util.GuavaSupport;
import com.alibaba.fastjson2.util.MapMultiValueType;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public class ObjectReaderImplMapMultiValueType implements ObjectReader {
    final Function builder;
    final Class instanceType;
    final Class mapType;
    final MapMultiValueType multiValueType;

    public ObjectReaderImplMapMultiValueType(MapMultiValueType mapMultiValueType) {
        this.multiValueType = mapMultiValueType;
        Class mapType = mapMultiValueType.getMapType();
        this.mapType = mapType;
        Function functionSingletonBiMapConverter = null;
        if (mapType == Map.class || mapType == AbstractMap.class || mapType == ObjectReaderImplMap.CLASS_SINGLETON_MAP) {
            mapType = HashMap.class;
        } else if (mapType == ObjectReaderImplMap.CLASS_UNMODIFIABLE_MAP) {
            mapType = LinkedHashMap.class;
        } else if (mapType == SortedMap.class || mapType == ObjectReaderImplMap.CLASS_UNMODIFIABLE_SORTED_MAP || mapType == ObjectReaderImplMap.CLASS_UNMODIFIABLE_NAVIGABLE_MAP) {
            mapType = TreeMap.class;
        } else if (mapType == ConcurrentMap.class) {
            mapType = ConcurrentHashMap.class;
        } else if (mapType == ConcurrentNavigableMap.class) {
            mapType = ConcurrentSkipListMap.class;
        } else {
            String typeName = mapType.getTypeName();
            typeName.hashCode();
            switch (typeName) {
                case "java.util.Collections$SynchronizedSortedMap":
                    mapType = TreeMap.class;
                    functionSingletonBiMapConverter = new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderImplMapMultiValueType$$ExternalSyntheticLambda2
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return Collections.synchronizedSortedMap((SortedMap) obj);
                        }
                    };
                    break;
                case "com.google.common.collect.SingletonImmutableBiMap":
                    mapType = HashMap.class;
                    functionSingletonBiMapConverter = GuavaSupport.singletonBiMapConverter();
                    break;
                case "java.util.Collections$SynchronizedMap":
                    mapType = HashMap.class;
                    functionSingletonBiMapConverter = new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderImplMapMultiValueType$$ExternalSyntheticLambda0
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return Collections.synchronizedMap((Map) obj);
                        }
                    };
                    break;
                case "java.util.Collections$SynchronizedNavigableMap":
                    mapType = TreeMap.class;
                    functionSingletonBiMapConverter = new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderImplMapMultiValueType$$ExternalSyntheticLambda1
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return Collections.synchronizedNavigableMap((NavigableMap) obj);
                        }
                    };
                    break;
                case "com.google.common.collect.ImmutableMap":
                case "com.google.common.collect.RegularImmutableMap":
                    mapType = HashMap.class;
                    functionSingletonBiMapConverter = GuavaSupport.immutableMapConverter();
                    break;
            }
        }
        this.instanceType = mapType;
        this.builder = functionSingletonBiMapConverter;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object createInstance(long j) {
        Class cls = this.instanceType;
        if (cls != null && !cls.isInterface()) {
            try {
                return this.instanceType.newInstance();
            } catch (Exception e) {
                throw new JSONException("create map error", e);
            }
        }
        return new HashMap();
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:23:0x0074
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public java.lang.Object readObject(com.alibaba.fastjson2.JSONReader r15, java.lang.reflect.Type r16, java.lang.Object r17, long r18) {
        /*
            Method dump skipped, instruction units count: 261
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderImplMapMultiValueType.readObject(com.alibaba.fastjson2.JSONReader, java.lang.reflect.Type, java.lang.Object, long):java.lang.Object");
    }
}
