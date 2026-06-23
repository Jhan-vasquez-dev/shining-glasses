package com.alibaba.fastjson2.internal.asm;

/* JADX INFO: loaded from: classes.dex */
public final class FieldWriter {
    private final int accessFlags;
    private final int descriptorIndex;
    FieldWriter fv;
    private final int nameIndex;

    FieldWriter(SymbolTable symbolTable, int i, String str, String str2) {
        this.accessFlags = i;
        this.nameIndex = symbolTable.addConstantUtf8(str);
        this.descriptorIndex = symbolTable.addConstantUtf8(str2);
    }

    void putFieldInfo(ByteVector byteVector) {
        byteVector.putShort(this.accessFlags).putShort(this.nameIndex).putShort(this.descriptorIndex);
        byteVector.putShort(0);
    }
}
