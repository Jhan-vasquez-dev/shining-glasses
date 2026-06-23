package com.alibaba.fastjson2.schema;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPathFunction$IndexDecimal$$ExternalSyntheticBackportWithForwarding0;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
public final class ArraySchema extends JSONSchema {
    final JSONSchema additionalItem;
    final boolean additionalItems;
    final AllOf allOf;
    final AnyOf anyOf;
    final JSONSchema contains;
    final Map<String, JSONSchema> definitions;
    final Map<String, JSONSchema> defs;
    final boolean encoded;
    JSONSchema itemSchema;
    final int maxContains;
    final int maxLength;
    final int minContains;
    final int minLength;
    final OneOf oneOf;
    final JSONSchema[] prefixItems;
    final boolean typed;
    final boolean uniqueItems;

    /* JADX WARN: Removed duplicated region for block: B:42:0x0103  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0112  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0136  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x013b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ArraySchema(com.alibaba.fastjson2.JSONObject r10, com.alibaba.fastjson2.schema.JSONSchema r11) {
        /*
            Method dump skipped, instruction units count: 427
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.schema.ArraySchema.<init>(com.alibaba.fastjson2.JSONObject, com.alibaba.fastjson2.schema.JSONSchema):void");
    }

    @Override // com.alibaba.fastjson2.schema.JSONSchema
    public JSONSchema.Type getType() {
        return JSONSchema.Type.Array;
    }

    @Override // com.alibaba.fastjson2.schema.JSONSchema
    public ValidateResult validate(final Object obj) {
        if (obj == null) {
            return this.typed ? FAIL_INPUT_NULL : SUCCESS;
        }
        if (this.encoded) {
            if (obj instanceof String) {
                try {
                    obj = JSON.parseArray((String) obj);
                } catch (JSONException unused) {
                    return FAIL_INPUT_NOT_ENCODED;
                }
            } else {
                return FAIL_INPUT_NOT_ENCODED;
            }
        }
        if (obj instanceof Object[]) {
            final Object[] objArr = (Object[]) obj;
            return validateItems(obj, objArr.length, new IntFunction() { // from class: com.alibaba.fastjson2.schema.ArraySchema$$ExternalSyntheticLambda0
                @Override // java.util.function.IntFunction
                public final Object apply(int i) {
                    return ArraySchema.lambda$validate$0(objArr, i);
                }
            });
        }
        if (obj.getClass().isArray()) {
            return validateItems(obj, Array.getLength(obj), new IntFunction() { // from class: com.alibaba.fastjson2.schema.ArraySchema$$ExternalSyntheticLambda1
                @Override // java.util.function.IntFunction
                public final Object apply(int i) {
                    return Array.get(obj, i);
                }
            });
        }
        if (!(obj instanceof Collection)) {
            return this.typed ? FAIL_TYPE_NOT_MATCH : SUCCESS;
        }
        Collection collection = (Collection) obj;
        final Iterator it = collection.iterator();
        return validateItems(obj, collection.size(), new IntFunction() { // from class: com.alibaba.fastjson2.schema.ArraySchema$$ExternalSyntheticLambda2
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return it.next();
            }
        });
    }

    static /* synthetic */ Object lambda$validate$0(Object[] objArr, int i) {
        return objArr[i];
    }

