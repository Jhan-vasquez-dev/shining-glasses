package com.alibaba.fastjson2.schema;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONSchemaValidException;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONCreator;
import com.alibaba.fastjson2.annotation.JSONType;
import com.alibaba.fastjson2.reader.FieldReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderAdapter;
import com.alibaba.fastjson2.reader.ObjectReaderBean;
import com.alibaba.fastjson2.schema.UnresolvedReference;
import com.alibaba.fastjson2.writer.ObjectWriter;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
@JSONType(serializer = JSONSchemaWriter.class)
public abstract class JSONSchema {
    final String description;
    final String title;
    static final Map<String, JSONSchema> CACHE = new ConcurrentHashMap();
    static final JSONReader.Context CONTEXT = JSONFactory.createReadContext();
    static final ValidateResult SUCCESS = new ValidateResult(true, "success", new Object[0]);
    static final ValidateResult FAIL_INPUT_NULL = new ValidateResult(false, "input null", new Object[0]);
    static final ValidateResult FAIL_INPUT_NOT_ENCODED = new ValidateResult(false, "input not encoded string", new Object[0]);
    static final ValidateResult FAIL_ANY_OF = new ValidateResult(false, "anyOf fail", new Object[0]);
    static final ValidateResult FAIL_ONE_OF = new ValidateResult(false, "oneOf fail", new Object[0]);
    static final ValidateResult FAIL_NOT = new ValidateResult(false, "not fail", new Object[0]);
    static final ValidateResult FAIL_TYPE_NOT_MATCH = new ValidateResult(false, "type not match", new Object[0]);
    static final ValidateResult FAIL_PROPERTY_NAME = new ValidateResult(false, "propertyName not match", new Object[0]);
    static final ValidateResult CONTAINS_NOT_MATCH = new ValidateResult(false, "contains not match", new Object[0]);
    static final ValidateResult UNIQUE_ITEMS_NOT_MATCH = new ValidateResult(false, "uniqueItems not match", new Object[0]);
    static final ValidateResult REQUIRED_NOT_MATCH = new ValidateResult(false, "required", new Object[0]);

    void addResolveTask(UnresolvedReference.ResolveTask resolveTask) {
    }

    public abstract Type getType();

    public abstract ValidateResult validate(Object obj);

    JSONSchema(JSONObject jSONObject) {
        this.title = jSONObject.getString("title");
        this.description = jSONObject.getString("description");
    }

    JSONSchema(String str, String str2) {
        this.title = str;
        this.description = str2;
    }

    public static JSONSchema of(JSONObject jSONObject, Class cls) {
        if (jSONObject == null || jSONObject.isEmpty()) {
            return null;
        }
        if (cls == null || cls == Object.class) {
            return of(jSONObject);
        }
        if (cls == Byte.TYPE || cls == Short.TYPE || cls == Integer.TYPE || cls == Long.TYPE || cls == Byte.class || cls == Short.class || cls == Integer.class || cls == Long.class || cls == BigInteger.class || cls == AtomicInteger.class || cls == AtomicLong.class) {
            if (jSONObject.containsKey("AnyOf") || jSONObject.containsKey("anyOf")) {
                return anyOf(jSONObject, cls);
            }
            if (jSONObject.containsKey("oneOf")) {
                return oneOf(jSONObject, cls);
            }
            if (jSONObject.containsKey("not")) {
                return ofNot(jSONObject, cls);
            }
            return new IntegerSchema(jSONObject);
        }
        if (cls == BigDecimal.class || cls == Float.TYPE || cls == Double.TYPE || cls == Float.class || cls == Double.class || cls == Number.class) {
            if (jSONObject.containsKey("AnyOf") || jSONObject.containsKey("anyOf")) {
                return anyOf(jSONObject, cls);
            }
            if (jSONObject.containsKey("oneOf")) {
                return oneOf(jSONObject, cls);
            }
            if (jSONObject.containsKey("not")) {
                return ofNot(jSONObject, cls);
            }
            return new NumberSchema(jSONObject);
        }
        if (cls == Boolean.TYPE || cls == Boolean.class) {
            return new BooleanSchema(jSONObject);
        }
        if (cls == String.class) {
            return new StringSchema(jSONObject);
        }
        if (Collection.class.isAssignableFrom(cls)) {
            return new ArraySchema(jSONObject, null);
        }
        if (cls.isArray()) {
            return new ArraySchema(jSONObject, null);
        }
        return new ObjectSchema(jSONObject, null);
    }

