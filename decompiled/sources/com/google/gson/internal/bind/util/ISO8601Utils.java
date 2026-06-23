package com.google.gson.internal.bind.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes.dex */
public class ISO8601Utils {
    private static final String UTC_ID = "UTC";
    private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone(UTC_ID);

    public static String format(Date date) {
        return format(date, false, TIMEZONE_UTC);
    }

    public static String format(Date date, boolean z) {
        return format(date, z, TIMEZONE_UTC);
    }

    public static String format(Date date, boolean z, TimeZone timeZone) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(timeZone, Locale.US);
        gregorianCalendar.setTime(date);
        StringBuilder sb = new StringBuilder("yyyy-MM-ddThh:mm:ss".length() + (z ? ".sss".length() : 0) + (timeZone.getRawOffset() == 0 ? "Z" : "+hh:mm").length());
        padInt(sb, gregorianCalendar.get(1), "yyyy".length());
        sb.append('-');
        padInt(sb, gregorianCalendar.get(2) + 1, "MM".length());
        sb.append('-');
        padInt(sb, gregorianCalendar.get(5), "dd".length());
        sb.append('T');
        padInt(sb, gregorianCalendar.get(11), "hh".length());
        sb.append(':');
        padInt(sb, gregorianCalendar.get(12), "mm".length());
        sb.append(':');
        padInt(sb, gregorianCalendar.get(13), "ss".length());
        if (z) {
            sb.append('.');
            padInt(sb, gregorianCalendar.get(14), "sss".length());
        }
        int offset = timeZone.getOffset(gregorianCalendar.getTimeInMillis());
        if (offset != 0) {
            int i = offset / 60000;
            int iAbs = Math.abs(i / 60);
            int iAbs2 = Math.abs(i % 60);
            sb.append(offset >= 0 ? '+' : '-');
            padInt(sb, iAbs, "hh".length());
            sb.append(':');
            padInt(sb, iAbs2, "mm".length());
        } else {
            sb.append('Z');
        }
        return sb.toString();
    }

    public static Date parse(String str, ParsePosition parsePosition) throws ParseException {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int length;
        TimeZone timeZone;
        char cCharAt;
        try {
            int index = parsePosition.getIndex();
            int i6 = index + 4;
            int i7 = parseInt(str, index, i6);
            if (checkOffset(str, i6, '-')) {
                i6 = index + 5;
            }
            int i8 = i6 + 2;
            int i9 = parseInt(str, i6, i8);
            if (checkOffset(str, i8, '-')) {
                i8 = i6 + 3;
            }
            int i10 = i8 + 2;
            int i11 = parseInt(str, i8, i10);
            boolean zCheckOffset = checkOffset(str, i10, 'T');
            if (!zCheckOffset && str.length() <= i10) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar(i7, i9 - 1, i11);
                parsePosition.setIndex(i10);
                return gregorianCalendar.getTime();
            }
            if (zCheckOffset) {
                int i12 = i8 + 5;
                int i13 = parseInt(str, i8 + 3, i12);
                if (checkOffset(str, i12, ':')) {
                    i12 = i8 + 6;
                }
                int i14 = i12 + 2;
                int i15 = parseInt(str, i12, i14);
                if (checkOffset(str, i14, ':')) {
                    i14 = i12 + 3;
                }
                if (str.length() <= i14 || (cCharAt = str.charAt(i14)) == 'Z' || cCharAt == '+' || cCharAt == '-') {
                    i10 = i14;
                    i2 = i13;
                    i3 = i15;
                    i4 = 0;
                    i5 = 0;
                } else {
                    int i16 = i14 + 2;
                    i5 = parseInt(str, i14, i16);
                    if (i5 > 59 && i5 < 63) {
                        i5 = 59;
                    }
                    if (checkOffset(str, i16, '.')) {
                        int i17 = i14 + 3;
                        int iIndexOfNonDigit = indexOfNonDigit(str, i14 + 4);
                        int iMin = Math.min(iIndexOfNonDigit, i14 + 6);
                        int i18 = parseInt(str, i17, iMin);
                        int i19 = iMin - i17;
                        if (i19 == 1) {
                            i18 *= 100;
                        } else if (i19 == 2) {
                            i18 *= 10;
                        }
                        i2 = i13;
                        i10 = iIndexOfNonDigit;
                        i3 = i15;
                        i4 = i18;
                    } else {
                        i2 = i13;
                        i10 = i16;
                        i3 = i15;
                        i4 = 0;
                    }
                }
                i = 1;
            } else {
                i = 1;
                i2 = 0;
                i3 = 0;
                i4 = 0;
                i5 = 0;
            }
            if (str.length() <= i10) {
                throw new IllegalArgumentException("No time zone indicator");
            }
            char cCharAt2 = str.charAt(i10);
            if (cCharAt2 == 'Z') {
                timeZone = TIMEZONE_UTC;
                length = i10 + 1;
            } else {
                if (cCharAt2 != '+' && cCharAt2 != '-') {
                    throw new IndexOutOfBoundsException("Invalid time zone indicator '" + cCharAt2 + "'");
                }
                String strSubstring = str.substring(i10);
                if (strSubstring.length() < 5) {
                    strSubstring = strSubstring + "00";
                }
                length = i10 + strSubstring.length();
                if ("+0000".equals(strSubstring) || "+00:00".equals(strSubstring)) {
                    timeZone = TIMEZONE_UTC;
                } else {
                    String str2 = "GMT" + strSubstring;
                    TimeZone timeZone2 = TimeZone.getTimeZone(str2);
                    String id = timeZone2.getID();
                    if (!id.equals(str2) && !id.replace(":", "").equals(str2)) {
                        throw new IndexOutOfBoundsException("Mismatching time zone indicator: " + str2 + " given, resolves to " + timeZone2.getID());
                    }
                    timeZone = timeZone2;
                }
            }
            GregorianCalendar gregorianCalendar2 = new GregorianCalendar(timeZone);
            gregorianCalendar2.setLenient(false);
            int i20 = i;
            gregorianCalendar2.set(i20, i7);
            gregorianCalendar2.set(2, i9 - i20);
            gregorianCalendar2.set(5, i11);
            gregorianCalendar2.set(11, i2);
            gregorianCalendar2.set(12, i3);
            gregorianCalendar2.set(13, i5);
            gregorianCalendar2.set(14, i4);
            parsePosition.setIndex(length);
            return gregorianCalendar2.getTime();
        } catch (IllegalArgumentException | IndexOutOfBoundsException | NumberFormatException e) {
            String str3 = str == null ? null : "\"" + str + '\"';
            String message = e.getMessage();
            if (message == null || message.isEmpty()) {
                message = "(" + e.getClass().getName() + ")";
            }
            ParseException parseException = new ParseException("Failed to parse date [" + str3 + "]: " + message, parsePosition.getIndex());
            parseException.initCause(e);
            throw parseException;
        }
    }

    private static boolean checkOffset(String str, int i, char c) {
        return i < str.length() && str.charAt(i) == c;
    }

    private static int parseInt(String str, int i, int i2) throws NumberFormatException {
        int i3;
        int i4;
        if (i < 0 || i2 > str.length() || i > i2) {
            throw new NumberFormatException(str);
        }
        if (i < i2) {
            i4 = i + 1;
            int iDigit = Character.digit(str.charAt(i), 10);
            if (iDigit < 0) {
                throw new NumberFormatException("Invalid number: " + str.substring(i, i2));
            }
            i3 = -iDigit;
        } else {
            i3 = 0;
            i4 = i;
        }
        while (i4 < i2) {
            int i5 = i4 + 1;
            int iDigit2 = Character.digit(str.charAt(i4), 10);
            if (iDigit2 < 0) {
                throw new NumberFormatException("Invalid number: " + str.substring(i, i2));
            }
            i3 = (i3 * 10) - iDigit2;
            i4 = i5;
        }
        return -i3;
    }

    private static void padInt(StringBuilder sb, int i, int i2) {
        String string = Integer.toString(i);
        for (int length = i2 - string.length(); length > 0; length--) {
            sb.append('0');
        }
        sb.append(string);
    }

    private static int indexOfNonDigit(String str, int i) {
        while (i < str.length()) {
            char cCharAt = str.charAt(i);
            if (cCharAt < '0' || cCharAt > '9') {
                return i;
            }
            i++;
        }
        return str.length();
    }
}
