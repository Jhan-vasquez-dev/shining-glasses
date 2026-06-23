package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
abstract class FieldWriterBoolean extends FieldWriter {
    final char[] utf16Value0;
    final char[] utf16Value1;
    final char[] utf16ValueFalse;
    final char[] utf16ValueTrue;
    final byte[] utf8Value0;
    final byte[] utf8Value1;
    final byte[] utf8ValueFalse;
    final byte[] utf8ValueTrue;

    FieldWriterBoolean(String str, int i, long j, String str2, String str3, Type type, Class cls, Field field, Method method) {
        super(str, i, j, str2, null, str3, type, cls, field, method);
        byte[] bArrCopyOf = Arrays.copyOf(this.nameWithColonUTF8, this.nameWithColonUTF8.length + 4);
        bArrCopyOf[this.nameWithColonUTF8.length] = 116;
        bArrCopyOf[this.nameWithColonUTF8.length + 1] = 114;
        bArrCopyOf[this.nameWithColonUTF8.length + 2] = 117;
        bArrCopyOf[this.nameWithColonUTF8.length + 3] = 101;
        this.utf8ValueTrue = bArrCopyOf;
        byte[] bArrCopyOf2 = Arrays.copyOf(this.nameWithColonUTF8, this.nameWithColonUTF8.length + 5);
        bArrCopyOf2[this.nameWithColonUTF8.length] = 102;
        bArrCopyOf2[this.nameWithColonUTF8.length + 1] = 97;
        bArrCopyOf2[this.nameWithColonUTF8.length + 2] = 108;
        bArrCopyOf2[this.nameWithColonUTF8.length + 3] = 115;
        bArrCopyOf2[this.nameWithColonUTF8.length + 4] = 101;
        this.utf8ValueFalse = bArrCopyOf2;
        byte[] bArrCopyOf3 = Arrays.copyOf(this.nameWithColonUTF8, this.nameWithColonUTF8.length + 1);
        bArrCopyOf3[this.nameWithColonUTF8.length] = 49;
        this.utf8Value1 = bArrCopyOf3;
        byte[] bArrCopyOf4 = Arrays.copyOf(this.nameWithColonUTF8, this.nameWithColonUTF8.length + 1);
        bArrCopyOf4[this.nameWithColonUTF8.length] = JSONB.Constants.BC_INT32_BYTE_MIN;
        this.utf8Value0 = bArrCopyOf4;
        char[] cArrCopyOf = Arrays.copyOf(this.nameWithColonUTF16, this.nameWithColonUTF16.length + 4);
        cArrCopyOf[this.nameWithColonUTF16.length] = 't';
        cArrCopyOf[this.nameWithColonUTF16.length + 1] = 'r';
        cArrCopyOf[this.nameWithColonUTF16.length + 2] = 'u';
        cArrCopyOf[this.nameWithColonUTF16.length + 3] = 'e';
        this.utf16ValueTrue = cArrCopyOf;
        char[] cArrCopyOf2 = Arrays.copyOf(this.nameWithColonUTF16, this.nameWithColonUTF16.length + 5);
        cArrCopyOf2[this.nameWithColonUTF16.length] = 'f';
        cArrCopyOf2[this.nameWithColonUTF16.length + 1] = 'a';
        cArrCopyOf2[this.nameWithColonUTF16.length + 2] = 'l';
        cArrCopyOf2[this.nameWithColonUTF16.length + 3] = 's';
        cArrCopyOf2[this.nameWithColonUTF16.length + 4] = 'e';
        this.utf16ValueFalse = cArrCopyOf2;
        char[] cArrCopyOf3 = Arrays.copyOf(this.nameWithColonUTF16, this.nameWithColonUTF16.length + 1);
        cArrCopyOf3[this.nameWithColonUTF16.length] = '1';
        this.utf16Value1 = cArrCopyOf3;
        char[] cArrCopyOf4 = Arrays.copyOf(this.nameWithColonUTF16, this.nameWithColonUTF16.length + 1);
        cArrCopyOf4[this.nameWithColonUTF16.length] = '0';
        this.utf16Value0 = cArrCopyOf4;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, Object obj) {
        Boolean bool = (Boolean) getFieldValue(obj);
        if (bool == null) {
            jSONWriter.writeNull();
        } else {
            jSONWriter.writeBool(bool.booleanValue());
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public final void writeBool(JSONWriter jSONWriter, boolean z) {
        char[] cArr;
        byte[] bArr;
        long features = jSONWriter.getFeatures(this.features);
        if ((JSONWriter.Feature.WriteNonStringValueAsString.mask & features) != 0) {
            writeFieldName(jSONWriter);
            jSONWriter.writeString(z ? "true" : "false");
            return;
        }
        if (jSONWriter.utf8) {
            if ((features & JSONWriter.Feature.WriteBooleanAsNumber.mask) != 0) {
                bArr = z ? this.utf8Value1 : this.utf8Value0;
            } else {
                bArr = z ? this.utf8ValueTrue : this.utf8ValueFalse;
            }
            jSONWriter.writeNameRaw(bArr);
            return;
        }
        if (jSONWriter.utf16) {
            if ((features & JSONWriter.Feature.WriteBooleanAsNumber.mask) != 0) {
                cArr = z ? this.utf16Value1 : this.utf16Value0;
            } else {
                cArr = z ? this.utf16ValueTrue : this.utf16ValueFalse;
            }
            jSONWriter.writeNameRaw(cArr);
            return;
        }
        writeFieldName(jSONWriter);
        jSONWriter.writeBool(z);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, Object obj) {
        try {
            Boolean bool = (Boolean) getFieldValue(obj);
            if (bool == null) {
                if (((this.features | jSONWriter.getFeatures()) & (JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullBooleanAsFalse.mask)) == 0) {
                    return false;
                }
                writeFieldName(jSONWriter);
                jSONWriter.writeBooleanNull();
                return true;
            }
            if (this.fieldClass == Boolean.TYPE && !bool.booleanValue() && (jSONWriter.getFeatures(this.features) & JSONWriter.Feature.NotWriteDefaultValue.mask) != 0) {
                return false;
            }
            writeBool(jSONWriter, bool.booleanValue());
            return true;
        } catch (RuntimeException e) {
            if (jSONWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw e;
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public ObjectWriter getObjectWriter(JSONWriter jSONWriter, Class cls) {
        if (cls == this.fieldClass) {
            return ObjectWriterImplBoolean.INSTANCE;
        }
        return jSONWriter.getObjectWriter(cls);
    }
}
