package com.alibaba.fastjson2.support.geo;

import com.alibaba.fastjson2.annotation.JSONType;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
@JSONType(orders = {"type", "bbox", "coordinates"}, typeName = "FeatureCollection")
public class FeatureCollection extends Geometry {
    private List<Feature> features;

    public FeatureCollection() {
        super("FeatureCollection");
        this.features = new ArrayList();
    }

    public List<Feature> getFeatures() {
        return this.features;
    }
}
