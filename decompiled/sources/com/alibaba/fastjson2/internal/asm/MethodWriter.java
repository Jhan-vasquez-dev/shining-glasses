package com.alibaba.fastjson2.internal.asm;

import androidx.core.provider.FontsContractCompat;
import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONException;
import org.apache.http.HttpStatus;

/* JADX INFO: loaded from: classes.dex */
public final class MethodWriter {
    private final int accessFlags;
    private final ByteVector code;
    private Label currentBasicBlock;
    private int[] currentFrame;
    private final String descriptor;
    private final int descriptorIndex;
    private final Label firstBasicBlock;
    boolean hasAsmInstructions;
    private Label lastBasicBlock;
    private int lastBytecodeOffset;
    private int maxLocals;
    private int maxStack;
    MethodWriter mv;
    private final String name;
    private final int nameIndex;
    private int[] previousFrame;
    private ByteVector stackMapTableEntries;
    int stackMapTableNumberOfEntries;
    private final SymbolTable symbolTable;

    MethodWriter(SymbolTable symbolTable, int i, String str, String str2, int i2) {
        this.symbolTable = symbolTable;
        this.accessFlags = "<init>".equals(str) ? 262144 | i : i;
        this.nameIndex = symbolTable.addConstantUtf8(str);
        this.name = str;
        this.descriptorIndex = symbolTable.addConstantUtf8(str2);
        this.descriptor = str2;
        this.code = new ByteVector(i2);
        int argumentsAndReturnSizes = Type.getArgumentsAndReturnSizes(str2) >> 2;
        this.maxLocals = (i & 8) != 0 ? argumentsAndReturnSizes - 1 : argumentsAndReturnSizes;
        Label label = new Label();
        this.firstBasicBlock = label;
        visitLabel(label);
    }

    public void return_() {
        visitInsn(Opcodes.RETURN);
    }

    public void areturn() {
        visitInsn(Opcodes.ARETURN);
    }

    public void iconst_0() {
        visitInsn(3);
    }

    public void iconst_1() {
        visitInsn(4);
    }

    public void iconst_2() {
        visitInsn(5);
    }

    public void iconst_3() {
        visitInsn(6);
    }

    public void iconst_4() {
        visitInsn(7);
    }

    public void iconst_5() {
        visitInsn(8);
    }

    public void iconst_n(int i) {
        switch (i) {
            case -1:
                iconst_m1();
                break;
            case 0:
                iconst_0();
                break;
            case 1:
                iconst_1();
                break;
            case 2:
                iconst_2();
                break;
            case 3:
                iconst_3();
                break;
            case 4:
                iconst_4();
                break;
            case 5:
                iconst_5();
                break;
            default:
                if (i >= -128 && i < 127) {
                    bipush(i);
                } else if (i >= -32768 && i < 32767) {
                    sipush(i);
                } else {
                    visitLdcInsn(i);
                }
                break;
        }
    }

    public void iconst_m1() {
        visitInsn(2);
    }

    public void lconst_0() {
        visitInsn(9);
    }

    public void lcmp() {
        visitInsn(Opcodes.LCMP);
    }

    public void land() {
        visitInsn(127);
    }

    public void iadd() {
        visitInsn(96);
    }

    public void imul() {
        visitInsn(104);
    }

    public void bastore() {
        visitInsn(84);
    }

    public void castore() {
        visitInsn(85);
    }

    public void aconst_null() {
        visitInsn(1);
    }

    public void ixor() {
        visitInsn(Opcodes.IXOR);
    }

    public void ineg() {
        visitInsn(116);
    }

    public void lor() {
        visitInsn(Opcodes.LOR);
    }

    public void swap() {
        visitInsn(95);
    }

    public void arraylength() {
        visitInsn(Opcodes.ARRAYLENGTH);
    }

    public void l2i() {
        visitInsn(Opcodes.L2I);
    }

