package kotlin.collections;

import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UByteArray;
import kotlin.UIntArray;
import kotlin.ULongArray;
import kotlin.UShort;
import kotlin.UShortArray;
import kotlin.UnsignedKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: UArraySorting.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u00000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0010\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0006\u0010\u0007\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\t\u0010\n\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\f\u0010\r\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u000f\u0010\u0010\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0013\u0010\u0014\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0017\u0010\u0018\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0019\u0010\u001a\u001a*\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001ø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u0014\u001a*\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001ø\u0001\u0000¢\u0006\u0004\b\u001f\u0010\u0016\u001a*\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001ø\u0001\u0000¢\u0006\u0004\b \u0010\u0018\u001a*\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001ø\u0001\u0000¢\u0006\u0004\b!\u0010\u001a\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\""}, d2 = {"partition", "", "array", "Lkotlin/UByteArray;", "left", "right", "partition-4UcCI2c", "([BII)I", "Lkotlin/UIntArray;", "partition-oBK06Vg", "([III)I", "Lkotlin/ULongArray;", "partition--nroSd4", "([JII)I", "Lkotlin/UShortArray;", "partition-Aa5vz7o", "([SII)I", "quickSort", "", "quickSort-4UcCI2c", "([BII)V", "quickSort-oBK06Vg", "([III)V", "quickSort--nroSd4", "([JII)V", "quickSort-Aa5vz7o", "([SII)V", "sortArray", "fromIndex", "toIndex", "sortArray-4UcCI2c", "sortArray-oBK06Vg", "sortArray--nroSd4", "sortArray-Aa5vz7o", "kotlin-stdlib"}, k = 2, mv = {1, 6, 0}, xi = 48)
public final class UArraySortingKt {
    /* JADX INFO: renamed from: partition-4UcCI2c, reason: not valid java name */
    private static final int m775partition4UcCI2c(byte[] bArr, int i, int i2) {
        int i3;
        byte bM402getw2LRezQ = UByteArray.m402getw2LRezQ(bArr, (i + i2) / 2);
        while (i <= i2) {
            while (true) {
                int iM402getw2LRezQ = UByteArray.m402getw2LRezQ(bArr, i) & UByte.MAX_VALUE;
                i3 = bM402getw2LRezQ & UByte.MAX_VALUE;
                if (Intrinsics.compare(iM402getw2LRezQ, i3) >= 0) {
                    break;
                }
                i++;
            }
            while (Intrinsics.compare(UByteArray.m402getw2LRezQ(bArr, i2) & UByte.MAX_VALUE, i3) > 0) {
                i2--;
            }
            if (i <= i2) {
                byte bM402getw2LRezQ2 = UByteArray.m402getw2LRezQ(bArr, i);
                UByteArray.m407setVurrAj0(bArr, i, UByteArray.m402getw2LRezQ(bArr, i2));
                UByteArray.m407setVurrAj0(bArr, i2, bM402getw2LRezQ2);
                i++;
                i2--;
            }
        }
        return i;
    }

    /* JADX INFO: renamed from: quickSort-4UcCI2c, reason: not valid java name */
    private static final void m779quickSort4UcCI2c(byte[] bArr, int i, int i2) {
        int iM775partition4UcCI2c = m775partition4UcCI2c(bArr, i, i2);
        int i3 = iM775partition4UcCI2c - 1;
        if (i < i3) {
            m779quickSort4UcCI2c(bArr, i, i3);
        }
        if (iM775partition4UcCI2c < i2) {
            m779quickSort4UcCI2c(bArr, iM775partition4UcCI2c, i2);
        }
    }

    /* JADX INFO: renamed from: partition-Aa5vz7o, reason: not valid java name */
    private static final int m776partitionAa5vz7o(short[] sArr, int i, int i2) {
        int i3;
        short sM662getMh2AYeg = UShortArray.m662getMh2AYeg(sArr, (i + i2) / 2);
        while (i <= i2) {
            while (true) {
                int iM662getMh2AYeg = UShortArray.m662getMh2AYeg(sArr, i) & UShort.MAX_VALUE;
                i3 = sM662getMh2AYeg & UShort.MAX_VALUE;
                if (Intrinsics.compare(iM662getMh2AYeg, i3) >= 0) {
                    break;
                }
                i++;
            }
            while (Intrinsics.compare(UShortArray.m662getMh2AYeg(sArr, i2) & UShort.MAX_VALUE, i3) > 0) {
                i2--;
            }
            if (i <= i2) {
                short sM662getMh2AYeg2 = UShortArray.m662getMh2AYeg(sArr, i);
                UShortArray.m667set01HTLdE(sArr, i, UShortArray.m662getMh2AYeg(sArr, i2));
                UShortArray.m667set01HTLdE(sArr, i2, sM662getMh2AYeg2);
                i++;
                i2--;
            }
        }
        return i;
    }