    static Not ofNot(JSONObject jSONObject, Class cls) {
        Object obj = jSONObject.get("not");
        if (obj instanceof Boolean) {
            return new Not(null, null, (Boolean) obj);
        }
        JSONObject jSONObject2 = (JSONObject) obj;
        if (jSONObject2 == null || jSONObject2.isEmpty()) {
            return new Not(null, new Type[]{Type.Any}, null);
        }
        if (jSONObject2.size() == 1) {
            Object obj2 = jSONObject2.get("type");
            if (obj2 instanceof JSONArray) {
                JSONArray jSONArray = (JSONArray) obj2;
                Type[] typeArr = new Type[jSONArray.size()];
                for (int i = 0; i < jSONArray.size(); i++) {
                    typeArr[i] = (Type) jSONArray.getObject(i, Type.class, new JSONReader.Feature[0]);
                }
                return new Not(null, typeArr, null);
            }
        }
        return new Not(of(jSONObject2, cls), null, null);
    }

    public static JSONSchema parseSchema(String str) {
        if ("true".equals(str)) {
            return Any.INSTANCE;
        }
        if ("false".equals(str)) {
            return Any.NOT_ANY;
        }
        JSONReader jSONReaderOf = JSONReader.of(str);
        try {
            JSONSchema jSONSchemaOf = of((JSONObject) jSONReaderOf.getObjectReader(Object.class).readObject(jSONReaderOf, null, null, 0L));
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return jSONSchemaOf;
        } finally {
        }
    }

    @JSONCreator
    public static JSONSchema of(JSONObject jSONObject) {
        return of(jSONObject, (JSONSchema) null);
    }

    public static JSONSchema of(java.lang.reflect.Type type) {
        return of(type, (JSONSchema) null);
    }

    public static JSONSchema ofValue(Object obj) {
        return ofValue(obj, null);
    }

    static JSONSchema ofValue(Object obj, JSONSchema jSONSchema) {
        JSONSchema jSONSchemaOfValue;
        JSONSchema jSONSchemaOf;
        Class<?> cls = null;
        if (obj == null) {
            return null;
        }
        if (obj instanceof Collection) {
            Collection collection = (Collection) obj;
            if (collection.isEmpty()) {
                return new ArraySchema(JSONObject.of("type", (Object) "array"), jSONSchema);
            }
            boolean z = true;
            Object obj2 = null;
            for (Object obj3 : collection) {
                if (obj3 != null) {
                    if (obj2 == null) {
                        obj2 = obj3;
                    }
                    if (cls == null) {
                        cls = obj3.getClass();
                    } else if (cls != obj3.getClass()) {
                        z = false;
                    }
                }
            }
            if (z) {
                if (Map.class.isAssignableFrom(cls)) {
                    jSONSchemaOf = ofValue(obj2, jSONSchema);
                } else {
                    jSONSchemaOf = of(cls, jSONSchema);
                }
                ArraySchema arraySchema = new ArraySchema(JSONObject.of("type", (Object) "array"), jSONSchema);
                arraySchema.itemSchema = jSONSchemaOf;
                return arraySchema;
            }
        }
        if (obj instanceof Map) {
            ObjectSchema objectSchema = new ObjectSchema(JSONObject.of("type", (Object) "object"), jSONSchema);
            for (Map.Entry entry : ((Map) obj).entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (key instanceof String) {
                    if (value == null) {
                        jSONSchemaOfValue = new StringSchema(JSONObject.of());
                    } else {
                        jSONSchemaOfValue = ofValue(value, jSONSchema == null ? objectSchema : jSONSchema);
                    }
                    objectSchema.properties.put((String) key, jSONSchemaOfValue);
                }
            }
            return objectSchema;
        }
        return of(obj.getClass(), jSONSchema);
    }