    public void i2l() {
        visitInsn(Opcodes.I2L);
    }

    public void i2f() {
        visitInsn(Opcodes.I2F);
    }

    public void i2d() {
        visitInsn(Opcodes.I2D);
    }

    public void cmpWithZero(Class<?> cls) {
        if (cls == Long.TYPE) {
            visitInsn(9);
            visitInsn(Opcodes.LCMP);
        } else if (cls == Double.TYPE) {
            visitInsn(14);
            visitInsn(Opcodes.DCMPL);
        } else if (cls == Float.TYPE) {
            visitInsn(11);
            visitInsn(Opcodes.FCMPL);
        }
    }

    public void lxor() {
        visitInsn(Opcodes.LXOR);
    }

    public void lushr() {
        visitInsn(125);
    }

    public void aaload() {
        visitInsn(50);
    }

    public void aastore() {
        visitInsn(83);
    }

    private void visitInsn(int i) {
        this.lastBytecodeOffset = this.code.length;
        this.code.putByte(i);
        Label label = this.currentBasicBlock;
        if (label != null) {
            label.frame.execute(i, 0, null, null);
            if ((i < 172 || i > 177) && i != 191) {
                return;
            }
            endCurrentBasicBlockWithNoSuccessor();
        }
    }

    public void sipush(int i) {
        visitIntInsn(17, i);
    }

    public void bipush(int i) {
        visitIntInsn(16, i);
    }

    private void visitIntInsn(int i, int i2) {
        this.lastBytecodeOffset = this.code.length;
        if (i == 17) {
            this.code.put12(i, i2);
        } else {
            this.code.put11(i, i2);
        }
        Label label = this.currentBasicBlock;
        if (label != null) {
            label.frame.execute(i, i2, null, null);
        }
    }

    public void loadLocal(Class<?> cls, int i) {
        if (cls == Byte.TYPE || cls == Short.TYPE || cls == Integer.TYPE || cls == Character.TYPE || cls == Boolean.TYPE) {
            visitVarInsn(21, i);
            return;
        }
        if (cls == Long.TYPE) {
            visitVarInsn(22, i);
            return;
        }
        if (cls == Float.TYPE) {
            visitVarInsn(23, i);
        } else if (cls == Double.TYPE) {
            visitVarInsn(24, i);
        } else {
            visitVarInsn(25, i);
        }
    }

    public void storeLocal(Class<?> cls, int i) {
        if (cls == Byte.TYPE || cls == Short.TYPE || cls == Integer.TYPE || cls == Character.TYPE || cls == Boolean.TYPE) {
            visitVarInsn(54, i);
            return;
        }
        if (cls == Long.TYPE) {
            visitVarInsn(55, i);
            return;
        }
        if (cls == Float.TYPE) {
            visitVarInsn(56, i);
        } else if (cls == Double.TYPE) {
            visitVarInsn(57, i);
        } else {
            visitVarInsn(58, i);
        }
    }

    public void aload(int i) {
        visitVarInsn(25, i);
    }

    public void astore(int i) {
        visitVarInsn(58, i);
    }

    public void iload(int i) {
        visitVarInsn(21, i);
    }

    public void istore(int i) {
        visitVarInsn(54, i);
    }

    public void lload(int i) {
        visitVarInsn(22, i);
    }

    public void lstore(int i) {
        visitVarInsn(55, i);
    }

    public void fstore(int i) {
        visitVarInsn(56, i);
    }

    public void dstore(int i) {
        visitVarInsn(57, i);
    }

    public void dup() {
        visitInsn(89);
    }

    public void dup(Class<?> cls) {
        if (cls == Long.TYPE || cls == Double.TYPE) {
            visitInsn(92);
        } else {
            visitInsn(89);
        }
    }

    public void dup2() {
        visitInsn(92);
    }

    public void pop() {
        visitInsn(87);
    }

