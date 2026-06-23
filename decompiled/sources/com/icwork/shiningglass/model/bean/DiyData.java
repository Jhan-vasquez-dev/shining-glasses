package com.icwork.shiningglass.model.bean;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class DiyData {
    private ArrayList<Integer> colorArray;
    private byte[] data;
    private Long diyId;
    private int index;

    public DiyData(byte[] bArr, ArrayList<Integer> arrayList) {
        this.data = bArr;
        this.colorArray = arrayList;
    }

    public DiyData(Long l, byte[] bArr, int i, ArrayList<Integer> arrayList) {
        this.diyId = l;
        this.data = bArr;
        this.index = i;
        this.colorArray = arrayList;
    }

    public DiyData() {
    }

    public Long getDiyId() {
        return this.diyId;
    }

    public void setDiyId(Long l) {
        this.diyId = l;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] bArr) {
        this.data = bArr;
    }

    public ArrayList<Integer> getColorArray() {
        return this.colorArray;
    }

    public void setColorArray(ArrayList<Integer> arrayList) {
        this.colorArray = arrayList;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public String toString() {
        return "DiyData{diyId=" + this.diyId + ", index=" + this.index + '}';
    }
}
