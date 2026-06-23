package com.alibaba.fastjson2.support.arrow;

import com.alibaba.fastjson2.stream.StreamReader;
import com.alibaba.fastjson2.support.csv.CSVReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import org.apache.http.message.TokenParser;

/* JADX INFO: loaded from: classes.dex */
public class CSVUtils {
    public static String genMaxComputeCreateTable(File file, String str) throws IOException {
        boolean z;
        CSVReader cSVReaderOf = CSVReader.of(file, new Type[0]);
        cSVReaderOf.readHeader();
        cSVReaderOf.statAll();
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        sb.append(str).append(" (\n");
        List<StreamReader.ColumnStat> columnStats = cSVReaderOf.getColumnStats();
        for (int i = 0; i < columnStats.size(); i++) {
            StreamReader.ColumnStat columnStat = columnStats.get(i);
            StringBuilder sb2 = new StringBuilder();
            for (int i2 = 0; i2 < columnStat.name.length(); i2++) {
                char cCharAt = columnStat.name.charAt(i2);
                if (cCharAt != 65533) {
                    if (cCharAt == ' ' || cCharAt == '-' || cCharAt == '+' || cCharAt == '.') {
                        sb2.append('_');
                    } else {
                        sb2.append(cCharAt);
                    }
                }
            }
            String string = sb2.toString();
            for (int i3 = 0; i3 < string.length(); i3++) {
                char cCharAt2 = string.charAt(i3);
                boolean z2 = (cCharAt2 >= 'a' && cCharAt2 <= 'z') || (cCharAt2 >= 'A' && cCharAt2 <= 'Z') || cCharAt2 == '_';
                if ((i3 == 0 && !z2) || (!z2 && (cCharAt2 < '0' || cCharAt2 > '9'))) {
                    z = true;
                    break;
                }
            }
            z = false;
            if (!z && string.length() > 30) {
                z = true;
            }
            sb.append('\t');
            if (z) {
                sb.append("COL_").append(i);
            } else {
                sb.append(string);
            }
            sb.append(TokenParser.SP);
            sb.append(columnStat.getInferSQLType());
            if (z) {
                sb.append(" COMMENT '");
                for (int i4 = 0; i4 < columnStat.name.length(); i4++) {
                    char cCharAt3 = columnStat.name.charAt(i4);
                    if (cCharAt3 != 65533) {
                        if (cCharAt3 == '\'') {
                            sb.append(cCharAt3);
                        }
                        sb.append(cCharAt3);
                    }
                }
                sb.append('\'');
            }
            if (i != columnStats.size() - 1) {
                sb.append(',');
            }
            sb.append("\n");
        }
        sb.append(");");
        return sb.toString();
    }
}