    public void visitVarInsn(int i, int i2) {
        this.lastBytecodeOffset = this.code.length;
        if (i2 < 4 && i != 169) {
            this.code.putByte((i < 54 ? ((i - 21) << 2) + 26 : ((i - 54) << 2) + 59) + i2);
        } else if (i2 >= 256) {
            this.code.putByte(196).put12(i, i2);
        } else {
            this.code.put11(i, i2);
        }
        Label label = this.currentBasicBlock;
        if (label != null) {
            label.frame.execute(i, i2, null, null);
        }
        int i3 = (i == 22 || i == 24 || i == 55 || i == 57) ? i2 + 2 : i2 + 1;
        if (i3 > this.maxLocals) {
            this.maxLocals = i3;
        }
    }

    public void checkcast(String str) {
        visitTypeInsn(Opcodes.CHECKCAST, str);
    }

    public void new_(String str) {
        visitTypeInsn(Opcodes.NEW, str);
    }

    public void anewArray(String str) {
        visitTypeInsn(Opcodes.ANEWARRAY, str);
    }

    public void instanceOf(String str) {
        visitTypeInsn(Opcodes.INSTANCEOF, str);
    }

    private void visitTypeInsn(int i, String str) {
        this.lastBytecodeOffset = this.code.length;
        Symbol symbolAddConstantUtf8Reference = this.symbolTable.addConstantUtf8Reference(7, str);
        this.code.put12(i, symbolAddConstantUtf8Reference.index);
        Label label = this.currentBasicBlock;
        if (label != null) {
            label.frame.execute(i, this.lastBytecodeOffset, symbolAddConstantUtf8Reference, this.symbolTable);
        }
    }

    public void getfield(String str, String str2, String str3) {
        visitFieldInsn(Opcodes.GETFIELD, str, str2, str3);
    }

    public void putfield(String str, String str2, String str3) {
        visitFieldInsn(Opcodes.PUTFIELD, str, str2, str3);
    }

    public void getstatic(String str, String str2, String str3) {
        visitFieldInsn(Opcodes.GETSTATIC, str, str2, str3);
    }

    private void visitFieldInsn(int i, String str, String str2, String str3) {
        this.lastBytecodeOffset = this.code.length;
        Symbol symbolAddConstantMemberReference = this.symbolTable.addConstantMemberReference(9, str, str2, str3);
        this.code.put12(i, symbolAddConstantMemberReference.index);
        Label label = this.currentBasicBlock;
        if (label != null) {
            label.frame.execute(i, 0, symbolAddConstantMemberReference, this.symbolTable);
        }
    }

    public void invokestatic(String str, String str2, String str3) {
        visitMethodInsn(Opcodes.INVOKESTATIC, str, str2, str3, false);
    }

    public void invokestatic(String str, String str2, String str3, boolean z) {
        visitMethodInsn(Opcodes.INVOKESTATIC, str, str2, str3, z);
    }

