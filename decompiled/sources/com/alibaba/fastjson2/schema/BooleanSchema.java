package com.alibaba.fastjson2.schema;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.schema.JSONSchema;

/* JADX INFO: loaded from: classes.dex */
public final class BooleanSchema extends JSONSchema {
    BooleanSchema(JSONObject jSONObject) {
        super(jSONObject);
    }

    @Override // com.alibaba.fastjson2.schema.JSONSchema
    public JSONSchema.Type getType() {
        return JSONSchema.Type.Boolean;
    }

    @Override // com.alibaba.fastjson2.schema.JSONSchema
    public ValidateResult validate(Object obj) {
        if (obj == null) {
            return FAIL_INPUT_NULL;
        }
        if (obj instanceof Boolean) {
            return SUCCESS;
        }
        return new ValidateResult(false, "expect type %s, but %s", JSONSchema.Type.Boolean, obj.getClass());
    }

    @Override // com.alibaba.fastjson2.schema.JSONSchema
    public JSONObject toJSONObject() {
        return JSONObject.of("type", (Object) "boolean");
    }
}
