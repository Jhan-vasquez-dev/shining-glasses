package org.greenrobot.greendao.converter;

/* JADX INFO: loaded from: classes2.dex */
public interface PropertyConverter<P, D> {
    D convertToDatabaseValue(P p);

    P convertToEntityProperty(D d);
}
