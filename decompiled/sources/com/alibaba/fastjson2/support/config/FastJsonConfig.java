package com.alibaba.fastjson2.support.config;

import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.SymbolTable;
import com.alibaba.fastjson2.filter.Filter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/* JADX INFO: loaded from: classes.dex */
public class FastJsonConfig {
    private boolean jsonb;
    private SymbolTable symbolTable;
    private String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private Charset charset = StandardCharsets.UTF_8;
    private JSONReader.Feature[] readerFeatures = new JSONReader.Feature[0];
    private JSONWriter.Feature[] writerFeatures = {JSONWriter.Feature.WriteByteArrayAsBase64, JSONWriter.Feature.BrowserSecure};
    private Filter[] readerFilters = new Filter[0];
    private Filter[] writerFilters = new Filter[0];
    private boolean writeContentLength = true;

    public Charset getCharset() {
        return this.charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public String getDateFormat() {
        return this.dateFormat;
    }

    public void setDateFormat(String str) {
        this.dateFormat = str;
    }

    public JSONReader.Feature[] getReaderFeatures() {
        return this.readerFeatures;
    }

    public void setReaderFeatures(JSONReader.Feature... featureArr) {
        this.readerFeatures = featureArr;
    }

    public JSONWriter.Feature[] getWriterFeatures() {
        return this.writerFeatures;
    }

    public void setWriterFeatures(JSONWriter.Feature... featureArr) {
        this.writerFeatures = featureArr;
    }

    public Filter[] getReaderFilters() {
        return this.readerFilters;
    }

    public void setReaderFilters(Filter... filterArr) {
        this.readerFilters = filterArr;
    }

    public Filter[] getWriterFilters() {
        return this.writerFilters;
    }

    public void setWriterFilters(Filter... filterArr) {
        this.writerFilters = filterArr;
    }

    public boolean isWriteContentLength() {
        return this.writeContentLength;
    }

    public void setWriteContentLength(boolean z) {
        this.writeContentLength = z;
    }

    public boolean isJSONB() {
        return this.jsonb;
    }

    public void setJSONB(boolean z) {
        this.jsonb = z;
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    public void setSymbolTable(String... strArr) {
        this.symbolTable = JSONB.symbolTable(strArr);
    }
}