    public void invokevirtual(String str, String str2, String str3) {
        visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, str2, str3, false);
    }

    public void invokeinterface(String str, String str2, String str3) {
        visitMethodInsn(Opcodes.INVOKEINTERFACE, str, str2, str3, true);
    }

    public void invokespecial(String str, String str2, String str3) {
        visitMethodInsn(Opcodes.INVOKESPECIAL, str, str2, str3, false);
    }

    private void visitMethodInsn(int i, String str, String str2, String str3, boolean z) {
        this.lastBytecodeOffset = this.code.length;
        Symbol symbolAddConstantMemberReference = this.symbolTable.addConstantMemberReference(z ? 11 : 10, str, str2, str3);
        if (i == 185) {
            this.code.put12(Opcodes.INVOKEINTERFACE, symbolAddConstantMemberReference.index).put11(symbolAddConstantMemberReference.getArgumentsAndReturnSizes() >> 2, 0);
        } else {
            this.code.put12(i, symbolAddConstantMemberReference.index);
        }
        Label label = this.currentBasicBlock;
        if (label != null) {
            label.frame.execute(i, 0, symbolAddConstantMemberReference, this.symbolTable);
        }
    }

    public void ifeq(Label label) {
        visitJumpInsn(Opcodes.IFEQ, label);
    }

    public void ifne(Label label) {
        visitJumpInsn(Opcodes.IFNE, label);
    }

    public void ifge(Label label) {
        visitJumpInsn(Opcodes.IFGE, label);
    }

    public void ifnull(Label label) {
        visitJumpInsn(Opcodes.IFNULL, label);
    }

    public void if_icmpeq(Label label) {
        visitJumpInsn(Opcodes.IF_ICMPEQ, label);
    }

    public void if_acmpeq(Label label) {
        visitJumpInsn(Opcodes.IF_ACMPEQ, label);
    }

    public void if_acmpne(Label label) {
        visitJumpInsn(Opcodes.IF_ACMPNE, label);
    }

    public void goto_(Label label) {
        visitJumpInsn(Opcodes.GOTO, label);
    }

    public void ifnonnull(Label label) {
        visitJumpInsn(Opcodes.IFNONNULL, label);
    }

    public void if_icmpge(Label label) {
        visitJumpInsn(Opcodes.IF_ICMPGE, label);
    }

    public void if_icmple(Label label) {
        visitJumpInsn(Opcodes.IF_ICMPLE, label);
    }

    public void if_icmpne(Label label) {
        visitJumpInsn(Opcodes.IF_ICMPNE, label);
    }

    private void visitJumpInsn(int i, Label label) {
        boolean z;
        this.lastBytecodeOffset = this.code.length;
        int i2 = i >= 200 ? i - 33 : i;
        if ((label.flags & 4) == 0 || label.bytecodeOffset - this.code.length >= -32768) {
            if (i2 != i) {
                this.code.putByte(i);
                ByteVector byteVector = this.code;
                label.put(byteVector, byteVector.length - 1, true);
            } else {
                this.code.putByte(i2);
                ByteVector byteVector2 = this.code;
                label.put(byteVector2, byteVector2.length - 1, false);
            }
            z = false;
        } else {
            if (i2 == 167) {
                this.code.putByte(200);
            } else if (i2 == 168) {
                this.code.putByte(HttpStatus.SC_CREATED);
            } else {
                this.code.putByte(i2 >= 198 ? i2 ^ 1 : ((i2 + 1) ^ 1) - 1);
                this.code.putShort(8);
                this.code.putByte(220);
                this.hasAsmInstructions = true;
                z = true;
                ByteVector byteVector3 = this.code;
                label.put(byteVector3, byteVector3.length - 1, true);
            }
            z = false;
            ByteVector byteVector32 = this.code;
            label.put(byteVector32, byteVector32.length - 1, true);
        }
        Label label2 = this.currentBasicBlock;
        if (label2 != null) {
            label2.frame.execute(i2, 0, null, null);
            Label canonicalInstance = label.getCanonicalInstance();
            canonicalInstance.flags = (short) (canonicalInstance.flags | 2);
            addSuccessorToCurrentBasicBlock(label);
            Label label3 = i2 != 167 ? new Label() : null;
            if (label3 != null) {
                if (z) {
                    label3.flags = (short) (label3.flags | 2);
                }
                visitLabel(label3);
            }
            if (i2 == 167) {
                endCurrentBasicBlockWithNoSuccessor();
            }
        }
    }

    public void visitLabel(Label label) {
        this.hasAsmInstructions |= label.resolve(this.code.data, this.code.length);
        if ((label.flags & 1) != 0) {
            return;
        }
        if (this.currentBasicBlock != null) {
            if (label.bytecodeOffset == this.currentBasicBlock.bytecodeOffset) {
                Label label2 = this.currentBasicBlock;
                label2.flags = (short) (label2.flags | (label.flags & 2));
                label.frame = this.currentBasicBlock.frame;
                return;
            }
            addSuccessorToCurrentBasicBlock(label);
        }
        if (this.lastBasicBlock != null) {
            if (label.bytecodeOffset == this.lastBasicBlock.bytecodeOffset) {
                Label label3 = this.lastBasicBlock;
                label3.flags = (short) (label3.flags | (label.flags & 2));
                label.frame = this.lastBasicBlock.frame;
                this.currentBasicBlock = this.lastBasicBlock;
                return;
            }
            this.lastBasicBlock.nextBasicBlock = label;
        }
        this.lastBasicBlock = label;
        this.currentBasicBlock = label;
        label.frame = new Frame(label);
    }

    public void visitLdcInsn(String str) {
        this.lastBytecodeOffset = this.code.length;
        Symbol symbolAddConstantUtf8Reference = this.symbolTable.addConstantUtf8Reference(8, str);
        int i = symbolAddConstantUtf8Reference.index;
        if (i >= 256) {
            this.code.put12(19, i);
        } else {
            this.code.put11(18, i);
        }
        Label label = this.currentBasicBlock;
        if (label != null) {
            label.frame.execute(18, 0, symbolAddConstantUtf8Reference, this.symbolTable);
        }
    }

    public void visitLdcInsn(Class cls) {
        Symbol symbolAddConstantUtf8Reference;
        String strDesc = ASMUtils.desc(cls);
        Type typeInternal = Type.getTypeInternal(strDesc, 0, strDesc.length());
        this.lastBytecodeOffset = this.code.length;
        if ((typeInternal.sort == 12 ? 10 : typeInternal.sort) == 10) {
            symbolAddConstantUtf8Reference = this.symbolTable.addConstantUtf8Reference(7, typeInternal.valueBuffer.substring(typeInternal.valueBegin, typeInternal.valueEnd));
        } else {
            symbolAddConstantUtf8Reference = this.symbolTable.addConstantUtf8Reference(7, typeInternal.getDescriptor());
        }
        int i = symbolAddConstantUtf8Reference.index;
        if (i >= 256) {
            this.code.put12(19, i);
        } else {
            this.code.put11(18, i);
        }
        Label label = this.currentBasicBlock;
        if (label != null) {
            label.frame.execute(18, 0, symbolAddConstantUtf8Reference, this.symbolTable);
        }
    }

    public void visitLdcInsn(Number number) {
        if (number instanceof Integer) {
            visitLdcInsn(number.intValue());
        } else {
            if (number instanceof Long) {
                visitLdcInsn(number.longValue());
                return;
            }
            throw new UnsupportedOperationException(number.getClass().getName());
        }
    }

    public void visitLdcInsn(int i) {
        this.lastBytecodeOffset = this.code.length;
        Symbol symbolAddConstantIntegerOrFloat = this.symbolTable.addConstantIntegerOrFloat(i);
        int i2 = symbolAddConstantIntegerOrFloat.index;
        if (i2 >= 256) {
            this.code.put12(19, i2);
        } else {
            this.code.put11(18, i2);
        }
        Label label = this.currentBasicBlock;
        if (label != null) {
            label.frame.execute(18, 0, symbolAddConstantIntegerOrFloat, this.symbolTable);
        }
    }

    public void visitLdcInsn(long j) {
        this.lastBytecodeOffset = this.code.length;
        Symbol symbolAddConstantLongOrDouble = this.symbolTable.addConstantLongOrDouble(j);
        this.code.put12(20, symbolAddConstantLongOrDouble.index);
        Label label = this.currentBasicBlock;
        if (label != null) {
            label.frame.execute(18, 0, symbolAddConstantLongOrDouble, this.symbolTable);
        }
    }

    public void visitIincInsn(int i, int i2) {
        this.lastBytecodeOffset = this.code.length;
        if (i > 255 || i2 > 127 || i2 < -128) {
            this.code.putByte(196).put12(Opcodes.IINC, i).putShort(i2);
        } else {
            this.code.putByte(Opcodes.IINC).put11(i, i2);
        }
        Label label = this.currentBasicBlock;
        if (label != null) {
            label.frame.execute(Opcodes.IINC, i, null, null);
        }
    }

    public void visitLookupSwitchInsn(Label label, int[] iArr, Label[] labelArr) {
        this.lastBytecodeOffset = this.code.length;
        this.code.putByte(Opcodes.LOOKUPSWITCH).putByteArray(null, 0, (4 - (this.code.length % 4)) % 4);
        label.put(this.code, this.lastBytecodeOffset, true);
        this.code.putInt(labelArr.length);
        for (int i = 0; i < labelArr.length; i++) {
            this.code.putInt(iArr[i]);
            labelArr[i].put(this.code, this.lastBytecodeOffset, true);
        }
        visitSwitchInsn(label, labelArr);
    }

    private void visitSwitchInsn(Label label, Label[] labelArr) {
        Label label2 = this.currentBasicBlock;
        if (label2 != null) {
            label2.frame.execute(Opcodes.LOOKUPSWITCH, 0, null, null);
            addSuccessorToCurrentBasicBlock(label);
            Label canonicalInstance = label.getCanonicalInstance();
            canonicalInstance.flags = (short) (canonicalInstance.flags | 2);
            for (Label label3 : labelArr) {
                addSuccessorToCurrentBasicBlock(label3);
                Label canonicalInstance2 = label3.getCanonicalInstance();
                canonicalInstance2.flags = (short) (canonicalInstance2.flags | 2);
            }
            endCurrentBasicBlockWithNoSuccessor();
        }
    }

    public void visitMaxs(int i, int i2) {
        Frame frame = this.firstBasicBlock.frame;
        frame.setInputFrameFromDescriptor(this.symbolTable, this.accessFlags, this.descriptor, this.maxLocals);
        frame.accept(this);
        Label label = this.firstBasicBlock;
        label.nextListElement = Label.EMPTY_LIST;
        int iMax = 0;
        while (label != Label.EMPTY_LIST) {
            Label label2 = label.nextListElement;
            label.nextListElement = null;
            label.flags = (short) (label.flags | 8);
            int length = label.frame.inputStack.length + label.outputStackMax;
            if (length > iMax) {
                iMax = length;
            }
            for (Edge edge = label.outgoingEdges; edge != null; edge = edge.nextEdge) {
                Label canonicalInstance = edge.successor.getCanonicalInstance();
                if (label.frame.merge(this.symbolTable, canonicalInstance.frame) && canonicalInstance.nextListElement == null) {
                    canonicalInstance.nextListElement = label2;
                    label2 = canonicalInstance;
                }
            }
            label = label2;
        }
        for (Label label3 = this.firstBasicBlock; label3 != null; label3 = label3.nextBasicBlock) {
            if ((label3.flags & 10) == 10) {
                label3.frame.accept(this);
            }
            if ((label3.flags & 8) == 0) {
                Label label4 = label3.nextBasicBlock;
                int i3 = label3.bytecodeOffset;
                int i4 = (label4 == null ? this.code.length : label4.bytecodeOffset) - 1;
                if (i4 >= i3) {
                    for (int i5 = i3; i5 < i4; i5++) {
                        this.code.data[i5] = 0;
                    }
                    this.code.data[i4] = JSONB.Constants.BC_INT64_INT;
                    this.currentFrame[visitFrameStart(i3, 0, 1)] = this.symbolTable.addType("java/lang/Throwable") | 8388608;
                    visitFrameEnd();
                    iMax = Math.max(iMax, 1);
                }
            }
        }
        this.maxStack = iMax;
    }

    private void addSuccessorToCurrentBasicBlock(Label label) {
        this.currentBasicBlock.outgoingEdges = new Edge(label, this.currentBasicBlock.outgoingEdges);
    }

    private void endCurrentBasicBlockWithNoSuccessor() {
        Label label = new Label();
        label.frame = new Frame(label);
        label.resolve(this.code.data, this.code.length);
        this.lastBasicBlock.nextBasicBlock = label;
        this.lastBasicBlock = label;
        this.currentBasicBlock = null;
    }

    int visitFrameStart(int i, int i2, int i3) {
        int i4 = i2 + 3 + i3;
        int[] iArr = this.currentFrame;
        if (iArr == null || iArr.length < i4) {
            this.currentFrame = new int[i4];
        }
        int[] iArr2 = this.currentFrame;
        iArr2[0] = i;
        iArr2[1] = i2;
        iArr2[2] = i3;
        return 3;
    }

    void visitAbstractType(int i, int i2) {
        this.currentFrame[i] = i2;
    }

    void visitFrameEnd() {
        if (this.previousFrame != null) {
            if (this.stackMapTableEntries == null) {
                this.stackMapTableEntries = new ByteVector(2048);
            }
            putFrame();
            this.stackMapTableNumberOfEntries++;
        }
        this.previousFrame = this.currentFrame;
        this.currentFrame = null;
    }

    private void putFrame() {
        int i;
        char c;
        int i2;
        int[] iArr = this.currentFrame;
        int i3 = iArr[1];
        int i4 = iArr[2];
        if (this.stackMapTableNumberOfEntries == 0) {
            i = iArr[0];
        } else {
            i = (iArr[0] - this.previousFrame[0]) - 1;
        }
        int i5 = this.previousFrame[1];
        int i6 = i3 - i5;
        if (i4 == 0) {
            switch (i6) {
                case FontsContractCompat.FontRequestCallback.FAIL_REASON_FONT_LOAD_ERROR /* -3 */:
                case -2:
                case -1:
                    c = 248;
                    break;
                case 0:
                    c = i < 64 ? (char) 0 : (char) 251;
                    break;
                case 1:
                case 2:
                case 3:
                    c = 252;
                    break;
                default:
                    c = 255;
                    break;
            }
        } else {
            c = (i6 == 0 && i4 == 1) ? i < 63 ? '@' : (char) 247 : (char) 255;
        }
        if (c != 255) {
            int i7 = 3;
            for (int i8 = 0; i8 < i5 && i8 < i3; i8++) {
                i2 = 3;
                if (this.currentFrame[i7] != this.previousFrame[i7]) {
                    c = 255;
                } else {
                    i7++;
                }
            }
            i2 = 3;
        } else {
            i2 = 3;
        }
        if (c == 0) {
            this.stackMapTableEntries.putByte(i);
            return;
        }
        if (c == '@') {
            this.stackMapTableEntries.putByte(i + 64);
            putAbstractTypes(i3 + 3, i3 + 4);
            return;
        }
        if (c == 247) {
            this.stackMapTableEntries.putByte(247).putShort(i);
            putAbstractTypes(i3 + 3, i3 + 4);
            return;
        }
        if (c == 248) {
            this.stackMapTableEntries.putByte(i6 + 251).putShort(i);
            return;
        }
        if (c == 251) {
            this.stackMapTableEntries.putByte(251).putShort(i);
            return;
        }
        if (c == 252) {
            int i9 = i2;
            this.stackMapTableEntries.putByte(i6 + 251).putShort(i);
            putAbstractTypes(i5 + i9, i3 + i9);
        } else {
            this.stackMapTableEntries.putByte(255).putShort(i).putShort(i3);
            int i10 = i3 + 3;
            putAbstractTypes(i2, i10);
            this.stackMapTableEntries.putShort(i4);
            putAbstractTypes(i10, i4 + i10);
        }
    }

    private void putAbstractTypes(int i, int i2) {
        while (i < i2) {
            int i3 = this.currentFrame[i];
            ByteVector byteVector = this.stackMapTableEntries;
            int i4 = ((-67108864) & i3) >> 26;
            if (i4 == 0) {
                int i5 = i3 & 1048575;
                int i6 = i3 & 62914560;
                if (i6 == 4194304) {
                    byteVector.putByte(i5);
                } else if (i6 == 8388608) {
                    ByteVector byteVectorPutByte = byteVector.putByte(7);
                    SymbolTable symbolTable = this.symbolTable;
                    byteVectorPutByte.putShort(symbolTable.addConstantUtf8Reference(7, symbolTable.typeTable[i5].value).index);
                } else if (i6 == 12582912) {
                    byteVector.putByte(8).putShort((int) this.symbolTable.typeTable[i5].data);
                } else {
                    throw new AssertionError();
                }
            } else {
                StringBuilder sb = new StringBuilder();
                while (true) {
                    int i7 = i4 - 1;
                    if (i4 > 0) {
                        sb.append('[');
                        i4 = i7;
                    } else {
                        if ((i3 & 62914560) == 8388608) {
                            sb.append('L').append(this.symbolTable.typeTable[i3 & 1048575].value).append(';');
                        } else {
                            int i8 = i3 & 1048575;
                            if (i8 == 1) {
                                sb.append('I');
                            } else if (i8 == 2) {
                                sb.append('F');
                            } else if (i8 == 3) {
                                sb.append('D');
                            } else if (i8 != 4) {
                                switch (i8) {
                                    case 9:
                                        sb.append('Z');
                                        break;
                                    case 10:
                                        sb.append('B');
                                        break;
                                    case 11:
                                        sb.append('C');
                                        break;
                                    case 12:
                                        sb.append('S');
                                        break;
                                    default:
                                        throw new AssertionError();
                                }
                            } else {
                                sb.append('J');
                            }
                        }
                        byteVector.putByte(7).putShort(this.symbolTable.addConstantUtf8Reference(7, sb.toString()).index);
                    }
                }
            }
            i++;
        }
    }

    int computeMethodInfoSize() {
        if (this.code.length <= 0) {
            return 8;
        }
        if (this.code.length > 65535) {
            throw new JSONException("Method too large: " + this.symbolTable.className + "." + this.name + " " + this.descriptor + ", length " + this.code.length);
        }
        this.symbolTable.addConstantUtf8("Code");
        int i = this.code.length + 26;
        if (this.stackMapTableEntries == null) {
            return i;
        }
        this.symbolTable.addConstantUtf8("StackMapTable");
        return i + this.stackMapTableEntries.length + 8;
    }

    void putMethodInfo(ByteVector byteVector) {
        byteVector.putShort(this.accessFlags).putShort(this.nameIndex).putShort(this.descriptorIndex);
        int i = 1;
        byteVector.putShort(this.code.length > 0 ? 1 : 0);
        if (this.code.length > 0) {
            int i2 = this.code.length + 12;
            ByteVector byteVector2 = this.stackMapTableEntries;
            if (byteVector2 != null) {
                i2 += byteVector2.length + 8;
            } else {
                i = 0;
            }
            byteVector.putShort(this.symbolTable.addConstantUtf8("Code")).putInt(i2).putShort(this.maxStack).putShort(this.maxLocals).putInt(this.code.length).putByteArray(this.code.data, 0, this.code.length);
            byteVector.putShort(0);
            byteVector.putShort(i);
            if (this.stackMapTableEntries != null) {
                byteVector.putShort(this.symbolTable.addConstantUtf8("StackMapTable")).putInt(this.stackMapTableEntries.length + 2).putShort(this.stackMapTableNumberOfEntries).putByteArray(this.stackMapTableEntries.data, 0, this.stackMapTableEntries.length);
            }
        }
    }
}
