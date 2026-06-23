package com.alibaba.fastjson2.schema;

import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class UnresolvedReference extends JSONSchema {
    final String refName;

    UnresolvedReference(String str) {
        super(null, null);
        this.refName = str;
    }

    @Override // com.alibaba.fastjson2.schema.JSONSchema
    public JSONSchema.Type getType() {
        return JSONSchema.Type.UnresolvedReference;
    }

    @Override // com.alibaba.fastjson2.schema.JSONSchema
    public ValidateResult validate(Object obj) {
        return JSONSchema.SUCCESS;
    }

    static abstract class ResolveTask {
        abstract void resolve(JSONSchema jSONSchema);

        ResolveTask() {
        }
    }

    static class PropertyResolveTask extends ResolveTask {
        final String entryKey;
        final Map<String, JSONSchema> properties;
        final String refName;

        PropertyResolveTask(Map<String, JSONSchema> map, String str, String str2) {
            this.properties = map;
            this.entryKey = str;
            this.refName = str2;
        }

        @Override // com.alibaba.fastjson2.schema.UnresolvedReference.ResolveTask
        void resolve(JSONSchema jSONSchema) {
            Map<String, JSONSchema> map;
            JSONSchema jSONSchema2;
            if (jSONSchema instanceof ObjectSchema) {
                map = ((ObjectSchema) jSONSchema).defs;
            } else {
                map = jSONSchema instanceof ArraySchema ? ((ArraySchema) jSONSchema).defs : null;
            }
            if (map == null || (jSONSchema2 = map.get(this.refName)) == null) {
                return;
            }
            this.properties.put(this.entryKey, jSONSchema2);
        }
    }
}