    static JSONSchema of(java.lang.reflect.Type type, final JSONSchema jSONSchema) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            java.lang.reflect.Type rawType = parameterizedType.getRawType();
            java.lang.reflect.Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            boolean z = rawType instanceof Class;
            if (z && Collection.class.isAssignableFrom((Class) rawType)) {
                ArraySchema arraySchema = new ArraySchema(JSONObject.of("type", (Object) "array"), jSONSchema);
                if (actualTypeArguments.length == 1) {
                    java.lang.reflect.Type type2 = actualTypeArguments[0];
                    if (jSONSchema == null) {
                        jSONSchema = arraySchema;
                    }
                    arraySchema.itemSchema = of(type2, jSONSchema);
                }
                return arraySchema;
            }
            if (z && Map.class.isAssignableFrom((Class) rawType)) {
                return new ObjectSchema(JSONObject.of("type", (Object) "object"), jSONSchema);
            }
        }
        if (type instanceof GenericArrayType) {
            java.lang.reflect.Type genericComponentType = ((GenericArrayType) type).getGenericComponentType();
            ArraySchema arraySchema2 = new ArraySchema(JSONObject.of("type", (Object) "array"), jSONSchema);
            if (jSONSchema == null) {
                jSONSchema = arraySchema2;
            }
            arraySchema2.itemSchema = of(genericComponentType, jSONSchema);
            return arraySchema2;
        }
        if (type == Byte.TYPE || type == Short.TYPE || type == Integer.TYPE || type == Long.TYPE || type == Byte.class || type == Short.class || type == Integer.class || type == Long.class || type == BigInteger.class || type == AtomicInteger.class || type == AtomicLong.class) {
            return new IntegerSchema(JSONObject.of("type", (Object) "integer"));
        }
        if (type == Float.TYPE || type == Double.TYPE || type == Float.class || type == Double.class || type == BigDecimal.class) {
            return new NumberSchema(JSONObject.of("type", (Object) "number"));
        }
        if (type == Boolean.TYPE || type == Boolean.class || type == AtomicBoolean.class) {
            return new BooleanSchema(JSONObject.of("type", (Object) "boolean"));
        }
        if (type == String.class) {
            return new StringSchema(JSONObject.of("type", (Object) "string"));
        }
        if (type instanceof Class) {
            Class cls = (Class) type;
            if (Enum.class.isAssignableFrom(cls)) {
                Object[] enumConstants = cls.getEnumConstants();
                String[] strArr = new String[enumConstants.length];
                for (int i = 0; i < enumConstants.length; i++) {
                    strArr[i] = ((Enum) enumConstants[i]).name();
                }
                return new StringSchema(JSONObject.of("type", (Object) "string", "enum", (Object) strArr));
            }
            if (cls.isArray()) {
                Class<?> componentType = cls.getComponentType();
                ArraySchema arraySchema3 = new ArraySchema(JSONObject.of("type", (Object) "array"), jSONSchema);
                if (jSONSchema == null) {
                    jSONSchema = arraySchema3;
                }
                arraySchema3.itemSchema = of(componentType, jSONSchema);
                return arraySchema3;
            }
            if (Map.class.isAssignableFrom(cls)) {
                return new ObjectSchema(JSONObject.of("type", (Object) "object"), jSONSchema);
            }
            if (Collection.class.isAssignableFrom(cls)) {
                return new ArraySchema(JSONObject.of("type", (Object) "array"), jSONSchema);
            }
        }
        ObjectReader objectReader = JSONFactory.getDefaultObjectReaderProvider().getObjectReader(type);
        if (objectReader instanceof ObjectReaderBean) {
            ObjectReaderAdapter objectReaderAdapter = (ObjectReaderAdapter) objectReader;
            final JSONArray jSONArray = new JSONArray();
            objectReaderAdapter.apply(new Consumer() { // from class: com.alibaba.fastjson2.schema.JSONSchema$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    JSONSchema.lambda$of$0(jSONArray, (FieldReader) obj);
                }
            });
            final ObjectSchema objectSchema = new ObjectSchema(JSONObject.of("type", (Object) "object", "required", (Object) jSONArray));
            objectReaderAdapter.apply(new Consumer() { // from class: com.alibaba.fastjson2.schema.JSONSchema$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    JSONSchema.lambda$of$1(objectSchema, jSONSchema, (FieldReader) obj);
                }
            });
            return objectSchema;
        }
        throw new JSONException("TODO : " + type);
    }

    static /* synthetic */ void lambda$of$0(JSONArray jSONArray, FieldReader fieldReader) {
        if (fieldReader.fieldClass.isPrimitive()) {
            jSONArray.add(fieldReader.fieldName);
        }
    }

    static /* synthetic */ void lambda$of$1(ObjectSchema objectSchema, JSONSchema jSONSchema, FieldReader fieldReader) {
        Map<String, JSONSchema> map = objectSchema.properties;
        String str = fieldReader.fieldName;
        java.lang.reflect.Type type = fieldReader.fieldType;
        JSONSchema jSONSchema2 = objectSchema;
        if (jSONSchema != null) {
            jSONSchema2 = jSONSchema;
        }
        map.put(str, of(type, jSONSchema2));
    }

    @JSONCreator
    public static JSONSchema of(JSONObject jSONObject, JSONSchema jSONSchema) {
        Map<String, JSONSchema> map;
        Map<String, JSONSchema> map2;
        Map<String, JSONSchema> map3;
        int i = 0;
        if (jSONObject.size() == 1 && jSONObject.isArray("type")) {
            JSONArray jSONArray = jSONObject.getJSONArray("type");
            JSONSchema[] jSONSchemaArr = new JSONSchema[jSONArray.size()];
            while (i < jSONArray.size()) {
                jSONSchemaArr[i] = of(JSONObject.of("type", jSONArray.get(i)));
                i++;
            }
            return new AnyOf(jSONSchemaArr);
        }
        Type typeOf = Type.of(jSONObject.getString("type"));
        if (typeOf == null) {
            Object[] objArr = (Object[]) jSONObject.getObject("enum", Object[].class, new JSONReader.Feature[0]);
            if (objArr != null) {
                int length = objArr.length;
                while (i < length) {
                    if (!(objArr[i] instanceof String)) {
                        return new EnumSchema(objArr);
                    }
                    i++;
                }
                return new StringSchema(jSONObject);
            }
            Object obj = jSONObject.get("const");
            if (obj instanceof String) {
                return new StringSchema(jSONObject);
            }
            if ((obj instanceof Integer) || (obj instanceof Long)) {
                return new IntegerSchema(jSONObject);
            }
            if (jSONObject.size() == 1) {
                String string = jSONObject.getString("$ref");
                if (string != null && !string.isEmpty()) {
                    if ("http://json-schema.org/draft-04/schema#".equals(string)) {
                        Map<String, JSONSchema> map4 = CACHE;
                        JSONSchema jSONSchemaOf = map4.get(string);
                        if (jSONSchemaOf == null) {
                            jSONSchemaOf = of(JSON.parseObject(JSONSchema.class.getClassLoader().getResource("schema/draft-04.json")), (JSONSchema) null);
                            JSONSchema jSONSchemaPutIfAbsent = map4.putIfAbsent(string, jSONSchemaOf);
                            if (jSONSchemaPutIfAbsent != null) {
                                return jSONSchemaPutIfAbsent;
                            }
                        }
                        return jSONSchemaOf;
                    }
                    if ("#".equals(string)) {
                        return jSONSchema;
                    }
                    if (jSONSchema instanceof ObjectSchema) {
                        ObjectSchema objectSchema = (ObjectSchema) jSONSchema;
                        map2 = objectSchema.definitions;
                        map3 = objectSchema.defs;
                        map = objectSchema.properties;
                    } else if (jSONSchema instanceof ArraySchema) {
                        ArraySchema arraySchema = (ArraySchema) jSONSchema;
                        map2 = arraySchema.definitions;
                        map3 = arraySchema.defs;
                        map = null;
                    } else {
                        map = null;
                        map2 = null;
                        map3 = null;
                    }
                    if (map2 != null && string.startsWith("#/definitions/")) {
                        return map2.get(string.substring(14));
                    }
                    if (map3 != null && string.startsWith("#/$defs/")) {
                        String strDecode = URLDecoder.decode(string.substring(8));
                        JSONSchema jSONSchema2 = map3.get(strDecode);
                        return jSONSchema2 == null ? new UnresolvedReference(strDecode) : jSONSchema2;
                    }
                    if (map != null && string.startsWith("#/properties/")) {
                        return map.get(string.substring(13));
                    }
                    if (string.startsWith("#/prefixItems/") && (jSONSchema instanceof ArraySchema)) {
                        return ((ArraySchema) jSONSchema).prefixItems[Integer.parseInt(string.substring(14))];
                    }
                }
                Object obj2 = jSONObject.get("exclusiveMaximum");
                Object obj3 = jSONObject.get("exclusiveMinimum");
                if ((obj2 instanceof Integer) || (obj3 instanceof Integer) || (obj2 instanceof Long) || (obj3 instanceof Long)) {
                    return new IntegerSchema(jSONObject);
                }
                if ((obj2 instanceof Number) || (obj3 instanceof Number)) {
                    return new NumberSchema(jSONObject);
                }
            }
            if (jSONObject.containsKey("properties") || jSONObject.containsKey("dependentSchemas") || jSONObject.containsKey("if") || jSONObject.containsKey("required") || jSONObject.containsKey("patternProperties") || jSONObject.containsKey("additionalProperties") || jSONObject.containsKey("minProperties") || jSONObject.containsKey("maxProperties") || jSONObject.containsKey("propertyNames") || jSONObject.containsKey("$ref")) {
                return new ObjectSchema(jSONObject, jSONSchema);
            }
            if (jSONObject.containsKey("maxItems") || jSONObject.containsKey("minItems") || jSONObject.containsKey("additionalItems") || jSONObject.containsKey("items") || jSONObject.containsKey("prefixItems") || jSONObject.containsKey("uniqueItems") || jSONObject.containsKey("maxContains") || jSONObject.containsKey("minContains")) {
                return new ArraySchema(jSONObject, jSONSchema);
            }
            if (jSONObject.containsKey("pattern") || jSONObject.containsKey("format") || jSONObject.containsKey("minLength") || jSONObject.containsKey("maxLength")) {
                return new StringSchema(jSONObject);
            }
            boolean zContainsKey = jSONObject.containsKey("allOf");
            boolean zContainsKey2 = jSONObject.containsKey("anyOf");
            boolean zContainsKey3 = jSONObject.containsKey("oneOf");
            if (zContainsKey || zContainsKey2 || zContainsKey3) {
                int i2 = (zContainsKey ? 1 : 0) + (zContainsKey2 ? 1 : 0) + (zContainsKey3 ? 1 : 0);
                if (i2 == 1) {
                    if (zContainsKey) {
                        return new AllOf(jSONObject, jSONSchema);
                    }
                    if (zContainsKey2) {
                        return new AnyOf(jSONObject, jSONSchema);
                    }
                    return new OneOf(jSONObject, jSONSchema);
                }
                JSONSchema[] jSONSchemaArr2 = new JSONSchema[i2];
                if (zContainsKey) {
                    jSONSchemaArr2[0] = new AllOf(jSONObject, jSONSchema);
                    i = 1;
                }
                if (zContainsKey2) {
                    jSONSchemaArr2[i] = new AnyOf(jSONObject, jSONSchema);
                    i++;
                }
                if (zContainsKey3) {
                    jSONSchemaArr2[i] = new OneOf(jSONObject, jSONSchema);
                }
                return new AllOf(jSONSchemaArr2);
            }
            if (jSONObject.containsKey("not")) {
                return ofNot(jSONObject, null);
            }
            if ((jSONObject.get("maximum") instanceof Number) || (jSONObject.get("minimum") instanceof Number) || jSONObject.containsKey("multipleOf")) {
                return new NumberSchema(jSONObject);
            }
            if (jSONObject.isEmpty()) {
                return Any.INSTANCE;
            }
            if (jSONObject.size() == 1) {
                Object obj4 = jSONObject.get("type");
                if (obj4 instanceof JSONArray) {
                    JSONArray jSONArray2 = (JSONArray) obj4;
                    JSONSchema[] jSONSchemaArr3 = new JSONSchema[jSONArray2.size()];
                    while (i < jSONArray2.size()) {
                        Type typeOf2 = Type.of(jSONArray2.getString(i));
                        switch (AnonymousClass1.$SwitchMap$com$alibaba$fastjson2$schema$JSONSchema$Type[typeOf2.ordinal()]) {
                            case 1:
                                jSONSchemaArr3[i] = new StringSchema(JSONObject.of("type", (Object) "string"));
                                break;
                            case 2:
                                jSONSchemaArr3[i] = new IntegerSchema(JSONObject.of("type", (Object) "integer"));
                                break;
                            case 3:
                                jSONSchemaArr3[i] = new NumberSchema(JSONObject.of("type", (Object) "number"));
                                break;
                            case 4:
                                jSONSchemaArr3[i] = new BooleanSchema(JSONObject.of("type", (Object) "boolean"));
                                break;
                            case 5:
                                jSONSchemaArr3[i] = new NullSchema(JSONObject.of("type", (Object) "null"));
                                break;
                            case 6:
                                jSONSchemaArr3[i] = new ObjectSchema(JSONObject.of("type", (Object) "object"));
                                break;
                            case 7:
                                jSONSchemaArr3[i] = new ArraySchema(JSONObject.of("type", (Object) "array"), null);
                                break;
                            default:
                                throw new JSONSchemaValidException("not support type : " + typeOf2);
                        }
                        i++;
                    }
                    return new AnyOf(jSONSchemaArr3);
                }
            }
            if (jSONObject.getString("type") == null) {
                throw new JSONSchemaValidException("type required");
            }
            throw new JSONSchemaValidException("not support type : " + jSONObject.getString("type"));
        }
        switch (AnonymousClass1.$SwitchMap$com$alibaba$fastjson2$schema$JSONSchema$Type[typeOf.ordinal()]) {
            case 1:
                return new StringSchema(jSONObject);
            case 2:
                return new IntegerSchema(jSONObject);
            case 3:
                return new NumberSchema(jSONObject);
            case 4:
                return new BooleanSchema(jSONObject);
            case 5:
                return new NullSchema(jSONObject);
            case 6:
                return new ObjectSchema(jSONObject, jSONSchema);
            case 7:
                return new ArraySchema(jSONObject, jSONSchema);
            default:
                throw new JSONSchemaValidException("not support type : " + typeOf);
        }
    }

    /* JADX INFO: renamed from: com.alibaba.fastjson2.schema.JSONSchema$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$alibaba$fastjson2$schema$JSONSchema$Type;

        static {
            int[] iArr = new int[Type.values().length];
            $SwitchMap$com$alibaba$fastjson2$schema$JSONSchema$Type = iArr;
            try {
                iArr[Type.String.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson2$schema$JSONSchema$Type[Type.Integer.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson2$schema$JSONSchema$Type[Type.Number.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson2$schema$JSONSchema$Type[Type.Boolean.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson2$schema$JSONSchema$Type[Type.Null.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson2$schema$JSONSchema$Type[Type.Object.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson2$schema$JSONSchema$Type[Type.Array.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    static AnyOf anyOf(JSONObject jSONObject, Class cls) {
        return anyOf(jSONObject.getJSONArray("anyOf"), cls);
    }

    static JSONSchema[] makeSchemaItems(JSONArray jSONArray, Class cls) {
        if (jSONArray == null || jSONArray.isEmpty()) {
            return null;
        }
        int size = jSONArray.size();
        JSONSchema[] jSONSchemaArr = new JSONSchema[size];
        for (int i = 0; i < size; i++) {
            jSONSchemaArr[i] = of(jSONArray.getJSONObject(i), cls);
        }
        return jSONSchemaArr;
    }

    static AnyOf anyOf(JSONArray jSONArray, Class cls) {
        JSONSchema[] jSONSchemaArrMakeSchemaItems = makeSchemaItems(jSONArray, cls);
        if (jSONSchemaArrMakeSchemaItems == null) {
            return null;
        }
        return new AnyOf(jSONSchemaArrMakeSchemaItems);
    }

    static AllOf allOf(JSONObject jSONObject, Class cls) {
        JSONSchema[] jSONSchemaArrMakeSchemaItems = makeSchemaItems(jSONObject.getJSONArray("allOf"), cls);
        if (jSONSchemaArrMakeSchemaItems == null) {
            return null;
        }
        return new AllOf(jSONSchemaArrMakeSchemaItems);
    }

    static OneOf oneOf(JSONObject jSONObject, Class cls) {
        return oneOf(jSONObject.getJSONArray("oneOf"), cls);
    }

    static OneOf oneOf(JSONArray jSONArray, Class cls) {
        JSONSchema[] jSONSchemaArrMakeSchemaItems = makeSchemaItems(jSONArray, cls);
        if (jSONSchemaArrMakeSchemaItems == null) {
            return null;
        }
        return new OneOf(jSONSchemaArrMakeSchemaItems);
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isValid(Object obj) {
        return validate(obj).isSuccess();
    }

    public boolean isValid(long j) {
        return validate(j).isSuccess();
    }

    public boolean isValid(double d) {
        return validate(d).isSuccess();
    }

    public boolean isValid(Double d) {
        return validate(d).isSuccess();
    }

    public boolean isValid(float f) {
        return validate(f).isSuccess();
    }

    public boolean isValid(Float f) {
        return validate(f).isSuccess();
    }

    public boolean isValid(Integer num) {
        return validate(num).isSuccess();
    }

    public boolean isValid(Long l) {
        return validate(l).isSuccess();
    }

    public ValidateResult validate(long j) {
        return validate((Object) Long.valueOf(j));
    }

    public ValidateResult validate(double d) {
        return validate((Object) Double.valueOf(d));
    }

    public ValidateResult validate(Float f) {
        return validate((Object) f);
    }

    public ValidateResult validate(Double d) {
        return validate((Object) d);
    }

    public ValidateResult validate(Integer num) {
        return validate((Object) num);
    }

    public ValidateResult validate(Long l) {
        return validate((Object) l);
    }

    public void assertValidate(Object obj) {
        ValidateResult validateResultValidate = validate(obj);
        if (!validateResultValidate.isSuccess()) {
            throw new JSONSchemaValidException(validateResultValidate.getMessage());
        }
    }

    public void assertValidate(Integer num) {
        ValidateResult validateResultValidate = validate(num);
        if (!validateResultValidate.isSuccess()) {
            throw new JSONSchemaValidException(validateResultValidate.getMessage());
        }
    }

    public void assertValidate(Long l) {
        ValidateResult validateResultValidate = validate(l);
        if (!validateResultValidate.isSuccess()) {
            throw new JSONSchemaValidException(validateResultValidate.getMessage());
        }
    }

    public void assertValidate(Double d) {
        ValidateResult validateResultValidate = validate(d);
        if (!validateResultValidate.isSuccess()) {
            throw new JSONSchemaValidException(validateResultValidate.getMessage());
        }
    }

    public void assertValidate(Float f) {
        ValidateResult validateResultValidate = validate(f);
        if (!validateResultValidate.isSuccess()) {
            throw new JSONSchemaValidException(validateResultValidate.getMessage());
        }
    }

    public void assertValidate(long j) {
        ValidateResult validateResultValidate = validate(j);
        if (!validateResultValidate.isSuccess()) {
            throw new JSONSchemaValidException(validateResultValidate.getMessage());
        }
    }

    public void assertValidate(double d) {
        ValidateResult validateResultValidate = validate(d);
        if (!validateResultValidate.isSuccess()) {
            throw new JSONSchemaValidException(validateResultValidate.getMessage());
        }
    }

    public String toString() {
        return toJSONObject().toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return toJSONObject().equals(((JSONSchema) obj).toJSONObject());
    }

    public int hashCode() {
        return toJSONObject().hashCode();
    }

    public JSONObject toJSONObject() {
        return new JSONObject();
    }

    public enum Type {
        Null,
        Boolean,
        Object,
        Array,
        Number,
        String,
        Integer,
        Enum,
        Const,
        OneOf,
        AllOf,
        AnyOf,
        Any,
        UnresolvedReference;

        public static Type of(String str) {
            if (str == null) {
                return null;
            }
            str.hashCode();
            switch (str) {
            }
            return null;
        }
    }

    static class JSONSchemaWriter implements ObjectWriter {
        JSONSchemaWriter() {
        }

        @Override // com.alibaba.fastjson2.writer.ObjectWriter
        public void write(JSONWriter jSONWriter, Object obj, Object obj2, java.lang.reflect.Type type, long j) {
            jSONWriter.write(((JSONSchema) obj).toJSONObject());
        }
    }

    public void accept(Predicate<JSONSchema> predicate) {
        predicate.test(this);
    }

    static JSONObject injectIfPresent(JSONObject jSONObject, AllOf allOf, AnyOf anyOf, OneOf oneOf) {
        if (allOf != null) {
            jSONObject.put("allOf", allOf);
        }
        if (anyOf != null) {
            jSONObject.put("anyOf", anyOf);
        }
        if (oneOf != null) {
            jSONObject.put("oneOf", oneOf);
        }
        return jSONObject;
    }
}
