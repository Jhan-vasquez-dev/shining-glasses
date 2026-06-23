package com.alibaba.fastjson2.support.geo;

import com.alibaba.fastjson2.annotation.JSONType;

/* JADX INFO: loaded from: classes.dex */
@JSONType(orders = {"type", "bbox", "coordinates"}, typeName = "LineString")
public class LineString extends Geometry {
    private double[][] coordinates;

    public LineString() {
        super("LineString");
    }

    public double[][] getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(double[][] dArr) {
        this.coordinates = dArr;
    }
}
