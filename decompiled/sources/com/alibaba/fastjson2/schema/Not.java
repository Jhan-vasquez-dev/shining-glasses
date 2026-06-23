package com.alibaba.fastjson2.schema;

import com.alibaba.fastjson2.reader.ObjectReaderBean;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/* JADX INFO: loaded from: classes.dex */
final class Not extends JSONSchema {
    final Boolean result;
    final JSONSchema schema;
    final JSONSchema.Type[] types;

    public Not(JSONSchema jSONSchema, JSONSchema.Type[] typeArr, Boolean bool) {
        super(null, null);
        this.schema = jSONSchema;
        this.types = typeArr;
        this.result = bool;
    }

    @Override // com.alibaba.fastjson2.schema.JSONSchema
    public JSONSchema.Type getType() {
        return JSONSchema.Type.AllOf;
    }

    @Override // com.alibaba.fastjson2.schema.JSONSchema
    public ValidateResult validate(Object obj) {
        JSONSchema jSONSchema = this.schema;
        if (jSONSchema != null && jSONSchema.validate(obj).isSuccess()) {
            return FAIL_NOT;
        }
        JSONSchema.Type[] typeArr = this.types;
        if (typeArr != null) {
            for (JSONSchema.Type type : typeArr) {
                switch (AnonymousClass1.$SwitchMap$com$alibaba$fastjson2$schema$JSONSchema$Type[type.ordinal()]) {
                    case 1:
                        if (obj instanceof String) {
                            return FAIL_NOT;
                        }
                        break;
                        break;
                    case 2:
                        if ((obj instanceof Byte) || (obj instanceof Short) || (obj instanceof Integer) || (obj instanceof Long) || (obj instanceof BigInteger) || (obj instanceof AtomicInteger) || (obj instanceof AtomicLong)) {
                            return FAIL_NOT;
                        }
                        break;
                    case 3:
                        if (obj instanceof Number) {
                            return FAIL_NOT;
                        }
                        break;
                        break;
                    case 4:
                        if (obj == null) {
                            return FAIL_NOT;
                        }
                        break;
                        break;
                    case 5:
                        if ((obj instanceof Object[]) || (obj instanceof Collection) || (obj != null && obj.getClass().isArray())) {
                            return FAIL_NOT;
                        }
                        break;
                    case 6:
                        if (obj instanceof Map) {
                            return FAIL_NOT;
                        }
                        if (obj != null && (JSONSchema.CONTEXT.getObjectReader(obj.getClass()) instanceof ObjectReaderBean)) {
                            return FAIL_NOT;
                        }
                        break;
                        break;
                    case 7:
                        if (obj instanceof Boolean) {
                            return FAIL_NOT;
                        }
                        break;
                        break;
                    case 8:
                        return FAIL_NOT;
                }
            }
        }
        Boolean bool = this.result;
        if (bool != null) {
            return bool.booleanValue() ? FAIL_NOT : SUCCESS;
        }
        return SUCCESS;
    }

    /* JADX INFO: renamed from: com.alibaba.fastjson2.schema.Not$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$alibaba$fastjson2$schema$JSONSchema$Type;

        static {
            int[] iArr = new int[JSONSchema.Type.values().length];
            $SwitchMap$com$alibaba$fastjson2$schema$JSONSchema$Type = iArr;
            try {
                iArr[JSONSchema.Type.String.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson2$schema$JSONSchema$Type[JSONSchema.Type.Integer.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson2$schema$JSONSchema$Type[JSONSchema.Type.Number.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson2$schema$JSONSchema$Type[JSONSchema.Type.Null.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson2$schema$JSONSchema$Type[JSONSchema.Type.Array.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson2$schema$JSONSchema$Type[JSONSchema.Type.Object.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson2$schema$JSONSchema$Type[JSONSchema.Type.Boolean.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson2$schema$JSONSchema$Type[JSONSchema.Type.Any.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
        }
    }
}
