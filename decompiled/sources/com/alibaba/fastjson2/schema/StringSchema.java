package com.alibaba.fastjson2.schema;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.TypeReference;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public final class StringSchema extends JSONSchema {
    static final Pattern EMAIL_PATTERN = Pattern.compile("^\\s*?(.+)@(.+?)\\s*$");
    static final Pattern IP_DOMAIN_PATTERN = Pattern.compile("^\\[(.*)\\]$");
    static final Pattern USER_PATTERN = Pattern.compile("^\\s*(((\\\\.)|[^\\s\\p{Cntrl}\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]]|')+|(\"[^\"]*\"))(\\.(((\\\\.)|[^\\s\\p{Cntrl}\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]]|')+|(\"[^\"]*\")))*$");
    final AnyOf anyOf;
    final String constValue;
    final Set<String> enumValues;
    final String format;
    final Predicate<String> formatValidator;
    final int maxLength;
    final int minLength;
    final OneOf oneOf;
    final Pattern pattern;
    final String patternFormat;
    final boolean typed;

    StringSchema(JSONObject jSONObject) {
        Set<String> linkedHashSet;
        super(jSONObject);
        this.typed = "string".equalsIgnoreCase(jSONObject.getString("type"));
        this.minLength = jSONObject.getIntValue("minLength", -1);
        this.maxLength = jSONObject.getIntValue("maxLength", -1);
        String string = jSONObject.getString("pattern");
        this.patternFormat = string;
        this.pattern = string == null ? null : Pattern.compile(string);
        String string2 = jSONObject.getString("format");
        this.format = string2;
        Object obj = jSONObject.get("anyOf");
        if (obj instanceof JSONArray) {
            this.anyOf = anyOf((JSONArray) obj, String.class);
        } else {
            this.anyOf = null;
        }
        Object obj2 = jSONObject.get("oneOf");
        if (obj2 instanceof JSONArray) {
            this.oneOf = oneOf((JSONArray) obj2, String.class);
        } else {
            this.oneOf = null;
        }
        this.constValue = jSONObject.getString("const");
        Object obj3 = jSONObject.get("enum");
        if (obj3 instanceof Collection) {
            Collection<? extends String> collection = (Collection) obj3;
            linkedHashSet = new LinkedHashSet<>(collection.size(), 1.0f);
            linkedHashSet.addAll(collection);
        } else {
            linkedHashSet = obj3 instanceof Object[] ? (Set) jSONObject.getObject("enum", TypeReference.collectionType(LinkedHashSet.class, String.class), new JSONReader.Feature[0]) : null;
        }
        this.enumValues = linkedHashSet;
        if (string2 == null) {
            this.formatValidator = null;
        }
        string2.hashCode();
        switch (string2) {
            case "duration":
                this.formatValidator = new Predicate() { // from class: com.alibaba.fastjson2.schema.StringSchema$$ExternalSyntheticLambda7
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj4) {
                        return StringSchema.lambda$new$1((String) obj4);
                    }
                };
                break;
            case "date-time":
                this.formatValidator = new Predicate() { // from class: com.alibaba.fastjson2.schema.StringSchema$$ExternalSyntheticLambda4
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj4) {
                        return DateUtils.isDate((String) obj4);
                    }
                };
                break;
            case "uri":
                this.formatValidator = new Predicate() { // from class: com.alibaba.fastjson2.schema.StringSchema$$ExternalSyntheticLambda3
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj4) {
                        return StringSchema.lambda$new$0((String) obj4);
                    }
                };
                break;
            case "date":
                this.formatValidator = new Predicate() { // from class: com.alibaba.fastjson2.schema.StringSchema$$ExternalSyntheticLambda5
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj4) {
                        return DateUtils.isLocalDate((String) obj4);
                    }
                };
                break;
            case "ipv4":
                this.formatValidator = new Predicate() { // from class: com.alibaba.fastjson2.schema.StringSchema$$ExternalSyntheticLambda1
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj4) {
                        return TypeUtils.validateIPv4((String) obj4);
                    }
                };
                break;
            case "ipv6":
                this.formatValidator = new Predicate() { // from class: com.alibaba.fastjson2.schema.StringSchema$$ExternalSyntheticLambda2
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj4) {
                        return TypeUtils.validateIPv6((String) obj4);
                    }
                };
                break;
            case "time":
                this.formatValidator = new Predicate() { // from class: com.alibaba.fastjson2.schema.StringSchema$$ExternalSyntheticLambda6
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj4) {
                        return DateUtils.isLocalTime((String) obj4);
                    }
                };
                break;
            case "uuid":
                this.formatValidator = new Predicate() { // from class: com.alibaba.fastjson2.schema.StringSchema$$ExternalSyntheticLambda8
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj4) {
                        return TypeUtils.isUUID((String) obj4);
                    }
                };
                break;
            case "email":
                this.formatValidator = new Predicate() { // from class: com.alibaba.fastjson2.schema.StringSchema$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj4) {
                        return StringSchema.isEmail((String) obj4);
                    }
                };
                break;
            default:
                this.formatValidator = null;
                break;
        }
    }

    static /* synthetic */ boolean lambda$new$0(String str) {
        if (str != null && !str.isEmpty()) {
            try {
                new URI(str);
                return true;
            } catch (URISyntaxException unused) {
            }
        }
        return false;
    }

    static /* synthetic */ boolean lambda$new$1(String str) {
        if (str != null && !str.isEmpty()) {
            try {
                Duration.parse(str);
                return true;
            } catch (DateTimeParseException unused) {
            }
        }
        return false;
    }

    @Override // com.alibaba.fastjson2.schema.JSONSchema
    public JSONSchema.Type getType() {
        return JSONSchema.Type.String;
    }

    @Override // com.alibaba.fastjson2.schema.JSONSchema
    public ValidateResult validate(Object obj) {
        if (obj == null) {
            if (this.typed) {
                return REQUIRED_NOT_MATCH;
            }
            return SUCCESS;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (this.minLength >= 0 || this.maxLength >= 0) {
                int iCodePointCount = str.codePointCount(0, str.length());
                int i = this.minLength;
                if (i >= 0 && iCodePointCount < i) {
                    return new ValidateResult(false, "minLength not match, expect >= %s, but %s", Integer.valueOf(this.minLength), Integer.valueOf(str.length()));
                }
                int i2 = this.maxLength;
                if (i2 >= 0 && iCodePointCount > i2) {
                    return new ValidateResult(false, "maxLength not match, expect <= %s, but %s", Integer.valueOf(this.maxLength), Integer.valueOf(str.length()));
                }
            }
            Pattern pattern = this.pattern;
            if (pattern != null && !pattern.matcher(str).find()) {
                return new ValidateResult(false, "pattern not match, expect %s, but %s", this.patternFormat, str);
            }
            Predicate<String> predicate = this.formatValidator;
            if (predicate != null && !predicate.test(str)) {
                return new ValidateResult(false, "format not match, expect %s, but %s", this.format, str);
            }
            AnyOf anyOf = this.anyOf;
            if (anyOf != null) {
                ValidateResult validateResultValidate = anyOf.validate(str);
                if (!validateResultValidate.isSuccess()) {
                    return validateResultValidate;
                }
            }
            OneOf oneOf = this.oneOf;
            if (oneOf != null) {
                ValidateResult validateResultValidate2 = oneOf.validate(str);
                if (!validateResultValidate2.isSuccess()) {
                    return validateResultValidate2;
                }
            }
            String str2 = this.constValue;
            if (str2 != null && !str2.equals(str)) {
                return new ValidateResult(false, "must be const %s, but %s", this.constValue, str);
            }
            Set<String> set = this.enumValues;
            if (set != null && !set.contains(str)) {
                return new ValidateResult(false, "not in enum values, %s", str);
            }
            return SUCCESS;
        }
        if (!this.typed) {
            return SUCCESS;
        }
        return new ValidateResult(false, "expect type %s, but %s", JSONSchema.Type.String, obj.getClass());
    }

    public static boolean isEmail(String str) {
        if (str == null || str.endsWith(".")) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(str);
        if (!matcher.matches()) {
            return false;
        }
        String strGroup = matcher.group(1);
        if (strGroup.length() > 64 || !USER_PATTERN.matcher(strGroup).matches()) {
            return false;
        }
        String strGroup2 = matcher.group(2);
        Matcher matcher2 = IP_DOMAIN_PATTERN.matcher(strGroup2);
        if (!matcher2.matches()) {
            return DomainValidator.isValid(strGroup2) || DomainValidator.isValidTld(strGroup2);
        }
        String strGroup3 = matcher2.group(1);
        return TypeUtils.validateIPv4(strGroup3) || TypeUtils.validateIPv6(strGroup3);
    }

    @Override // com.alibaba.fastjson2.schema.JSONSchema
    public JSONObject toJSONObject() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("type", "string");
        int i = this.minLength;
        if (i != -1) {
            jSONObject.put("minLength", Integer.valueOf(i));
        }
        String str = this.format;
        if (str != null) {
            jSONObject.put("format", str);
        }
        if (this.patternFormat != null) {
            jSONObject.put("pattern", this.pattern);
        }
        AnyOf anyOf = this.anyOf;
        if (anyOf != null) {
            jSONObject.put("anyOf", anyOf);
        }
        OneOf oneOf = this.oneOf;
        if (oneOf != null) {
            jSONObject.put("oneOf", oneOf);
        }
        String str2 = this.constValue;
        if (str2 != null) {
            jSONObject.put("const", str2);
        }
        Set<String> set = this.enumValues;
        if (set != null && !set.isEmpty()) {
            jSONObject.put("enum", this.enumValues);
        }
        return jSONObject;
    }
}