    /* JADX INFO: renamed from: quickSort-Aa5vz7o, reason: not valid java name */
    private static final void m780quickSortAa5vz7o(short[] sArr, int i, int i2) {
        int iM776partitionAa5vz7o = m776partitionAa5vz7o(sArr, i, i2);
        int i3 = iM776partitionAa5vz7o - 1;
        if (i < i3) {
            m780quickSortAa5vz7o(sArr, i, i3);
        }
        if (iM776partitionAa5vz7o < i2) {
            m780quickSortAa5vz7o(sArr, iM776partitionAa5vz7o, i2);
        }
    }

    /* JADX INFO: renamed from: partition-oBK06Vg, reason: not valid java name */
    private static final int m777partitionoBK06Vg(int[] iArr, int i, int i2) {
        int iM480getpVg5ArA = UIntArray.m480getpVg5ArA(iArr, (i + i2) / 2);
        while (i <= i2) {
            while (UnsignedKt.uintCompare(UIntArray.m480getpVg5ArA(iArr, i), iM480getpVg5ArA) < 0) {
                i++;
            }
            while (UnsignedKt.uintCompare(UIntArray.m480getpVg5ArA(iArr, i2), iM480getpVg5ArA) > 0) {
                i2--;
            }
            if (i <= i2) {
                int iM480getpVg5ArA2 = UIntArray.m480getpVg5ArA(iArr, i);
                UIntArray.m485setVXSXFK8(iArr, i, UIntArray.m480getpVg5ArA(iArr, i2));
                UIntArray.m485setVXSXFK8(iArr, i2, iM480getpVg5ArA2);
                i++;
                i2--;
            }
        }
        return i;
    }

    /* JADX INFO: renamed from: quickSort-oBK06Vg, reason: not valid java name */
    private static final void m781quickSortoBK06Vg(int[] iArr, int i, int i2) {
        int iM777partitionoBK06Vg = m777partitionoBK06Vg(iArr, i, i2);
        int i3 = iM777partitionoBK06Vg - 1;
        if (i < i3) {
            m781quickSortoBK06Vg(iArr, i, i3);
        }
        if (iM777partitionoBK06Vg < i2) {
            m781quickSortoBK06Vg(iArr, iM777partitionoBK06Vg, i2);
        }
    }

    /* JADX INFO: renamed from: partition--nroSd4, reason: not valid java name */
    private static final int m774partitionnroSd4(long[] jArr, int i, int i2) {
        long jM558getsVKNKU = ULongArray.m558getsVKNKU(jArr, (i + i2) / 2);
        while (i <= i2) {
            while (UnsignedKt.ulongCompare(ULongArray.m558getsVKNKU(jArr, i), jM558getsVKNKU) < 0) {
                i++;
            }
            while (UnsignedKt.ulongCompare(ULongArray.m558getsVKNKU(jArr, i2), jM558getsVKNKU) > 0) {
                i2--;
            }
            if (i <= i2) {
                long jM558getsVKNKU2 = ULongArray.m558getsVKNKU(jArr, i);
                ULongArray.m563setk8EXiF4(jArr, i, ULongArray.m558getsVKNKU(jArr, i2));
                ULongArray.m563setk8EXiF4(jArr, i2, jM558getsVKNKU2);
                i++;
                i2--;
            }
        }
        return i;
    }

    /* JADX INFO: renamed from: quickSort--nroSd4, reason: not valid java name */
    private static final void m778quickSortnroSd4(long[] jArr, int i, int i2) {
        int iM774partitionnroSd4 = m774partitionnroSd4(jArr, i, i2);
        int i3 = iM774partitionnroSd4 - 1;
        if (i < i3) {
            m778quickSortnroSd4(jArr, i, i3);
        }
        if (iM774partitionnroSd4 < i2) {
            m778quickSortnroSd4(jArr, iM774partitionnroSd4, i2);
        }
    }

    /* JADX INFO: renamed from: sortArray-4UcCI2c, reason: not valid java name */
    public static final void m783sortArray4UcCI2c(byte[] array, int i, int i2) {
        Intrinsics.checkNotNullParameter(array, "array");
        m779quickSort4UcCI2c(array, i, i2 - 1);
    }

    /* JADX INFO: renamed from: sortArray-Aa5vz7o, reason: not valid java name */
    public static final void m784sortArrayAa5vz7o(short[] array, int i, int i2) {
        Intrinsics.checkNotNullParameter(array, "array");
        m780quickSortAa5vz7o(array, i, i2 - 1);
    }

    /* JADX INFO: renamed from: sortArray-oBK06Vg, reason: not valid java name */
    public static final void m785sortArrayoBK06Vg(int[] array, int i, int i2) {
        Intrinsics.checkNotNullParameter(array, "array");
        m781quickSortoBK06Vg(array, i, i2 - 1);
    }

    /* JADX INFO: renamed from: sortArray--nroSd4, reason: not valid java name */
    public static final void m782sortArraynroSd4(long[] array, int i, int i2) {
        Intrinsics.checkNotNullParameter(array, "array");
        m778quickSortnroSd4(array, i, i2 - 1);
    }
}
