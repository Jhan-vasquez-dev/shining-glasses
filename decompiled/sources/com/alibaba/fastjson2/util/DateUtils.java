package com.alibaba.fastjson2.util;

import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.internal.asm.Opcodes;
import com.alibaba.fastjson2.reader.ObjectReaderImplDate;
import com.cdbwsoft.library.audio.AudioPlayer;
import com.loopj.android.http.AsyncHttpClient;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.zone.ZoneRules;
import java.util.Date;
import java.util.Locale;
import java.util.function.BiFunction;
import kotlin.time.DurationKt;
import okhttp3.internal.http2.Http2Connection;
import org.apache.http.HttpStatus;
import org.apache.http.message.TokenParser;

/* JADX INFO: loaded from: classes.dex */
public class DateUtils {
    static DateTimeFormatter DATE_TIME_FORMATTER_34 = null;
    static DateTimeFormatter DATE_TIME_FORMATTER_COOKIE = null;
    static DateTimeFormatter DATE_TIME_FORMATTER_COOKIE_LOCAL = null;
    static DateTimeFormatter DATE_TIME_FORMATTER_RFC_2822 = null;
    public static final ZoneId DEFAULT_ZONE_ID;
    public static final LocalDate LOCAL_DATE_19700101;
    static final int LOCAL_EPOCH_DAY;
    public static final ZoneId OFFSET_8_ZONE_ID;
    public static final String OFFSET_8_ZONE_ID_NAME = "+08:00";
    private static final int[] POWERS;
    public static final ZoneId SHANGHAI_ZONE_ID;
    public static final String SHANGHAI_ZONE_ID_NAME = "Asia/Shanghai";
    public static final ZoneRules SHANGHAI_ZONE_RULES;

    public static int getShanghaiZoneOffsetTotalSeconds(long j) {
        if (j >= 684900000) {
            return 28800;
        }
        if (j >= 671598000) {
            return 32400;
        }
        if (j >= 653450400) {
            return 28800;
        }
        if (j >= 640148400) {
            return 32400;
        }
        if (j >= 622000800) {
            return 28800;
        }
        if (j >= 608698800) {
            return 32400;
        }
        if (j >= 589946400) {
            return 28800;
        }
        if (j >= 577249200) {
            return 32400;
        }
        if (j >= 558496800) {
            return 28800;
        }
        if (j >= 545194800) {
            return 32400;
        }
        if (j >= 527047200) {
            return 28800;
        }
        if (j >= 515559600) {
            return 32400;
        }
        if (j >= -649987200) {
            return 28800;
        }
        if (j >= -652316400) {
            return 32400;
        }
        if (j >= -670636800) {
            return 28800;
        }
        if (j >= -683852400) {
            return 32400;
        }
        if (j >= -699580800) {
            return 28800;
        }
        if (j >= -716857200) {
            return 32400;
        }
        if (j >= -733795200) {
            return 28800;
        }
        if (j >= -745801200) {
            return 32400;
        }
        if (j >= -767836800) {
            return 28800;
        }
        if (j >= -881017200) {
            return 32400;
        }
        if (j >= -888796800) {
            return 28800;
        }
        if (j >= -908838000) {
            return 32400;
        }
        if (j >= -922060800) {
            return 28800;
        }
        if (j >= -933634800) {
            return 32400;
        }
        if (j >= -1585872000) {
            return 28800;
        }
        if (j >= -1600642800) {
            return 32400;
        }
        return j >= -2177452800L ? 28800 : 29143;
    }

    public static int hourAfterNoon(char c, char c2) {
        if (c != '0') {
            if (c == '1') {
                switch (c2) {
                    case '0':
                        c = '2';
                        c2 = '2';
                        break;
                    case '1':
                        c2 = '3';
                        c = '2';
                        break;
                    case '2':
                        c2 = '4';
                        c = '2';
                        break;
                }
            }
        } else {
            switch (c2) {
                case '0':
                    c2 = '2';
                    c = '1';
                    break;
                case '1':
                    c2 = '3';
                    c = '1';
                    break;
                case '2':
                    c2 = '4';
                    c = '1';
                    break;
                case '3':
                    c2 = '5';
                    c = '1';
                    break;
                case '4':
                    c2 = '6';
                    c = '1';
                    break;
                case '5':
                    c2 = '7';
                    c = '1';
                    break;
                case '6':
                    c2 = '8';
                    c = '1';
                    break;
                case '7':
                    c2 = '9';
                    c = '1';
                    break;
                case '8':
                    c = '2';
                    c2 = '0';
                    break;
                case '9':
                    c = '2';
                    c2 = '1';
                    break;
            }
        }
        return (c << 16) | c2;
    }

    public static int month(char c, char c2, char c3) {
        switch ((c << 16) | (c2 << '\b') | c3) {
            case 4288626:
                return 4;
            case 4289895:
                return 8;
            case 4482403:
                return 12;
            case 4613474:
                return 2;
            case 4874606:
                return 1;
            case 4879724:
                return 7;
            case 4879726:
                return 6;
            case 5071218:
                return 3;
            case 5071225:
                return 5;
            case 5140342:
                return 11;
            case 5202804:
                return 10;
            case 5465456:
                return 9;
            default:
                return -1;
        }
    }

    static {
        int shanghaiZoneOffsetTotalSeconds;
        ZoneId zoneIdSystemDefault = ZoneId.systemDefault();
        DEFAULT_ZONE_ID = zoneIdSystemDefault;
        ZoneRules rules = null;
        try {
            if (!SHANGHAI_ZONE_ID_NAME.equals(zoneIdSystemDefault.getId())) {
                zoneIdSystemDefault = ZoneId.of(SHANGHAI_ZONE_ID_NAME);
            }
            try {
                rules = zoneIdSystemDefault.getRules();
            } catch (Exception unused) {
            }
        } catch (Exception unused2) {
            zoneIdSystemDefault = null;
        }
        SHANGHAI_ZONE_ID = zoneIdSystemDefault;
        SHANGHAI_ZONE_RULES = rules;
        OFFSET_8_ZONE_ID = ZoneId.of(OFFSET_8_ZONE_ID_NAME);
        LOCAL_DATE_19700101 = LocalDate.of(1970, 1, 1);
        long jCurrentTimeMillis = System.currentTimeMillis();
        ZoneId zoneId = DEFAULT_ZONE_ID;
        long jFloorDiv = Math.floorDiv(jCurrentTimeMillis, 1000L);
        if (zoneId == zoneIdSystemDefault || zoneId.getRules() == rules) {
            shanghaiZoneOffsetTotalSeconds = getShanghaiZoneOffsetTotalSeconds(jFloorDiv);
        } else {
            shanghaiZoneOffsetTotalSeconds = zoneId.getRules().getOffset(Instant.ofEpochMilli(jCurrentTimeMillis)).getTotalSeconds();
        }
        LOCAL_EPOCH_DAY = (int) Math.floorDiv(jFloorDiv + ((long) shanghaiZoneOffsetTotalSeconds), 86400L);
        POWERS = new int[]{1, 10, 100, 1000, AsyncHttpClient.DEFAULT_SOCKET_TIMEOUT, 100000, DurationKt.NANOS_IN_MILLIS, 10000000, 100000000, Http2Connection.DEGRADED_PONG_TIMEOUT_NS, 0, 0, 0, 0, 0, 0};
    }

    static class CacheDate8 {
        static final String[] CACHE = new String[1024];

        CacheDate8() {
        }
    }

    static class CacheDate10 {
        static final String[] CACHE = new String[1024];

        CacheDate10() {
        }
    }

    public static Date parseDateYMDHMS19(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        return new Date(parseMillisYMDHMS19(str, DEFAULT_ZONE_ID));
    }

    public static Date parseDate(String str, String str2) {
        return parseDate(str, str2, DEFAULT_ZONE_ID);
    }

    public static Date parseDate(String str, String str2, ZoneId zoneId) {
        if (str == null || str.isEmpty() || "null".equals(str)) {
            return null;
        }
        if (str2 == null || str2.isEmpty()) {
            long millis = parseMillis(str, zoneId);
            if (millis == 0) {
                return null;
            }
            return new Date(millis);
        }
        str2.hashCode();
        switch (str2) {
            case "yyyyMMddHHmmssSSSZ":
                return new Date(parseMillis(str, DEFAULT_ZONE_ID));
            case "yyyyMMdd":
                LocalDate localDate = LocalDate.parse(str, DateTimeFormatter.ofPattern(str2));
                return new Date(millis(zoneId, localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0, 0, 0));
            case "yyyy-MM-dd":
                return new Date(parseMillis10(str, zoneId, DateTimeFormatPattern.DATE_FORMAT_10_DASH));
            case "yyyy/MM/dd":
                return new Date(parseMillis10(str, zoneId, DateTimeFormatPattern.DATE_FORMAT_10_SLASH));
            case "yyyy/MM/dd HH:mm:ss":
                return new Date(parseMillis19(str, zoneId, DateTimeFormatPattern.DATE_TIME_FORMAT_19_SLASH));
            case "yyyy-MM-dd HH:mm:ss":
                return new Date(parseMillisYMDHMS19(str, zoneId));
            case "dd.MM.yyyy HH:mm:ss":
                return new Date(parseMillis19(str, zoneId, DateTimeFormatPattern.DATE_TIME_FORMAT_19_DOT));
            case "yyyy-MM-dd'T'HH:mm:ss":
                return new Date(parseMillis19(str, zoneId, DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH_T));
            case "iso8601":
                return parseDate(str);
            default:
                if (zoneId == null) {
                    zoneId = DEFAULT_ZONE_ID;
                }
                return new Date(millis(LocalDateTime.parse(str, DateTimeFormatter.ofPattern(str2)), zoneId));
        }
    }

    public static Date parseDate(String str) {
        long millis = parseMillis(str, DEFAULT_ZONE_ID);
        if (millis == 0) {
            return null;
        }
        return new Date(millis);
    }

    public static Date parseDate(String str, ZoneId zoneId) {
        long millis = parseMillis(str, zoneId);
        if (millis == 0) {
            return null;
        }
        return new Date(millis);
    }

    public static long parseMillis(String str) {
        return parseMillis(str, DEFAULT_ZONE_ID);
    }

    public static long parseMillis(String str, ZoneId zoneId) {
        if (str == null) {
            return 0L;
        }
        if (JDKUtils.STRING_CODER != null && JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER.applyAsInt(str) == 0) {
            byte[] bArrApply = JDKUtils.STRING_VALUE.apply(str);
            return parseMillis(bArrApply, 0, bArrApply.length, StandardCharsets.ISO_8859_1, zoneId);
        }
        char[] charArray = JDKUtils.getCharArray(str);
        return parseMillis(charArray, 0, charArray.length, zoneId);
    }

    public static LocalDateTime parseLocalDateTime(String str) {
        if (str == null) {
            return null;
        }
        return parseLocalDateTime(str, 0, str.length());
    }

    public static LocalDateTime parseLocalDateTime(String str, int i, int i2) {
        LocalDateTime localDateTime;
        if (str == null || i2 == 0) {
            return null;
        }
        if (JDKUtils.STRING_CODER != null && JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER.applyAsInt(str) == 0) {
            localDateTime = parseLocalDateTime(JDKUtils.STRING_VALUE.apply(str), i, i2);
        } else if (JDKUtils.JVM_VERSION == 8 && !JDKUtils.FIELD_STRING_VALUE_ERROR) {
            localDateTime = parseLocalDateTime(JDKUtils.getCharArray(str), i, i2);
        } else {
            char[] cArr = new char[i2];
            str.getChars(i, i + i2, cArr, 0);
            localDateTime = parseLocalDateTime(cArr, i, i2);
        }
        if (localDateTime != null) {
            return localDateTime;
        }
        str.hashCode();
        switch (str) {
            case "0000-0-00":
            case "0000-00-0":
            case "000000000000":
            case "00000000":
            case "":
            case "null":
            case "0000年00月00日":
            case "0000-00-00":
                return null;
            default:
                throw new DateTimeParseException(str, str, i);
        }
    }

    public static LocalDateTime parseLocalDateTime(char[] cArr, int i, int i2) {
        if (cArr == null || i2 == 0) {
            return null;
        }
        switch (i2) {
            case 4:
                if (cArr[i] == 'n' && cArr[i + 1] == 'u' && cArr[i + 2] == 'l' && cArr[i + 3] == 'l') {
                    return null;
                }
                String str = new String(cArr, i, i2);
                throw new DateTimeParseException("illegal input ".concat(str), str, 0);
            case 5:
            case 6:
            case 7:
            case 13:
            case 15:
            default:
                return parseLocalDateTimeX(cArr, i, i2);
            case 8:
                if (cArr[2] == ':' && cArr[5] == ':') {
                    return LocalDateTime.of(LOCAL_DATE_19700101, parseLocalTime8(cArr, i));
                }
                LocalDate localDate8 = parseLocalDate8(cArr, i);
                if (localDate8 == null) {
                    return null;
                }
                return LocalDateTime.of(localDate8, LocalTime.MIN);
            case 9:
                LocalDate localDate9 = parseLocalDate9(cArr, i);
                if (localDate9 == null) {
                    return null;
                }
                return LocalDateTime.of(localDate9, LocalTime.MIN);
            case 10:
                LocalDate localDate10 = parseLocalDate10(cArr, i);
                if (localDate10 == null) {
                    return null;
                }
                return LocalDateTime.of(localDate10, LocalTime.MIN);
            case 11:
                LocalDate localDate11 = parseLocalDate11(cArr, i);
                if (localDate11 == null) {
                    return null;
                }
                return LocalDateTime.of(localDate11, LocalTime.MIN);
            case 12:
                return parseLocalDateTime12(cArr, i);
            case 14:
                return parseLocalDateTime14(cArr, i);
            case 16:
                return parseLocalDateTime16(cArr, i);
            case 17:
                return parseLocalDateTime17(cArr, i);
            case 18:
                return parseLocalDateTime18(cArr, i);
            case 19:
                return parseLocalDateTime19(cArr, i);
            case 20:
                return parseLocalDateTime20(cArr, i);
        }
    }

    public static LocalTime parseLocalTime5(byte[] bArr, int i) {
        int iDigit1;
        int iDigit2;
        int iDigit22;
        if (i + 5 > bArr.length) {
            return null;
        }
        int i2 = i + 2;
        if (bArr[i2] == 58) {
            iDigit22 = IOUtils.digit2(bArr, i);
            iDigit2 = IOUtils.digit2(bArr, i + 3);
            iDigit1 = 0;
        } else {
            if (bArr[i + 1] != 58 || bArr[i + 3] != 58) {
                return null;
            }
            int iDigit12 = IOUtils.digit1(bArr, i);
            int iDigit13 = IOUtils.digit1(bArr, i2);
            iDigit1 = IOUtils.digit1(bArr, i + 4);
            iDigit2 = iDigit13;
            iDigit22 = iDigit12;
        }
        return localTime(iDigit22, iDigit2, iDigit1);
    }

    public static LocalTime parseLocalTime5(char[] cArr, int i) {
        int iDigit1;
        int iDigit2;
        int iDigit22;
        if (i + 5 > cArr.length) {
            return null;
        }
        int i2 = i + 2;
        if (cArr[i2] == ':') {
            iDigit22 = IOUtils.digit2(cArr, i);
            iDigit2 = IOUtils.digit2(cArr, i + 3);
            iDigit1 = 0;
        } else {
            if (cArr[i + 1] != ':' || cArr[i + 3] != ':') {
                return null;
            }
            int iDigit12 = IOUtils.digit1(cArr, i);
            int iDigit13 = IOUtils.digit1(cArr, i2);
            iDigit1 = IOUtils.digit1(cArr, i + 4);
            iDigit2 = iDigit13;
            iDigit22 = iDigit12;
        }
        return localTime(iDigit22, iDigit2, iDigit1);
    }

    public static LocalTime parseLocalTime6(byte[] bArr, int i) {
        int iDigit1;
        int iDigit12;
        int iDigit2;
        int i2 = i + 5;
        if (i2 > bArr.length) {
            return null;
        }
        byte b = bArr[i + 1];
        int i3 = i + 4;
        byte b2 = bArr[i3];
        int i4 = i + 2;
        if (bArr[i4] == 58 && b2 == 58) {
            iDigit1 = IOUtils.digit2(bArr, i);
            iDigit12 = IOUtils.digit1(bArr, i + 3);
            iDigit2 = IOUtils.digit1(bArr, i2);
        } else if (b == 58 && b2 == 58) {
            iDigit1 = IOUtils.digit1(bArr, i);
            iDigit12 = IOUtils.digit2(bArr, i4);
            iDigit2 = IOUtils.digit1(bArr, i2);
        } else {
            if (b != 58 || bArr[i + 3] != 58) {
                return null;
            }
            iDigit1 = IOUtils.digit1(bArr, i);
            iDigit12 = IOUtils.digit1(bArr, i4);
            iDigit2 = IOUtils.digit2(bArr, i3);
        }
        return localTime(iDigit1, iDigit12, iDigit2);
    }

    public static LocalTime parseLocalTime6(char[] cArr, int i) {
        int iDigit1;
        int iDigit12;
        int iDigit2;
        int i2 = i + 5;
        if (i2 > cArr.length) {
            return null;
        }
        char c = cArr[i + 1];
        int i3 = i + 4;
        char c2 = cArr[i3];
        int i4 = i + 2;
        if (cArr[i4] == ':' && c2 == ':') {
            iDigit1 = IOUtils.digit2(cArr, i);
            iDigit12 = IOUtils.digit1(cArr, i + 3);
            iDigit2 = IOUtils.digit1(cArr, i2);
        } else if (c == ':' && c2 == ':') {
            iDigit1 = IOUtils.digit1(cArr, i);
            iDigit12 = IOUtils.digit2(cArr, i4);
            iDigit2 = IOUtils.digit1(cArr, i2);
        } else {
            if (c != ':' || cArr[i + 3] != ':') {
                return null;
            }
            iDigit1 = IOUtils.digit1(cArr, i);
            iDigit12 = IOUtils.digit1(cArr, i4);
            iDigit2 = IOUtils.digit2(cArr, i3);
        }
        return localTime(iDigit1, iDigit12, iDigit2);
    }

    public static LocalTime parseLocalTime7(byte[] bArr, int i) {
        int iDigit2;
        int iDigit1;
        int iDigit12;
        int i2 = i + 5;
        if (i2 > bArr.length) {
            return null;
        }
        int i3 = i + 2;
        byte b = bArr[i3];
        byte b2 = bArr[i + 4];
        if (bArr[i + 1] == 58 && b2 == 58) {
            iDigit12 = IOUtils.digit1(bArr, i);
            iDigit2 = IOUtils.digit2(bArr, i3);
            iDigit1 = IOUtils.digit2(bArr, i2);
        } else if (b == 58 && b2 == 58) {
            int iDigit22 = IOUtils.digit2(bArr, i);
            int iDigit13 = IOUtils.digit1(bArr, i + 3);
            iDigit1 = IOUtils.digit2(bArr, i2);
            iDigit2 = iDigit13;
            iDigit12 = iDigit22;
        } else {
            if (b != 58 || bArr[i2] != 58) {
                return null;
            }
            int iDigit23 = IOUtils.digit2(bArr, i);
            iDigit2 = IOUtils.digit2(bArr, i + 3);
            iDigit1 = IOUtils.digit1(bArr, i + 6);
            iDigit12 = iDigit23;
        }
        return localTime(iDigit12, iDigit2, iDigit1);
    }

    public static LocalTime parseLocalTime7(char[] cArr, int i) {
        int iDigit2;
        int iDigit1;
        int iDigit12;
        int i2 = i + 5;
        if (i2 > cArr.length) {
            return null;
        }
        int i3 = i + 2;
        char c = cArr[i3];
        char c2 = cArr[i + 4];
        if (cArr[i + 1] == ':' && c2 == ':') {
            iDigit12 = IOUtils.digit1(cArr, i);
            iDigit2 = IOUtils.digit2(cArr, i3);
            iDigit1 = IOUtils.digit2(cArr, i2);
        } else if (c == ':' && c2 == ':') {
            int iDigit22 = IOUtils.digit2(cArr, i);
            int iDigit13 = IOUtils.digit1(cArr, i + 3);
            iDigit1 = IOUtils.digit2(cArr, i2);
            iDigit2 = iDigit13;
            iDigit12 = iDigit22;
        } else {
            if (c != ':' || cArr[i2] != ':') {
                return null;
            }
            int iDigit23 = IOUtils.digit2(cArr, i);
            iDigit2 = IOUtils.digit2(cArr, i + 3);
            iDigit1 = IOUtils.digit1(cArr, i + 6);
            iDigit12 = iDigit23;
        }
        return localTime(iDigit12, iDigit2, iDigit1);
    }

    public static LocalTime parseLocalTime8(byte[] bArr, int i) {
        if (i + 8 > bArr.length) {
            return null;
        }
        long jHms = hms(bArr, i);
        if (jHms == -1) {
            return null;
        }
        return LocalTime.of(((int) jHms) & 255, ((int) (jHms >> 24)) & 255, ((int) (jHms >> 48)) & 255);
    }

    public static LocalTime parseLocalTime8(char[] cArr, int i) {
        if (i + 8 <= cArr.length && cArr[i + 2] == ':' && cArr[i + 5] == ':') {
            return localTime(IOUtils.digit2(cArr, i), IOUtils.digit2(cArr, i + 3), IOUtils.digit2(cArr, i + 6));
        }
        return null;
    }

    public static LocalTime parseLocalTime(char c, char c2, char c3, char c4, char c5, char c6, char c7, char c8) {
        if (c3 == ':' && c6 == ':' && c >= '0' && c <= '9' && c2 >= '0' && c2 <= '9') {
            int i = ((c - '0') * 10) + (c2 - '0');
            if (c4 >= '0' && c4 <= '9' && c5 >= '0' && c5 <= '9') {
                int i2 = ((c4 - '0') * 10) + (c5 - '0');
                if (c7 >= '0' && c7 <= '9' && c8 >= '0' && c8 <= '9') {
                    return LocalTime.of(i, i2, ((c7 - '0') * 10) + (c8 - '0'));
                }
            }
        }
        return null;
    }

