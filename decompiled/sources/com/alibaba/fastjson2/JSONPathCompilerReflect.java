package com.alibaba.fastjson2;

import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.FieldReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.writer.FieldWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import java.util.function.BiFunction;

/* JADX INFO: loaded from: classes.dex */
public class JSONPathCompilerReflect implements JSONFactory.JSONPathCompiler {
    static final JSONPathCompilerReflect INSTANCE = new JSONPathCompilerReflect();

    @Override // com.alibaba.fastjson2.JSONFactory.JSONPathCompiler
    public JSONPath compile(Class cls, JSONPath jSONPath) {
        if (jSONPath instanceof JSONPathSingleName) {
            return compileSingleNamePath(cls, (JSONPathSingleName) jSONPath);
        }
        if (!(jSONPath instanceof JSONPathTwoSegment)) {
            return jSONPath;
        }
        JSONPathTwoSegment jSONPathTwoSegment = (JSONPathTwoSegment) jSONPath;
        JSONPathSegment jSONPathSegmentCompile = compile(cls, jSONPath, jSONPathTwoSegment.first, null);
        JSONPathSegment jSONPathSegmentCompile2 = compile(cls, jSONPath, jSONPathTwoSegment.second, jSONPathSegmentCompile);
        if (jSONPathSegmentCompile == jSONPathTwoSegment.first && jSONPathSegmentCompile2 == jSONPathTwoSegment.second) {
            return jSONPath;
        }
        if ((jSONPathSegmentCompile instanceof NameSegmentTyped) && (jSONPathSegmentCompile2 instanceof NameSegmentTyped)) {
            return new TwoNameSegmentTypedPath(jSONPathTwoSegment.path, (NameSegmentTyped) jSONPathSegmentCompile, (NameSegmentTyped) jSONPathSegmentCompile2);
        }
        return new JSONPathTwoSegment(jSONPathTwoSegment.path, jSONPathSegmentCompile, jSONPathSegmentCompile2, new JSONPath.Feature[0]);
    }

