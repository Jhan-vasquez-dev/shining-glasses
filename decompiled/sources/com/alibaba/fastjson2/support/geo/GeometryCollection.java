package com.alibaba.fastjson2.support.geo;

import com.alibaba.fastjson2.annotation.JSONType;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
@JSONType(orders = {"type", "bbox", "geometries"}, typeName = "GeometryCollection")
public class GeometryCollection extends Geometry {
    private List<Geometry> geometries;

    public GeometryCollection() {
        super("GeometryCollection");
        this.geometries = new ArrayList();
    }

    public List<Geometry> getGeometries() {
        return this.geometries;
    }
}
