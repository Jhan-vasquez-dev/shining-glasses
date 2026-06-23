package com.alibaba.fastjson2.support.arrow;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.reader.ByteArrayValueConsumer;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.BigIntVector;
import org.apache.arrow.vector.BitVector;
import org.apache.arrow.vector.DateMilliVector;
import org.apache.arrow.vector.Decimal256Vector;
import org.apache.arrow.vector.DecimalVector;
import org.apache.arrow.vector.FixedWidthVector;
import org.apache.arrow.vector.Float4Vector;
import org.apache.arrow.vector.Float8Vector;
import org.apache.arrow.vector.IntVector;
import org.apache.arrow.vector.SmallIntVector;
import org.apache.arrow.vector.TimeStampMilliVector;
import org.apache.arrow.vector.TinyIntVector;
import org.apache.arrow.vector.VarCharVector;
import org.apache.arrow.vector.VariableWidthVector;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.types.pojo.Schema;

/* JADX INFO: loaded from: classes.dex */
public class ArrowByteArrayConsumer implements ByteArrayValueConsumer {
    static final int CHUNK_SIZE = 1000000;
    int blockRowIndex;
    int blockSize;
    final Consumer<Long[]> committer;
    VectorSchemaRoot root;
    final ObjIntConsumer<VectorSchemaRoot> rootConsumer;
    final int rowCount;
    final Schema schema;
    int[] valueCapacities;
    final int varcharValueSize = 128;
    int blockIndex = -1;
    BufferAllocator allocator = new RootAllocator();

    public ArrowByteArrayConsumer(Schema schema, int i, ObjIntConsumer<VectorSchemaRoot> objIntConsumer, Consumer<Long[]> consumer) {
        this.schema = schema;
        this.rowCount = i;
        this.rootConsumer = objIntConsumer;
        this.committer = consumer;
        allocateNew(Math.min(1000000, i));
    }

    @Override // com.alibaba.fastjson2.reader.ByteArrayValueConsumer
    public void afterRow(int i) {
        int i2 = this.blockRowIndex + 1;
        this.blockRowIndex = i2;
        if (i2 == this.blockSize) {
            List fields = this.root.getSchema().getFields();
            for (int i3 = 0; i3 < fields.size(); i3++) {
                this.root.getVector(i3).setValueCount(this.blockSize);
            }
            this.rootConsumer.accept(this.root, this.blockIndex);
            this.root.close();
            int i4 = i + 1;
            int i5 = this.rowCount;
            if (i4 != i5) {
                if (i < i5) {
                    allocateNew(Math.min((i5 - i) - 1, 1000000));
                }
            } else if (this.committer != null) {
                Long[] lArr = new Long[this.blockIndex + 1];
                for (int i6 = 0; i6 <= this.blockIndex; i6++) {
                    lArr[i6] = Long.valueOf(i6);
                }
                this.committer.accept(lArr);
            }
        }
    }

    public void allocateNew(int i) {
        VectorSchemaRoot vectorSchemaRootCreate = VectorSchemaRoot.create(this.schema, this.allocator);
        this.root = vectorSchemaRootCreate;
        this.blockSize = i;
        this.blockRowIndex = 0;
        vectorSchemaRootCreate.setRowCount(i);
        int size = this.root.getSchema().getFields().size();
        this.valueCapacities = new int[size];
        for (int i2 = 0; i2 < size; i2++) {
            FixedWidthVector vector = this.root.getVector(i2);
            if (vector instanceof FixedWidthVector) {
                vector.allocateNew(i);
            } else if (vector instanceof VariableWidthVector) {
                VariableWidthVector variableWidthVector = (VariableWidthVector) vector;
                variableWidthVector.allocateNew(((long) i) * 128, i);
                this.valueCapacities[i2] = variableWidthVector.getValueCapacity();
            } else {
                throw new JSONException("TODO");
            }
        }
        this.blockIndex++;
    }

    @Override // com.alibaba.fastjson2.reader.ByteArrayValueConsumer
    public void accept(int i, int i2, byte[] bArr, int i3, int i4, Charset charset) {
        if (i2 >= this.root.getSchema().getFields().size()) {
            return;
        }
        IntVector vector = this.root.getVector(i2);
        if (i4 == 0) {
            return;
        }
        int i5 = this.blockRowIndex;
        if (vector instanceof IntVector) {
            vector.set(i5, TypeUtils.parseInt(bArr, i3, i4));
            return;
        }
        if (vector instanceof BigIntVector) {
            ((BigIntVector) vector).set(i5, TypeUtils.parseLong(bArr, i3, i4));
            return;
        }
        if (vector instanceof VarCharVector) {
            VarCharVector varCharVector = (VarCharVector) vector;
            int i6 = this.valueCapacities[i2];
            if (varCharVector.getStartOffset(i5) + i4 >= i6) {
                int iMax = i6 + Math.max(i4, this.rowCount * 128);
                varCharVector.reallocDataBuffer(iMax);
                this.valueCapacities[i2] = iMax;
            }
            varCharVector.set(i5, bArr, i3, i4);
            return;
        }
        if (vector instanceof DecimalVector) {
            ArrowUtils.setDecimal((DecimalVector) vector, i5, bArr, i3, i4);
            return;
        }
        if (vector instanceof Decimal256Vector) {
            BigDecimal bigDecimal = TypeUtils.parseBigDecimal(bArr, i3, i4);
            Decimal256Vector decimal256Vector = (Decimal256Vector) vector;
            int scale = decimal256Vector.getScale();
            if (bigDecimal.scale() != scale) {
                bigDecimal = bigDecimal.setScale(scale, RoundingMode.CEILING);
            }
            decimal256Vector.set(i5, bigDecimal);
            return;
        }
        if (vector instanceof SmallIntVector) {
            ((SmallIntVector) vector).set(i5, TypeUtils.parseInt(bArr, i3, i4));
            return;
        }
        if (vector instanceof TinyIntVector) {
            ((TinyIntVector) vector).set(i5, TypeUtils.parseInt(bArr, i3, i4));
            return;
        }
        if (vector instanceof Float4Vector) {
            ((Float4Vector) vector).set(i5, TypeUtils.parseFloat(bArr, i3, i4));
            return;
        }
        if (vector instanceof Float8Vector) {
            ((Float8Vector) vector).set(i5, TypeUtils.parseFloat(bArr, i3, i4));
            return;
        }
        if (vector instanceof DateMilliVector) {
            ((DateMilliVector) vector).set(i5, DateUtils.parseMillis(bArr, i3, i4, charset));
        } else if (vector instanceof TimeStampMilliVector) {
            ((TimeStampMilliVector) vector).set(i5, DateUtils.parseMillis(bArr, i3, i4, charset));
        } else {
            if (vector instanceof BitVector) {
                Boolean bool = TypeUtils.parseBoolean(bArr, i3, i4);
                if (bool != null) {
                    ((BitVector) vector).set(i5, bool.booleanValue() ? 1 : 0);
                    return;
                }
                return;
            }
            throw new JSONException("TODO : " + vector.getClass().getName());
        }
    }
}