    protected JSONPath compileSingleNamePath(Class cls, JSONPathSingleName jSONPathSingleName) {
        String str = jSONPathSingleName.name;
        ObjectReader objectReader = jSONPathSingleName.getReaderContext().getObjectReader(cls);
        FieldReader fieldReader = objectReader.getFieldReader(str);
        ObjectWriter objectWriter = jSONPathSingleName.getWriterContext().getObjectWriter(cls);
        return new SingleNamePathTyped(jSONPathSingleName.path, cls, objectReader, fieldReader, objectWriter, objectWriter.getFieldWriter(str));
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0030  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0036  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0039  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x003f  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0055  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected com.alibaba.fastjson2.JSONPathSegment compile(java.lang.Class r10, com.alibaba.fastjson2.JSONPath r11, com.alibaba.fastjson2.JSONPathSegment r12, com.alibaba.fastjson2.JSONPathSegment r13) {
        /*
            r9 = this;
            boolean r0 = r12 instanceof com.alibaba.fastjson2.JSONPathSegmentName
            if (r0 == 0) goto L63
            com.alibaba.fastjson2.JSONPathSegmentName r12 = (com.alibaba.fastjson2.JSONPathSegmentName) r12
            java.lang.String r6 = r12.name
            com.alibaba.fastjson2.JSONReader$Context r0 = r11.getReaderContext()
            com.alibaba.fastjson2.JSONWriter$Context r11 = r11.getWriterContext()
            r1 = 0
            if (r13 != 0) goto L19
            com.alibaba.fastjson2.reader.ObjectReader r0 = r0.getObjectReader(r10)
        L17:
            r2 = r0
            goto L2e
        L19:
            boolean r2 = r13 instanceof com.alibaba.fastjson2.JSONPathCompilerReflect.NameSegmentTyped
            if (r2 == 0) goto L2d
            r2 = r13
            com.alibaba.fastjson2.JSONPathCompilerReflect$NameSegmentTyped r2 = (com.alibaba.fastjson2.JSONPathCompilerReflect.NameSegmentTyped) r2
            com.alibaba.fastjson2.reader.FieldReader r3 = r2.fieldReader
            if (r3 == 0) goto L2d
            com.alibaba.fastjson2.reader.FieldReader r2 = r2.fieldReader
            java.lang.reflect.Type r2 = r2.fieldType
            com.alibaba.fastjson2.reader.ObjectReader r0 = r0.getObjectReader(r2)
            goto L17
        L2d:
            r2 = r1
        L2e:
            if (r2 == 0) goto L36
            com.alibaba.fastjson2.reader.FieldReader r0 = r2.getFieldReader(r6)
            r3 = r0
            goto L37
        L36:
            r3 = r1
        L37:
            if (r13 != 0) goto L3f
            com.alibaba.fastjson2.writer.ObjectWriter r11 = r11.getObjectWriter(r10)
        L3d:
            r4 = r11
            goto L53
        L3f:
            boolean r0 = r13 instanceof com.alibaba.fastjson2.JSONPathCompilerReflect.NameSegmentTyped
            if (r0 == 0) goto L52
            com.alibaba.fastjson2.JSONPathCompilerReflect$NameSegmentTyped r13 = (com.alibaba.fastjson2.JSONPathCompilerReflect.NameSegmentTyped) r13
            com.alibaba.fastjson2.writer.FieldWriter r0 = r13.fieldWriter
            if (r0 == 0) goto L52
            com.alibaba.fastjson2.writer.FieldWriter r13 = r13.fieldWriter
            java.lang.Class r13 = r13.fieldClass
            com.alibaba.fastjson2.writer.ObjectWriter r11 = r11.getObjectWriter(r13)
            goto L3d
        L52:
            r4 = r1
        L53:
            if (r4 == 0) goto L59
            com.alibaba.fastjson2.writer.FieldWriter r1 = r4.getFieldWriter(r6)
        L59:
            r5 = r1
            com.alibaba.fastjson2.JSONPathCompilerReflect$NameSegmentTyped r0 = new com.alibaba.fastjson2.JSONPathCompilerReflect$NameSegmentTyped
            long r7 = r12.nameHashCode
            r1 = r10
            r0.<init>(r1, r2, r3, r4, r5, r6, r7)
            return r0
        L63:
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONPathCompilerReflect.compile(java.lang.Class, com.alibaba.fastjson2.JSONPath, com.alibaba.fastjson2.JSONPathSegment, com.alibaba.fastjson2.JSONPathSegment):com.alibaba.fastjson2.JSONPathSegment");
    }

    public static class TwoNameSegmentTypedPath extends JSONPathTwoSegment {
        final NameSegmentTyped first;
        final NameSegmentTyped second;

        @Override // com.alibaba.fastjson2.JSONPathTwoSegment, com.alibaba.fastjson2.JSONPath
        public /* bridge */ /* synthetic */ boolean contains(Object obj) {
            return super.contains(obj);
        }

        @Override // com.alibaba.fastjson2.JSONPathTwoSegment, com.alibaba.fastjson2.JSONPath
        public /* bridge */ /* synthetic */ boolean endsWithFilter() {
            return super.endsWithFilter();
        }

        @Override // com.alibaba.fastjson2.JSONPathTwoSegment, com.alibaba.fastjson2.JSONPath
        public /* bridge */ /* synthetic */ Object extract(JSONReader jSONReader) {
            return super.extract(jSONReader);
        }

        @Override // com.alibaba.fastjson2.JSONPathTwoSegment, com.alibaba.fastjson2.JSONPath
        public /* bridge */ /* synthetic */ String extractScalar(JSONReader jSONReader) {
            return super.extractScalar(jSONReader);
        }

        @Override // com.alibaba.fastjson2.JSONPathTwoSegment, com.alibaba.fastjson2.JSONPath
        public /* bridge */ /* synthetic */ JSONPath getParent() {
            return super.getParent();
        }

        @Override // com.alibaba.fastjson2.JSONPathTwoSegment, com.alibaba.fastjson2.JSONPath
        public /* bridge */ /* synthetic */ boolean isRef() {
            return super.isRef();
        }

        @Override // com.alibaba.fastjson2.JSONPathTwoSegment, com.alibaba.fastjson2.JSONPath
        public /* bridge */ /* synthetic */ boolean remove(Object obj) {
            return super.remove(obj);
        }

        @Override // com.alibaba.fastjson2.JSONPathTwoSegment, com.alibaba.fastjson2.JSONPath
        public /* bridge */ /* synthetic */ void set(Object obj, Object obj2, JSONReader.Feature[] featureArr) {
            super.set(obj, obj2, featureArr);
        }

        public TwoNameSegmentTypedPath(String str, NameSegmentTyped nameSegmentTyped, NameSegmentTyped nameSegmentTyped2) {
            super(str, nameSegmentTyped, nameSegmentTyped2, new JSONPath.Feature[0]);
            this.first = nameSegmentTyped;
            this.second = nameSegmentTyped2;
        }

        @Override // com.alibaba.fastjson2.JSONPathTwoSegment, com.alibaba.fastjson2.JSONPath
        public Object eval(Object obj) {
            Object fieldValue = this.first.fieldWriter.getFieldValue(obj);
            if (fieldValue == null) {
                return null;
            }
            return this.second.fieldWriter.getFieldValue(fieldValue);
        }

        @Override // com.alibaba.fastjson2.JSONPathTwoSegment, com.alibaba.fastjson2.JSONPath
        public void set(Object obj, Object obj2) {
            Object fieldValue = this.first.fieldWriter.getFieldValue(obj);
            if (fieldValue == null) {
                return;
            }
            this.second.fieldReader.accept(fieldValue, obj2);
        }

        @Override // com.alibaba.fastjson2.JSONPathTwoSegment, com.alibaba.fastjson2.JSONPath
        public void setInt(Object obj, int i) {
            Object fieldValue = this.first.fieldWriter.getFieldValue(obj);
            if (fieldValue == null) {
                return;
            }
            this.second.fieldReader.accept(fieldValue, i);
        }

        @Override // com.alibaba.fastjson2.JSONPathTwoSegment, com.alibaba.fastjson2.JSONPath
        public void setLong(Object obj, long j) {
            Object fieldValue = this.first.fieldWriter.getFieldValue(obj);
            if (fieldValue == null) {
                return;
            }
            this.second.fieldReader.accept(fieldValue, j);
        }

        @Override // com.alibaba.fastjson2.JSONPathTwoSegment, com.alibaba.fastjson2.JSONPath
        public void setCallback(Object obj, BiFunction biFunction) {
            Object fieldValue;
            Object objApply;
            Object fieldValue2 = this.first.fieldWriter.getFieldValue(obj);
            if (fieldValue2 == null || (objApply = biFunction.apply(fieldValue2, (fieldValue = this.second.fieldWriter.getFieldValue(fieldValue2)))) == fieldValue) {
                return;
            }
            if (this.second.fieldReader == null) {
                throw new UnsupportedOperationException();
            }
            this.second.fieldReader.accept(fieldValue2, objApply);
        }
    }

    public static class NameSegmentTyped extends JSONPathSegmentName {
        final FieldReader fieldReader;
        final FieldWriter fieldWriter;
        final Class objectClass;

        @Override // com.alibaba.fastjson2.JSONPathSegmentName, com.alibaba.fastjson2.JSONPathSegment
        public /* bridge */ /* synthetic */ void accept(JSONReader jSONReader, JSONPath.Context context) {
            super.accept(jSONReader, context);
        }

        @Override // com.alibaba.fastjson2.JSONPathSegmentName, com.alibaba.fastjson2.JSONPathSegment
        public /* bridge */ /* synthetic */ boolean contains(JSONPath.Context context) {
            return super.contains(context);
        }

        @Override // com.alibaba.fastjson2.JSONPathSegmentName
        public /* bridge */ /* synthetic */ boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override // com.alibaba.fastjson2.JSONPathSegmentName
        public /* bridge */ /* synthetic */ int hashCode() {
            return super.hashCode();
        }

        @Override // com.alibaba.fastjson2.JSONPathSegmentName, com.alibaba.fastjson2.JSONPathSegment
        public /* bridge */ /* synthetic */ boolean remove(JSONPath.Context context) {
            return super.remove(context);
        }

        @Override // com.alibaba.fastjson2.JSONPathSegmentName, com.alibaba.fastjson2.JSONPathSegment
        public /* bridge */ /* synthetic */ void set(JSONPath.Context context, Object obj) {
            super.set(context, obj);
        }

        @Override // com.alibaba.fastjson2.JSONPathSegmentName, com.alibaba.fastjson2.JSONPathSegment
        public /* bridge */ /* synthetic */ void setCallback(JSONPath.Context context, BiFunction biFunction) {
            super.setCallback(context, biFunction);
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public /* bridge */ /* synthetic */ void setInt(JSONPath.Context context, int i) {
            super.setInt(context, i);
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public /* bridge */ /* synthetic */ void setLong(JSONPath.Context context, long j) {
            super.setLong(context, j);
        }

        @Override // com.alibaba.fastjson2.JSONPathSegmentName
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        public NameSegmentTyped(Class cls, ObjectReader objectReader, FieldReader fieldReader, ObjectWriter objectWriter, FieldWriter fieldWriter, String str, long j) {
            super(str, j);
            this.objectClass = cls;
            this.fieldReader = fieldReader;
            this.fieldWriter = fieldWriter;
        }

        @Override // com.alibaba.fastjson2.JSONPathSegmentName, com.alibaba.fastjson2.JSONPathSegment
        public void eval(JSONPath.Context context) {
            Object obj;
            if (this.fieldWriter == null) {
                throw new UnsupportedOperationException();
            }
            if (context.parent == null) {
                obj = context.root;
            } else {
                obj = context.parent.value;
            }
            if (obj == null) {
                return;
            }
            context.value = this.fieldWriter.getFieldValue(obj);
        }
    }

    public static class SingleNamePathTyped extends JSONPath {
        final FieldReader fieldReader;
        final FieldWriter fieldWriter;
        final Class objectClass;
        final ObjectReader objectReader;
        final ObjectWriter objectWriter;

        @Override // com.alibaba.fastjson2.JSONPath
        public JSONPath getParent() {
            return null;
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public boolean isRef() {
            return true;
        }

        public SingleNamePathTyped(String str, Class cls, ObjectReader objectReader, FieldReader fieldReader, ObjectWriter objectWriter, FieldWriter fieldWriter) {
            super(str, new JSONPath.Feature[0]);
            this.objectClass = cls;
            this.objectReader = objectReader;
            this.fieldReader = fieldReader;
            this.objectWriter = objectWriter;
            this.fieldWriter = fieldWriter;
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public boolean contains(Object obj) {
            FieldWriter fieldWriter = this.fieldWriter;
            return (fieldWriter == null || fieldWriter.getFieldValue(obj) == null) ? false : true;
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public Object eval(Object obj) {
            FieldWriter fieldWriter = this.fieldWriter;
            if (fieldWriter == null) {
                throw new UnsupportedOperationException();
            }
            return fieldWriter.getFieldValue(obj);
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public Object extract(JSONReader jSONReader) {
            throw new UnsupportedOperationException();
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public String extractScalar(JSONReader jSONReader) {
            throw new UnsupportedOperationException();
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public void set(Object obj, Object obj2) {
            FieldReader fieldReader = this.fieldReader;
            if (fieldReader == null) {
                throw new UnsupportedOperationException();
            }
            fieldReader.accept(obj, obj2);
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public void set(Object obj, Object obj2, JSONReader.Feature... featureArr) {
            FieldReader fieldReader = this.fieldReader;
            if (fieldReader == null) {
                throw new UnsupportedOperationException();
            }
            fieldReader.accept(obj, obj2);
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public void setCallback(Object obj, BiFunction biFunction) {
            FieldWriter fieldWriter = this.fieldWriter;
            if (fieldWriter == null) {
                throw new UnsupportedOperationException();
            }
            Object fieldValue = fieldWriter.getFieldValue(obj);
            Object objApply = biFunction.apply(obj, fieldValue);
            if (objApply == fieldValue) {
                return;
            }
            FieldReader fieldReader = this.fieldReader;
            if (fieldReader == null) {
                throw new UnsupportedOperationException();
            }
            fieldReader.accept(obj, objApply);
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public void setInt(Object obj, int i) {
            FieldReader fieldReader = this.fieldReader;
            if (fieldReader == null) {
                throw new UnsupportedOperationException();
            }
            fieldReader.accept(obj, i);
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public void setLong(Object obj, long j) {
            FieldReader fieldReader = this.fieldReader;
            if (fieldReader == null) {
                throw new UnsupportedOperationException();
            }
            fieldReader.accept(obj, j);
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public boolean remove(Object obj) {
            throw new UnsupportedOperationException();
        }
    }
}
