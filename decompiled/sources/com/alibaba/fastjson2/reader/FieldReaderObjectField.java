package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
class FieldReaderObjectField<T> extends FieldReaderObject<T> {
    FieldReaderObjectField(String str, Type type, Class cls, int i, long j, String str2, Locale locale, Object obj, JSONSchema jSONSchema, Field field) {
        super(str, type == null ? field.getType() : type, cls, i, j, str2, locale, obj, jSONSchema, null, field, null);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, boolean z) {
        if (this.schema != null) {
            this.schema.assertValidate(Boolean.valueOf(z));
        }
        if (this.fieldOffset != -1 && this.fieldClass == Boolean.TYPE) {
            JDKUtils.UNSAFE.putBoolean(t, this.fieldOffset, z);
            return;
        }
        try {
            this.field.setBoolean(t, z);
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, byte b) {
        if (this.schema != null) {
            this.schema.assertValidate(b);
        }
        if (this.fieldOffset != -1 && this.fieldClass == Byte.TYPE) {
            JDKUtils.UNSAFE.putByte(t, this.fieldOffset, b);
            return;
        }
        try {
            this.field.setByte(t, b);
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, short s) {
        if (this.schema != null) {
            this.schema.assertValidate(s);
        }
        if (this.fieldOffset != -1 && this.fieldClass == Short.TYPE) {
            JDKUtils.UNSAFE.putShort(t, this.fieldOffset, s);
            return;
        }
        try {
            this.field.setShort(t, s);
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, int i) {
        if (this.schema != null) {
            this.schema.assertValidate(i);
        }
        if (this.fieldOffset != -1 && this.fieldClass == Integer.TYPE) {
            JDKUtils.UNSAFE.putInt(t, this.fieldOffset, i);
            return;
        }
        try {
            this.field.setInt(t, i);
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, long j) {
        if (this.schema != null) {
            this.schema.assertValidate(j);
        }
        if (this.fieldOffset != -1 && this.fieldClass == Long.TYPE) {
            JDKUtils.UNSAFE.putLong(t, this.fieldOffset, j);
            return;
        }
        try {
            this.field.setLong(t, j);
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, float f) {
        if (this.schema != null) {
            this.schema.assertValidate(f);
        }
        if (this.fieldOffset != -1 && this.fieldClass == Float.TYPE) {
            JDKUtils.UNSAFE.putFloat(t, this.fieldOffset, f);
            return;
        }
        try {
            this.field.setFloat(t, f);
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, double d) {
        if (this.schema != null) {
            this.schema.assertValidate(d);
        }
        if (this.fieldOffset != -1 && this.fieldClass == Double.TYPE) {
            JDKUtils.UNSAFE.putDouble(t, this.fieldOffset, d);
            return;
        }
        try {
            this.field.setDouble(t, d);
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, char c) {
        if (this.schema != null) {
            this.schema.assertValidate(c);
        }
        if (this.fieldOffset != -1 && this.fieldClass == Character.TYPE) {
            JDKUtils.UNSAFE.putChar(t, this.fieldOffset, c);
            return;
        }
        try {
            this.field.setChar(t, c);
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        if (this.schema != null) {
            this.schema.assertValidate(obj);
        }
        if (obj == null) {
            if ((this.features & JSONReader.Feature.IgnoreSetNullValue.mask) != 0) {
                return;
            }
        } else {
            if (this.fieldClass.isPrimitive()) {
                acceptPrimitive(t, obj);
                return;
            }
            if (this.fieldType != this.fieldClass && Map.class.isAssignableFrom(this.fieldClass) && (obj instanceof Map) && this.fieldClass != Map.class) {
                obj = getObjectReader(JSONFactory.createReadContext()).createInstance((Map) obj, new JSONReader.Feature[0]);
            } else if (!this.fieldClass.isInstance(obj)) {
                if (obj instanceof String) {
                    String str = (String) obj;
                    if (this.fieldClass == LocalDate.class) {
                        if (this.format != null) {
                            obj = LocalDate.parse(str, DateTimeFormatter.ofPattern(this.format));
                        } else {
                            obj = DateUtils.parseLocalDate(str);
                        }
                    } else if (this.fieldClass == Date.class) {
                        if (this.format != null) {
                            obj = DateUtils.parseDate(str, this.format, DateUtils.DEFAULT_ZONE_ID);
                        } else {
                            obj = DateUtils.parseDate(str);
                        }
                    }
                }
                if (!this.fieldClass.isInstance(obj)) {
                    obj = TypeUtils.cast(obj, this.fieldType);
                }
            }
        }
        if (this.fieldOffset != -1) {
            JDKUtils.UNSAFE.putObject(t, this.fieldOffset, obj);
            return;
        }
        try {
            this.field.set(t, obj);
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    final void acceptPrimitive(T t, Object obj) {
        if (this.fieldClass == Integer.TYPE) {
            if (obj instanceof Number) {
                accept((Object) t, ((Number) obj).intValue());
                return;
            }
        } else if (this.fieldClass == Long.TYPE) {
            if (obj instanceof Number) {
                accept((Object) t, ((Number) obj).longValue());
                return;
            }
        } else if (this.fieldClass == Float.TYPE) {
            if (obj instanceof Number) {
                accept((Object) t, ((Number) obj).floatValue());
                return;
            }
        } else if (this.fieldClass == Double.TYPE) {
            if (obj instanceof Number) {
                accept(t, ((Number) obj).doubleValue());
                return;
            }
        } else if (this.fieldClass == Short.TYPE) {
            if (obj instanceof Number) {
                accept((Object) t, ((Number) obj).shortValue());
                return;
            }
        } else if (this.fieldClass == Byte.TYPE) {
            if (obj instanceof Number) {
                accept((Object) t, ((Number) obj).byteValue());
                return;
            }
        } else if (this.fieldClass == Character.TYPE) {
            if (obj instanceof Character) {
                accept((Object) t, ((Character) obj).charValue());
                return;
            }
        } else if (this.fieldClass == Boolean.TYPE && (obj instanceof Boolean)) {
            accept(t, ((Boolean) obj).booleanValue());
            return;
        }
        throw new JSONException("set " + this.fieldName + " error, type not support " + obj.getClass());
    }
}
