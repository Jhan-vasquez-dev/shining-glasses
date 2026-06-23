package com.alibaba.fastjson2.schema;

import com.alibaba.fastjson2.JSONPathFunction$IndexDecimal$$ExternalSyntheticBackportWithForwarding0;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class EnumSchema extends JSONSchema {
    final Set<Object> items;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v3, types: [java.math.BigDecimal] */
    /* JADX WARN: Type inference failed for: r2v4, types: [java.math.BigInteger] */
    /* JADX WARN: Type inference failed for: r2v6, types: [java.lang.Long] */
    /* JADX WARN: Type inference failed for: r2v8, types: [java.lang.Integer] */
    EnumSchema(Object... objArr) {
        super(null, null);
        this.items = new LinkedHashSet(objArr.length);
        for (Object objM : objArr) {
            if (objM instanceof BigDecimal) {
                objM = JSONPathFunction$IndexDecimal$$ExternalSyntheticBackportWithForwarding0.m((BigDecimal) objM);
                if (objM.scale() == 0) {
                    objM = objM.toBigInteger();
                    if (objM.compareTo(TypeUtils.BIGINT_INT32_MIN) >= 0 && objM.compareTo(TypeUtils.BIGINT_INT32_MAX) <= 0) {
                        objM = Integer.valueOf(objM.intValue());
                    } else if (objM.compareTo(TypeUtils.BIGINT_INT64_MIN) >= 0 && objM.compareTo(TypeUtils.BIGINT_INT64_MAX) <= 0) {
                        objM = Long.valueOf(objM.longValue());
                    }
                }
            }
            this.items.add(objM);
        }
    }

    @Override // com.alibaba.fastjson2.schema.JSONSchema
    public JSONSchema.Type getType() {
        return JSONSchema.Type.Enum;
    }

    @Override // com.alibaba.fastjson2.schema.JSONSchema
    public ValidateResult validate(Object obj) {
        if (obj instanceof BigDecimal) {
            BigDecimal bigDecimal = (BigDecimal) obj;
            BigDecimal bigDecimalM = JSONPathFunction$IndexDecimal$$ExternalSyntheticBackportWithForwarding0.m(bigDecimal);
            long jLongValue = bigDecimal.longValue();
            if (bigDecimal.compareTo(BigDecimal.valueOf(jLongValue)) == 0) {
                obj = Long.valueOf(jLongValue);
            } else {
                obj = bigDecimal.scale() == 0 ? bigDecimal.unscaledValue() : bigDecimalM;
            }
        } else if (obj instanceof BigInteger) {
            BigInteger bigInteger = (BigInteger) obj;
            if (bigInteger.compareTo(TypeUtils.BIGINT_INT64_MIN) >= 0 && bigInteger.compareTo(TypeUtils.BIGINT_INT64_MAX) <= 0) {
                obj = Long.valueOf(bigInteger.longValue());
            }
        }
        if (obj instanceof Long) {
            long jLongValue2 = ((Long) obj).longValue();
            if (jLongValue2 >= -2147483648L && jLongValue2 <= 2147483647L) {
                obj = Integer.valueOf((int) jLongValue2);
            }
        }
        if (this.items.contains(obj)) {
            return SUCCESS;
        }
        if (obj == null) {
            return FAIL_INPUT_NULL;
        }
        return new ValidateResult(false, "expect type %s, but %s", JSONSchema.Type.Enum, obj.getClass());
    }
}