    public static LocalTime parseLocalTime10(byte[] bArr, int i) {
        if (i + 10 > bArr.length || bArr[i + 2] != 58 || bArr[i + 5] != 58 || bArr[i + 8] != 46) {
            return null;
        }
        int iDigit2 = IOUtils.digit2(bArr, i);
        int iDigit22 = IOUtils.digit2(bArr, i + 3);
        int iDigit23 = IOUtils.digit2(bArr, i + 6);
        int iDigit1 = IOUtils.digit1(bArr, i + 9);
        if (iDigit1 > 0) {
            iDigit1 *= 100000000;
        }
        if ((iDigit2 | iDigit22 | iDigit23 | iDigit22) < 0) {
            return null;
        }
        return LocalTime.of(iDigit2, iDigit22, iDigit23, iDigit1);
    }

    public static LocalTime parseLocalTime10(char[] cArr, int i) {
        if (i + 10 > cArr.length || cArr[i + 2] != ':' || cArr[i + 5] != ':' || cArr[i + 8] != '.') {
            return null;
        }
        int iDigit2 = IOUtils.digit2(cArr, i);
        int iDigit22 = IOUtils.digit2(cArr, i + 3);
        int iDigit23 = IOUtils.digit2(cArr, i + 6);
        int iDigit1 = IOUtils.digit1(cArr, i + 9);
        if (iDigit1 > 0) {
            iDigit1 *= 100000000;
        }
        if ((iDigit2 | iDigit22 | iDigit23 | iDigit22) < 0) {
            return null;
        }
        return LocalTime.of(iDigit2, iDigit22, iDigit23, iDigit1);
    }

    public static LocalTime parseLocalTime11(byte[] bArr, int i) {
        if (i + 11 > bArr.length) {
            return null;
        }
        long jHms = hms(bArr, i);
        if (jHms == -1 || bArr[i + 8] != 46) {
            return null;
        }
        int i2 = ((int) jHms) & 255;
        int i3 = ((int) (jHms >> 24)) & 255;
        int i4 = ((int) (jHms >> 48)) & 255;
        int iDigit2 = IOUtils.digit2(bArr, i + 9);
        if (iDigit2 > 0) {
            iDigit2 *= 10000000;
        }
        return LocalTime.of(i2, i3, i4, iDigit2);
    }

    public static LocalTime parseLocalTime11(char[] cArr, int i) {
        if (i + 11 > cArr.length || cArr[i + 2] != ':' || cArr[i + 5] != ':' || cArr[i + 8] != '.') {
            return null;
        }
        int iDigit2 = IOUtils.digit2(cArr, i);
        int iDigit22 = IOUtils.digit2(cArr, i + 3);
        int iDigit23 = IOUtils.digit2(cArr, i + 6);
        int iDigit24 = IOUtils.digit2(cArr, i + 9);
        if (iDigit24 > 0) {
            iDigit24 *= 10000000;
        }
        if ((iDigit2 | iDigit22 | iDigit23 | iDigit22) < 0) {
            return null;
        }
        return LocalTime.of(iDigit2, iDigit22, iDigit23, iDigit24);
    }

    public static LocalTime parseLocalTime12(byte[] bArr, int i) {
        if (i + 12 > bArr.length) {
            return null;
        }
        long jHms = hms(bArr, i);
        if (jHms == -1 || bArr[i + 8] != 46) {
            return null;
        }
        int i2 = ((int) jHms) & 255;
        int i3 = ((int) (jHms >> 24)) & 255;
        int i4 = ((int) (jHms >> 48)) & 255;
        int iDigit3 = IOUtils.digit3(bArr, i + 9);
        if (iDigit3 > 0) {
            iDigit3 *= DurationKt.NANOS_IN_MILLIS;
        }
        return LocalTime.of(i2, i3, i4, iDigit3);
    }

    public static LocalTime parseLocalTime12(char[] cArr, int i) {
        if (i + 12 > cArr.length || cArr[i + 2] != ':' || cArr[i + 5] != ':' || cArr[i + 8] != '.') {
            return null;
        }
        int iDigit2 = IOUtils.digit2(cArr, i);
        int iDigit22 = IOUtils.digit2(cArr, i + 3);
        int iDigit23 = IOUtils.digit2(cArr, i + 6);
        int iDigit3 = IOUtils.digit3(cArr, i + 9);
        if (iDigit3 > 0) {
            iDigit3 *= DurationKt.NANOS_IN_MILLIS;
        }
        if ((iDigit2 | iDigit22 | iDigit23 | iDigit22) < 0) {
            return null;
        }
        return LocalTime.of(iDigit2, iDigit22, iDigit23, iDigit3);
    }

    public static LocalTime parseLocalTime15(byte[] bArr, int i) {
        if (i + 15 <= bArr.length) {
            long jHms = hms(bArr, i);
            if (jHms != -1 && bArr[i + 8] == 46) {
                int i2 = ((int) jHms) & 255;
                int i3 = ((int) (jHms >> 24)) & 255;
                int i4 = ((int) (jHms >> 48)) & 255;
                int nanos = readNanos(bArr, 6, i + 9);
                if (nanos < 0) {
                    return null;
                }
                return LocalTime.of(i2, i3, i4, nanos);
            }
        }
        return null;
    }

    public static LocalTime parseLocalTime15(char[] cArr, int i) {
        if (i + 15 > cArr.length || cArr[i + 2] != ':' || cArr[i + 5] != ':' || cArr[i + 8] != '.') {
            return null;
        }
        int iDigit2 = IOUtils.digit2(cArr, i);
        int iDigit22 = IOUtils.digit2(cArr, i + 3);
        int iDigit23 = IOUtils.digit2(cArr, i + 6);
        int nanos = readNanos(cArr, 6, i + 9);
        if ((iDigit2 | iDigit22 | iDigit23 | nanos) < 0) {
            return null;
        }
        return LocalTime.of(iDigit2, iDigit22, iDigit23, nanos);
    }

    public static LocalTime parseLocalTime18(byte[] bArr, int i) {
        if (i + 18 <= bArr.length) {
            long jHms = hms(bArr, i);
            if (jHms != -1 && bArr[i + 8] == 46) {
                int i2 = ((int) jHms) & 255;
                int i3 = ((int) (jHms >> 24)) & 255;
                int i4 = ((int) (jHms >> 48)) & 255;
                int nanos = readNanos(bArr, 9, i + 9);
                if (nanos < 0) {
                    return null;
                }
                return LocalTime.of(i2, i3, i4, nanos);
            }
        }
        return null;
    }

    public static LocalTime parseLocalTime18(char[] cArr, int i) {
        if (i + 18 > cArr.length || cArr[i + 2] != ':' || cArr[i + 5] != ':' || cArr[i + 8] != '.') {
            return null;
        }
        int iDigit2 = IOUtils.digit2(cArr, i);
        int iDigit22 = IOUtils.digit2(cArr, i + 3);
        int iDigit23 = IOUtils.digit2(cArr, i + 6);
        int nanos = readNanos(cArr, 9, i + 9);
        if ((iDigit2 | iDigit22 | iDigit23 | nanos) < 0) {
            return null;
        }
        return LocalTime.of(iDigit2, iDigit22, iDigit23, nanos);
    }

    private static LocalTime localTime(int i, int i2, int i3) {
        if ((i | i2 | i3) < 0) {
            return null;
        }
        return LocalTime.of(i, i2, i3);
    }

    public static LocalDateTime parseLocalDateTime(byte[] bArr, int i, int i2) {
        if (bArr == null || i2 == 0) {
            return null;
        }
        switch (i2) {
            case 4:
                if (bArr[i] == 110 && bArr[i + 1] == 117 && bArr[i + 2] == 108 && bArr[i + 3] == 108) {
                    return null;
                }
                String str = new String(bArr, i, i2);
                throw new DateTimeParseException("illegal input ".concat(str), str, 0);
            case 5:
            case 6:
            case 7:
            case 13:
            case 15:
            default:
                return parseLocalDateTimeX(bArr, i, i2);
            case 8:
                LocalDate localDate8 = parseLocalDate8(bArr, i);
                if (localDate8 == null) {
                    return null;
                }
                return LocalDateTime.of(localDate8, LocalTime.MIN);
            case 9:
                LocalDate localDate9 = parseLocalDate9(bArr, i);
                if (localDate9 == null) {
                    return null;
                }
                return LocalDateTime.of(localDate9, LocalTime.MIN);
            case 10:
                LocalDate localDate10 = parseLocalDate10(bArr, i);
                if (localDate10 == null) {
                    return null;
                }
                return LocalDateTime.of(localDate10, LocalTime.MIN);
            case 11:
                return LocalDateTime.of(parseLocalDate11(bArr, i), LocalTime.MIN);
            case 12:
                return parseLocalDateTime12(bArr, i);
            case 14:
                return parseLocalDateTime14(bArr, i);
            case 16:
                return parseLocalDateTime16(bArr, i);
            case 17:
                return parseLocalDateTime17(bArr, i);
            case 18:
                return parseLocalDateTime18(bArr, i);
            case 19:
                return parseLocalDateTime19(bArr, i);
            case 20:
                return parseLocalDateTime20(bArr, i);
        }
    }

    public static LocalDate parseLocalDate(String str) {
        LocalDate localDate;
        if (str == null) {
            return null;
        }
        if (JDKUtils.STRING_CODER != null && JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER.applyAsInt(str) == 0) {
            byte[] bArrApply = JDKUtils.STRING_VALUE.apply(str);
            localDate = parseLocalDate(bArrApply, 0, bArrApply.length);
        } else {
            char[] charArray = JDKUtils.getCharArray(str);
            localDate = parseLocalDate(charArray, 0, charArray.length);
        }
        if (localDate != null) {
            return localDate;
        }
        str.hashCode();
        switch (str) {
            case "0000-0-00":
            case "00000000":
            case "":
            case "null":
            case "0000年00月00日":
            case "0000-00-00":
                return null;
            default:
                throw new DateTimeParseException(str, str, 0);
        }
    }

    public static LocalDate parseLocalDate(byte[] bArr, int i, int i2) {
        if (bArr == null || i2 == 0) {
            return null;
        }
        if (i + i2 > bArr.length) {
            String str = new String(bArr, i, i2);
            throw new DateTimeParseException("illegal input ".concat(str), str, 0);
        }
        switch (i2) {
            case 8:
                return parseLocalDate8(bArr, i);
            case 9:
                return parseLocalDate9(bArr, i);
            case 10:
                return parseLocalDate10(bArr, i);
            case 11:
                return parseLocalDate11(bArr, i);
            default:
                if (i2 == 4 && bArr[i] == 110 && bArr[i + 1] == 117 && bArr[i + 2] == 108 && bArr[i + 3] == 108) {
                    return null;
                }
                String str2 = new String(bArr, i, i2);
                throw new DateTimeParseException("illegal input ".concat(str2), str2, 0);
        }
    }

    public static LocalDate parseLocalDate(char[] cArr, int i, int i2) {
        if (cArr == null || i2 == 0) {
            return null;
        }
        if (i + i2 > cArr.length) {
            String str = new String(cArr, i, i2);
            throw new DateTimeParseException("illegal input ".concat(str), str, 0);
        }
        switch (i2) {
            case 8:
                return parseLocalDate8(cArr, i);
            case 9:
                return parseLocalDate9(cArr, i);
            case 10:
                return parseLocalDate10(cArr, i);
            case 11:
                return parseLocalDate11(cArr, i);
            default:
                if (i2 == 4 && cArr[i] == 'n' && cArr[i + 1] == 'u' && cArr[i + 2] == 'l' && cArr[i + 3] == 'l') {
                    return null;
                }
                String str2 = new String(cArr, i, i2);
                throw new DateTimeParseException("illegal input ".concat(str2), str2, 0);
        }
    }

    public static long parseMillis(byte[] bArr, int i, int i2) {
        return parseMillis(bArr, i, i2, StandardCharsets.UTF_8, DEFAULT_ZONE_ID);
    }

    public static long parseMillis(byte[] bArr, int i, int i2, Charset charset) {
        return parseMillis(bArr, i, i2, charset, DEFAULT_ZONE_ID);
    }

    public static long parseMillis(byte[] bArr, int i, int i2, Charset charset, ZoneId zoneId) {
        int i3;
        char c;
        ZoneId zoneId2 = zoneId;
        if (bArr == null || i2 == 0) {
            return 0L;
        }
        if (i2 == 4 && IOUtils.isNULL(bArr, i)) {
            return 0L;
        }
        char c2 = (char) bArr[i];
        if (c2 != '\"' || bArr[i2 - 1] != 34) {
            if (i2 == 19) {
                return parseMillis19(bArr, i, zoneId2);
            }
            if (i2 > 19 || (i2 == 16 && ((c = (char) bArr[i + 10]) == '+' || c == '-'))) {
                ZonedDateTime zonedDateTime = parseZonedDateTime(bArr, i, i2, zoneId2);
                if (zonedDateTime == null) {
                    String str = new String(bArr, i, i2 - i);
                    throw new DateTimeParseException("illegal input ".concat(str), str, 0);
                }
                return zonedDateTime.toInstant().toEpochMilli();
            }
            if ((c2 == '-' || (c2 >= '0' && c2 <= '9')) && IOUtils.isNumber(bArr, i, i2)) {
                long j = TypeUtils.parseLong(bArr, i, i2);
                if (i2 != 8 || j < 19700101 || j > 21000101) {
                    return j;
                }
                int i4 = (int) j;
                int i5 = i4 / AsyncHttpClient.DEFAULT_SOCKET_TIMEOUT;
                int i6 = (i4 % AsyncHttpClient.DEFAULT_SOCKET_TIMEOUT) / 100;
                int i7 = i4 % 100;
                if (i6 < 1 || i6 > 12) {
                    return j;
                }
                if (i6 != 2) {
                    i3 = (i6 == 4 || i6 == 6 || i6 == 9 || i6 == 11) ? 30 : 31;
                } else {
                    i3 = ((i5 & 3) != 0 || (i5 % 100 == 0 && i5 % HttpStatus.SC_BAD_REQUEST != 0)) ? 28 : 29;
                }
                return i7 <= i3 ? ZonedDateTime.ofLocal(LocalDateTime.of(i5, i6, i7, 0, 0, 0), zoneId2, null).toEpochSecond() * 1000 : j;
            }
            if (((char) bArr[i2 - 1]) == 'Z') {
                zoneId2 = ZoneOffset.UTC;
            }
            LocalDateTime localDateTime = parseLocalDateTime(bArr, i, i2);
            if (localDateTime == null && IOUtils.getLongLE(bArr, i) == 3256155501228994608L && IOUtils.getShortLE(bArr, i + 8) == 12336) {
                localDateTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
            }
            long epochSecond = ZonedDateTime.ofLocal(localDateTime, zoneId2, null).toEpochSecond();
            int nano = localDateTime.getNano();
            if (epochSecond < 0 && nano > 0) {
                return (((epochSecond + 1) * 1000) + ((long) (nano / DurationKt.NANOS_IN_MILLIS))) - 1000;
            }
            return (epochSecond * 1000) + ((long) (nano / DurationKt.NANOS_IN_MILLIS));
        }
        JSONReader jSONReaderOf = JSONReader.of(bArr, i, i2, charset);
        try {
            long time = ((Date) ObjectReaderImplDate.INSTANCE.readObject(jSONReaderOf, null, null, 0L)).getTime();
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return time;
        } finally {
        }
    }

    public static long parseMillis(char[] cArr, int i, int i2) {
        return parseMillis(cArr, i, i2, DEFAULT_ZONE_ID);
    }

    public static long parseMillis(char[] cArr, int i, int i2, ZoneId zoneId) {
        int i3;
        char c;
        int i4 = i2;
        ZoneId zoneId2 = zoneId;
        if (cArr == null || i4 == 0) {
            return 0L;
        }
        if (i4 == 4 && IOUtils.isNULL(cArr, i)) {
            return 0L;
        }
        char c2 = cArr[i];
        if (c2 != '\"' || cArr[i4 - 1] != '\"') {
            if (i4 == 19) {
                return parseMillis19(cArr, i, zoneId2);
            }
            if (i4 > 19 || (i4 == 16 && ((c = cArr[i + 10]) == '+' || c == '-'))) {
                ZonedDateTime zonedDateTime = parseZonedDateTime(cArr, i, i2, zoneId);
                if (zonedDateTime == null) {
                    String str = new String(cArr, i, i4 - i);
                    throw new DateTimeParseException("illegal input ".concat(str), str, 0);
                }
                return zonedDateTime.toInstant().toEpochMilli();
            }
            if ((c2 == '-' || (c2 >= '0' && c2 <= '9')) && IOUtils.isNumber(cArr, i, i2)) {
                long j = TypeUtils.parseLong(cArr, i, i2);
                if (i4 != 8 || j < 19700101 || j > 21000101) {
                    return j;
                }
                int i5 = (int) j;
                int i6 = i5 / AsyncHttpClient.DEFAULT_SOCKET_TIMEOUT;
                int i7 = (i5 % AsyncHttpClient.DEFAULT_SOCKET_TIMEOUT) / 100;
                int i8 = i5 % 100;
                if (i7 < 1 || i7 > 12) {
                    return j;
                }
                if (i7 != 2) {
                    i3 = (i7 == 4 || i7 == 6 || i7 == 9 || i7 == 11) ? 30 : 31;
                } else {
                    i3 = ((i6 & 3) != 0 || (i6 % 100 == 0 && i6 % HttpStatus.SC_BAD_REQUEST != 0)) ? 28 : 29;
                }
                return i8 <= i3 ? ZonedDateTime.ofLocal(LocalDateTime.of(i6, i7, i8, 0, 0, 0), zoneId2, null).toEpochSecond() * 1000 : j;
            }
            if (cArr[i4 - 1] == 'Z') {
                i4--;
                zoneId2 = ZoneOffset.UTC;
            }
            LocalDateTime localDateTime = parseLocalDateTime(cArr, i, i4);
            if (localDateTime == null && IOUtils.getLongLE(cArr, i) == 13511005043687472L && IOUtils.getLongLE(cArr, i + 4) == 12666580113555501L && IOUtils.getIntLE(cArr, i + 8) == 3145776) {
                localDateTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
            }
            if (localDateTime == null) {
                String str2 = new String(cArr, i, i4 - i);
                throw new DateTimeParseException("illegal input ".concat(str2), str2, 0);
            }
            long epochSecond = ZonedDateTime.ofLocal(localDateTime, zoneId2, null).toEpochSecond();
            int nano = localDateTime.getNano();
            if (epochSecond < 0 && nano > 0) {
                return (((epochSecond + 1) * 1000) + ((long) (nano / DurationKt.NANOS_IN_MILLIS))) - 1000;
            }
            return (epochSecond * 1000) + ((long) (nano / DurationKt.NANOS_IN_MILLIS));
        }
        JSONReader jSONReaderOf = JSONReader.of(cArr, i, i2);
        try {
            long time = ((Date) ObjectReaderImplDate.INSTANCE.readObject(jSONReaderOf, null, null, 0L)).getTime();
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return time;
        } finally {
        }
    }

    public static LocalDate parseLocalDate8(byte[] bArr, int i) {
        int iDigit4;
        int iDigit2;
        int iDigit22;
        if (i + 8 > bArr.length) {
            return null;
        }
        char c = (char) bArr[i + 1];
        char c2 = (char) bArr[i + 3];
        int i2 = i + 4;
        char c3 = (char) bArr[i2];
        if (c3 == '-' && bArr[i + 6] == 45) {
            iDigit4 = IOUtils.digit4(bArr, i);
            iDigit2 = IOUtils.digit1(bArr, i + 5);
            iDigit22 = IOUtils.digit1(bArr, i + 7);
        } else if (c == '/' && c2 == '/') {
            iDigit2 = IOUtils.digit1(bArr, i);
            int iDigit1 = IOUtils.digit1(bArr, i + 2);
            iDigit4 = IOUtils.digit4(bArr, i2);
            iDigit22 = iDigit1;
        } else if (c == '-' && bArr[i + 5] == 45) {
            int iDigit12 = IOUtils.digit1(bArr, i);
            iDigit2 = month((char) bArr[i + 2], c2, c3);
            int iDigit23 = IOUtils.digit2(bArr, i + 6);
            if (iDigit23 != -1) {
                iDigit23 += AudioPlayer.HEADER_SAMPLE_RATE;
            }
            iDigit4 = iDigit23;
            iDigit22 = iDigit12;
        } else {
            iDigit4 = IOUtils.digit4(bArr, i);
            iDigit2 = IOUtils.digit2(bArr, i2);
            iDigit22 = IOUtils.digit2(bArr, i + 6);
        }
        if ((iDigit4 | iDigit2 | iDigit22) <= 0) {
            return null;
        }
        return LocalDate.of(iDigit4, iDigit2, iDigit22);
    }

    public static LocalDate parseLocalDate8(char[] cArr, int i) {
        int iDigit4;
        int iDigit2;
        int iDigit22;
        if (i + 8 > cArr.length) {
            return null;
        }
        char c = cArr[i + 1];
        char c2 = cArr[i + 3];
        int i2 = i + 4;
        char c3 = cArr[i2];
        if (c3 == '-' && cArr[i + 6] == '-') {
            iDigit4 = IOUtils.digit4(cArr, i);
            iDigit2 = IOUtils.digit1(cArr, i + 5);
            iDigit22 = IOUtils.digit1(cArr, i + 7);
        } else if (c == '/' && c2 == '/') {
            iDigit2 = IOUtils.digit1(cArr, i);
            int iDigit1 = IOUtils.digit1(cArr, i + 2);
            iDigit4 = IOUtils.digit4(cArr, i2);
            iDigit22 = iDigit1;
        } else if (c == '-' && cArr[i + 5] == '-') {
            int iDigit12 = IOUtils.digit1(cArr, i);
            iDigit2 = month(cArr[i + 2], c2, c3);
            int iDigit23 = IOUtils.digit2(cArr, i + 6);
            if (iDigit23 != -1) {
                iDigit23 += AudioPlayer.HEADER_SAMPLE_RATE;
            }
            iDigit4 = iDigit23;
            iDigit22 = iDigit12;
        } else {
            iDigit4 = IOUtils.digit4(cArr, i);
            iDigit2 = IOUtils.digit2(cArr, i2);
            iDigit22 = IOUtils.digit2(cArr, i + 6);
        }
        if ((iDigit4 | iDigit2 | iDigit22) <= 0) {
            return null;
        }
        return LocalDate.of(iDigit4, iDigit2, iDigit22);
    }

