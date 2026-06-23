package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.SymbolTable;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.IOUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
class FieldWriterEnum extends FieldWriter {
    final Enum[] enumConstants;
    final Class enumType;
    final long[] hashCodes;
    final long[] hashCodesSymbolCache;
    final char[][] utf16ValueCache;
    final byte[][] utf8ValueCache;
    final char[][] valueNameCacheUTF16;
    final byte[][] valueNameCacheUTF8;

    protected FieldWriterEnum(String str, int i, long j, String str2, String str3, Type type, Class<? extends Enum> cls, Field field, Method method) {
        super(str, i, j, str2, null, str3, type, cls, field, method);
        this.enumType = cls;
        Enum[] enumArr = (Enum[]) cls.getEnumConstants();
        this.enumConstants = enumArr;
        this.hashCodes = new long[enumArr.length];
        this.hashCodesSymbolCache = new long[enumArr.length];
        int i2 = 0;
        while (true) {
            Enum[] enumArr2 = this.enumConstants;
            if (i2 < enumArr2.length) {
                this.hashCodes[i2] = Fnv.hashCode64(enumArr2[i2].name());
                i2++;
            } else {
                this.valueNameCacheUTF8 = new byte[enumArr2.length][];
                this.valueNameCacheUTF16 = new char[enumArr2.length][];
                this.utf8ValueCache = new byte[enumArr2.length][];
                this.utf16ValueCache = new char[enumArr2.length][];
                return;
            }
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public final int writeEnumValueJSONB(byte[] bArr, int i, Enum r8, SymbolTable symbolTable, long j) {
        if (r8 == null) {
            bArr[i] = JSONB.Constants.BC_NULL;
            return i + 1;
        }
        long j2 = j | this.features;
        boolean z = ((JSONWriter.Feature.WriteEnumUsingToString.mask | JSONWriter.Feature.WriteEnumsUsingName.mask) & j2) == 0;
        String string = (j2 & JSONWriter.Feature.WriteEnumUsingToString.mask) != 0 ? r8.toString() : r8.name();
        if (IOUtils.isASCII(string)) {
            return JSONB.IO.writeSymbol(bArr, i, string, symbolTable);
        }
        if (z) {
            return JSONB.IO.writeInt32(bArr, i, r8.ordinal());
        }
        return JSONB.IO.writeString(bArr, i, string);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public final void writeEnumJSONB(JSONWriter jSONWriter, Enum r11) {
        int ordinalByHashCode;
        if (r11 == null) {
            return;
        }
        long features = jSONWriter.getFeatures(this.features);
        boolean z = ((JSONWriter.Feature.WriteEnumUsingToString.mask | JSONWriter.Feature.WriteEnumsUsingName.mask) & features) == 0;
        boolean z2 = (features & JSONWriter.Feature.WriteEnumUsingToString.mask) != 0;
        int iOrdinal = r11.ordinal();
        SymbolTable symbolTable = jSONWriter.symbolTable;
        if (symbolTable == null || !z || z2 || !writeSymbolNameOrdinal(jSONWriter, iOrdinal, symbolTable)) {
            if (z2) {
                writeJSONBToString(jSONWriter, r11, symbolTable);
                return;
            }
            if (z) {
                if (symbolTable != null) {
                    int iIdentityHashCode = System.identityHashCode(symbolTable);
                    if (this.nameSymbolCache == 0) {
                        ordinalByHashCode = symbolTable.getOrdinalByHashCode(this.hashCode);
                        this.nameSymbolCache = (((long) ordinalByHashCode) << 32) | ((long) iIdentityHashCode);
                    } else if (((int) this.nameSymbolCache) == iIdentityHashCode) {
                        ordinalByHashCode = (int) (this.nameSymbolCache >> 32);
                    } else {
                        ordinalByHashCode = symbolTable.getOrdinalByHashCode(this.hashCode);
                        this.nameSymbolCache = (((long) ordinalByHashCode) << 32) | ((long) iIdentityHashCode);
                    }
                } else {
                    ordinalByHashCode = -1;
                }
                if (ordinalByHashCode != -1) {
                    jSONWriter.writeSymbol(-ordinalByHashCode);
                } else {
                    jSONWriter.writeNameRaw(this.nameJSONB, this.hashCode);
                }
                jSONWriter.writeInt32(iOrdinal);
                return;
            }
            writeFieldName(jSONWriter);
            jSONWriter.writeString(r11.name());
        }
    }

    private boolean writeSymbolNameOrdinal(JSONWriter jSONWriter, int i, SymbolTable symbolTable) {
        int ordinalByHashCode;
        int ordinalByHashCode2;
        int iIdentityHashCode = System.identityHashCode(symbolTable);
        long j = this.hashCodesSymbolCache[i];
        if (j == 0) {
            ordinalByHashCode = symbolTable.getOrdinalByHashCode(this.hashCodes[i]);
            this.hashCodesSymbolCache[i] = (((long) ordinalByHashCode) << 32) | ((long) iIdentityHashCode);
        } else if (((int) j) == iIdentityHashCode) {
            ordinalByHashCode = (int) (j >> 32);
        } else {
            ordinalByHashCode = symbolTable.getOrdinalByHashCode(this.hashCodes[i]);
            this.hashCodesSymbolCache[i] = (((long) ordinalByHashCode) << 32) | ((long) iIdentityHashCode);
        }
        if (ordinalByHashCode < 0) {
            return false;
        }
        if (this.nameSymbolCache == 0) {
            ordinalByHashCode2 = symbolTable.getOrdinalByHashCode(this.hashCode);
            if (ordinalByHashCode2 != -1) {
                this.nameSymbolCache = (((long) ordinalByHashCode2) << 32) | ((long) iIdentityHashCode);
            }
        } else if (((int) this.nameSymbolCache) == iIdentityHashCode) {
            ordinalByHashCode2 = (int) (this.nameSymbolCache >> 32);
        } else {
            ordinalByHashCode2 = symbolTable.getOrdinalByHashCode(this.hashCode);
            this.nameSymbolCache = (((long) ordinalByHashCode2) << 32) | ((long) iIdentityHashCode);
        }
        if (ordinalByHashCode2 != -1) {
            jSONWriter.writeSymbol(-ordinalByHashCode2);
        } else {
            jSONWriter.writeNameRaw(this.nameJSONB, this.hashCode);
        }
        jSONWriter.writeRaw(JSONB.Constants.BC_STR_ASCII);
        jSONWriter.writeInt32(-ordinalByHashCode);
        return true;
    }

    private void writeJSONBToString(JSONWriter jSONWriter, Enum r8, SymbolTable symbolTable) {
        int ordinalByHashCode;
        if (symbolTable != null) {
            int iIdentityHashCode = System.identityHashCode(symbolTable);
            if (this.nameSymbolCache == 0) {
                ordinalByHashCode = symbolTable.getOrdinalByHashCode(this.hashCode);
                this.nameSymbolCache = (((long) ordinalByHashCode) << 32) | ((long) iIdentityHashCode);
            } else if (((int) this.nameSymbolCache) == iIdentityHashCode) {
                ordinalByHashCode = (int) (this.nameSymbolCache >> 32);
            } else {
                ordinalByHashCode = symbolTable.getOrdinalByHashCode(this.hashCode);
                this.nameSymbolCache = (((long) ordinalByHashCode) << 32) | ((long) iIdentityHashCode);
            }
        } else {
            ordinalByHashCode = -1;
        }
        if (ordinalByHashCode != -1) {
            jSONWriter.writeSymbol(-ordinalByHashCode);
        } else {
            jSONWriter.writeNameRaw(this.nameJSONB, this.hashCode);
        }
        jSONWriter.writeString(r8.toString());
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public final void writeEnum(JSONWriter jSONWriter, Enum r10) {
        long features = jSONWriter.getFeatures(this.features);
        if ((JSONWriter.Feature.WriteEnumUsingToString.mask & features) == 0) {
            if (jSONWriter.jsonb) {
                writeEnumJSONB(jSONWriter, r10);
                return;
            }
            int iOrdinal = r10.ordinal();
            if ((JSONWriter.Feature.WriteEnumUsingOrdinal.mask & features) != 0) {
                writeEnumUsingOrdinal(jSONWriter, iOrdinal);
                return;
            }
            if ((features & JSONWriter.Feature.UnquoteFieldName.mask) == 0) {
                if (jSONWriter.utf8) {
                    byte[][] bArr = this.valueNameCacheUTF8;
                    byte[] nameBytes = bArr[iOrdinal];
                    if (nameBytes == null) {
                        nameBytes = getNameBytes(iOrdinal);
                        bArr[iOrdinal] = nameBytes;
                    }
                    jSONWriter.writeNameRaw(nameBytes);
                    return;
                }
                if (jSONWriter.utf16) {
                    char[][] cArr = this.valueNameCacheUTF16;
                    char[] nameChars = cArr[iOrdinal];
                    if (nameChars == null) {
                        nameChars = getNameChars(iOrdinal);
                        cArr[iOrdinal] = nameChars;
                    }
                    jSONWriter.writeNameRaw(nameChars);
                    return;
                }
            }
        }
        writeFieldName(jSONWriter);
        jSONWriter.writeString(r10.toString());
    }

    private void writeEnumUsingOrdinal(JSONWriter jSONWriter, int i) {
        if ((this.features & JSONWriter.Feature.UnquoteFieldName.mask) == 0) {
            if (jSONWriter.utf8) {
                byte[][] bArr = this.utf8ValueCache;
                byte[] bytes = bArr[i];
                if (bytes == null) {
                    bytes = getBytes(i);
                    bArr[i] = bytes;
                }
                jSONWriter.writeNameRaw(bytes);
                return;
            }
            if (jSONWriter.utf16) {
                char[][] cArr = this.utf16ValueCache;
                char[] chars = cArr[i];
                if (chars == null) {
                    chars = getChars(i);
                    cArr[i] = chars;
                }
                jSONWriter.writeNameRaw(chars);
                return;
            }
        }
        writeFieldName(jSONWriter);
        jSONWriter.writeInt32(i);
    }

    private char[] getNameChars(int i) {
        String strName = this.enumConstants[i].name();
        char[] cArrCopyOf = Arrays.copyOf(this.nameWithColonUTF16, this.nameWithColonUTF16.length + strName.length() + 2);
        cArrCopyOf[this.nameWithColonUTF16.length] = '\"';
        strName.getChars(0, strName.length(), cArrCopyOf, this.nameWithColonUTF16.length + 1);
        cArrCopyOf[cArrCopyOf.length - 1] = '\"';
        return cArrCopyOf;
    }

    private byte[] getNameBytes(int i) {
        byte[] bytes = this.enumConstants[i].name().getBytes(StandardCharsets.UTF_8);
        byte[] bArrCopyOf = Arrays.copyOf(this.nameWithColonUTF8, this.nameWithColonUTF8.length + bytes.length + 2);
        bArrCopyOf[this.nameWithColonUTF8.length] = 34;
        int length = this.nameWithColonUTF8.length + 1;
        int length2 = bytes.length;
        int i2 = 0;
        while (i2 < length2) {
            bArrCopyOf[length] = bytes[i2];
            i2++;
            length++;
        }
        bArrCopyOf[bArrCopyOf.length - 1] = 34;
        return bArrCopyOf;
    }

    private char[] getChars(int i) {
        char[] cArrCopyOf = Arrays.copyOf(this.nameWithColonUTF16, this.nameWithColonUTF16.length + IOUtils.stringSize(i));
        char[] cArrCopyOf2 = Arrays.copyOf(cArrCopyOf, cArrCopyOf.length);
        IOUtils.getChars(i, cArrCopyOf2.length, cArrCopyOf2);
        return cArrCopyOf2;
    }

    private byte[] getBytes(int i) {
        byte[] bArrCopyOf = Arrays.copyOf(this.nameWithColonUTF8, this.nameWithColonUTF8.length + IOUtils.stringSize(i));
        byte[] bArrCopyOf2 = Arrays.copyOf(bArrCopyOf, bArrCopyOf.length);
        IOUtils.getChars(i, bArrCopyOf2.length, bArrCopyOf2);
        return bArrCopyOf2;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public final void writeValue(JSONWriter jSONWriter, Object obj) {
        jSONWriter.writeEnum((Enum) getFieldValue(obj));
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, Object obj) {
        Enum r7 = (Enum) getFieldValue(obj);
        if (r7 != null) {
            if (jSONWriter.jsonb) {
                writeEnumJSONB(jSONWriter, r7);
            } else {
                writeEnum(jSONWriter, r7);
            }
            return true;
        }
        if (((this.features | jSONWriter.getFeatures()) & JSONWriter.Feature.WriteNulls.mask) == 0) {
            return false;
        }
        writeFieldName(jSONWriter);
        jSONWriter.writeNull();
        return true;
    }
}
