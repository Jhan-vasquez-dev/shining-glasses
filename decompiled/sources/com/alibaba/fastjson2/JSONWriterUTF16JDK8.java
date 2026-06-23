package com.alibaba.fastjson2;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.JDKUtils;

/* JADX INFO: loaded from: classes.dex */
final class JSONWriterUTF16JDK8 extends JSONWriterUTF16 {
    JSONWriterUTF16JDK8(JSONWriter.Context context) {
        super(context);
    }

    @Override // com.alibaba.fastjson2.JSONWriterUTF16, com.alibaba.fastjson2.JSONWriter
    public void writeString(String str) {
        if (str == null) {
            if (isEnabled(JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullStringAsEmpty.mask)) {
                writeString("");
                return;
            } else {
                writeNull();
                return;
            }
        }
        boolean z = (this.context.features & JSONWriter.Feature.BrowserSecure.mask) != 0;
        boolean z2 = (this.context.features & JSONWriter.Feature.EscapeNoneAscii.mask) != 0;
        char[] charArray = JDKUtils.getCharArray(str);
        int length = charArray.length;
        for (char c : charArray) {
            if (c == this.quote || c == '\\' || c < ' ' || ((z && (c == '<' || c == '>' || c == '(' || c == ')')) || (z2 && c > 127))) {
                writeStringEscape(str);
                return;
            }
        }
        int i = this.off + length + 2;
        if (i >= this.chars.length) {
            grow0(i);
        }
        char[] cArr = this.chars;
        int i2 = this.off;
        this.off = i2 + 1;
        cArr[i2] = this.quote;
        System.arraycopy(charArray, 0, this.chars, this.off, charArray.length);
        this.off += length;
        char[] cArr2 = this.chars;
        int i3 = this.off;
        this.off = i3 + 1;
        cArr2[i3] = this.quote;
    }
}