    public static LocalDate parseLocalDate9(byte[] bArr, int i) {
        int iDigit2;
        int iDigit1;
        int iDigit4;
        int iDigit42;
        int iDigit22;
        if (i + 9 > bArr.length) {
            return null;
        }
        char c = (char) bArr[i + 1];
        int i2 = i + 2;
        char c2 = (char) bArr[i2];
        char c3 = (char) bArr[i + 4];
        char c4 = (char) bArr[i + 6];
        int i3 = i + 7;
        char c5 = (char) bArr[i3];
        if ((c3 == '-' && c5 == '-') || (c3 == '/' && c5 == '/')) {
            iDigit42 = IOUtils.digit4(bArr, i);
            iDigit2 = IOUtils.digit2(bArr, i + 5);
            iDigit22 = IOUtils.digit1(bArr, i + 8);
        } else if ((c3 == '-' && c4 == '-') || (c3 == '/' && c4 == '/')) {
            iDigit42 = IOUtils.digit4(bArr, i);
            iDigit2 = IOUtils.digit1(bArr, i + 5);
            iDigit22 = IOUtils.digit2(bArr, i3);
        } else {
            if (c == '.' && c3 == '.') {
                iDigit1 = IOUtils.digit1(bArr, i);
                iDigit2 = IOUtils.digit2(bArr, i2);
                iDigit4 = IOUtils.digit4(bArr, i + 5);
            } else if ((c2 == '.' && c3 == '.') || (c2 == '-' && c3 == '-')) {
                iDigit1 = IOUtils.digit2(bArr, i);
                iDigit2 = IOUtils.digit1(bArr, i + 3);
                iDigit4 = IOUtils.digit4(bArr, i + 5);
            } else if (c == '-' && c3 == '-') {
                iDigit1 = IOUtils.digit1(bArr, i);
                iDigit2 = IOUtils.digit2(bArr, i2);
                iDigit4 = IOUtils.digit4(bArr, i + 5);
            } else if (c2 == '-' && c4 == '-') {
                iDigit1 = IOUtils.digit2(bArr, i);
                iDigit2 = month((char) bArr[i + 3], c3, (char) bArr[i + 5]);
                iDigit4 = IOUtils.digit2(bArr, i3);
                if (iDigit4 != -1) {
                    iDigit4 += AudioPlayer.HEADER_SAMPLE_RATE;
                }
            } else if (c == '/' && c3 == '/') {
                int iDigit12 = IOUtils.digit1(bArr, i);
                int iDigit23 = IOUtils.digit2(bArr, i2);
                iDigit42 = IOUtils.digit4(bArr, i + 5);
                iDigit22 = iDigit23;
                iDigit2 = iDigit12;
            } else {
                if (c2 != '/' || c3 != '/') {
                    return null;
                }
                iDigit2 = IOUtils.digit2(bArr, i);
                iDigit1 = IOUtils.digit1(bArr, i + 3);
                iDigit4 = IOUtils.digit4(bArr, i + 5);
            }
            int i4 = iDigit1;
            iDigit42 = iDigit4;
            iDigit22 = i4;
        }
        if ((iDigit42 | iDigit2 | iDigit22) <= 0) {
            return null;
        }
        return LocalDate.of(iDigit42, iDigit2, iDigit22);
    }

    public static LocalDate parseLocalDate9(char[] cArr, int i) {
        int iDigit2;
        int iDigit1;
        int iDigit4;
        int iMonth;
        int iDigit42;
        int iDigit12;
        int iDigit13;
        int iDigit22;
        int iDigit43;
        if (i + 9 > cArr.length) {
            return null;
        }
        char c = cArr[i + 1];
        int i2 = i + 2;
        char c2 = cArr[i2];
        char c3 = cArr[i + 4];
        char c4 = cArr[i + 6];
        int i3 = i + 7;
        char c5 = cArr[i3];
        int i4 = i + 8;
        char c6 = cArr[i4];
        if ((c3 == '-' && c5 == '-') || (c3 == '/' && c5 == '/')) {
            iDigit42 = IOUtils.digit4(cArr, i);
            iMonth = IOUtils.digit2(cArr, i + 5);
            iDigit12 = IOUtils.digit1(cArr, i4);
        } else if ((c3 == '-' && c4 == '-') || (c3 == '/' && c4 == '/')) {
            iDigit42 = IOUtils.digit4(cArr, i);
            iMonth = IOUtils.digit1(cArr, i + 5);
            iDigit12 = IOUtils.digit2(cArr, i3);
        } else if ((c3 == 24180 && c4 == 26376 && c6 == 26085) || (c3 == 45380 && c4 == 50900 && c6 == 51068)) {
            iDigit42 = IOUtils.digit4(cArr, i);
            iMonth = IOUtils.digit1(cArr, i + 5);
            iDigit12 = IOUtils.digit1(cArr, i3);
        } else {
            if (c == '.' && c3 == '.') {
                iDigit13 = IOUtils.digit1(cArr, i);
                iDigit22 = IOUtils.digit2(cArr, i2);
                iDigit43 = IOUtils.digit4(cArr, i + 5);
            } else if ((c2 == '.' && c3 == '.') || (c2 == '-' && c3 == '-')) {
                iDigit13 = IOUtils.digit2(cArr, i);
                iDigit22 = IOUtils.digit1(cArr, i + 3);
                iDigit43 = IOUtils.digit4(cArr, i + 5);
            } else if (c == '-' && c3 == '-') {
                iDigit13 = IOUtils.digit1(cArr, i);
                iDigit22 = IOUtils.digit2(cArr, i2);
                iDigit43 = IOUtils.digit4(cArr, i + 5);
            } else if (c2 == '-' && c4 == '-') {
                int iDigit23 = IOUtils.digit2(cArr, i);
                iMonth = month(cArr[i + 3], c3, cArr[i + 5]);
                int iDigit24 = IOUtils.digit2(cArr, i3);
                if (iDigit24 != -1) {
                    iDigit24 += AudioPlayer.HEADER_SAMPLE_RATE;
                }
                iDigit42 = iDigit24;
                iDigit12 = iDigit23;
            } else {
                if (c == '/' && c3 == '/') {
                    iDigit2 = IOUtils.digit1(cArr, i);
                    iDigit1 = IOUtils.digit2(cArr, i2);
                    iDigit4 = IOUtils.digit4(cArr, i + 5);
                } else {
                    if (c2 != '/' || c3 != '/') {
                        return null;
                    }
                    iDigit2 = IOUtils.digit2(cArr, i);
                    iDigit1 = IOUtils.digit1(cArr, i + 3);
                    iDigit4 = IOUtils.digit4(cArr, i + 5);
                }
                iMonth = iDigit2;
                iDigit42 = iDigit4;
                iDigit12 = iDigit1;
            }
            int i5 = iDigit13;
            iDigit42 = iDigit43;
            iDigit12 = i5;
            iMonth = iDigit22;
        }
        if ((iDigit42 | iMonth | iDigit12) <= 0) {
            return null;
        }
        return LocalDate.of(iDigit42, iMonth, iDigit12);
    }

    public static LocalDate parseLocalDate10(byte[] bArr, int i) {
        int iMonth;
        int iDigit4;
        int iDigit2;
        int iDigit22;
        int iDigit42;
        if (i + 10 > bArr.length) {
            return null;
        }
        char c = (char) bArr[i + 2];
        char c2 = (char) bArr[i + 4];
        int i2 = i + 5;
        char c3 = (char) bArr[i2];
        char c4 = (char) bArr[i + 7];
        if ((c2 == '-' && c4 == '-') || (c2 == '/' && c4 == '/')) {
            iDigit4 = IOUtils.digit4(bArr, i);
            iMonth = IOUtils.digit2(bArr, i2);
            iDigit2 = IOUtils.digit2(bArr, i + 8);
        } else {
            if ((c == '.' && c3 == '.') || (c == '-' && c3 == '-')) {
                iDigit22 = IOUtils.digit2(bArr, i);
                iMonth = IOUtils.digit2(bArr, i + 3);
                iDigit42 = IOUtils.digit4(bArr, i + 6);
            } else if (c == '/' && c3 == '/') {
                iMonth = IOUtils.digit2(bArr, i);
                iDigit22 = IOUtils.digit2(bArr, i + 3);
                iDigit42 = IOUtils.digit4(bArr, i + 6);
            } else {
                if (bArr[i + 1] != 32 || c3 != ' ') {
                    return null;
                }
                int iDigit1 = IOUtils.digit1(bArr, i);
                iMonth = month(c, (char) bArr[i + 3], c2);
                iDigit4 = IOUtils.digit4(bArr, i + 6);
                iDigit2 = iDigit1;
            }
            int i3 = iDigit22;
            iDigit4 = iDigit42;
            iDigit2 = i3;
        }
        if ((iDigit4 | iMonth | iDigit2) <= 0) {
            return null;
        }
        return LocalDate.of(iDigit4, iMonth, iDigit2);
    }

