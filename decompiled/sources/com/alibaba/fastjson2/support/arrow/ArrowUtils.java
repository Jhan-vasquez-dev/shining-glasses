package com.alibaba.fastjson2.support.arrow;

import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.support.csv.CSVWriter;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.arrow.memory.ArrowBuf;
import org.apache.arrow.vector.BigIntVector;
import org.apache.arrow.vector.BitVector;
import org.apache.arrow.vector.BitVectorHelper;
import org.apache.arrow.vector.DateMilliVector;
import org.apache.arrow.vector.Decimal256Vector;
import org.apache.arrow.vector.DecimalVector;
import org.apache.arrow.vector.FieldVector;
import org.apache.arrow.vector.Float4Vector;
import org.apache.arrow.vector.Float8Vector;
import org.apache.arrow.vector.IntVector;
import org.apache.arrow.vector.SmallIntVector;
import org.apache.arrow.vector.TimeStampMilliVector;
import org.apache.arrow.vector.TinyIntVector;
import org.apache.arrow.vector.VarCharVector;
import org.apache.arrow.vector.VectorSchemaRoot;

/* JADX INFO: loaded from: classes.dex */
public class ArrowUtils {
    static final byte DECIMAL_TYPE_WIDTH = 16;
    static final boolean LITTLE_ENDIAN;

    static {
        LITTLE_ENDIAN = ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN;
    }