    private ValidateResult validateItems(Object obj, int i, IntFunction<Object> intFunction) {
        JSONSchema jSONSchema;
        int i2 = this.minLength;
        if (i2 >= 0 && i < i2) {
            return new ValidateResult(false, "minLength not match, expect >= %s, but %s", Integer.valueOf(this.minLength), Integer.valueOf(i));
        }
        int i3 = this.maxLength;
        if (i3 >= 0 && i > i3) {
            return new ValidateResult(false, "maxLength not match, expect <= %s, but %s", Integer.valueOf(this.maxLength), Integer.valueOf(i));
        }
        if (!this.additionalItems && i > this.prefixItems.length) {
            return new ValidateResult(false, "additional items not match, max size %s, but %s", Integer.valueOf(this.prefixItems.length), Integer.valueOf(i));
        }
        boolean z = obj instanceof Collection;
        HashSet hashSet = null;
        int i4 = 0;
        for (int i5 = 0; i5 < i; i5++) {
            Object objApply = intFunction.apply(i5);
            JSONSchema[] jSONSchemaArr = this.prefixItems;
            if (i5 < jSONSchemaArr.length) {
                ValidateResult validateResultValidate = jSONSchemaArr[i5].validate(objApply);
                if (!validateResultValidate.isSuccess()) {
                    return validateResultValidate;
                }
            } else {
                if (z && this.itemSchema == null && (jSONSchema = this.additionalItem) != null) {
                    ValidateResult validateResultValidate2 = jSONSchema.validate(objApply);
                    if (!validateResultValidate2.isSuccess()) {
                        return validateResultValidate2;
                    }
                }
                JSONSchema jSONSchema2 = this.itemSchema;
                if (jSONSchema2 != null) {
                    ValidateResult validateResultValidate3 = jSONSchema2.validate(objApply);
                    if (!validateResultValidate3.isSuccess()) {
                        return validateResultValidate3;
                    }
                }
            }
            JSONSchema jSONSchema3 = this.contains;
            if (jSONSchema3 != null && ((this.minContains > 0 || this.maxContains > 0 || i4 == 0) && jSONSchema3.validate(objApply) == SUCCESS)) {
                i4++;
            }
            if (this.uniqueItems) {
                if (hashSet == null) {
                    hashSet = new HashSet(i, 1.0f);
                }
                if (objApply instanceof BigDecimal) {
                    objApply = JSONPathFunction$IndexDecimal$$ExternalSyntheticBackportWithForwarding0.m((BigDecimal) objApply);
                }
                if (!hashSet.add(objApply)) {
                    return UNIQUE_ITEMS_NOT_MATCH;
                }
            }
        }
        if (!z || this.contains != null) {
            int i6 = this.minContains;
            if (i6 >= 0 && i4 < i6) {
                return new ValidateResult(false, "minContains not match, expect %s, but %s", Integer.valueOf(this.minContains), Integer.valueOf(i4));
            }
            if (z) {
                if (i4 == 0 && i6 != 0) {
                    return CONTAINS_NOT_MATCH;
                }
            } else if (this.contains != null && i4 == 0) {
                return CONTAINS_NOT_MATCH;
            }
            int i7 = this.maxContains;
            if (i7 >= 0 && i4 > i7) {
                return new ValidateResult(false, "maxContains not match, expect %s, but %s", Integer.valueOf(this.maxContains), Integer.valueOf(i4));
            }
        }
        AllOf allOf = this.allOf;
        if (allOf != null) {
            ValidateResult validateResultValidate4 = allOf.validate(obj);
            if (!validateResultValidate4.isSuccess()) {
                return validateResultValidate4;
            }
        }
        AnyOf anyOf = this.anyOf;
        if (anyOf != null) {
            ValidateResult validateResultValidate5 = anyOf.validate(obj);
            if (!validateResultValidate5.isSuccess()) {
                return validateResultValidate5;
            }
        }
        OneOf oneOf = this.oneOf;
        if (oneOf != null) {
            ValidateResult validateResultValidate6 = oneOf.validate(obj);
            if (!validateResultValidate6.isSuccess()) {
                return validateResultValidate6;
            }
        }
        return SUCCESS;
    }

    @Override // com.alibaba.fastjson2.schema.JSONSchema
    public JSONObject toJSONObject() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("type", "array");
        int i = this.maxLength;
        if (i != -1) {
            jSONObject.put("maxLength", Integer.valueOf(i));
        }
        int i2 = this.minLength;
        if (i2 != -1) {
            jSONObject.put("minLength", Integer.valueOf(i2));
        }
        JSONSchema jSONSchema = this.itemSchema;
        if (jSONSchema != null) {
            jSONObject.put("items", jSONSchema);
        }
        JSONSchema[] jSONSchemaArr = this.prefixItems;
        if (jSONSchemaArr != null && jSONSchemaArr.length != 0) {
            jSONObject.put("prefixItems", jSONSchemaArr);
        }
        boolean z = this.additionalItems;
        if (!z) {
            jSONObject.put("additionalItems", Boolean.valueOf(z));
        }
        JSONSchema jSONSchema2 = this.additionalItem;
        if (jSONSchema2 != null) {
            jSONObject.put("additionalItem", jSONSchema2);
        }
        JSONSchema jSONSchema3 = this.contains;
        if (jSONSchema3 != null) {
            jSONObject.put("contains", jSONSchema3);
        }
        int i3 = this.minContains;
        if (i3 != -1) {
            jSONObject.put("minContains", Integer.valueOf(i3));
        }
        int i4 = this.maxContains;
        if (i4 != -1) {
            jSONObject.put("maxContains", Integer.valueOf(i4));
        }
        boolean z2 = this.uniqueItems;
        if (z2) {
            jSONObject.put("uniqueItems", Boolean.valueOf(z2));
        }
        return injectIfPresent(jSONObject, this.allOf, this.anyOf, this.oneOf);
    }

    @Override // com.alibaba.fastjson2.schema.JSONSchema
    public void accept(Predicate<JSONSchema> predicate) {
        JSONSchema jSONSchema;
        if (!predicate.test(this) || (jSONSchema = this.itemSchema) == null) {
            return;
        }
        jSONSchema.accept(predicate);
    }

    public JSONSchema getItemSchema() {
        return this.itemSchema;
    }
}
