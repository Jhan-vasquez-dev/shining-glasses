package com.cdbwsoft.library.adapter;

import android.view.View;
import android.widget.TextView;
import com.cdbwsoft.library.AppConfig;
import com.cdbwsoft.library.R;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class DataVO {
    private int mPosition;

    public long getId() {
        return this.mPosition;
    }

    public void setPosition(int i) {
        this.mPosition = i;
    }

    public void bindData(View view) {
        Map map;
        View viewFindViewById;
        View view2;
        Field[] declaredFields = getClass().getDeclaredFields();
        Object tag = view.getTag(R.id.data_holder_tag);
        if (tag != null) {
            map = (Map) tag;
        } else {
            map = new HashMap();
            if (declaredFields != null && declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    FieldBind fieldBind = (FieldBind) field.getAnnotation(FieldBind.class);
                    if (fieldBind != null && (viewFindViewById = view.findViewById(fieldBind.value())) != null) {
                        map.put(field, viewFindViewById);
                    }
                }
            }
        }
        for (Field field2 : map.keySet()) {
            if (field2 != null && (view2 = (View) map.get(field2)) != null) {
                field2.setAccessible(true);
                try {
                    Object obj = field2.get(this);
                    if (view2 instanceof TextView) {
                        ((TextView) view2).setText(String.valueOf(obj));
                    }
                } catch (IllegalAccessException e) {
                    if (AppConfig.DEBUG) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
