package com.alibaba.fastjson.support.spring.annotation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.AbstractView;

/* JADX INFO: loaded from: classes.dex */
public class FastJsonView extends AbstractView {
    private boolean extractValueFromSingleKeyModel;
    private Set<String> renderedAttributes;
    private boolean disableCaching = true;
    private FastJsonConfig fastJsonConfig = new FastJsonConfig();

    public FastJsonView() {
        setContentType(FastJsonJsonView.DEFAULT_CONTENT_TYPE);
        setExposePathVariables(false);
    }

    public FastJsonConfig getFastJsonConfig() {
        return this.fastJsonConfig;
    }

    public void setFastJsonConfig(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
    }

    public void setRenderedAttributes(Set<String> set) {
        this.renderedAttributes = set;
    }

    public boolean isExtractValueFromSingleKeyModel() {
        return this.extractValueFromSingleKeyModel;
    }

    public void setExtractValueFromSingleKeyModel(boolean z) {
        this.extractValueFromSingleKeyModel = z;
    }

    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        Object objFilterModel = filterModel(map);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int iWriteJSONString = JSON.writeJSONString(byteArrayOutputStream, objFilterModel, this.fastJsonConfig.getSerializeFilters(), this.fastJsonConfig.getSerializerFeatures());
        if (this.fastJsonConfig.isWriteContentLength()) {
            httpServletResponse.setContentLength(iWriteJSONString);
        }
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        byteArrayOutputStream.writeTo(outputStream);
        byteArrayOutputStream.close();
        outputStream.flush();
    }

    protected void prepareResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        setResponseContentType(httpServletRequest, httpServletResponse);
        httpServletResponse.setCharacterEncoding(this.fastJsonConfig.getCharset().name());
        if (this.disableCaching) {
            httpServletResponse.addHeader(HttpHeaders.PRAGMA, "no-cache");
            httpServletResponse.addHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0");
            httpServletResponse.addDateHeader(HttpHeaders.EXPIRES, 1L);
        }
    }

    public void setDisableCaching(boolean z) {
        this.disableCaching = z;
    }

    public void setUpdateContentLength(boolean z) {
        this.fastJsonConfig.setWriteContentLength(z);
    }

    protected Object filterModel(Map<String, Object> map) {
        Set<String> setKeySet;
        HashMap map2 = new HashMap(map.size());
        if (!CollectionUtils.isEmpty(this.renderedAttributes)) {
            setKeySet = this.renderedAttributes;
        } else {
            setKeySet = map.keySet();
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!(entry.getValue() instanceof BindingResult) && setKeySet.contains(entry.getKey())) {
                map2.put(entry.getKey(), entry.getValue());
            }
        }
        if (this.extractValueFromSingleKeyModel && map2.size() == 1) {
            Iterator it = map2.entrySet().iterator();
            if (it.hasNext()) {
                return ((Map.Entry) it.next()).getValue();
            }
        }
        return map2;
    }

    protected void setResponseContentType(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        super.setResponseContentType(httpServletRequest, httpServletResponse);
    }
}
