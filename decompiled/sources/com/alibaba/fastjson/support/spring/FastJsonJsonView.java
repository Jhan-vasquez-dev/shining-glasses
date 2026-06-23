package com.alibaba.fastjson.support.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.util.IOUtils;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpHeaders;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.AbstractView;

/* JADX INFO: loaded from: classes.dex */
public class FastJsonJsonView extends AbstractView {
    private static final Pattern CALLBACK_PARAM_PATTERN = Pattern.compile("[0-9A-Za-z_\\.]*");
    public static final String DEFAULT_CONTENT_TYPE = "application/json;charset=UTF-8";
    public static final String DEFAULT_JSONP_CONTENT_TYPE = "application/javascript";

    @Deprecated
    protected String dateFormat;
    private boolean extractValueFromSingleKeyModel;
    private Set<String> renderedAttributes;

    @Deprecated
    protected Charset charset = StandardCharsets.UTF_8;

    @Deprecated
    protected SerializerFeature[] features = SerializerFeature.EMPTY;

    @Deprecated
    protected SerializeFilter[] filters = new SerializeFilter[0];
    private boolean disableCaching = true;
    private boolean updateContentLength = true;
    private FastJsonConfig fastJsonConfig = new FastJsonConfig();
    private String[] jsonpParameterNames = {"jsonp", "callback"};

    public FastJsonJsonView() {
        setContentType(DEFAULT_CONTENT_TYPE);
        setExposePathVariables(false);
    }

    public FastJsonConfig getFastJsonConfig() {
        return this.fastJsonConfig;
    }

    public void setFastJsonConfig(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
    }

    @Deprecated
    public void setSerializerFeature(SerializerFeature... serializerFeatureArr) {
        this.fastJsonConfig.setSerializerFeatures(serializerFeatureArr);
    }

    @Deprecated
    public Charset getCharset() {
        return this.fastJsonConfig.getCharset();
    }

    @Deprecated
    public void setCharset(Charset charset) {
        this.fastJsonConfig.setCharset(charset);
    }

    @Deprecated
    public String getDateFormat() {
        return this.fastJsonConfig.getDateFormat();
    }

    @Deprecated
    public void setDateFormat(String str) {
        this.fastJsonConfig.setDateFormat(str);
    }

    @Deprecated
    public SerializerFeature[] getFeatures() {
        return this.fastJsonConfig.getSerializerFeatures();
    }

    @Deprecated
    public void setFeatures(SerializerFeature... serializerFeatureArr) {
        this.fastJsonConfig.setSerializerFeatures(serializerFeatureArr);
    }

    @Deprecated
    public SerializeFilter[] getFilters() {
        return this.fastJsonConfig.getSerializeFilters();
    }

    @Deprecated
    public void setFilters(SerializeFilter... serializeFilterArr) {
        this.fastJsonConfig.setSerializeFilters(serializeFilterArr);
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

    public void setJsonpParameterNames(Set<String> set) {
        Assert.notEmpty(set, "jsonpParameterName cannot be empty");
        this.jsonpParameterNames = (String[]) set.toArray(new String[set.size()]);
    }

    private String getJsonpParameterValue(HttpServletRequest httpServletRequest) {
        String[] strArr = this.jsonpParameterNames;
        if (strArr == null) {
            return null;
        }
        for (String str : strArr) {
            String parameter = httpServletRequest.getParameter(str);
            if (IOUtils.isValidJsonpQueryParam(parameter)) {
                return parameter;
            }
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Ignoring invalid jsonp parameter value: " + parameter);
            }
        }
        return null;
    }

    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        Object objFilterModel = filterModel(map);
        String jsonpParameterValue = getJsonpParameterValue(httpServletRequest);
        Object obj = objFilterModel;
        if (jsonpParameterValue != null) {
            JSONPObject jSONPObject = new JSONPObject(jsonpParameterValue);
            jSONPObject.addParameter(objFilterModel);
            obj = jSONPObject;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int iWriteJSONString = JSON.writeJSONString(byteArrayOutputStream, obj, this.fastJsonConfig.getSerializeFilters(), this.fastJsonConfig.getSerializerFeatures());
        if (this.updateContentLength) {
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
        this.updateContentLength = z;
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
        if (getJsonpParameterValue(httpServletRequest) != null) {
            httpServletResponse.setContentType(DEFAULT_JSONP_CONTENT_TYPE);
        } else {
            super.setResponseContentType(httpServletRequest, httpServletResponse);
        }
    }
}