    /* JADX WARN: Removed duplicated region for block: B:48:0x00df A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00e0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.time.LocalDate parseLocalDate10(char[] r18, int r19) {
        /*
            Method dump skipped, instruction units count: 230
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.DateUtils.parseLocalDate10(char[], int):java.time.LocalDate");
    }

    public static LocalDate parseLocalDate11(char[] cArr, int i) {
        int iDigit2;
        int iDigit22;
        int iDigit4;
        if (i + 11 > cArr.length) {
            return null;
        }
        char c = cArr[i + 4];
        int i2 = i + 7;
        char c2 = cArr[i2];
        char c3 = cArr[i + 10];
        if ((c == 24180 && c2 == 26376 && c3 == 26085) || ((c == '-' && c2 == '-' && c3 == 'Z') || (c == 45380 && c2 == 50900 && c3 == 51068))) {
            iDigit4 = IOUtils.digit4(cArr, i);
            iDigit22 = IOUtils.digit2(cArr, i + 5);
            iDigit2 = IOUtils.digit2(cArr, i + 8);
        } else {
            if (cArr[i + 2] == ' ' && cArr[i + 6] == ' ') {
                int iDigit42 = IOUtils.digit4(cArr, i2);
                int iMonth = month(cArr[i + 3], c, cArr[i + 5]);
                iDigit2 = IOUtils.digit2(cArr, i);
                iDigit22 = iMonth;
                iDigit4 = iDigit42;
            }
            return null;
        }
        if ((iDigit4 | iDigit22 | iDigit2) >= 0 && (iDigit4 != 0 || iDigit22 != 0 || iDigit2 != 0)) {
            return LocalDate.of(iDigit4, iDigit22, iDigit2);
        }
        return null;
    }

    public static LocalDate parseLocalDate11(byte[] bArr, int i) {
        int iDigit2;
        int iDigit22;
        int iDigit4;
        if (i + 11 > bArr.length) {
            return null;
        }
        int i2 = i + 4;
        if (bArr[i2] == 45 && bArr[i + 7] == 45 && bArr[i + 10] == 90) {
            iDigit4 = IOUtils.digit4(bArr, i);
            iDigit22 = IOUtils.digit2(bArr, i + 5);
            iDigit2 = IOUtils.digit2(bArr, i + 8);
        } else {
            if (bArr[i + 2] == 32 && bArr[i + 6] == 32) {
                int iDigit42 = IOUtils.digit4(bArr, i + 7);
                int iMonth = month((char) bArr[i + 3], (char) bArr[i2], (char) bArr[i + 5]);
                iDigit2 = IOUtils.digit2(bArr, i);
                iDigit22 = iMonth;
                iDigit4 = iDigit42;
            }
            return null;
        }
        if ((iDigit4 | iDigit22 | iDigit2) >= 0 && (iDigit4 != 0 || iDigit22 != 0 || iDigit2 != 0)) {
            return LocalDate.of(iDigit4, iDigit22, iDigit2);
        }
        return null;
    }

    public static LocalDateTime parseLocalDateTime12(char[] cArr, int i) {
        int i2 = i + 12;
        if (i2 > cArr.length) {
            String str = new String(cArr, i, cArr.length - i);
            throw new DateTimeParseException("illegal input ".concat(str), str, 0);
        }
        int iDigit4 = IOUtils.digit4(cArr, i);
        int iDigit2 = IOUtils.digit2(cArr, i + 4);
        int iDigit22 = IOUtils.digit2(cArr, i + 6);
        int iDigit23 = IOUtils.digit2(cArr, i + 8);
        int iDigit24 = IOUtils.digit2(cArr, i + 10);
        if ((iDigit4 | iDigit2 | iDigit22 | iDigit23 | iDigit24) < 0) {
            String str2 = new String(cArr, i, i2);
            throw new DateTimeParseException("illegal input ".concat(str2), str2, 0);
        }
        if (iDigit4 == 0 && iDigit2 == 0 && iDigit22 == 0 && iDigit23 == 0 && iDigit24 == 0) {
            return null;
        }
        return LocalDateTime.of(iDigit4, iDigit2, iDigit22, iDigit23, iDigit24, 0);
    }

    public static LocalDateTime parseLocalDateTime12(byte[] bArr, int i) {
        int i2 = i + 12;
        if (i2 > bArr.length) {
            String str = new String(bArr, i, bArr.length - i);
            throw new DateTimeParseException("illegal input ".concat(str), str, 0);
        }
        int iDigit4 = IOUtils.digit4(bArr, i);
        int iDigit2 = IOUtils.digit2(bArr, i + 4);
        int iDigit22 = IOUtils.digit2(bArr, i + 6);
        int iDigit23 = IOUtils.digit2(bArr, i + 8);
        int iDigit24 = IOUtils.digit2(bArr, i + 10);
        if ((iDigit4 | iDigit2 | iDigit22 | iDigit23 | iDigit24) < 0) {
            String str2 = new String(bArr, i, i2);
            throw new DateTimeParseException("illegal input ".concat(str2), str2, 0);
        }
        if (iDigit4 == 0 && iDigit2 == 0 && iDigit22 == 0 && iDigit23 == 0 && iDigit24 == 0) {
            return null;
        }
        return LocalDateTime.of(iDigit4, iDigit2, iDigit22, iDigit23, iDigit24, 0);
    }

    public static LocalDateTime parseLocalDateTime14(char[] cArr, int i) {
        if (i + 14 > cArr.length) {
            return null;
        }
        int iDigit4 = IOUtils.digit4(cArr, i);
        int iDigit2 = IOUtils.digit2(cArr, i + 4);
        int iDigit22 = IOUtils.digit2(cArr, i + 6);
        int iDigit23 = IOUtils.digit2(cArr, i + 8);
        int iDigit24 = IOUtils.digit2(cArr, i + 10);
        int iDigit25 = IOUtils.digit2(cArr, i + 12);
        if ((iDigit4 | iDigit2 | iDigit22 | iDigit23 | iDigit24 | iDigit25) < 0) {
            return null;
        }
        return LocalDateTime.of(iDigit4, iDigit2, iDigit22, iDigit23, iDigit24, iDigit25);
    }

    public static LocalDateTime parseLocalDateTime14(byte[] bArr, int i) {
        if (i + 14 > bArr.length) {
            return null;
        }
        int iDigit4 = IOUtils.digit4(bArr, i);
        int iDigit2 = IOUtils.digit2(bArr, i + 4);
        int iDigit22 = IOUtils.digit2(bArr, i + 6);
        int iDigit23 = IOUtils.digit2(bArr, i + 8);
        int iDigit24 = IOUtils.digit2(bArr, i + 10);
        int iDigit25 = IOUtils.digit2(bArr, i + 12);
        if ((iDigit4 | iDigit2 | iDigit22 | iDigit23 | iDigit24 | iDigit25) < 0) {
            return null;
        }
        return LocalDateTime.of(iDigit4, iDigit2, iDigit22, iDigit23, iDigit24, iDigit25);
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00d5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0101 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0133 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0134  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.time.LocalDateTime parseLocalDateTime16(char[] r20, int r21) {
        /*
            Method dump skipped, instruction units count: 314
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.DateUtils.parseLocalDateTime16(char[], int):java.time.LocalDateTime");
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x0107  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0130  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x01a6  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x01b0  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x01dd A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x01de  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.time.LocalDateTime parseLocalDateTime16(byte[] r31, int r32) {
        /*
            Method dump skipped, instruction units count: 484
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.DateUtils.parseLocalDateTime16(byte[], int):java.time.LocalDateTime");
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00dd A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x010c  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0147  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0157 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0185  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x01b3 A[PHI: r3
      0x01b3: PHI (r3v15 char) = (r3v14 char), (r3v14 char), (r3v14 char), (r3v20 char) binds: [B:60:0x018b, B:61:0x018d, B:62:0x018f, B:67:0x01b1] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x01b5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0212 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0214  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.time.LocalDateTime parseLocalDateTime17(char[] r31, int r32) {
        /*
            Method dump skipped, instruction units count: 560
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.DateUtils.parseLocalDateTime17(char[], int):java.time.LocalDateTime");
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x00e5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x010b  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0149  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x018b  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0252 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0254  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.time.LocalDateTime parseLocalDateTime17(byte[] r32, int r33) {
        /*
            Method dump skipped, instruction units count: 624
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.DateUtils.parseLocalDateTime17(byte[], int):java.time.LocalDateTime");
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x0238 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0260 A[PHI: r12
      0x0260: PHI (r12v5 char) = (r12v4 char), (r12v4 char), (r12v4 char), (r12v8 char) binds: [B:101:0x0236, B:102:0x0238, B:103:0x023a, B:108:0x025e] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0262 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x015e  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0166  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0199  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x01a7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x01cd A[PHI: r12
      0x01cd: PHI (r12v2 char) = (r12v1 char), (r12v1 char), (r12v1 char), (r12v12 char) binds: [B:74:0x01a5, B:75:0x01a7, B:76:0x01a9, B:81:0x01cb] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01cf A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x01f7 A[PHI: r12
      0x01f7: PHI (r12v3 char) = (r12v2 char), (r12v2 char), (r12v2 char), (r12v10 char) binds: [B:82:0x01cd, B:83:0x01cf, B:84:0x01d1, B:89:0x01f5] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:91:0x01f9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0230  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.time.LocalDateTime parseLocalDateTime18(char[] r29, int r30) {
        /*
            Method dump skipped, instruction units count: 704
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.DateUtils.parseLocalDateTime18(char[], int):java.time.LocalDateTime");
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x0238 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0260 A[PHI: r12
      0x0260: PHI (r12v5 byte) = (r12v4 byte), (r12v4 byte), (r12v4 byte), (r12v8 byte) binds: [B:101:0x0236, B:102:0x0238, B:103:0x023a, B:108:0x025e] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0262 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x015e  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0166  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0199  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x01a7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x01cd A[PHI: r12
      0x01cd: PHI (r12v2 byte) = (r12v1 byte), (r12v1 byte), (r12v1 byte), (r12v12 byte) binds: [B:74:0x01a5, B:75:0x01a7, B:76:0x01a9, B:81:0x01cb] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01cf A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x01f7 A[PHI: r12
      0x01f7: PHI (r12v3 byte) = (r12v2 byte), (r12v2 byte), (r12v2 byte), (r12v10 byte) binds: [B:82:0x01cd, B:83:0x01cf, B:84:0x01d1, B:89:0x01f5] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:91:0x01f9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0230  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.time.LocalDateTime parseLocalDateTime18(byte[] r29, int r30) {
        /*
            Method dump skipped, instruction units count: 704
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.DateUtils.parseLocalDateTime18(byte[], int):java.time.LocalDateTime");
    }

    public static LocalDateTime parseLocalDateTime19(char[] cArr, int i) {
        int iDigit2;
        int iDigit22;
        int iDigit1;
        int iMonth;
        int iDigit4;
        int iDigit23;
        if (i + 19 > cArr.length) {
            return null;
        }
        char c = cArr[i + 1];
        char c2 = cArr[i + 2];
        int i2 = i + 3;
        char c3 = cArr[i2];
        char c4 = cArr[i + 4];
        int i3 = i + 5;
        char c5 = cArr[i3];
        char c6 = cArr[i + 7];
        char c7 = cArr[i + 10];
        char c8 = cArr[i + 13];
        char c9 = cArr[i + 16];
        if (((c4 == '-' && c6 == '-') || (c4 == '/' && c6 == '/')) && ((c7 == ' ' || c7 == 'T') && c8 == ':' && c9 == ':')) {
            iDigit4 = IOUtils.digit4(cArr, i);
            iMonth = IOUtils.digit2(cArr, i3);
            iDigit1 = IOUtils.digit2(cArr, i + 8);
            iDigit22 = IOUtils.digit2(cArr, i + 11);
            iDigit2 = IOUtils.digit2(cArr, i + 14);
            iDigit23 = IOUtils.digit2(cArr, i + 17);
        } else if (c2 == '/' && c5 == '/' && ((c7 == ' ' || c7 == 'T') && c8 == ':' && c9 == ':')) {
            iDigit1 = IOUtils.digit2(cArr, i);
            iMonth = IOUtils.digit2(cArr, i2);
            iDigit4 = IOUtils.digit4(cArr, i + 6);
            iDigit22 = IOUtils.digit2(cArr, i + 11);
            iDigit2 = IOUtils.digit2(cArr, i + 14);
            iDigit23 = IOUtils.digit2(cArr, i + 17);
        } else {
            if (c != ' ' || c5 != ' ' || c7 != ' ' || c8 != ':' || c9 != ':') {
                return null;
            }
            iDigit1 = IOUtils.digit1(cArr, i);
            iMonth = month(c2, c3, c4);
            iDigit4 = IOUtils.digit4(cArr, i + 6);
            iDigit22 = IOUtils.digit2(cArr, i + 11);
            iDigit2 = IOUtils.digit2(cArr, i + 14);
            iDigit23 = IOUtils.digit2(cArr, i + 17);
        }
        int i4 = iDigit23;
        int i5 = iDigit4;
        int i6 = iMonth;
        int i7 = iDigit1;
        int i8 = iDigit22;
        int i9 = iDigit2;
        if ((i5 | i6 | i7 | i8 | i9 | i4) <= 0) {
            return null;
        }
        return LocalDateTime.of(i5, i6, i7, i8, i9, i4);
    }

    public static LocalDateTime parseLocalDateTime19(String str, int i) {
        int i2 = i + 19;
        if (i2 > str.length()) {
            return null;
        }
        if (JDKUtils.STRING_CODER != null && JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER.applyAsInt(str) == 0) {
            return parseLocalDateTime19(JDKUtils.STRING_VALUE.apply(str), i);
        }
        if (JDKUtils.JVM_VERSION == 8 && !JDKUtils.FIELD_STRING_VALUE_ERROR) {
            return parseLocalDateTime19(JDKUtils.getCharArray(str), i);
        }
        char[] cArr = new char[19];
        str.getChars(i, i2, cArr, 0);
        return parseLocalDateTime19(cArr, i);
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0089 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x008a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.time.LocalDateTime parseLocalDateTime19(byte[] r14, int r15) {
        /*
            int r0 = r15 + 19
            int r1 = r14.length
            r2 = 0
            if (r0 <= r1) goto L7
            return r2
        L7:
            int r0 = r15 + 1
            r0 = r14[r0]
            int r1 = r15 + 2
            r1 = r14[r1]
            int r3 = r15 + 3
            r4 = r14[r3]
            int r5 = r15 + 4
            r5 = r14[r5]
            int r6 = r15 + 5
            r7 = r14[r6]
            int r8 = r15 + 7
            r8 = r14[r8]
            int r9 = r15 + 10
            r9 = r14[r9]
            r10 = 84
            r11 = 45
            r12 = 47
            r13 = 32
            if (r5 != r11) goto L2f
            if (r8 == r11) goto L33
        L2f:
            if (r5 != r12) goto L49
            if (r8 != r12) goto L49
        L33:
            if (r9 == r13) goto L37
            if (r9 != r10) goto L49
        L37:
            int r0 = com.alibaba.fastjson2.util.IOUtils.digit4(r14, r15)
            int r1 = com.alibaba.fastjson2.util.IOUtils.digit2(r14, r6)
            int r3 = r15 + 8
            int r3 = com.alibaba.fastjson2.util.IOUtils.digit2(r14, r3)
        L45:
            r4 = r0
            r5 = r1
            r6 = r3
            goto L78
        L49:
            if (r1 != r12) goto L63
            if (r7 != r12) goto L63
            if (r9 == r13) goto L51
            if (r9 != r10) goto L63
        L51:
            int r0 = com.alibaba.fastjson2.util.IOUtils.digit2(r14, r15)
            int r1 = com.alibaba.fastjson2.util.IOUtils.digit2(r14, r3)
            int r3 = r15 + 6
            int r3 = com.alibaba.fastjson2.util.IOUtils.digit4(r14, r3)
            r6 = r0
            r5 = r1
            r4 = r3
            goto L78
        L63:
            if (r0 != r13) goto L9f
            if (r7 != r13) goto L9f
            if (r9 != r13) goto L9f
            int r3 = com.alibaba.fastjson2.util.IOUtils.digit1(r14, r15)
            int r1 = month(r1, r4, r5)
            int r0 = r15 + 6
            int r0 = com.alibaba.fastjson2.util.IOUtils.digit4(r14, r0)
            goto L45
        L78:
            int r15 = r15 + 11
            long r14 = hms(r14, r15)
            r0 = r4 | r5
            r0 = r0 | r6
            long r0 = (long) r0
            long r0 = r0 | r14
            r7 = 0
            int r0 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1))
            if (r0 > 0) goto L8a
            return r2
        L8a:
            int r0 = (int) r14
            r7 = r0 & 255(0xff, float:3.57E-43)
            r0 = 24
            long r0 = r14 >> r0
            int r0 = (int) r0
            r8 = r0 & 255(0xff, float:3.57E-43)
            r0 = 48
            long r14 = r14 >> r0
            int r14 = (int) r14
            r9 = r14 & 255(0xff, float:3.57E-43)
            java.time.LocalDateTime r14 = java.time.LocalDateTime.of(r4, r5, r6, r7, r8, r9)
            return r14
        L9f:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.DateUtils.parseLocalDateTime19(byte[], int):java.time.LocalDateTime");
    }

    public static LocalDateTime parseLocalDateTime20(char[] cArr, int i) {
        if (i + 19 <= cArr.length && cArr[i + 2] == ' ' && cArr[i + 6] == ' ' && cArr[i + 11] == ' ' && cArr[i + 14] == ':' && cArr[i + 17] == ':') {
            int iDigit2 = IOUtils.digit2(cArr, i);
            int iMonth = month(cArr[i + 3], cArr[i + 4], cArr[i + 5]);
            int iDigit4 = IOUtils.digit4(cArr, i + 7);
            int iDigit22 = IOUtils.digit2(cArr, i + 12);
            int iDigit23 = IOUtils.digit2(cArr, i + 15);
            int iDigit24 = IOUtils.digit2(cArr, i + 18);
            if ((iDigit4 | iMonth | iDigit2 | iDigit22 | iDigit23 | iDigit24) > 0 && iDigit22 <= 24 && iDigit23 <= 59 && iDigit24 <= 60) {
                return LocalDateTime.of(iDigit4, iMonth, iDigit2, iDigit22, iDigit23, iDigit24);
            }
        }
        return null;
    }

    public static LocalDateTime parseLocalDateTime20(byte[] bArr, int i) {
        if (i + 19 <= bArr.length && bArr[i + 2] == 32 && bArr[i + 6] == 32 && bArr[i + 11] == 32) {
            long jHms = hms(bArr, i + 12);
            if (jHms != -1) {
                int iDigit2 = IOUtils.digit2(bArr, i);
                int iMonth = month(bArr[i + 3], bArr[i + 4], bArr[i + 5]);
                int iDigit4 = IOUtils.digit4(bArr, i + 7);
                int i2 = ((int) jHms) & 255;
                int i3 = ((int) (jHms >> 24)) & 255;
                int i4 = ((int) (jHms >> 48)) & 255;
                if ((iDigit4 | iMonth | iDigit2 | i2 | i3 | i4) > 0 && i2 <= 24 && i3 <= 59 && i4 <= 60) {
                    return LocalDateTime.of(iDigit4, iMonth, iDigit2, i2, i3, i4);
                }
            }
        }
        return null;
    }

    public static LocalDateTime parseLocalDateTime26(byte[] bArr, int i) {
        byte b;
        if (i + 26 <= bArr.length && bArr[i + 4] == 45 && bArr[i + 7] == 45 && ((b = bArr[i + 10]) == 32 || b == 84)) {
            long jHms = hms(bArr, i + 11);
            if (jHms != -1 && bArr[i + 19] == 46) {
                int iDigit4 = IOUtils.digit4(bArr, i);
                int iDigit2 = IOUtils.digit2(bArr, i + 5);
                int iDigit22 = IOUtils.digit2(bArr, i + 8);
                int i2 = ((int) jHms) & 255;
                int i3 = ((int) (jHms >> 24)) & 255;
                int i4 = ((int) (jHms >> 48)) & 255;
                int nanos = readNanos(bArr, 6, i + 20);
                if ((iDigit4 | iDigit2 | iDigit22 | i2 | i3 | i4 | nanos) > 0 && i2 <= 24 && i3 <= 59 && i4 <= 60) {
                    return LocalDateTime.of(iDigit4, iDigit2, iDigit22, i2, i3, i4, nanos);
                }
            }
        }
        return null;
    }

    public static LocalDateTime parseLocalDateTime26(char[] cArr, int i) {
        char c;
        if (i + 26 <= cArr.length && cArr[i + 4] == '-' && cArr[i + 7] == '-' && (((c = cArr[i + 10]) == ' ' || c == 'T') && cArr[i + 13] == ':' && cArr[i + 16] == ':' && cArr[i + 19] == '.')) {
            int iDigit4 = IOUtils.digit4(cArr, i);
            int iDigit2 = IOUtils.digit2(cArr, i + 5);
            int iDigit22 = IOUtils.digit2(cArr, i + 8);
            int iDigit23 = IOUtils.digit2(cArr, i + 11);
            int iDigit24 = IOUtils.digit2(cArr, i + 14);
            int iDigit25 = IOUtils.digit2(cArr, i + 17);
            int nanos = readNanos(cArr, 6, i + 20);
            if ((iDigit4 | iDigit2 | iDigit22 | iDigit23 | iDigit24 | iDigit25 | nanos) > 0 && iDigit23 <= 24 && iDigit24 <= 59 && iDigit25 <= 60) {
                return LocalDateTime.of(iDigit4, iDigit2, iDigit22, iDigit23, iDigit24, iDigit25, nanos);
            }
        }
        return null;
    }

    public static LocalDateTime parseLocalDateTime27(byte[] bArr, int i) {
        byte b;
        if (i + 27 <= bArr.length && bArr[i + 4] == 45 && bArr[i + 7] == 45 && ((b = bArr[i + 10]) == 32 || b == 84)) {
            long jHms = hms(bArr, i + 11);
            if (jHms != -1 && bArr[i + 19] == 46) {
                int iDigit4 = IOUtils.digit4(bArr, i);
                int iDigit2 = IOUtils.digit2(bArr, i + 5);
                int iDigit22 = IOUtils.digit2(bArr, i + 8);
                int i2 = ((int) jHms) & 255;
                int i3 = ((int) (jHms >> 24)) & 255;
                int i4 = ((int) (jHms >> 48)) & 255;
                int nanos = readNanos(bArr, 7, i + 20);
                if ((iDigit4 | iDigit2 | iDigit22 | i2 | i3 | i4 | nanos) > 0 && i2 <= 24 && i3 <= 59 && i4 <= 60) {
                    return LocalDateTime.of(iDigit4, iDigit2, iDigit22, i2, i3, i4, nanos);
                }
            }
        }
        return null;
    }

    public static LocalDateTime parseLocalDateTime27(char[] cArr, int i) {
        char c;
        if (i + 27 <= cArr.length && cArr[i + 4] == '-' && cArr[i + 7] == '-' && (((c = cArr[i + 10]) == ' ' || c == 'T') && cArr[i + 13] == ':' && cArr[i + 16] == ':' && cArr[i + 19] == '.')) {
            int iDigit4 = IOUtils.digit4(cArr, i);
            int iDigit2 = IOUtils.digit2(cArr, i + 5);
            int iDigit22 = IOUtils.digit2(cArr, i + 8);
            int iDigit23 = IOUtils.digit2(cArr, i + 11);
            int iDigit24 = IOUtils.digit2(cArr, i + 14);
            int iDigit25 = IOUtils.digit2(cArr, i + 17);
            int nanos = readNanos(cArr, 7, i + 20);
            if ((iDigit4 | iDigit2 | iDigit22 | iDigit23 | iDigit24 | iDigit25 | nanos) > 0 && iDigit23 <= 24 && iDigit24 <= 59 && iDigit25 <= 60) {
                return LocalDateTime.of(iDigit4, iDigit2, iDigit22, iDigit23, iDigit24, iDigit25, nanos);
            }
        }
        return null;
    }

    public static LocalDateTime parseLocalDateTime28(char[] cArr, int i) {
        char c;
        if (i + 28 <= cArr.length && cArr[i + 4] == '-' && cArr[i + 7] == '-' && (((c = cArr[i + 10]) == ' ' || c == 'T') && cArr[i + 13] == ':' && cArr[i + 16] == ':' && cArr[i + 19] == '.')) {
            int iDigit4 = IOUtils.digit4(cArr, i);
            int iDigit2 = IOUtils.digit2(cArr, i + 5);
            int iDigit22 = IOUtils.digit2(cArr, i + 8);
            int iDigit23 = IOUtils.digit2(cArr, i + 11);
            int iDigit24 = IOUtils.digit2(cArr, i + 14);
            int iDigit25 = IOUtils.digit2(cArr, i + 17);
            int nanos = readNanos(cArr, 8, i + 20);
            if ((iDigit4 | iDigit2 | iDigit22 | iDigit23 | iDigit24 | iDigit25 | nanos) > 0 && iDigit23 <= 24 && iDigit24 <= 59 && iDigit25 <= 60) {
                return LocalDateTime.of(iDigit4, iDigit2, iDigit22, iDigit23, iDigit24, iDigit25, nanos);
            }
        }
        return null;
    }

    public static LocalDateTime parseLocalDateTime28(byte[] bArr, int i) {
        byte b;
        if (i + 28 <= bArr.length && bArr[i + 4] == 45 && bArr[i + 7] == 45 && ((b = bArr[i + 10]) == 32 || b == 84)) {
            long jHms = hms(bArr, i + 11);
            if (jHms != -1 && bArr[i + 19] == 46) {
                int iDigit4 = IOUtils.digit4(bArr, i);
                int iDigit2 = IOUtils.digit2(bArr, i + 5);
                int iDigit22 = IOUtils.digit2(bArr, i + 8);
                int i2 = ((int) jHms) & 255;
                int i3 = ((int) (jHms >> 24)) & 255;
                int i4 = ((int) (jHms >> 48)) & 255;
                int nanos = readNanos(bArr, 8, i + 20);
                if ((iDigit4 | iDigit2 | iDigit22 | i2 | i3 | i4 | nanos) > 0 && i2 <= 24 && i3 <= 59 && i4 <= 60) {
                    return LocalDateTime.of(iDigit4, iDigit2, iDigit22, i2, i3, i4, nanos);
                }
            }
        }
        return null;
    }

    public static LocalDateTime parseLocalDateTime29(byte[] bArr, int i) {
        byte b;
        if (i + 29 <= bArr.length && bArr[i + 4] == 45 && bArr[i + 7] == 45 && ((b = bArr[i + 10]) == 32 || b == 84)) {
            long jHms = hms(bArr, i + 11);
            if (jHms != -1 && bArr[i + 19] == 46) {
                int iDigit4 = IOUtils.digit4(bArr, i);
                int iDigit2 = IOUtils.digit2(bArr, i + 5);
                int iDigit22 = IOUtils.digit2(bArr, i + 8);
                int i2 = ((int) jHms) & 255;
                int i3 = ((int) (jHms >> 24)) & 255;
                int i4 = ((int) (jHms >> 48)) & 255;
                int nanos = readNanos(bArr, 9, i + 20);
                if ((iDigit4 | iDigit2 | iDigit22 | i2 | i3 | i4 | nanos) > 0 && i2 <= 24 && i3 <= 59 && i4 <= 60) {
                    return LocalDateTime.of(iDigit4, iDigit2, iDigit22, i2, i3, i4, nanos);
                }
            }
        }
        return null;
    }

    public static LocalDateTime parseLocalDateTime29(char[] cArr, int i) {
        char c;
        if (i + 29 <= cArr.length && cArr[i + 4] == '-' && cArr[i + 7] == '-' && (((c = cArr[i + 10]) == ' ' || c == 'T') && cArr[i + 13] == ':' && cArr[i + 16] == ':' && cArr[i + 19] == '.')) {
            int iDigit4 = IOUtils.digit4(cArr, i);
            int iDigit2 = IOUtils.digit2(cArr, i + 5);
            int iDigit22 = IOUtils.digit2(cArr, i + 8);
            int iDigit23 = IOUtils.digit2(cArr, i + 11);
            int iDigit24 = IOUtils.digit2(cArr, i + 14);
            int iDigit25 = IOUtils.digit2(cArr, i + 17);
            int nanos = readNanos(cArr, 9, i + 20);
            if ((iDigit4 | iDigit2 | iDigit22 | iDigit23 | iDigit24 | iDigit25 | nanos) > 0 && iDigit23 <= 24 && iDigit24 <= 59 && iDigit25 <= 60) {
                return LocalDateTime.of(iDigit4, iDigit2, iDigit22, iDigit23, iDigit24, iDigit25, nanos);
            }
        }
        return null;
    }

    public static LocalDateTime parseLocalDateTimeX(char[] cArr, int i, int i2) {
        char c;
        char c2;
        char c3;
        char c4;
        char c5;
        char c6;
        char c7;
        char c8;
        char c9;
        char c10;
        char c11;
        char c12;
        char c13;
        char c14;
        char c15;
        char c16;
        char c17;
        char c18;
        char c19;
        char c20;
        if (cArr == null || i2 == 0 || i2 < 21 || i2 > 29) {
            return null;
        }
        char c21 = cArr[i];
        char c22 = cArr[i + 1];
        char c23 = cArr[i + 2];
        char c24 = cArr[i + 3];
        char c25 = cArr[i + 4];
        char c26 = cArr[i + 5];
        char c27 = cArr[i + 6];
        char c28 = cArr[i + 7];
        char c29 = cArr[i + 8];
        char c30 = cArr[i + 9];
        char c31 = cArr[i + 10];
        char c32 = cArr[i + 11];
        char c33 = cArr[i + 12];
        char c34 = cArr[i + 13];
        char c35 = cArr[i + 14];
        char c36 = cArr[i + 15];
        char c37 = cArr[i + 16];
        char c38 = cArr[i + 17];
        char c39 = cArr[i + 18];
        char c40 = cArr[i + 19];
        char c41 = '0';
        switch (i2) {
            case 21:
                c = c29;
                c2 = c30;
                c3 = c32;
                c4 = c35;
                c5 = c38;
                c6 = '0';
                c7 = '0';
                c8 = '0';
                c9 = '0';
                c10 = '0';
                c11 = c21;
                c12 = cArr[i + 20];
                c13 = '0';
                c14 = '0';
                break;
            case 22:
                c15 = cArr[i + 20];
                c16 = cArr[i + 21];
                c = c29;
                c2 = c30;
                c3 = c32;
                c4 = c35;
                c5 = c38;
                c7 = '0';
                c8 = '0';
                c9 = c8;
                c10 = c9;
                c11 = c21;
                c12 = c15;
                c13 = c10;
                c14 = c13;
                c41 = c16;
                c6 = c14;
                break;
            case 23:
                c15 = cArr[i + 20];
                c16 = cArr[i + 21];
                c7 = cArr[i + 22];
                c = c29;
                c2 = c30;
                c3 = c32;
                c4 = c35;
                c5 = c38;
                c8 = '0';
                c9 = c8;
                c10 = c9;
                c11 = c21;
                c12 = c15;
                c13 = c10;
                c14 = c13;
                c41 = c16;
                c6 = c14;
                break;
            case 24:
                char c42 = cArr[i + 20];
                char c43 = cArr[i + 21];
                c7 = cArr[i + 22];
                c = c29;
                c2 = c30;
                c3 = c32;
                c4 = c35;
                c5 = c38;
                c9 = '0';
                c10 = '0';
                c11 = c21;
                c12 = c42;
                c13 = cArr[i + 23];
                c14 = '0';
                c8 = '0';
                c41 = c43;
                c6 = '0';
                break;
            case 25:
                char c44 = cArr[i + 20];
                char c45 = cArr[i + 21];
                c7 = cArr[i + 22];
                c = c29;
                c2 = c30;
                c3 = c32;
                c4 = c35;
                c5 = c38;
                c10 = '0';
                c11 = c21;
                c12 = c44;
                c13 = cArr[i + 23];
                c14 = cArr[i + 24];
                c8 = '0';
                c9 = '0';
                c41 = c45;
                c6 = '0';
                break;
            case 26:
                char c46 = cArr[i + 20];
                c17 = cArr[i + 21];
                c7 = cArr[i + 22];
                c18 = cArr[i + 23];
                char c47 = cArr[i + 24];
                c19 = cArr[i + 25];
                c = c29;
                c2 = c30;
                c3 = c32;
                c4 = c35;
                c5 = c38;
                c10 = '0';
                c12 = c46;
                c14 = c47;
                c9 = '0';
                c41 = c17;
                c6 = c19;
                c11 = c21;
                c13 = c18;
                c8 = c9;
                break;
            case 27:
                char c48 = cArr[i + 20];
                c17 = cArr[i + 21];
                c7 = cArr[i + 22];
                c18 = cArr[i + 23];
                char c49 = cArr[i + 24];
                c19 = cArr[i + 25];
                c10 = cArr[i + 26];
                c = c29;
                c2 = c30;
                c3 = c32;
                c4 = c35;
                c5 = c38;
                c12 = c48;
                c14 = c49;
                c9 = '0';
                c41 = c17;
                c6 = c19;
                c11 = c21;
                c13 = c18;
                c8 = c9;
                break;
            case 28:
                char c50 = cArr[i + 20];
                char c51 = cArr[i + 21];
                c7 = cArr[i + 22];
                char c52 = cArr[i + 23];
                char c53 = cArr[i + 24];
                char c54 = cArr[i + 25];
                c10 = cArr[i + 26];
                c = c29;
                c2 = c30;
                c3 = c32;
                c4 = c35;
                c5 = c38;
                c12 = c50;
                c14 = c53;
                c9 = '0';
                c41 = c51;
                c6 = c54;
                c11 = c21;
                c13 = c52;
                c8 = cArr[i + 27];
                break;
            default:
                char c55 = cArr[i + 20];
                c41 = cArr[i + 21];
                char c56 = cArr[i + 22];
                char c57 = cArr[i + 23];
                char c58 = cArr[i + 24];
                char c59 = cArr[i + 25];
                char c60 = cArr[i + 26];
                c = c29;
                c2 = c30;
                c3 = c32;
                c4 = c35;
                c5 = c38;
                c12 = c55;
                c14 = c58;
                c8 = cArr[i + 27];
                c10 = c60;
                c11 = c21;
                c13 = c57;
                c7 = c56;
                c6 = c59;
                c9 = cArr[i + 28];
                break;
        }
        if (c25 != '-' || c28 != '-' || ((c31 != ' ' && c31 != 'T') || c34 != ':' || c37 != ':' || c40 != '.')) {
            int i3 = i + i2;
            if (cArr[i3 - 15] == '-' && cArr[i3 - 12] == '-' && (((c20 = cArr[i3 - 9]) == ' ' || c20 == 'T') && cArr[i3 - 6] == ':' && cArr[i3 - 3] == ':')) {
                return LocalDateTime.of(TypeUtils.parseInt(cArr, i, i2 - 15), TypeUtils.parseInt(cArr, i3 - 14, 2), TypeUtils.parseInt(cArr, i3 - 11, 2), TypeUtils.parseInt(cArr, i3 - 8, 2), TypeUtils.parseInt(cArr, i3 - 5, 2), TypeUtils.parseInt(cArr, i3 - 2, 2));
            }
            return null;
        }
        return localDateTime(c11, c22, c23, c24, c26, c27, c, c2, c3, c33, c4, c36, c5, c39, c12, c41, c7, c13, c14, c6, c10, c8, c9);
    }

    public static LocalDateTime parseLocalDateTimeX(byte[] bArr, int i, int i2) {
        char c;
        char c2;
        char c3;
        char c4;
        char c5;
        char c6;
        char c7;
        char c8;
        char c9;
        char c10;
        char c11;
        char c12;
        char c13;
        char c14;
        char c15;
        char c16;
        byte b;
        if (bArr == null || i2 == 0 || i2 < 21 || i2 > 29) {
            return null;
        }
        char c17 = (char) bArr[i];
        char c18 = (char) bArr[i + 1];
        char c19 = (char) bArr[i + 2];
        char c20 = (char) bArr[i + 3];
        char c21 = (char) bArr[i + 4];
        char c22 = (char) bArr[i + 5];
        char c23 = (char) bArr[i + 6];
        char c24 = (char) bArr[i + 7];
        char c25 = (char) bArr[i + 8];
        char c26 = (char) bArr[i + 9];
        char c27 = (char) bArr[i + 10];
        char c28 = (char) bArr[i + 11];
        char c29 = (char) bArr[i + 12];
        char c30 = (char) bArr[i + 13];
        char c31 = (char) bArr[i + 14];
        char c32 = (char) bArr[i + 15];
        char c33 = (char) bArr[i + 16];
        char c34 = (char) bArr[i + 17];
        char c35 = (char) bArr[i + 18];
        char c36 = (char) bArr[i + 19];
        char c37 = '0';
        switch (i2) {
            case 21:
                c = (char) bArr[i + 20];
                c2 = c25;
                c3 = c26;
                c4 = c28;
                c5 = '0';
                c6 = '0';
                c7 = '0';
                c8 = '0';
                c9 = c29;
                c10 = c31;
                c11 = c34;
                c12 = c35;
                c13 = c18;
                c14 = '0';
                c15 = '0';
                c16 = '0';
                break;
            case 22:
                char c38 = (char) bArr[i + 20];
                c2 = c25;
                c3 = c26;
                c4 = c28;
                c5 = '0';
                c7 = '0';
                c8 = '0';
                c9 = c29;
                c10 = c31;
                c11 = c34;
                c12 = c35;
                c13 = c18;
                c37 = (char) bArr[i + 21];
                c14 = '0';
                c15 = '0';
                c16 = '0';
                c = c38;
                c6 = '0';
                break;
            case 23:
                char c39 = (char) bArr[i + 20];
                char c40 = (char) bArr[i + 21];
                c2 = c25;
                c3 = c26;
                c4 = c28;
                c5 = '0';
                c8 = '0';
                c9 = c29;
                c10 = c31;
                c11 = c34;
                c12 = c35;
                c13 = c18;
                c14 = (char) bArr[i + 22];
                c15 = '0';
                c16 = '0';
                c = c39;
                c37 = c40;
                c6 = '0';
                c7 = c6;
                break;
            case 24:
                char c41 = (char) bArr[i + 20];
                char c42 = (char) bArr[i + 21];
                c2 = c25;
                c3 = c26;
                c4 = c28;
                c5 = '0';
                c9 = c29;
                c10 = c31;
                c11 = c34;
                c12 = c35;
                c13 = c18;
                c14 = (char) bArr[i + 22];
                c15 = (char) bArr[i + 23];
                c16 = '0';
                c8 = '0';
                c = c41;
                c37 = c42;
                c6 = '0';
                c7 = c6;
                break;
            case 25:
                char c43 = (char) bArr[i + 20];
                char c44 = (char) bArr[i + 21];
                char c45 = (char) bArr[i + 22];
                char c46 = (char) bArr[i + 23];
                c5 = (char) bArr[i + 24];
                c2 = c25;
                c3 = c26;
                c4 = c28;
                c9 = c29;
                c10 = c31;
                c11 = c34;
                c12 = c35;
                c = c43;
                c14 = c45;
                c15 = c46;
                c6 = '0';
                c8 = '0';
                c13 = c18;
                c16 = '0';
                c37 = c44;
                c7 = '0';
                break;
            case 26:
                char c47 = (char) bArr[i + 20];
                char c48 = (char) bArr[i + 21];
                char c49 = (char) bArr[i + 22];
                c2 = c25;
                c3 = c26;
                c4 = c28;
                c10 = c31;
                c12 = c35;
                c15 = (char) bArr[i + 23];
                c5 = (char) bArr[i + 24];
                c9 = c29;
                c11 = c34;
                c13 = c18;
                c14 = c49;
                c16 = (char) bArr[i + 25];
                c8 = '0';
                c = c47;
                c6 = '0';
                c37 = c48;
                c7 = c6;
                break;
            case 27:
                char c50 = (char) bArr[i + 20];
                char c51 = (char) bArr[i + 21];
                char c52 = (char) bArr[i + 22];
                char c53 = (char) bArr[i + 23];
                char c54 = (char) bArr[i + 24];
                char c55 = (char) bArr[i + 25];
                c6 = (char) bArr[i + 26];
                c = c50;
                c2 = c25;
                c3 = c26;
                c4 = c28;
                c10 = c31;
                c12 = c35;
                c15 = c53;
                c13 = c18;
                c16 = c55;
                c5 = c54;
                c9 = c29;
                c11 = c34;
                c14 = c52;
                c8 = '0';
                c37 = c51;
                c7 = '0';
                break;
            case 28:
                char c56 = (char) bArr[i + 20];
                char c57 = (char) bArr[i + 21];
                char c58 = (char) bArr[i + 22];
                char c59 = (char) bArr[i + 23];
                char c60 = (char) bArr[i + 24];
                char c61 = (char) bArr[i + 25];
                char c62 = (char) bArr[i + 26];
                c2 = c25;
                c3 = c26;
                c4 = c28;
                c10 = c31;
                c12 = c35;
                c15 = c59;
                c13 = c18;
                c16 = c61;
                c5 = c60;
                c9 = c29;
                c11 = c34;
                c14 = c58;
                c8 = '0';
                c37 = c57;
                c7 = (char) bArr[i + 27];
                c = c56;
                c6 = c62;
                break;
            default:
                char c63 = (char) bArr[i + 20];
                char c64 = (char) bArr[i + 21];
                char c65 = (char) bArr[i + 22];
                char c66 = (char) bArr[i + 23];
                char c67 = (char) bArr[i + 24];
                char c68 = (char) bArr[i + 25];
                char c69 = (char) bArr[i + 26];
                char c70 = (char) bArr[i + 27];
                c37 = c64;
                c6 = c69;
                c2 = c25;
                c3 = c26;
                c4 = c28;
                c10 = c31;
                c12 = c35;
                c15 = c66;
                c8 = (char) bArr[i + 28];
                c = c63;
                c5 = c67;
                c13 = c18;
                c16 = c68;
                c9 = c29;
                c11 = c34;
                c14 = c65;
                c7 = c70;
                break;
        }
        if (c21 != '-' || c24 != '-' || ((c27 != ' ' && c27 != 'T') || c30 != ':' || c33 != ':' || c36 != '.')) {
            int i3 = i + i2;
            if (bArr[i3 - 15] == 45 && bArr[i3 - 12] == 45 && (((b = bArr[i3 - 9]) == 32 || b == 84) && bArr[i3 - 6] == 58 && bArr[i3 - 3] == 58)) {
                return LocalDateTime.of(TypeUtils.parseInt(bArr, i, i2 - 15), TypeUtils.parseInt(bArr, i3 - 14, 2), TypeUtils.parseInt(bArr, i3 - 11, 2), TypeUtils.parseInt(bArr, i3 - 8, 2), TypeUtils.parseInt(bArr, i3 - 5, 2), TypeUtils.parseInt(bArr, i3 - 2, 2));
            }
            return null;
        }
        return localDateTime(c17, c13, c19, c20, c22, c23, c2, c3, c4, c9, c10, c32, c11, c12, c, c37, c14, c15, c5, c16, c6, c7, c8);
    }

    static ZonedDateTime parseZonedDateTime16(char[] cArr, int i, ZoneId zoneId) {
        if (i + 16 > cArr.length) {
            String str = new String(cArr, i, cArr.length - i);
            throw new DateTimeParseException("illegal input ".concat(str), str, 0);
        }
        char c = cArr[i];
        char c2 = cArr[i + 1];
        char c3 = cArr[i + 2];
        char c4 = cArr[i + 3];
        char c5 = cArr[i + 4];
        char c6 = cArr[i + 5];
        char c7 = cArr[i + 6];
        char c8 = cArr[i + 7];
        char c9 = cArr[i + 8];
        char c10 = cArr[i + 9];
        int i2 = i + 10;
        char c11 = cArr[i2];
        char c12 = cArr[i + 13];
        if (c5 != '-' || c8 != '-' || ((c11 != '+' && c11 != '-') || c12 != ':')) {
            String str2 = new String(cArr, i, 16);
            throw new DateTimeParseException("illegal input ".concat(str2), str2, 0);
        }
        if (c < '0' || c > '9' || c2 < '0' || c2 > '9' || c3 < '0' || c3 > '9' || c4 < '0' || c4 > '9') {
            String str3 = new String(cArr, i, 16);
            throw new DateTimeParseException("illegal input ".concat(str3), str3, 0);
        }
        int i3 = ((c - '0') * 1000) + ((c2 - '0') * 100) + ((c3 - '0') * 10) + (c4 - '0');
        if (c6 < '0' || c6 > '9' || c7 < '0' || c7 > '9') {
            String str4 = new String(cArr, i, 16);
            throw new DateTimeParseException("illegal input ".concat(str4), str4, 0);
        }
        int i4 = ((c6 - '0') * 10) + (c7 - '0');
        if (c9 < '0' || c9 > '9' || c10 < '0' || c10 > '9') {
            String str5 = new String(cArr, i, 16);
            throw new DateTimeParseException("illegal input ".concat(str5), str5, 0);
        }
        return ZonedDateTime.of(LocalDateTime.of(LocalDate.of(i3, i4, ((c9 - '0') * 10) + (c10 - '0')), LocalTime.MIN), getZoneId(new String(cArr, i2, 6), zoneId));
    }

    static ZonedDateTime parseZonedDateTime16(byte[] bArr, int i, ZoneId zoneId) {
        if (i + 16 > bArr.length) {
            String str = new String(bArr, i, bArr.length - i);
            throw new DateTimeParseException("illegal input ".concat(str), str, 0);
        }
        char c = (char) bArr[i];
        char c2 = (char) bArr[i + 1];
        char c3 = (char) bArr[i + 2];
        char c4 = (char) bArr[i + 3];
        char c5 = (char) bArr[i + 4];
        char c6 = (char) bArr[i + 5];
        char c7 = (char) bArr[i + 6];
        char c8 = (char) bArr[i + 7];
        char c9 = (char) bArr[i + 8];
        char c10 = (char) bArr[i + 9];
        int i2 = i + 10;
        char c11 = (char) bArr[i2];
        char c12 = (char) bArr[i + 13];
        if (c5 != '-' || c8 != '-' || ((c11 != '+' && c11 != '-') || c12 != ':')) {
            String str2 = new String(bArr, i, 16);
            throw new DateTimeParseException("illegal input ".concat(str2), str2, 0);
        }
        if (c < '0' || c > '9' || c2 < '0' || c2 > '9' || c3 < '0' || c3 > '9' || c4 < '0' || c4 > '9') {
            String str3 = new String(bArr, i, 16);
            throw new DateTimeParseException("illegal input ".concat(str3), str3, 0);
        }
        int i3 = ((c - '0') * 1000) + ((c2 - '0') * 100) + ((c3 - '0') * 10) + (c4 - '0');
        if (c6 < '0' || c6 > '9' || c7 < '0' || c7 > '9') {
            String str4 = new String(bArr, i, 16);
            throw new DateTimeParseException("illegal input ".concat(str4), str4, 0);
        }
        int i4 = ((c6 - '0') * 10) + (c7 - '0');
        if (c9 < '0' || c9 > '9' || c10 < '0' || c10 > '9') {
            String str5 = new String(bArr, i, 16);
            throw new DateTimeParseException("illegal input ".concat(str5), str5, 0);
        }
        return ZonedDateTime.of(LocalDateTime.of(LocalDate.of(i3, i4, ((c9 - '0') * 10) + (c10 - '0')), LocalTime.MIN), getZoneId(new String(bArr, i2, 6), zoneId));
    }

    public static ZonedDateTime parseZonedDateTime(String str) {
        return parseZonedDateTime(str, DEFAULT_ZONE_ID);
    }

    public static ZonedDateTime parseZonedDateTime(String str, ZoneId zoneId) {
        ZonedDateTime zonedDateTime;
        if (str == null || str.length() == 0) {
            return null;
        }
        if (JDKUtils.STRING_CODER != null && JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER.applyAsInt(str) == 0) {
            byte[] bArrApply = JDKUtils.STRING_VALUE.apply(str);
            zonedDateTime = parseZonedDateTime(bArrApply, 0, bArrApply.length, zoneId);
        } else {
            char[] charArray = JDKUtils.getCharArray(str);
            zonedDateTime = parseZonedDateTime(charArray, 0, charArray.length, zoneId);
        }
        if (zonedDateTime != null) {
            return zonedDateTime;
        }
        str.hashCode();
        switch (str) {
            case "0":
            case "null":
            case "0000-00-00":
                return null;
            default:
                throw new DateTimeParseException(str, str, 0);
        }
    }

    public static ZonedDateTime parseZonedDateTime(byte[] bArr, int i, int i2) {
        return parseZonedDateTime(bArr, i, i2, DEFAULT_ZONE_ID);
    }

    public static ZonedDateTime parseZonedDateTime(char[] cArr, int i, int i2) {
        return parseZonedDateTime(cArr, i, i2, DEFAULT_ZONE_ID);
    }

    /* JADX WARN: Code restructure failed: missing block: B:131:0x04a0, code lost:
    
        if (r4 != 'Z') goto L140;
     */
    /* JADX WARN: Code restructure failed: missing block: B:425:0x09a6, code lost:
    
        if (r14 != 'Z') goto L437;
     */
    /* JADX WARN: Code restructure failed: missing block: B:460:0x0a30, code lost:
    
        if (r1 != 'Z') goto L471;
     */
    /* JADX WARN: Code restructure failed: missing block: B:494:0x0aaa, code lost:
    
        if (r15 != 'Z') goto L505;
     */
    /* JADX WARN: Code restructure failed: missing block: B:557:0x0ba5, code lost:
    
        if (r15 != 'Z') goto L568;
     */
    /* JADX WARN: Code restructure failed: missing block: B:626:0x0cd0, code lost:
    
        if (r12 != 'Z') goto L637;
     */
    /* JADX WARN: Code restructure failed: missing block: B:725:0x0ecc, code lost:
    
        if (r14 != 'Z') goto L735;
     */
    /* JADX WARN: Removed duplicated region for block: B:214:0x0634  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x063a  */
    /* JADX WARN: Removed duplicated region for block: B:244:0x06b3  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x06bd  */
    /* JADX WARN: Removed duplicated region for block: B:277:0x0743  */
    /* JADX WARN: Removed duplicated region for block: B:305:0x07c4  */
    /* JADX WARN: Removed duplicated region for block: B:336:0x0853  */
    /* JADX WARN: Removed duplicated region for block: B:340:0x0865  */
    /* JADX WARN: Removed duplicated region for block: B:370:0x08e4  */
    /* JADX WARN: Removed duplicated region for block: B:372:0x08ea  */
    /* JADX WARN: Removed duplicated region for block: B:399:0x0964  */
    /* JADX WARN: Removed duplicated region for block: B:403:0x0970 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:430:0x09af  */
    /* JADX WARN: Removed duplicated region for block: B:431:0x09b2  */
    /* JADX WARN: Removed duplicated region for block: B:435:0x09f2  */
    /* JADX WARN: Removed duplicated region for block: B:439:0x0a02 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:466:0x0a3b  */
    /* JADX WARN: Removed duplicated region for block: B:467:0x0a3e  */
    /* JADX WARN: Removed duplicated region for block: B:470:0x0a74  */
    /* JADX WARN: Removed duplicated region for block: B:500:0x0ab5  */
    /* JADX WARN: Removed duplicated region for block: B:501:0x0ab8  */
    /* JADX WARN: Removed duplicated region for block: B:504:0x0ae8  */
    /* JADX WARN: Removed duplicated region for block: B:507:0x0af0  */
    /* JADX WARN: Removed duplicated region for block: B:533:0x0b6d  */
    /* JADX WARN: Removed duplicated region for block: B:563:0x0bb0  */
    /* JADX WARN: Removed duplicated region for block: B:564:0x0bb3  */
    /* JADX WARN: Removed duplicated region for block: B:567:0x0be7  */
    /* JADX WARN: Removed duplicated region for block: B:602:0x0c9a  */
    /* JADX WARN: Removed duplicated region for block: B:632:0x0cdb  */
    /* JADX WARN: Removed duplicated region for block: B:633:0x0cde  */
    /* JADX WARN: Removed duplicated region for block: B:636:0x0d16  */
    /* JADX WARN: Removed duplicated region for block: B:639:0x0d20  */
    /* JADX WARN: Removed duplicated region for block: B:654:0x0d8f  */
    /* JADX WARN: Removed duplicated region for block: B:677:0x0e11  */
    /* JADX WARN: Removed duplicated region for block: B:682:0x0e23  */
    /* JADX WARN: Removed duplicated region for block: B:701:0x0e92  */
    /* JADX WARN: Removed duplicated region for block: B:731:0x0ed7  */
    /* JADX WARN: Removed duplicated region for block: B:732:0x0eda  */
    /* JADX WARN: Removed duplicated region for block: B:734:0x0f0e  */
    /* JADX WARN: Removed duplicated region for block: B:737:0x0f14  */
    /* JADX WARN: Removed duplicated region for block: B:786:0x0fe6  */
    /* JADX WARN: Removed duplicated region for block: B:788:0x0fe9  */
    /* JADX WARN: Removed duplicated region for block: B:791:0x0ff1  */
    /* JADX WARN: Removed duplicated region for block: B:861:0x1099  */
    /* JADX WARN: Removed duplicated region for block: B:866:0x10b1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.time.ZonedDateTime parseZonedDateTime(byte[] r58, int r59, int r60, java.time.ZoneId r61) {
        /*
            Method dump skipped, instruction units count: 4336
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.DateUtils.parseZonedDateTime(byte[], int, int, java.time.ZoneId):java.time.ZonedDateTime");
    }

    /* JADX WARN: Code restructure failed: missing block: B:365:0x07f8, code lost:
    
        if (r14 != 'Z') goto L376;
     */
    /* JADX WARN: Code restructure failed: missing block: B:399:0x0881, code lost:
    
        if (r13 != 'Z') goto L409;
     */
    /* JADX WARN: Code restructure failed: missing block: B:494:0x0a21, code lost:
    
        if (r1 != 'Z') goto L509;
     */
    /* JADX WARN: Code restructure failed: missing block: B:562:0x0b3f, code lost:
    
        if (r1 != 'Z') goto L573;
     */
    /* JADX WARN: Code restructure failed: missing block: B:631:0x0c5b, code lost:
    
        if (r12 != 'Z') goto L642;
     */
    /* JADX WARN: Code restructure failed: missing block: B:731:0x0e4a, code lost:
    
        if (r14 != 'Z') goto L741;
     */
    /* JADX WARN: Removed duplicated region for block: B:212:0x0571  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x0577  */
    /* JADX WARN: Removed duplicated region for block: B:242:0x05f0  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x05fa  */
    /* JADX WARN: Removed duplicated region for block: B:274:0x067e  */
    /* JADX WARN: Removed duplicated region for block: B:302:0x06fd  */
    /* JADX WARN: Removed duplicated region for block: B:336:0x079b  */
    /* JADX WARN: Removed duplicated region for block: B:341:0x07b1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:370:0x0801  */
    /* JADX WARN: Removed duplicated region for block: B:371:0x0804  */
    /* JADX WARN: Removed duplicated region for block: B:374:0x083f  */
    /* JADX WARN: Removed duplicated region for block: B:378:0x0853 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:405:0x088c  */
    /* JADX WARN: Removed duplicated region for block: B:406:0x088f  */
    /* JADX WARN: Removed duplicated region for block: B:408:0x08c5  */
    /* JADX WARN: Removed duplicated region for block: B:437:0x094b  */
    /* JADX WARN: Removed duplicated region for block: B:468:0x09dd  */
    /* JADX WARN: Removed duplicated region for block: B:472:0x09e5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:500:0x0a2e  */
    /* JADX WARN: Removed duplicated region for block: B:501:0x0a31  */
    /* JADX WARN: Removed duplicated region for block: B:506:0x0a73  */
    /* JADX WARN: Removed duplicated region for block: B:511:0x0a89  */
    /* JADX WARN: Removed duplicated region for block: B:538:0x0b07  */
    /* JADX WARN: Removed duplicated region for block: B:541:0x0b11 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:568:0x0b4a  */
    /* JADX WARN: Removed duplicated region for block: B:569:0x0b4d  */
    /* JADX WARN: Removed duplicated region for block: B:572:0x0b84  */
    /* JADX WARN: Removed duplicated region for block: B:607:0x0c25  */
    /* JADX WARN: Removed duplicated region for block: B:637:0x0c66  */
    /* JADX WARN: Removed duplicated region for block: B:638:0x0c69  */
    /* JADX WARN: Removed duplicated region for block: B:641:0x0c98  */
    /* JADX WARN: Removed duplicated region for block: B:644:0x0ca2  */
    /* JADX WARN: Removed duplicated region for block: B:659:0x0d0e  */
    /* JADX WARN: Removed duplicated region for block: B:682:0x0d91  */
    /* JADX WARN: Removed duplicated region for block: B:687:0x0da3  */
    /* JADX WARN: Removed duplicated region for block: B:706:0x0e10  */
    /* JADX WARN: Removed duplicated region for block: B:710:0x0e1c A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:737:0x0e55  */
    /* JADX WARN: Removed duplicated region for block: B:738:0x0e58  */
    /* JADX WARN: Removed duplicated region for block: B:740:0x0e8a  */
    /* JADX WARN: Removed duplicated region for block: B:743:0x0e90  */
    /* JADX WARN: Removed duplicated region for block: B:755:0x0ed6  */
    /* JADX WARN: Removed duplicated region for block: B:757:0x0ed9  */
    /* JADX WARN: Removed duplicated region for block: B:760:0x0eec A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:762:0x0eee  */
    /* JADX WARN: Removed duplicated region for block: B:792:0x0f65  */
    /* JADX WARN: Removed duplicated region for block: B:794:0x0f68  */
    /* JADX WARN: Removed duplicated region for block: B:797:0x0f70  */
    /* JADX WARN: Removed duplicated region for block: B:867:0x1018  */
    /* JADX WARN: Removed duplicated region for block: B:872:0x1030  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.time.ZonedDateTime parseZonedDateTime(char[] r55, int r56, int r57, java.time.ZoneId r58) {
        /*
            Method dump skipped, instruction units count: 4228
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.DateUtils.parseZonedDateTime(char[], int, int, java.time.ZoneId):java.time.ZonedDateTime");
    }

    static ZonedDateTime parseZonedDateTimeCookie(String str) {
        if (str.endsWith(" CST")) {
            DateTimeFormatter dateTimeFormatterOfPattern = DATE_TIME_FORMATTER_COOKIE_LOCAL;
            if (dateTimeFormatterOfPattern == null) {
                dateTimeFormatterOfPattern = DateTimeFormatter.ofPattern("EEEE, dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
                DATE_TIME_FORMATTER_COOKIE_LOCAL = dateTimeFormatterOfPattern;
            }
            return ZonedDateTime.of(LocalDateTime.parse(str.substring(0, str.length() - 4), dateTimeFormatterOfPattern), SHANGHAI_ZONE_ID);
        }
        DateTimeFormatter dateTimeFormatterOfPattern2 = DATE_TIME_FORMATTER_COOKIE;
        if (dateTimeFormatterOfPattern2 == null) {
            dateTimeFormatterOfPattern2 = DateTimeFormatter.ofPattern("EEEE, dd-MMM-yyyy HH:mm:ss zzz", Locale.ENGLISH);
            DATE_TIME_FORMATTER_COOKIE = dateTimeFormatterOfPattern2;
        }
        return ZonedDateTime.parse(str, dateTimeFormatterOfPattern2);
    }

    public static ZoneId getZoneId(String str, ZoneId zoneId) {
        int iIndexOf;
        char cCharAt;
        if (str == null) {
            return zoneId != null ? zoneId : DEFAULT_ZONE_ID;
        }
        str.hashCode();
        switch (str) {
            case "000":
                return ZoneOffset.UTC;
            case "CST":
                return SHANGHAI_ZONE_ID;
            case "+08:00":
                return OFFSET_8_ZONE_ID;
            default:
                if (str.length() > 0 && (((cCharAt = str.charAt(0)) == '+' || cCharAt == '-') && str.charAt(str.length() - 1) != ']')) {
                    return ZoneOffset.of(str);
                }
                int iIndexOf2 = str.indexOf(91);
                if (iIndexOf2 > 0 && (iIndexOf = str.indexOf(93, iIndexOf2)) > 0) {
                    return ZoneId.of(str.substring(iIndexOf2 + 1, iIndexOf));
                }
                return ZoneId.of(str);
        }
    }

    public static long parseMillisYMDHMS19(String str, ZoneId zoneId) {
        char cCharAt;
        char cCharAt2;
        char cCharAt3;
        char cCharAt4;
        char cCharAt5;
        char cCharAt6;
        char cCharAt7;
        char cCharAt8;
        char c;
        char c2;
        char c3;
        char c4;
        char c5;
        char cCharAt9;
        char c6;
        char cCharAt10;
        char cCharAt11;
        char c7;
        char c8;
        int i;
        char c9;
        char c10;
        int totalSeconds;
        if (str == null) {
            return 0L;
        }
        if (JDKUtils.JVM_VERSION == 8) {
            char[] charArray = JDKUtils.getCharArray(str);
            if (charArray.length != 19) {
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            cCharAt = charArray[0];
            cCharAt2 = charArray[1];
            char c11 = charArray[2];
            cCharAt3 = charArray[3];
            char c12 = charArray[4];
            cCharAt4 = charArray[5];
            char c13 = charArray[6];
            cCharAt5 = charArray[7];
            cCharAt6 = charArray[8];
            char c14 = charArray[9];
            char c15 = charArray[10];
            char c16 = charArray[11];
            char c17 = charArray[12];
            char c18 = charArray[13];
            cCharAt7 = charArray[14];
            cCharAt8 = charArray[15];
            cCharAt9 = charArray[16];
            c8 = c18;
            c = c11;
            c3 = c12;
            c5 = c14;
            c7 = c15;
            c2 = c16;
            c6 = c17;
            cCharAt10 = charArray[17];
            cCharAt11 = charArray[18];
            c4 = c13;
        } else if (JDKUtils.STRING_CODER != null && JDKUtils.STRING_CODER.applyAsInt(str) == 0 && JDKUtils.STRING_VALUE != null) {
            byte[] bArrApply = JDKUtils.STRING_VALUE.apply(str);
            if (bArrApply.length != 19) {
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            cCharAt = (char) bArrApply[0];
            cCharAt2 = (char) bArrApply[1];
            char c19 = (char) bArrApply[2];
            cCharAt3 = (char) bArrApply[3];
            char c20 = (char) bArrApply[4];
            cCharAt4 = (char) bArrApply[5];
            char c21 = (char) bArrApply[6];
            cCharAt5 = (char) bArrApply[7];
            cCharAt6 = (char) bArrApply[8];
            char c22 = (char) bArrApply[9];
            char c23 = (char) bArrApply[10];
            char c24 = (char) bArrApply[11];
            char c25 = (char) bArrApply[12];
            char c26 = (char) bArrApply[13];
            cCharAt7 = (char) bArrApply[14];
            cCharAt8 = (char) bArrApply[15];
            char c27 = (char) bArrApply[16];
            char c28 = (char) bArrApply[17];
            cCharAt11 = (char) bArrApply[18];
            c4 = c21;
            c5 = c22;
            c8 = c26;
            c = c19;
            c6 = c25;
            c2 = c24;
            c7 = c23;
            c3 = c20;
            cCharAt9 = c27;
            cCharAt10 = c28;
        } else {
            if (str.length() != 19) {
                throw new DateTimeParseException("illegal input " + str, str, 0);
            }
            cCharAt = str.charAt(0);
            cCharAt2 = str.charAt(1);
            char cCharAt12 = str.charAt(2);
            cCharAt3 = str.charAt(3);
            char cCharAt13 = str.charAt(4);
            cCharAt4 = str.charAt(5);
            char cCharAt14 = str.charAt(6);
            cCharAt5 = str.charAt(7);
            cCharAt6 = str.charAt(8);
            char cCharAt15 = str.charAt(9);
            char cCharAt16 = str.charAt(10);
            char cCharAt17 = str.charAt(11);
            char cCharAt18 = str.charAt(12);
            char cCharAt19 = str.charAt(13);
            cCharAt7 = str.charAt(14);
            cCharAt8 = str.charAt(15);
            c = cCharAt12;
            c2 = cCharAt17;
            c3 = cCharAt13;
            c4 = cCharAt14;
            c5 = cCharAt15;
            cCharAt9 = str.charAt(16);
            c6 = cCharAt18;
            cCharAt10 = str.charAt(17);
            cCharAt11 = str.charAt(18);
            c7 = cCharAt16;
            c8 = cCharAt19;
        }
        char c29 = c6;
        if (c3 != '-' || cCharAt5 != '-' || c7 != ' ' || c8 != ':' || cCharAt9 != ':') {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        if (cCharAt < '0' || cCharAt > '9' || cCharAt2 < '0' || cCharAt2 > '9' || c < '0' || c > '9' || cCharAt3 < '0' || cCharAt3 > '9') {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        int i2 = ((cCharAt - '0') * 1000) + ((cCharAt2 - '0') * 100) + ((c - '0') * 10) + (cCharAt3 - '0');
        if (cCharAt4 < '0' || cCharAt4 > '9' || c4 < '0' || c4 > '9') {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        int i3 = ((cCharAt4 - '0') * 10) + (c4 - '0');
        if ((i3 == 0 && i2 != 0) || i3 > 12) {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        if (cCharAt6 < '0' || cCharAt6 > '9' || c5 < '0' || c5 > '9') {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        int i4 = ((cCharAt6 - '0') * 10) + (c5 - '0');
        if (i3 != 2) {
            i = (i3 == 4 || i3 == 6 || i3 == 9 || i3 == 11) ? 30 : 31;
        } else {
            i = ((i2 & 3) != 0 || (i2 % 100 == 0 && i2 % HttpStatus.SC_BAD_REQUEST != 0)) ? 28 : 29;
        }
        if ((i4 == 0 && i2 != 0) || i4 > i) {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        if (c2 < '0' || c2 > '9' || c29 < '0' || c29 > '9') {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        int i5 = ((c2 - '0') * 10) + (c29 - '0');
        char c30 = cCharAt7;
        if (c30 < '0' || c30 > '9' || (c9 = cCharAt8) < '0' || c9 > '9') {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        int i6 = ((c30 - '0') * 10) + (c9 - '0');
        char c31 = cCharAt10;
        if (c31 < '0' || c31 > '9' || (c10 = cCharAt11) < '0' || c10 > '9') {
            throw new DateTimeParseException("illegal input", str, 0);
        }
        int i7 = ((c31 - '0') * 10) + (c10 - '0');
        if (i2 == 0 && i3 == 0 && i4 == 0) {
            i2 = 1970;
            i4 = 1;
            i3 = 1;
        }
        long jCalcEpochDay = (calcEpochDay(i2, i3, i4) * 86400) + ((long) (i5 * 3600)) + ((long) (i6 * 60)) + ((long) i7);
        ZoneId zoneId2 = zoneId == null ? DEFAULT_ZONE_ID : zoneId;
        if (!(zoneId2 == SHANGHAI_ZONE_ID || zoneId2.getRules() == SHANGHAI_ZONE_RULES) || jCalcEpochDay < 684900000) {
            totalSeconds = (zoneId2 == ZoneOffset.UTC || "UTC".equals(zoneId2.getId())) ? 0 : zoneId2.getRules().getOffset(LocalDateTime.of(LocalDate.of(i2, i3, i4), LocalTime.of(i5, i6, i7, 0))).getTotalSeconds();
        } else {
            totalSeconds = 28800;
        }
        return (jCalcEpochDay - ((long) totalSeconds)) * 1000;
    }

    private static long calcEpochDay(int i, int i2, int i3) {
        long j = (i * 365) + (((i + 3) / 4) - ((i + 99) / 100)) + ((i + 399) / HttpStatus.SC_BAD_REQUEST) + (((i2 * 367) - 362) / 12) + (i3 - 1);
        if (i2 > 2) {
            j = ((i & 3) != 0 || (i % 100 == 0 && i % HttpStatus.SC_BAD_REQUEST != 0)) ? j - 2 : j - 1;
        }
        return j - 719528;
    }

    /* JADX WARN: Removed duplicated region for block: B:191:0x03ba  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0264  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static long parseMillis19(java.lang.String r34, java.time.ZoneId r35, com.alibaba.fastjson2.util.DateUtils.DateTimeFormatPattern r36) {
        /*
            Method dump skipped, instruction units count: 1001
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.DateUtils.parseMillis19(java.lang.String, java.time.ZoneId, com.alibaba.fastjson2.util.DateUtils$DateTimeFormatPattern):long");
    }

    /* JADX INFO: renamed from: com.alibaba.fastjson2.util.DateUtils$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$alibaba$fastjson2$util$DateUtils$DateTimeFormatPattern;

        static {
            int[] iArr = new int[DateTimeFormatPattern.values().length];
            $SwitchMap$com$alibaba$fastjson2$util$DateUtils$DateTimeFormatPattern = iArr;
            try {
                iArr[DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson2$util$DateUtils$DateTimeFormatPattern[DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH_T.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson2$util$DateUtils$DateTimeFormatPattern[DateTimeFormatPattern.DATE_TIME_FORMAT_19_SLASH.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson2$util$DateUtils$DateTimeFormatPattern[DateTimeFormatPattern.DATE_TIME_FORMAT_19_DOT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson2$util$DateUtils$DateTimeFormatPattern[DateTimeFormatPattern.DATE_FORMAT_10_DASH.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson2$util$DateUtils$DateTimeFormatPattern[DateTimeFormatPattern.DATE_FORMAT_10_SLASH.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x0120  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0137  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static long parseMillis10(java.lang.String r21, java.time.ZoneId r22, com.alibaba.fastjson2.util.DateUtils.DateTimeFormatPattern r23) {
        /*
            Method dump skipped, instruction units count: 594
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.DateUtils.parseMillis10(java.lang.String, java.time.ZoneId, com.alibaba.fastjson2.util.DateUtils$DateTimeFormatPattern):long");
    }

    /* JADX WARN: Code restructure failed: missing block: B:63:0x020f, code lost:
    
        if (r7 == ':') goto L64;
     */
    /* JADX WARN: Removed duplicated region for block: B:102:0x02f5 A[PHI: r1
      0x02f5: PHI (r1v38 char) = (r1v37 char), (r1v37 char), (r1v37 char), (r1v51 char) binds: [B:90:0x02b1, B:91:0x02b3, B:92:0x02b5, B:101:0x02f3] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:103:0x02f7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x026c A[PHI: r1
      0x026c: PHI (r1v36 char) = (r1v35 char), (r1v35 char), (r1v35 char), (r1v65 char) binds: [B:66:0x0226, B:67:0x0228, B:68:0x022a, B:77:0x026a] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x026e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x02b1 A[PHI: r1
      0x02b1: PHI (r1v37 char) = (r1v36 char), (r1v36 char), (r1v36 char), (r1v58 char) binds: [B:78:0x026c, B:79:0x026e, B:80:0x0270, B:89:0x02af] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:91:0x02b3 A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static long parseMillis19(java.lang.String r33, java.time.ZoneId r34) {
        /*
            Method dump skipped, instruction units count: 1318
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.DateUtils.parseMillis19(java.lang.String, java.time.ZoneId):long");
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x00ef A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0132 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0178 A[PHI: r11
      0x0178: PHI (r11v12 char) = (r11v11 char), (r11v11 char), (r11v11 char), (r11v20 char) binds: [B:55:0x0130, B:56:0x0132, B:57:0x0134, B:66:0x0176] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x017a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x01be A[PHI: r11
      0x01be: PHI (r11v13 char) = (r11v12 char), (r11v12 char), (r11v12 char), (r11v17 char) binds: [B:67:0x0178, B:68:0x017a, B:69:0x017c, B:78:0x01bc] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01c0 A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static long parseMillis19(byte[] r24, int r25, java.time.ZoneId r26) {
        /*
            Method dump skipped, instruction units count: 980
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.DateUtils.parseMillis19(byte[], int, java.time.ZoneId):long");
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x00da A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x011d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0163 A[PHI: r11
      0x0163: PHI (r11v10 char) = (r11v9 char), (r11v9 char), (r11v9 char), (r11v18 char) binds: [B:55:0x011b, B:56:0x011d, B:57:0x011f, B:66:0x0161] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0165 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x01a9 A[PHI: r11
      0x01a9: PHI (r11v11 char) = (r11v10 char), (r11v10 char), (r11v10 char), (r11v15 char) binds: [B:67:0x0163, B:68:0x0165, B:69:0x0167, B:78:0x01a7] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01ab A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static long parseMillis19(char[] r24, int r25, java.time.ZoneId r26) {
        /*
            Method dump skipped, instruction units count: 959
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.DateUtils.parseMillis19(char[], int, java.time.ZoneId):long");
    }

    public static LocalDateTime localDateTime(char c, char c2, char c3, char c4, char c5, char c6, char c7, char c8, char c9, char c10, char c11, char c12, char c13, char c14) {
        if (c >= '0' && c <= '9' && c2 >= '0' && c2 <= '9' && c3 >= '0' && c3 <= '9' && c4 >= '0' && c4 <= '9') {
            int i = ((c - '0') * 1000) + ((c2 - '0') * 100) + ((c3 - '0') * 10) + (c4 - '0');
            if (c5 >= '0' && c5 <= '9' && c6 >= '0' && c6 <= '9') {
                int i2 = ((c5 - '0') * 10) + (c6 - '0');
                if (c7 >= '0' && c7 <= '9' && c8 >= '0' && c8 <= '9') {
                    int i3 = ((c7 - '0') * 10) + (c8 - '0');
                    if (c9 >= '0' && c9 <= '9' && c10 >= '0' && c10 <= '9') {
                        int i4 = ((c9 - '0') * 10) + (c10 - '0');
                        if (c11 >= '0' && c11 <= '9' && c12 >= '0' && c12 <= '9') {
                            int i5 = ((c11 - '0') * 10) + (c12 - '0');
                            if (c13 >= '0' && c13 <= '9' && c14 >= '0' && c14 <= '9') {
                                int i6 = ((c13 - '0') * 10) + (c14 - '0');
                                if ((i != 0 || i2 != 0 || i3 != 0 || i4 != 0 || i5 != 0 || i6 != 0) && i4 <= 24 && i5 <= 60 && i6 <= 60) {
                                    return LocalDateTime.of(i, i2, i3, i4, i5, i6, 0);
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static LocalDateTime localDateTime(char c, char c2, char c3, char c4, char c5, char c6, char c7, char c8, char c9, char c10, char c11, char c12, char c13, char c14, char c15, char c16, char c17, char c18, char c19, char c20, char c21, char c22, char c23) {
        if (c >= '0' && c <= '9' && c2 >= '0' && c2 <= '9' && c3 >= '0' && c3 <= '9' && c4 >= '0' && c4 <= '9') {
            int i = ((c - '0') * 1000) + ((c2 - '0') * 100) + ((c3 - '0') * 10) + (c4 - '0');
            if (c5 >= '0' && c5 <= '9' && c6 >= '0' && c6 <= '9') {
                int i2 = ((c5 - '0') * 10) + (c6 - '0');
                if (c7 >= '0' && c7 <= '9' && c8 >= '0' && c8 <= '9') {
                    int i3 = ((c7 - '0') * 10) + (c8 - '0');
                    if (c9 >= '0' && c9 <= '9' && c10 >= '0' && c10 <= '9') {
                        int i4 = ((c9 - '0') * 10) + (c10 - '0');
                        if (c11 >= '0' && c11 <= '9' && c12 >= '0' && c12 <= '9') {
                            int i5 = ((c11 - '0') * 10) + (c12 - '0');
                            if (c13 >= '0' && c13 <= '9' && c14 >= '0' && c14 <= '9') {
                                int i6 = ((c13 - '0') * 10) + (c14 - '0');
                                if (c15 >= '0' && c15 <= '9' && c16 >= '0' && c16 <= '9' && c17 >= '0' && c17 <= '9' && c18 >= '0' && c18 <= '9' && c19 >= '0' && c19 <= '9' && c20 >= '0' && c20 <= '9' && c21 >= '0' && c21 <= '9' && c22 >= '0' && c22 <= '9' && c23 >= '0' && c23 <= '9') {
                                    return LocalDateTime.of(i, i2, i3, i4, i5, i6, ((c15 - '0') * 100000000) + ((c16 - '0') * 10000000) + ((c17 - '0') * DurationKt.NANOS_IN_MILLIS) + ((c18 - '0') * 100000) + ((c19 - '0') * AsyncHttpClient.DEFAULT_SOCKET_TIMEOUT) + ((c20 - '0') * 1000) + ((c21 - '0') * 100) + ((c22 - '0') * 10) + (c23 - '0'));
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static long millis(LocalDateTime localDateTime) {
        return millis(null, localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth(), localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond(), localDateTime.getNano());
    }

    public static long millis(LocalDateTime localDateTime, ZoneId zoneId) {
        return millis(zoneId, localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth(), localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond(), localDateTime.getNano());
    }

    public static long millis(ZoneId zoneId, int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        if (zoneId == null) {
            zoneId = DEFAULT_ZONE_ID;
        }
        long jCalcEpochDay = (calcEpochDay(i, i2, i3) * 86400) + ((long) (i4 * 3600)) + ((long) (i5 * 60)) + ((long) i6);
        int totalSeconds = 0;
        if ((zoneId == SHANGHAI_ZONE_ID || zoneId.getRules() == SHANGHAI_ZONE_RULES) && jCalcEpochDay >= 684900000) {
            totalSeconds = 28800;
        } else if (zoneId != ZoneOffset.UTC && !"UTC".equals(zoneId.getId())) {
            totalSeconds = zoneId.getRules().getOffset(LocalDateTime.of(LocalDate.of(i, i2, i3), LocalTime.of(i4, i5, i6, i7))).getTotalSeconds();
        }
        long j = (jCalcEpochDay - ((long) totalSeconds)) * 1000;
        return i7 != 0 ? j + ((long) (i7 / DurationKt.NANOS_IN_MILLIS)) : j;
    }

    public static long utcSeconds(int i, int i2, int i3, int i4, int i5, int i6) {
        return (calcEpochDay(i, i2, i3) * 86400) + ((long) (i4 * 3600)) + ((long) (i5 * 60)) + ((long) i6);
    }

    public static String formatYMDHMS19(Date date) {
        return formatYMDHMS19(date, DEFAULT_ZONE_ID);
    }

    public static String formatYMDHMS19(Date date, ZoneId zoneId) {
        long j;
        if (date == null) {
            return null;
        }
        long time = date.getTime();
        ZoneId zoneId2 = zoneId == null ? DEFAULT_ZONE_ID : zoneId;
        long jFloorDiv = Math.floorDiv(time, 1000L);
        long totalSeconds = jFloorDiv + ((long) (((zoneId2 == SHANGHAI_ZONE_ID || zoneId2.getRules() == SHANGHAI_ZONE_RULES) && jFloorDiv > 684900000) ? 28800 : zoneId2.getRules().getOffset(Instant.ofEpochMilli(time)).getTotalSeconds()));
        long jFloorDiv2 = Math.floorDiv(totalSeconds, 86400L);
        int iFloorMod = (int) Math.floorMod(totalSeconds, 86400L);
        long j2 = 719468 + jFloorDiv2;
        if (j2 < 0) {
            long j3 = ((jFloorDiv2 + 719469) / 146097) - 1;
            j = j3 * 400;
            j2 += (-j3) * 146097;
        } else {
            j = 0;
        }
        long j4 = ((j2 * 400) + 591) / 146097;
        long j5 = j2 - ((((j4 * 365) + (j4 / 4)) - (j4 / 100)) + (j4 / 400));
        if (j5 < 0) {
            j4--;
            j5 = j2 - ((((365 * j4) + (j4 / 4)) - (j4 / 100)) + (j4 / 400));
        }
        int i = (int) j5;
        int i2 = ((i * 5) + 2) / Opcodes.IFEQ;
        int i3 = ((i2 + 2) % 12) + 1;
        int i4 = (i - (((i2 * 306) + 5) / 10)) + 1;
        long j6 = j4 + j + ((long) (i2 / 10));
        if (j6 < -999999999 || j6 > 999999999) {
            throw new DateTimeException("Invalid year " + j6);
        }
        int i5 = (int) j6;
        long j7 = iFloorMod;
        if (j7 < 0 || j7 > 86399) {
            throw new DateTimeException("Invalid secondOfDay " + j7);
        }
        int i6 = (int) (j7 / 3600);
        long j8 = j7 - ((long) (i6 * 3600));
        int i7 = (int) (j8 / 60);
        int i8 = (int) (j8 - ((long) (i7 * 60)));
        if (JDKUtils.STRING_CREATOR_JDK11 != null) {
            byte[] bArr = new byte[19];
            IOUtils.writeLocalDate(bArr, 0, i5, i3, i4);
            bArr[10] = 32;
            IOUtils.writeLocalTime(bArr, 11, i6, i7, i8);
            return JDKUtils.STRING_CREATOR_JDK11.apply(bArr, JDKUtils.LATIN1);
        }
        char[] cArr = new char[19];
        IOUtils.writeLocalDate(cArr, 0, i5, i3, i4);
        cArr[10] = TokenParser.SP;
        IOUtils.writeLocalTime(cArr, 11, i6, i7, i8);
        if (JDKUtils.STRING_CREATOR_JDK8 != null) {
            return JDKUtils.STRING_CREATOR_JDK8.apply(cArr, Boolean.TRUE);
        }
        return new String(cArr);
    }

    public static String formatYMD8(Date date) {
        if (date == null) {
            return null;
        }
        return formatYMD8(date.getTime(), DEFAULT_ZONE_ID);
    }

    public static String formatYMD8(long j, ZoneId zoneId) {
        int shanghaiZoneOffsetTotalSeconds;
        long j2;
        String str;
        String str2;
        long jFloorDiv = Math.floorDiv(j, 1000L);
        ZoneId zoneId2 = zoneId == null ? DEFAULT_ZONE_ID : zoneId;
        if (zoneId2 == SHANGHAI_ZONE_ID || zoneId2.getRules() == SHANGHAI_ZONE_RULES) {
            shanghaiZoneOffsetTotalSeconds = getShanghaiZoneOffsetTotalSeconds(jFloorDiv);
        } else {
            shanghaiZoneOffsetTotalSeconds = zoneId2.getRules().getOffset(Instant.ofEpochMilli(j)).getTotalSeconds();
        }
        long jFloorDiv2 = Math.floorDiv(jFloorDiv + ((long) shanghaiZoneOffsetTotalSeconds), 86400L);
        int i = (int) ((jFloorDiv2 - ((long) LOCAL_EPOCH_DAY)) + 128);
        String[] strArr = CacheDate8.CACHE;
        if (i >= 0 && i < strArr.length && (str2 = strArr[i]) != null) {
            return str2;
        }
        long j3 = 719468 + jFloorDiv2;
        if (j3 < 0) {
            long j4 = ((jFloorDiv2 + 719469) / 146097) - 1;
            j2 = j4 * 400;
            j3 += (-j4) * 146097;
        } else {
            j2 = 0;
        }
        long j5 = ((j3 * 400) + 591) / 146097;
        long j6 = j3 - ((((j5 * 365) + (j5 / 4)) - (j5 / 100)) + (j5 / 400));
        if (j6 < 0) {
            j5--;
            j6 = j3 - ((((365 * j5) + (j5 / 4)) - (j5 / 100)) + (j5 / 400));
        }
        int i2 = (int) j6;
        int i3 = ((i2 * 5) + 2) / Opcodes.IFEQ;
        int i4 = ((i3 + 2) % 12) + 1;
        int i5 = (i2 - (((i3 * 306) + 5) / 10)) + 1;
        long j7 = j5 + j2 + ((long) (i3 / 10));
        if (j7 < -999999999 || j7 > 999999999) {
            throw new DateTimeException("Invalid year " + j7);
        }
        int i6 = (int) j7;
        int i7 = i6 / 100;
        int i8 = i6 - (i7 * 100);
        if (JDKUtils.STRING_CREATOR_JDK11 != null) {
            byte[] bArr = new byte[8];
            IOUtils.writeDigitPair(bArr, 0, i7);
            IOUtils.writeDigitPair(bArr, 2, i8);
            IOUtils.writeDigitPair(bArr, 4, i4);
            IOUtils.writeDigitPair(bArr, 6, i5);
            str = JDKUtils.STRING_CREATOR_JDK11.apply(bArr, JDKUtils.LATIN1);
        } else {
            char[] cArr = new char[8];
            IOUtils.writeDigitPair(cArr, 0, i7);
            IOUtils.writeDigitPair(cArr, 2, i8);
            IOUtils.writeDigitPair(cArr, 4, i4);
            IOUtils.writeDigitPair(cArr, 6, i5);
            if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                str = JDKUtils.STRING_CREATOR_JDK8.apply(cArr, Boolean.TRUE);
            } else {
                str = new String(cArr);
            }
        }
        if (i >= 0 && i < strArr.length) {
            strArr[i] = str;
        }
        return str;
    }

    public static String formatYMD10(int i, int i2, int i3) {
        if (JDKUtils.STRING_CREATOR_JDK11 != null) {
            byte[] bArr = new byte[10];
            IOUtils.writeLocalDate(bArr, 0, i, i2, i3);
            return JDKUtils.STRING_CREATOR_JDK11.apply(bArr, JDKUtils.LATIN1);
        }
        char[] cArr = new char[10];
        IOUtils.writeLocalDate(cArr, 0, i, i2, i3);
        if (JDKUtils.STRING_CREATOR_JDK8 != null) {
            return JDKUtils.STRING_CREATOR_JDK8.apply(cArr, Boolean.TRUE);
        }
        return new String(cArr);
    }

    public static String formatYMD10(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return formatYMD10(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
    }

    public static String formatYMD10(Date date) {
        if (date == null) {
            return null;
        }
        return formatYMD10(date.getTime(), DEFAULT_ZONE_ID);
    }

    public static String formatYMD8(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        int year = localDate.getYear();
        int monthValue = localDate.getMonthValue();
        int dayOfMonth = localDate.getDayOfMonth();
        int i = year / 100;
        int i2 = year - (i * 100);
        if (JDKUtils.STRING_CREATOR_JDK11 != null) {
            byte[] bArr = new byte[8];
            IOUtils.writeDigitPair(bArr, 0, i);
            IOUtils.writeDigitPair(bArr, 2, i2);
            IOUtils.writeDigitPair(bArr, 4, monthValue);
            IOUtils.writeDigitPair(bArr, 6, dayOfMonth);
            return JDKUtils.STRING_CREATOR_JDK11.apply(bArr, JDKUtils.LATIN1);
        }
        char[] cArr = new char[8];
        IOUtils.writeDigitPair(cArr, 0, i);
        IOUtils.writeDigitPair(cArr, 2, i2);
        IOUtils.writeDigitPair(cArr, 4, monthValue);
        IOUtils.writeDigitPair(cArr, 6, dayOfMonth);
        if (JDKUtils.STRING_CREATOR_JDK8 != null) {
            return JDKUtils.STRING_CREATOR_JDK8.apply(cArr, Boolean.TRUE);
        }
        return new String(cArr);
    }

    public static String formatYMD10(long j, ZoneId zoneId) {
        int shanghaiZoneOffsetTotalSeconds;
        long j2;
        String str;
        String str2;
        ZoneId zoneId2 = zoneId == null ? DEFAULT_ZONE_ID : zoneId;
        long jFloorDiv = Math.floorDiv(j, 1000L);
        if (zoneId2 == SHANGHAI_ZONE_ID || zoneId2.getRules() == SHANGHAI_ZONE_RULES) {
            shanghaiZoneOffsetTotalSeconds = getShanghaiZoneOffsetTotalSeconds(jFloorDiv);
        } else {
            shanghaiZoneOffsetTotalSeconds = zoneId2.getRules().getOffset(Instant.ofEpochMilli(j)).getTotalSeconds();
        }
        long jFloorDiv2 = Math.floorDiv(jFloorDiv + ((long) shanghaiZoneOffsetTotalSeconds), 86400L);
        int i = (int) ((jFloorDiv2 - ((long) LOCAL_EPOCH_DAY)) + 128);
        String[] strArr = CacheDate10.CACHE;
        if (i >= 0 && i < strArr.length && (str2 = strArr[i]) != null) {
            return str2;
        }
        long j3 = 719468 + jFloorDiv2;
        if (j3 < 0) {
            long j4 = ((jFloorDiv2 + 719469) / 146097) - 1;
            j2 = j4 * 400;
            j3 += (-j4) * 146097;
        } else {
            j2 = 0;
        }
        long j5 = ((j3 * 400) + 591) / 146097;
        long j6 = j3 - ((((j5 * 365) + (j5 / 4)) - (j5 / 100)) + (j5 / 400));
        if (j6 < 0) {
            j5--;
            j6 = j3 - ((((365 * j5) + (j5 / 4)) - (j5 / 100)) + (j5 / 400));
        }
        int i2 = (int) j6;
        int i3 = ((i2 * 5) + 2) / Opcodes.IFEQ;
        int i4 = ((i3 + 2) % 12) + 1;
        int i5 = (i2 - (((i3 * 306) + 5) / 10)) + 1;
        long j7 = j5 + j2 + ((long) (i3 / 10));
        if (j7 < -999999999 || j7 > 999999999) {
            throw new DateTimeException("Invalid year " + j7);
        }
        int i6 = (int) j7;
        if (JDKUtils.STRING_CREATOR_JDK11 != null) {
            byte[] bArr = new byte[10];
            IOUtils.writeLocalDate(bArr, 0, i6, i4, i5);
            str = JDKUtils.STRING_CREATOR_JDK11.apply(bArr, JDKUtils.LATIN1);
        } else {
            char[] cArr = new char[10];
            IOUtils.writeLocalDate(cArr, 0, i6, i4, i5);
            if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                str = JDKUtils.STRING_CREATOR_JDK8.apply(cArr, Boolean.TRUE);
            } else {
                str = new String(cArr);
            }
        }
        if (i >= 0 && i < strArr.length) {
            strArr[i] = str;
        }
        return str;
    }

    public static String format(Date date, String str) {
        if (date == null) {
            return null;
        }
        if (str == null) {
            return format(date);
        }
        str.hashCode();
        switch (str) {
            case "yyyyMMdd":
                return formatYMD8(date.getTime(), DEFAULT_ZONE_ID);
            case "yyyy-MM-dd":
                return formatYMD10(date.getTime(), DEFAULT_ZONE_ID);
            case "yyyy/MM/dd":
                return format(date.getTime(), DateTimeFormatPattern.DATE_FORMAT_10_SLASH);
            case "yyyy-MM-dd HH:mm:ss":
                return format(date.getTime(), DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH);
            case "dd.MM.yyyy HH:mm:ss":
                return format(date.getTime(), DateTimeFormatPattern.DATE_TIME_FORMAT_19_DOT);
            case "yyyy-MM-dd'T'HH:mm:ss":
            case "yyyy-MM-ddTHH:mm:ss":
                return format(date.getTime(), DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH_T);
            case "dd.MM.yyyy":
                return format(date.getTime(), DateTimeFormatPattern.DATE_FORMAT_10_DOT);
            default:
                return DateTimeFormatter.ofPattern(str).format(Instant.ofEpochMilli(date.getTime()).atZone(DEFAULT_ZONE_ID));
        }
    }

    public static String formatYMDHMS19(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) {
            return null;
        }
        return format(zonedDateTime.getYear(), zonedDateTime.getMonthValue(), zonedDateTime.getDayOfMonth(), zonedDateTime.getHour(), zonedDateTime.getMinute(), zonedDateTime.getSecond(), DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH);
    }

    public static String format(ZonedDateTime zonedDateTime, String str) {
        int year;
        int monthValue;
        int dayOfMonth;
        if (zonedDateTime == null) {
            return null;
        }
        year = zonedDateTime.getYear();
        monthValue = zonedDateTime.getMonthValue();
        dayOfMonth = zonedDateTime.getDayOfMonth();
        str.hashCode();
        switch (str) {
            case "yyyy-MM-dd":
                return format(year, monthValue, dayOfMonth, DateTimeFormatPattern.DATE_FORMAT_10_DASH);
            case "yyyy/MM/dd":
                return format(year, monthValue, dayOfMonth, DateTimeFormatPattern.DATE_FORMAT_10_SLASH);
            case "yyyy-MM-dd HH:mm:ss":
                return format(year, monthValue, dayOfMonth, zonedDateTime.getHour(), zonedDateTime.getMinute(), zonedDateTime.getSecond(), DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH);
            case "yyyy-MM-dd'T'HH:mm:ss":
            case "yyyy-MM-ddTHH:mm:ss":
                return format(year, monthValue, dayOfMonth, zonedDateTime.getHour(), zonedDateTime.getMinute(), zonedDateTime.getSecond(), DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH_T);
            case "dd.MM.yyyy":
                return format(year, monthValue, dayOfMonth, DateTimeFormatPattern.DATE_FORMAT_10_DOT);
            default:
                return DateTimeFormatter.ofPattern(str).format(zonedDateTime);
        }
    }

    public static String formatYMDHMS19(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        int year = localDateTime.getYear();
        int monthValue = localDateTime.getMonthValue();
        int dayOfMonth = localDateTime.getDayOfMonth();
        int hour = localDateTime.getHour();
        int minute = localDateTime.getMinute();
        int second = localDateTime.getSecond();
        if (JDKUtils.STRING_CREATOR_JDK11 != null) {
            byte[] bArr = new byte[19];
            IOUtils.writeLocalDate(bArr, 0, year, monthValue, dayOfMonth);
            bArr[10] = 32;
            IOUtils.writeLocalTime(bArr, 11, hour, minute, second);
            return JDKUtils.STRING_CREATOR_JDK11.apply(bArr, JDKUtils.LATIN1);
        }
        char[] cArr = new char[19];
        IOUtils.writeLocalDate(cArr, 0, year, monthValue, dayOfMonth);
        cArr[10] = TokenParser.SP;
        IOUtils.writeLocalTime(cArr, 11, hour, minute, second);
        if (JDKUtils.STRING_CREATOR_JDK8 != null) {
            return JDKUtils.STRING_CREATOR_JDK8.apply(cArr, Boolean.TRUE);
        }
        return new String(cArr);
    }

    public static String format(LocalDateTime localDateTime, String str) {
        int year;
        int monthValue;
        int dayOfMonth;
        if (localDateTime == null) {
            return null;
        }
        year = localDateTime.getYear();
        monthValue = localDateTime.getMonthValue();
        dayOfMonth = localDateTime.getDayOfMonth();
        str.hashCode();
        switch (str) {
            case "yyyy-MM-dd":
                return formatYMD10(year, monthValue, dayOfMonth);
            case "yyyy/MM/dd":
                return format(year, monthValue, dayOfMonth, DateTimeFormatPattern.DATE_FORMAT_10_SLASH);
            case "yyyy-MM-dd HH:mm:ss":
                return format(year, monthValue, dayOfMonth, localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond(), DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH);
            case "yyyy-MM-dd'T'HH:mm:ss":
            case "yyyy-MM-ddTHH:mm:ss":
                return format(year, monthValue, dayOfMonth, localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond(), DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH_T);
            case "dd.MM.yyyy":
                return format(year, monthValue, dayOfMonth, DateTimeFormatPattern.DATE_FORMAT_10_DOT);
            default:
                return DateTimeFormatter.ofPattern(str).format(localDateTime);
        }
    }

    public static String formatYMDHMS19(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        int year = localDate.getYear();
        int monthValue = localDate.getMonthValue();
        int dayOfMonth = localDate.getDayOfMonth();
        if (JDKUtils.STRING_CREATOR_JDK11 != null) {
            byte[] bArr = new byte[19];
            IOUtils.writeLocalDate(bArr, 0, year, monthValue, dayOfMonth);
            bArr[10] = 32;
            IOUtils.writeLocalTime(bArr, 11, 0, 0, 0);
            return JDKUtils.STRING_CREATOR_JDK11.apply(bArr, JDKUtils.LATIN1);
        }
        char[] cArr = new char[19];
        IOUtils.writeLocalDate(cArr, 0, year, monthValue, dayOfMonth);
        cArr[10] = TokenParser.SP;
        IOUtils.writeLocalTime(cArr, 11, 0, 0, 0);
        if (JDKUtils.STRING_CREATOR_JDK8 != null) {
            return JDKUtils.STRING_CREATOR_JDK8.apply(cArr, Boolean.TRUE);
        }
        return new String(cArr);
    }

    public static String format(LocalDate localDate, String str) {
        int year;
        int monthValue;
        int dayOfMonth;
        if (localDate == null) {
            return null;
        }
        year = localDate.getYear();
        monthValue = localDate.getMonthValue();
        dayOfMonth = localDate.getDayOfMonth();
        str.hashCode();
        switch (str) {
            case "yyyy-MM-dd":
                return format(year, monthValue, dayOfMonth, DateTimeFormatPattern.DATE_FORMAT_10_DASH);
            case "yyyy/MM/dd":
                return format(year, monthValue, dayOfMonth, DateTimeFormatPattern.DATE_FORMAT_10_SLASH);
            case "yyyy-MM-dd HH:mm:ss":
                return format(year, monthValue, dayOfMonth, 0, 0, 0, DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH);
            case "yyyy-MM-dd'T'HH:mm:ss":
            case "yyyy-MM-ddTHH:mm:ss":
                return format(year, monthValue, dayOfMonth, 0, 0, 0, DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH_T);
            case "dd.MM.yyyy":
                return format(year, monthValue, dayOfMonth, DateTimeFormatPattern.DATE_FORMAT_10_DOT);
            default:
                return DateTimeFormatter.ofPattern(str).format(localDate);
        }
    }

    public static String format(int i, int i2, int i3) {
        return format(i, i2, i3, DateTimeFormatPattern.DATE_FORMAT_10_DASH);
    }

    public static String format(int i, int i2, int i3, DateTimeFormatPattern dateTimeFormatPattern) {
        int i4 = i / 100;
        int i5 = i - (i4 * 100);
        if (JDKUtils.STRING_CREATOR_JDK11 != null) {
            byte[] bArr = new byte[10];
            if (dateTimeFormatPattern == DateTimeFormatPattern.DATE_FORMAT_10_DOT) {
                IOUtils.writeDigitPair(bArr, 0, i3);
                bArr[2] = 46;
                IOUtils.writeDigitPair(bArr, 3, i2);
                bArr[5] = 46;
                IOUtils.writeDigitPair(bArr, 6, i4);
                IOUtils.writeDigitPair(bArr, 8, i5);
            } else {
                byte b = (byte) (dateTimeFormatPattern != DateTimeFormatPattern.DATE_FORMAT_10_DASH ? '/' : '-');
                IOUtils.writeDigitPair(bArr, 0, i4);
                IOUtils.writeDigitPair(bArr, 2, i5);
                bArr[4] = b;
                IOUtils.writeDigitPair(bArr, 5, i2);
                bArr[7] = b;
                IOUtils.writeDigitPair(bArr, 8, i3);
            }
            return JDKUtils.STRING_CREATOR_JDK11.apply(bArr, JDKUtils.LATIN1);
        }
        char[] cArr = new char[10];
        if (dateTimeFormatPattern == DateTimeFormatPattern.DATE_FORMAT_10_DOT) {
            IOUtils.writeDigitPair(cArr, 0, i3);
            cArr[2] = '.';
            IOUtils.writeDigitPair(cArr, 3, i2);
            cArr[5] = '.';
            IOUtils.writeDigitPair(cArr, 6, i4);
            IOUtils.writeDigitPair(cArr, 8, i5);
        } else {
            char c = dateTimeFormatPattern != DateTimeFormatPattern.DATE_FORMAT_10_DASH ? '/' : '-';
            IOUtils.writeDigitPair(cArr, 0, i4);
            IOUtils.writeDigitPair(cArr, 2, i5);
            cArr[4] = c;
            IOUtils.writeDigitPair(cArr, 5, i2);
            cArr[7] = c;
            IOUtils.writeDigitPair(cArr, 8, i3);
        }
        if (JDKUtils.STRING_CREATOR_JDK8 != null) {
            return JDKUtils.STRING_CREATOR_JDK8.apply(cArr, Boolean.TRUE);
        }
        return new String(cArr);
    }

    public static String format(long j) {
        return format(j, DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH);
    }

    public static String format(Date date) {
        if (date == null) {
            return null;
        }
        return format(date.getTime(), DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH);
    }

    public static String format(long j, DateTimeFormatPattern dateTimeFormatPattern) {
        int shanghaiZoneOffsetTotalSeconds;
        long j2;
        ZoneId zoneId = DEFAULT_ZONE_ID;
        long jFloorDiv = Math.floorDiv(j, 1000L);
        if (zoneId == SHANGHAI_ZONE_ID || zoneId.getRules() == SHANGHAI_ZONE_RULES) {
            shanghaiZoneOffsetTotalSeconds = getShanghaiZoneOffsetTotalSeconds(jFloorDiv);
        } else {
            shanghaiZoneOffsetTotalSeconds = zoneId.getRules().getOffset(Instant.ofEpochMilli(j)).getTotalSeconds();
        }
        long j3 = jFloorDiv + ((long) shanghaiZoneOffsetTotalSeconds);
        long jFloorDiv2 = Math.floorDiv(j3, 86400L);
        int iFloorMod = (int) Math.floorMod(j3, 86400L);
        long j4 = 719468 + jFloorDiv2;
        if (j4 < 0) {
            long j5 = ((jFloorDiv2 + 719469) / 146097) - 1;
            j2 = j5 * 400;
            j4 += (-j5) * 146097;
        } else {
            j2 = 0;
        }
        long j6 = ((j4 * 400) + 591) / 146097;
        long j7 = j4 - ((((j6 * 365) + (j6 / 4)) - (j6 / 100)) + (j6 / 400));
        if (j7 < 0) {
            j6--;
            j7 = j4 - ((((365 * j6) + (j6 / 4)) - (j6 / 100)) + (j6 / 400));
        }
        int i = (int) j7;
        int i2 = ((i * 5) + 2) / Opcodes.IFEQ;
        int i3 = ((i2 + 2) % 12) + 1;
        int i4 = (i - (((i2 * 306) + 5) / 10)) + 1;
        long j8 = j6 + j2 + ((long) (i2 / 10));
        if (j8 < -999999999 || j8 > 999999999) {
            throw new DateTimeException("Invalid year " + j8);
        }
        int i5 = (int) j8;
        if (dateTimeFormatPattern == DateTimeFormatPattern.DATE_FORMAT_10_DASH || dateTimeFormatPattern == DateTimeFormatPattern.DATE_FORMAT_10_SLASH || dateTimeFormatPattern == DateTimeFormatPattern.DATE_FORMAT_10_DOT) {
            return format(i5, i3, i4, dateTimeFormatPattern);
        }
        long j9 = iFloorMod;
        if (j9 < 0 || j9 > 86399) {
            throw new DateTimeException("Invalid secondOfDay " + j9);
        }
        int i6 = (int) (j9 / 3600);
        long j10 = j9 - ((long) (i6 * 3600));
        int i7 = (int) (j10 / 60);
        return format(i5, i3, i4, i6, i7, (int) (j10 - ((long) (i7 * 60))), dateTimeFormatPattern);
    }

    public static String format(int i, int i2, int i3, int i4, int i5, int i6) {
        return format(i, i2, i3, i4, i5, i6, DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH);
    }

    static String format(int i, int i2, int i3, int i4, int i5, int i6, DateTimeFormatPattern dateTimeFormatPattern) {
        int i7 = i / 100;
        int i8 = i - (i7 * 100);
        BiFunction<byte[], Byte, String> biFunction = JDKUtils.STRING_CREATOR_JDK11;
        char c = TokenParser.SP;
        if (biFunction != null) {
            byte[] bArr = new byte[19];
            if (dateTimeFormatPattern == DateTimeFormatPattern.DATE_TIME_FORMAT_19_DOT) {
                IOUtils.writeDigitPair(bArr, 0, i3);
                bArr[2] = 46;
                IOUtils.writeDigitPair(bArr, 3, i2);
                bArr[5] = 46;
                IOUtils.writeDigitPair(bArr, 6, i7);
                IOUtils.writeDigitPair(bArr, 8, i8);
                bArr[10] = 32;
            } else {
                int i9 = dateTimeFormatPattern == DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH ? 32 : 84;
                byte b = (byte) (dateTimeFormatPattern == DateTimeFormatPattern.DATE_TIME_FORMAT_19_SLASH ? 47 : 45);
                IOUtils.writeDigitPair(bArr, 0, i7);
                IOUtils.writeDigitPair(bArr, 2, i8);
                bArr[4] = b;
                IOUtils.writeDigitPair(bArr, 5, i2);
                bArr[7] = b;
                IOUtils.writeDigitPair(bArr, 8, i3);
                bArr[10] = (byte) i9;
            }
            IOUtils.writeLocalTime(bArr, 11, i4, i5, i6);
            return JDKUtils.STRING_CREATOR_JDK11.apply(bArr, JDKUtils.LATIN1);
        }
        char[] cArr = new char[19];
        if (dateTimeFormatPattern == DateTimeFormatPattern.DATE_TIME_FORMAT_19_DOT) {
            IOUtils.writeDigitPair(cArr, 0, i3);
            cArr[2] = '.';
            IOUtils.writeDigitPair(cArr, 3, i2);
            cArr[5] = '.';
            IOUtils.writeDigitPair(cArr, 6, i7);
            IOUtils.writeDigitPair(cArr, 8, i8);
            cArr[10] = TokenParser.SP;
        } else {
            if (dateTimeFormatPattern != DateTimeFormatPattern.DATE_TIME_FORMAT_19_DASH) {
                c = 'T';
            }
            char c2 = dateTimeFormatPattern == DateTimeFormatPattern.DATE_TIME_FORMAT_19_SLASH ? '/' : '-';
            IOUtils.writeDigitPair(cArr, 0, i7);
            IOUtils.writeDigitPair(cArr, 2, i8);
            cArr[4] = c2;
            IOUtils.writeDigitPair(cArr, 5, i2);
            cArr[7] = c2;
            IOUtils.writeDigitPair(cArr, 8, i3);
            cArr[10] = c;
        }
        IOUtils.writeLocalTime(cArr, 11, i4, i5, i6);
        if (JDKUtils.STRING_CREATOR_JDK8 != null) {
            return JDKUtils.STRING_CREATOR_JDK8.apply(cArr, Boolean.TRUE);
        }
        return new String(cArr);
    }

    public static String toString(Date date) {
        return toString(date.getTime(), false, DEFAULT_ZONE_ID);
    }

    public static String toString(long j, boolean z, ZoneId zoneId) {
        int shanghaiZoneOffsetTotalSeconds;
        long j2;
        int i;
        long jFloorDiv = Math.floorDiv(j, 1000L);
        if (zoneId == SHANGHAI_ZONE_ID || zoneId.getRules() == SHANGHAI_ZONE_RULES) {
            shanghaiZoneOffsetTotalSeconds = getShanghaiZoneOffsetTotalSeconds(jFloorDiv);
        } else {
            shanghaiZoneOffsetTotalSeconds = zoneId.getRules().getOffset(Instant.ofEpochMilli(j)).getTotalSeconds();
        }
        long j3 = jFloorDiv + ((long) shanghaiZoneOffsetTotalSeconds);
        long jFloorDiv2 = Math.floorDiv(j3, 86400L);
        int iFloorMod = (int) Math.floorMod(j3, 86400L);
        long j4 = 719468 + jFloorDiv2;
        if (j4 < 0) {
            long j5 = ((jFloorDiv2 + 719469) / 146097) - 1;
            j2 = j5 * 400;
            j4 += (-j5) * 146097;
        } else {
            j2 = 0;
        }
        long j6 = ((j4 * 400) + 591) / 146097;
        long j7 = j4 - ((((j6 * 365) + (j6 / 4)) - (j6 / 100)) + (j6 / 400));
        if (j7 < 0) {
            j6--;
            j7 = j4 - ((((365 * j6) + (j6 / 4)) - (j6 / 100)) + (j6 / 400));
        }
        int i2 = (int) j7;
        int i3 = ((i2 * 5) + 2) / Opcodes.IFEQ;
        int i4 = ((i3 + 2) % 12) + 1;
        int i5 = (i2 - (((i3 * 306) + 5) / 10)) + 1;
        long j8 = j6 + j2 + ((long) (i3 / 10));
        if (j8 < -999999999 || j8 > 999999999) {
            throw new DateTimeException("Invalid year " + j8);
        }
        int i6 = (int) j8;
        long j9 = iFloorMod;
        if (j9 < 0 || j9 > 86399) {
            throw new DateTimeException("Invalid secondOfDay " + j9);
        }
        int i7 = (int) (j9 / 3600);
        long j10 = j9 - ((long) (i7 * 3600));
        int i8 = (int) (j10 / 60);
        int i9 = (int) (j10 - ((long) (i8 * 60)));
        int iFloorMod2 = (int) Math.floorMod(j, 1000L);
        if (iFloorMod2 == 0) {
            i = 0;
        } else {
            i = 4;
            if (iFloorMod2 >= 10) {
                if (iFloorMod2 % 100 == 0) {
                    i = 2;
                } else if (iFloorMod2 % 10 == 0) {
                    i = 3;
                }
            }
        }
        int i10 = i + 19;
        int i11 = (z ? shanghaiZoneOffsetTotalSeconds == 0 ? 1 : 6 : 0) + i10;
        if (JDKUtils.STRING_CREATOR_JDK8 != null) {
            char[] cArr = new char[i11];
            IOUtils.writeLocalDate(cArr, 0, i6, i4, i5);
            cArr[10] = TokenParser.SP;
            IOUtils.writeLocalTime(cArr, 11, i7, i8, i9);
            if (i > 0) {
                cArr[19] = '.';
                for (int i12 = 20; i12 < i11; i12++) {
                    cArr[i12] = '0';
                }
                if (iFloorMod2 < 10) {
                    IOUtils.getChars(iFloorMod2, i10, cArr);
                } else if (iFloorMod2 % 100 == 0) {
                    IOUtils.getChars(iFloorMod2 / 100, i10, cArr);
                } else if (iFloorMod2 % 10 == 0) {
                    IOUtils.getChars(iFloorMod2 / 10, i10, cArr);
                } else {
                    IOUtils.getChars(iFloorMod2, i10, cArr);
                }
            }
            if (z) {
                int i13 = shanghaiZoneOffsetTotalSeconds / 3600;
                if (shanghaiZoneOffsetTotalSeconds == 0) {
                    cArr[i10] = 'Z';
                } else {
                    int iAbs = Math.abs(i13);
                    if (i13 >= 0) {
                        cArr[i10] = '+';
                    } else {
                        cArr[i10] = '-';
                    }
                    cArr[i + 20] = '0';
                    int i14 = i + 22;
                    IOUtils.getChars(iAbs, i14, cArr);
                    cArr[i14] = ':';
                    cArr[i + 23] = '0';
                    int i15 = (shanghaiZoneOffsetTotalSeconds - (i13 * 3600)) / 60;
                    if (i15 < 0) {
                        i15 = -i15;
                    }
                    IOUtils.getChars(i15, i11, cArr);
                }
            }
            return JDKUtils.STRING_CREATOR_JDK8.apply(cArr, Boolean.TRUE);
        }
        byte[] bArr = new byte[i11];
        IOUtils.writeLocalDate(bArr, 0, i6, i4, i5);
        bArr[10] = 32;
        IOUtils.writeLocalTime(bArr, 11, i7, i8, i9);
        if (i > 0) {
            bArr[19] = 46;
            for (int i16 = 20; i16 < i11; i16++) {
                bArr[i16] = JSONB.Constants.BC_INT32_BYTE_MIN;
            }
            if (iFloorMod2 < 10) {
                IOUtils.getChars(iFloorMod2, i10, bArr);
            } else if (iFloorMod2 % 100 == 0) {
                IOUtils.getChars(iFloorMod2 / 100, i10, bArr);
            } else if (iFloorMod2 % 10 == 0) {
                IOUtils.getChars(iFloorMod2 / 10, i10, bArr);
            } else {
                IOUtils.getChars(iFloorMod2, i10, bArr);
            }
        }
        if (z) {
            int i17 = shanghaiZoneOffsetTotalSeconds / 3600;
            if (shanghaiZoneOffsetTotalSeconds == 0) {
                bArr[i10] = 90;
            } else {
                int iAbs2 = Math.abs(i17);
                if (i17 >= 0) {
                    bArr[i10] = 43;
                } else {
                    bArr[i10] = 45;
                }
                bArr[i + 20] = JSONB.Constants.BC_INT32_BYTE_MIN;
                int i18 = i + 22;
                IOUtils.getChars(iAbs2, i18, bArr);
                bArr[i18] = 58;
                bArr[i + 23] = JSONB.Constants.BC_INT32_BYTE_MIN;
                int i19 = (shanghaiZoneOffsetTotalSeconds - (i17 * 3600)) / 60;
                if (i19 < 0) {
                    i19 = -i19;
                }
                IOUtils.getChars(i19, i11, bArr);
            }
        }
        if (JDKUtils.STRING_CREATOR_JDK11 != null) {
            return JDKUtils.STRING_CREATOR_JDK11.apply(bArr, JDKUtils.LATIN1);
        }
        return new String(bArr, 0, i11, StandardCharsets.ISO_8859_1);
    }

    private static int month(byte b, byte b2, byte b3) {
        return month((char) b, (char) b2, (char) b3);
    }

    public enum DateTimeFormatPattern {
        DATE_FORMAT_10_DASH("yyyy-MM-dd", 10),
        DATE_FORMAT_10_SLASH("yyyy/MM/dd", 10),
        DATE_FORMAT_10_DOT("dd.MM.yyyy", 10),
        DATE_TIME_FORMAT_19_DASH("yyyy-MM-dd HH:mm:ss", 19),
        DATE_TIME_FORMAT_19_DASH_T("yyyy-MM-dd'T'HH:mm:ss", 19),
        DATE_TIME_FORMAT_19_SLASH("yyyy/MM/dd HH:mm:ss", 19),
        DATE_TIME_FORMAT_19_DOT("dd.MM.yyyy HH:mm:ss", 19);

        public final int length;
        public final String pattern;

        DateTimeFormatPattern(String str, int i) {
            this.pattern = str;
            this.length = i;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x0094, code lost:
    
        r15 = 29;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean isLocalDate(java.lang.String r15) {
        /*
            r0 = 0
            if (r15 == 0) goto Lb1
            boolean r1 = r15.isEmpty()
            if (r1 == 0) goto Lb
            goto Lb1
        Lb:
            int r1 = r15.length()
            r2 = 9
            r3 = 1
            r4 = 10
            if (r1 != r4) goto L9b
            r1 = 4
            char r5 = r15.charAt(r1)
            r6 = 45
            if (r5 != r6) goto L9b
            r5 = 7
            char r5 = r15.charAt(r5)
            if (r5 != r6) goto L9b
            char r5 = r15.charAt(r0)
            char r6 = r15.charAt(r3)
            r7 = 2
            char r8 = r15.charAt(r7)
            r9 = 3
            char r10 = r15.charAt(r9)
            r11 = 5
            char r11 = r15.charAt(r11)
            r12 = 6
            char r13 = r15.charAt(r12)
            r14 = 8
            char r14 = r15.charAt(r14)
            char r15 = r15.charAt(r2)
            int r5 = r5 + (-48)
            int r5 = r5 * 1000
            int r6 = r6 + (-48)
            int r6 = r6 * 100
            int r5 = r5 + r6
            int r8 = r8 + (-48)
            int r8 = r8 * r4
            int r5 = r5 + r8
            int r10 = r10 + (-48)
            int r5 = r5 + r10
            int r11 = r11 + (-48)
            int r11 = r11 * r4
            int r13 = r13 + (-48)
            int r11 = r11 + r13
            int r14 = r14 + (-48)
            int r14 = r14 * r4
            int r15 = r15 + (-48)
            int r14 = r14 + r15
            r15 = 12
            if (r11 <= r15) goto L6d
            return r0
        L6d:
            r15 = 28
            if (r14 <= r15) goto L9a
            if (r11 == r7) goto L83
            if (r11 == r1) goto L80
            if (r11 == r12) goto L80
            if (r11 == r2) goto L80
            r15 = 11
            if (r11 == r15) goto L80
            r15 = 31
            goto L96
        L80:
            r15 = 30
            goto L96
        L83:
            r1 = r5 & 15
            if (r1 != 0) goto L8c
            r1 = r5 & 3
            if (r1 != 0) goto L96
            goto L94
        L8c:
            r1 = r5 & 3
            if (r1 != 0) goto L96
            int r5 = r5 % 100
            if (r5 == 0) goto L96
        L94:
            r15 = 29
        L96:
            if (r14 > r15) goto L99
            return r3
        L99:
            return r0
        L9a:
            return r3
        L9b:
            int r1 = r15.length()
            if (r1 < r2) goto Lb1
            int r1 = r15.length()
            r2 = 40
            if (r1 <= r2) goto Laa
            goto Lb1
        Laa:
            java.time.LocalDate r15 = parseLocalDate(r15)     // Catch: java.lang.Throwable -> Lb1
            if (r15 == 0) goto Lb1
            return r3
        Lb1:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.DateUtils.isLocalDate(java.lang.String):boolean");
    }

    /* JADX WARN: Code restructure failed: missing block: B:78:0x012f, code lost:
    
        r0 = 29;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean isDate(java.lang.String r20) {
        /*
            Method dump skipped, instruction units count: 345
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.DateUtils.isDate(java.lang.String):boolean");
    }

    public static boolean isLocalTime(String str) {
        if (str != null && !str.isEmpty()) {
            if (str.length() == 8 && str.charAt(2) == ':' && str.charAt(5) == ':') {
                char cCharAt = str.charAt(0);
                char cCharAt2 = str.charAt(1);
                char cCharAt3 = str.charAt(3);
                char cCharAt4 = str.charAt(4);
                char cCharAt5 = str.charAt(6);
                char cCharAt6 = str.charAt(7);
                return cCharAt >= '0' && cCharAt <= '2' && cCharAt2 >= '0' && cCharAt2 <= '9' && cCharAt3 >= '0' && cCharAt3 <= '6' && cCharAt4 >= '0' && cCharAt4 <= '9' && cCharAt5 >= '0' && cCharAt5 <= '6' && cCharAt6 >= '0' && cCharAt6 <= '9' && ((cCharAt - '0') * 10) + (cCharAt2 - '0') <= 24 && ((cCharAt3 - '0') * 10) + (cCharAt4 - '0') <= 60 && ((cCharAt5 - '0') * 10) + (cCharAt6 - '0') <= 61;
            }
            try {
                LocalTime.parse(str);
                return true;
            } catch (DateTimeParseException unused) {
            }
        }
        return false;
    }

    public static int readNanos(char[] cArr, int i, int i2) {
        int i3 = 0;
        for (int i4 = 0; i4 < i; i4++) {
            int i5 = cArr[i2 + i4] - '0';
            if ((i5 < 0) || (i5 > 9)) {
                return -1;
            }
            i3 = (i3 * 10) + i5;
        }
        return i3 * POWERS[(9 - i) & 15];
    }

    public static int readNanos(byte[] bArr, int i, int i2) {
        int i3 = 0;
        for (int i4 = 0; i4 < i; i4++) {
            int i5 = bArr[i2 + i4] + JSONB.Constants.BC_INT64_BYTE_ZERO;
            if ((i5 < 0) || (i5 > 9)) {
                return -1;
            }
            i3 = (i3 * 10) + i5;
        }
        return i3 * POWERS[(9 - i) & 15];
    }

    public static ZoneOffset zoneOffset(byte[] bArr, int i, int i2) {
        return ZoneOffset.of(new String(bArr, i, i2));
    }

    public static ZoneOffset zoneOffset(char[] cArr, int i, int i2) {
        return ZoneOffset.of(new String(cArr, i, i2));
    }

    public static int nanos(int i, int i2) {
        return i * POWERS[(9 - i2) & 15];
    }

    public static long hms(byte[] bArr, int i) {
        long jReverseBytes = JDKUtils.UNSAFE.getLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i));
        if (JDKUtils.BIG_ENDIAN) {
            jReverseBytes = Long.reverseBytes(jReverseBytes);
        }
        if (((((-1085102592571150096L) & jReverseBytes) - 3472328296227680304L) | (((1085102592571150095L & jReverseBytes) + 434034439958300166L) & (-1085366475377544976L))) != 0 || (16492675399680L & jReverseBytes) != 10995116933120L) {
            return -1L;
        }
        long j = 4222124902318095L & jReverseBytes;
        return (j << 3) + (j << 1) + ((jReverseBytes & 1080863974993432320L) >> 8);
    }

    public static long ymd(byte[] bArr, int i) {
        long jReverseBytes = JDKUtils.UNSAFE.getLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i));
        if (JDKUtils.BIG_ENDIAN) {
            jReverseBytes = Long.reverseBytes(jReverseBytes);
        }
        if ((280375481794560L & jReverseBytes) != 49478026199040L) {
            return -1L;
        }
        if ((((-1085366475377544976L) & ((1085086099895750415L & jReverseBytes) + 434034439958300166L)) | ((jReverseBytes & (-1085366475377544976L)) - 3472275519666401328L)) != 0) {
            return -1L;
        }
        long j = 4222124902318095L & jReverseBytes;
        return (j << 3) + (j << 1) + ((jReverseBytes & 1080863974993432320L) >> 8);
    }

    public static int yy(byte[] bArr, int i) {
        short sReverseBytes = JDKUtils.UNSAFE.getShort(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i));
        if (JDKUtils.BIG_ENDIAN) {
            sReverseBytes = Short.reverseBytes(sReverseBytes);
        }
        int i2 = sReverseBytes & 3855;
        if (((61680 & (i2 + 1542)) | ((sReverseBytes & 61680) - 12336)) != 0) {
            return -1;
        }
        return ((sReverseBytes & 15) * 1000) + ((i2 >> 8) * 100);
    }
}