    public static void write(CSVWriter cSVWriter, VectorSchemaRoot vectorSchemaRoot) throws IOException {
        List fieldVectors = vectorSchemaRoot.getFieldVectors();
        int rowCount = vectorSchemaRoot.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            for (int i2 = 0; i2 < fieldVectors.size(); i2++) {
                if (i2 != 0) {
                    cSVWriter.writeComma();
                }
                IntVector intVector = (FieldVector) fieldVectors.get(i2);
                if (!intVector.isNull(i)) {
                    if (intVector instanceof IntVector) {
                        cSVWriter.writeInt32(intVector.get(i));
                    } else if (intVector instanceof BigIntVector) {
                        cSVWriter.writeInt64(((BigIntVector) intVector).get(i));
                    } else if (intVector instanceof VarCharVector) {
                        cSVWriter.writeString(((VarCharVector) intVector).get(i));
                    } else if (intVector instanceof DecimalVector) {
                        writeDecimal(cSVWriter, i, (DecimalVector) intVector);
                    } else if (intVector instanceof DateMilliVector) {
                        cSVWriter.writeDate(((DateMilliVector) intVector).get(i));
                    } else if (intVector instanceof Float8Vector) {
                        cSVWriter.writeDouble(((Float8Vector) intVector).get(i));
                    } else if (intVector instanceof Float4Vector) {
                        cSVWriter.writeFloat(((Float4Vector) intVector).get(i));
                    } else if (intVector instanceof SmallIntVector) {
                        cSVWriter.writeInt32(((SmallIntVector) intVector).get(i));
                    } else if (intVector instanceof TinyIntVector) {
                        cSVWriter.writeInt32(((TinyIntVector) intVector).get(i));
                    } else if (intVector instanceof BitVector) {
                        cSVWriter.writeInt32(((BitVector) intVector).get(i));
                    } else if (intVector instanceof Decimal256Vector) {
                        cSVWriter.writeString(intVector.getObject(i).toString());
                    } else {
                        throw new JSONException("TODO : " + intVector.getClass().getName());
                    }
                }
            }
            cSVWriter.writeLine();
        }
    }

    private static void writeDecimal(CSVWriter cSVWriter, int i, DecimalVector decimalVector) {
        long jReverseBytes;
        int precision = decimalVector.getPrecision();
        decimalVector.getObject(i);
        if (precision < 20) {
            long j = ((long) i) * 16;
            int scale = decimalVector.getScale();
            ArrowBuf dataBuffer = decimalVector.getDataBuffer();
            if (LITTLE_ENDIAN) {
                jReverseBytes = dataBuffer.getLong(j);
            } else {
                jReverseBytes = Long.reverseBytes(dataBuffer.getLong(j + 8));
            }
            cSVWriter.writeDecimal(jReverseBytes, scale);
            return;
        }
        cSVWriter.writeDecimal(decimalVector.getObject(i));
    }

    public static void setValue(FieldVector fieldVector, int i, String str) {
        if (str == null || str.isEmpty()) {
            return;
        }
        if (fieldVector instanceof IntVector) {
            ((IntVector) fieldVector).set(i, Integer.parseInt(str));
            return;
        }
        if (fieldVector instanceof BigIntVector) {
            ((BigIntVector) fieldVector).set(i, Long.parseLong(str));
            return;
        }
        if (fieldVector instanceof DecimalVector) {
            setDecimal((DecimalVector) fieldVector, i, str);
            return;
        }
        if (fieldVector instanceof DateMilliVector) {
            ((DateMilliVector) fieldVector).set(i, DateUtils.parseMillis(str));
            return;
        }
        if (fieldVector instanceof VarCharVector) {
            setString((VarCharVector) fieldVector, i, str);
            return;
        }
        if (fieldVector instanceof Float8Vector) {
            ((Float8Vector) fieldVector).set(i, Double.parseDouble(str));
            return;
        }
        if (fieldVector instanceof Float4Vector) {
            ((Float4Vector) fieldVector).set(i, Float.parseFloat(str));
            return;
        }
        if (fieldVector instanceof TinyIntVector) {
            ((TinyIntVector) fieldVector).set(i, (byte) Integer.parseInt(str));
            return;
        }
        if (fieldVector instanceof SmallIntVector) {
            ((SmallIntVector) fieldVector).set(i, (short) Integer.parseInt(str));
            return;
        }
        if (fieldVector instanceof TimeStampMilliVector) {
            ((TimeStampMilliVector) fieldVector).set(i, DateUtils.parseMillis(str));
            return;
        }
        if (fieldVector instanceof BitVector) {
            ((BitVector) fieldVector).set(i, Boolean.parseBoolean(str) ? 1 : 0);
            return;
        }
        if (fieldVector instanceof Decimal256Vector) {
            BigDecimal bigDecimal = TypeUtils.toBigDecimal(str);
            Decimal256Vector decimal256Vector = (Decimal256Vector) fieldVector;
            int scale = decimal256Vector.getScale();
            if (bigDecimal.scale() != scale) {
                bigDecimal = bigDecimal.setScale(scale, RoundingMode.CEILING);
            }
            decimal256Vector.set(i, bigDecimal);
            return;
        }
        throw new JSONException("TODO " + fieldVector.getClass());
    }

    public static void setDecimal(DecimalVector decimalVector, int i, String str) {
        if (str == null || str.isEmpty()) {
            decimalVector.setNull(i);
            return;
        }
        if (JDKUtils.STRING_CODER != null && JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER.applyAsInt(str) == 0) {
            byte[] bArrApply = JDKUtils.STRING_VALUE.apply(str);
            setDecimal(decimalVector, i, bArrApply, 0, bArrApply.length);
        }
        char[] charArray = JDKUtils.getCharArray(str);
        setDecimal(decimalVector, i, charArray, 0, charArray.length);
    }

    public static void setString(VarCharVector varCharVector, int i, String str) {
        byte[] bytes;
        if (str == null || str.length() == 0) {
            varCharVector.setNull(i);
            return;
        }
        if (JDKUtils.STRING_CODER != null && JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER.applyAsInt(str) == 0) {
            bytes = JDKUtils.STRING_VALUE.apply(str);
        } else {
            bytes = str.getBytes(StandardCharsets.UTF_8);
        }
        varCharVector.set(i, bytes);
    }

    public static void setDecimal(DecimalVector decimalVector, int i, char[] cArr, int i2, int i3) {
        int i4;
        boolean z;
        long j;
        long j2;
        long j3;
        boolean z2 = true;
        if (cArr[i2] == '-') {
            i4 = i2 + 1;
            z = true;
        } else {
            i4 = i2;
            z = false;
        }
        if (i3 <= 20 || (z && i3 == 21)) {
            int i5 = i2 + i3;
            int i6 = -1;
            int i7 = 0;
            long j4 = 0;
            while (true) {
                if (i4 >= i5) {
                    j = 0;
                    j2 = 10;
                    break;
                }
                char c = cArr[i4];
                j2 = 10;
                if (c == '.') {
                    i7++;
                    if (i7 > 1) {
                        j = 0;
                        break;
                    } else {
                        i6 = i4;
                        i4++;
                    }
                } else {
                    if (c < '0' || c > '9') {
                        break;
                    }
                    j4 = (j4 * 10) + ((long) (c - '0'));
                    i4++;
                }
            }
            j = 0;
            j4 = -1;
            if (j4 >= j && i7 <= 1) {
                int i8 = i6 != -1 ? (i3 - (i6 - i2)) - 1 : 0;
                int scale = decimalVector.getScale();
                if (scale > i8) {
                    j3 = j4;
                    for (int i9 = i8; i9 < scale; i9++) {
                        j3 *= j2;
                        if (j3 < j) {
                            break;
                        }
                    }
                    z2 = false;
                } else {
                    j3 = j4;
                    if (scale >= i8) {
                        z2 = false;
                    }
                }
                if (!z2) {
                    if (z) {
                        j3 = -j3;
                    }
                    long j5 = i;
                    BitVectorHelper.setBit(decimalVector.getValidityBuffer(), j5);
                    ArrowBuf dataBuffer = decimalVector.getDataBuffer();
                    long j6 = j5 * 16;
                    if (LITTLE_ENDIAN) {
                        dataBuffer.setLong(j6, j3);
                        return;
                    } else {
                        dataBuffer.setLong(j6, j);
                        dataBuffer.setLong(j6 + 8, Long.reverseBytes(j3));
                        return;
                    }
                }
                if (z) {
                    j4 = -j4;
                }
                BigDecimal bigDecimalValueOf = BigDecimal.valueOf(j4, i8);
                if (decimalVector.getScale() != bigDecimalValueOf.scale()) {
                    bigDecimalValueOf = bigDecimalValueOf.setScale(decimalVector.getScale(), RoundingMode.CEILING);
                }
                decimalVector.set(i, bigDecimalValueOf);
                return;
            }
        }
        BigDecimal bigDecimal = TypeUtils.parseBigDecimal(cArr, i2, i3);
        if (decimalVector.getScale() != bigDecimal.scale()) {
            bigDecimal = bigDecimal.setScale(decimalVector.getScale(), RoundingMode.CEILING);
        }
        decimalVector.set(i, bigDecimal);
    }

    public static void setDecimal(DecimalVector decimalVector, int i, byte[] bArr, int i2, int i3) {
        int i4;
        boolean z;
        long j;
        long j2;
        long j3;
        boolean z2 = true;
        if (bArr[i2] == 45) {
            i4 = i2 + 1;
            z = true;
        } else {
            i4 = i2;
            z = false;
        }
        if (i3 <= 20 || (z && i3 == 21)) {
            int i5 = i2 + i3;
            int i6 = -1;
            int i7 = 0;
            long j4 = 0;
            while (true) {
                if (i4 >= i5) {
                    j = 0;
                    j2 = 10;
                    break;
                }
                byte b = bArr[i4];
                j2 = 10;
                if (b == 46) {
                    i7++;
                    if (i7 > 1) {
                        j = 0;
                        break;
                    } else {
                        i6 = i4;
                        i4++;
                    }
                } else {
                    if (b < 48 || b > 57) {
                        break;
                    }
                    j4 = (j4 * 10) + ((long) (b + JSONB.Constants.BC_INT64_BYTE_ZERO));
                    i4++;
                }
            }
            j = 0;
            j4 = -1;
            if (j4 >= j && i7 <= 1) {
                int i8 = i6 != -1 ? (i3 - (i6 - i2)) - 1 : 0;
                int scale = decimalVector.getScale();
                if (scale > i8) {
                    j3 = j4;
                    for (int i9 = i8; i9 < scale; i9++) {
                        j3 *= j2;
                        if (j3 < j) {
                            break;
                        }
                    }
                    z2 = false;
                } else {
                    j3 = j4;
                    if (scale >= i8) {
                        z2 = false;
                    }
                }
                if (!z2) {
                    if (z) {
                        j3 = -j3;
                    }
                    long j5 = i;
                    BitVectorHelper.setBit(decimalVector.getValidityBuffer(), j5);
                    ArrowBuf dataBuffer = decimalVector.getDataBuffer();
                    long j6 = j5 * 16;
                    if (LITTLE_ENDIAN) {
                        dataBuffer.setLong(j6, j3);
                        return;
                    } else {
                        dataBuffer.setLong(j6, j);
                        dataBuffer.setLong(j6 + 8, Long.reverseBytes(j3));
                        return;
                    }
                }
                if (z) {
                    j4 = -j4;
                }
                BigDecimal bigDecimalValueOf = BigDecimal.valueOf(j4, i8);
                if (decimalVector.getScale() != bigDecimalValueOf.scale()) {
                    bigDecimalValueOf = bigDecimalValueOf.setScale(decimalVector.getScale(), RoundingMode.CEILING);
                }
                decimalVector.set(i, bigDecimalValueOf);
                return;
            }
        }
        BigDecimal bigDecimal = TypeUtils.parseBigDecimal(bArr, i2, i3);
        if (decimalVector.getScale() != bigDecimal.scale()) {
            bigDecimal = bigDecimal.setScale(decimalVector.getScale(), RoundingMode.CEILING);
        }
        decimalVector.set(i, bigDecimal);
    }

    public static void setDecimal(DecimalVector decimalVector, int i, BigDecimal bigDecimal) {
        int scale = decimalVector.getScale();
        if (bigDecimal.scale() != scale) {
            bigDecimal = bigDecimal.setScale(scale, RoundingMode.CEILING);
        }
        if (bigDecimal.precision() < 19 && JDKUtils.FIELD_DECIMAL_INT_COMPACT_OFFSET != -1) {
            long j = JDKUtils.UNSAFE.getLong(bigDecimal, JDKUtils.FIELD_DECIMAL_INT_COMPACT_OFFSET);
            if (j != Long.MIN_VALUE) {
                long j2 = i;
                BitVectorHelper.setBit(decimalVector.getValidityBuffer(), j2);
                ArrowBuf dataBuffer = decimalVector.getDataBuffer();
                long j3 = j2 * 16;
                if (LITTLE_ENDIAN) {
                    dataBuffer.setLong(j3, j);
                    return;
                }
                dataBuffer.setLong(j3, 0L);
                dataBuffer.setLong(j3 + 8, Long.reverseBytes(j));
                return;
            }
        }
        decimalVector.set(i, bigDecimal);
    }
}
