package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpStatus;

/* JADX INFO: loaded from: classes.dex */
public class TypefaceCompatApi29Impl extends TypefaceCompatBaseImpl {
    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    protected FontsContractCompat.FontInfo findBestInfo(FontsContractCompat.FontInfo[] fontInfoArr, int i) {
        throw new RuntimeException("Do not use this function in API 29 or later.");
    }

    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    protected Typeface createFromInputStream(Context context, InputStream inputStream) {
        throw new RuntimeException("Do not use this function in API 29 or later.");
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x001b A[Catch: IOException -> 0x005b, Exception -> 0x0086, PHI: r4
      0x001b: PHI (r4v5 android.graphics.fonts.FontFamily$Builder) = (r4v3 android.graphics.fonts.FontFamily$Builder), (r4v1 android.graphics.fonts.FontFamily$Builder) binds: [B:15:0x004c, B:8:0x0019] A[DONT_GENERATE, DONT_INLINE], TRY_LEAVE, TryCatch #1 {Exception -> 0x0086, blocks: (B:3:0x0005, B:5:0x000b, B:6:0x000d, B:9:0x001b, B:23:0x005a, B:22:0x0057, B:27:0x0061, B:31:0x006c, B:34:0x0071), top: B:40:0x0005 }] */
    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.graphics.Typeface createFromFontInfo(android.content.Context r10, android.os.CancellationSignal r11, androidx.core.provider.FontsContractCompat.FontInfo[] r12, int r13) {
        /*
            r9 = this;
            android.content.ContentResolver r10 = r10.getContentResolver()
            r0 = 0
            int r1 = r12.length     // Catch: java.lang.Exception -> L86
            r2 = 0
            r4 = r0
            r3 = r2
        L9:
            if (r3 >= r1) goto L5e
            r5 = r12[r3]     // Catch: java.lang.Exception -> L86
            android.net.Uri r6 = r5.getUri()     // Catch: java.io.IOException -> L5b java.lang.Exception -> L86
            java.lang.String r7 = "r"
            android.os.ParcelFileDescriptor r6 = r10.openFileDescriptor(r6, r7, r11)     // Catch: java.io.IOException -> L5b java.lang.Exception -> L86
            if (r6 != 0) goto L1f
            if (r6 == 0) goto L5b
        L1b:
            r6.close()     // Catch: java.io.IOException -> L5b java.lang.Exception -> L86
            goto L5b
        L1f:
            android.graphics.fonts.Font$Builder r7 = new android.graphics.fonts.Font$Builder     // Catch: java.lang.Throwable -> L4f
            r7.<init>(r6)     // Catch: java.lang.Throwable -> L4f
            int r8 = r5.getWeight()     // Catch: java.lang.Throwable -> L4f
            android.graphics.fonts.Font$Builder r7 = r7.setWeight(r8)     // Catch: java.lang.Throwable -> L4f
            boolean r8 = r5.isItalic()     // Catch: java.lang.Throwable -> L4f
            android.graphics.fonts.Font$Builder r7 = r7.setSlant(r8)     // Catch: java.lang.Throwable -> L4f
            int r5 = r5.getTtcIndex()     // Catch: java.lang.Throwable -> L4f
            android.graphics.fonts.Font$Builder r5 = r7.setTtcIndex(r5)     // Catch: java.lang.Throwable -> L4f
            android.graphics.fonts.Font r5 = r5.build()     // Catch: java.lang.Throwable -> L4f
            if (r4 != 0) goto L49
            android.graphics.fonts.FontFamily$Builder r7 = new android.graphics.fonts.FontFamily$Builder     // Catch: java.lang.Throwable -> L4f
            r7.<init>(r5)     // Catch: java.lang.Throwable -> L4f
            r4 = r7
            goto L4c
        L49:
            r4.addFont(r5)     // Catch: java.lang.Throwable -> L4f
        L4c:
            if (r6 == 0) goto L5b
            goto L1b
        L4f:
            r5 = move-exception
            if (r6 == 0) goto L5a
            r6.close()     // Catch: java.lang.Throwable -> L56
            goto L5a
        L56:
            r6 = move-exception
            r5.addSuppressed(r6)     // Catch: java.io.IOException -> L5b java.lang.Exception -> L86
        L5a:
            throw r5     // Catch: java.io.IOException -> L5b java.lang.Exception -> L86
        L5b:
            int r3 = r3 + 1
            goto L9
        L5e:
            if (r4 != 0) goto L61
            return r0
        L61:
            android.graphics.fonts.FontStyle r10 = new android.graphics.fonts.FontStyle     // Catch: java.lang.Exception -> L86
            r11 = r13 & 1
            if (r11 == 0) goto L6a
            r11 = 700(0x2bc, float:9.81E-43)
            goto L6c
        L6a:
            r11 = 400(0x190, float:5.6E-43)
        L6c:
            r12 = r13 & 2
            if (r12 == 0) goto L71
            r2 = 1
        L71:
            r10.<init>(r11, r2)     // Catch: java.lang.Exception -> L86
            android.graphics.Typeface$CustomFallbackBuilder r11 = new android.graphics.Typeface$CustomFallbackBuilder     // Catch: java.lang.Exception -> L86
            android.graphics.fonts.FontFamily r12 = r4.build()     // Catch: java.lang.Exception -> L86
            r11.<init>(r12)     // Catch: java.lang.Exception -> L86
            android.graphics.Typeface$CustomFallbackBuilder r10 = r11.setStyle(r10)     // Catch: java.lang.Exception -> L86
            android.graphics.Typeface r10 = r10.build()     // Catch: java.lang.Exception -> L86
            return r10
        L86:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.graphics.TypefaceCompatApi29Impl.createFromFontInfo(android.content.Context, android.os.CancellationSignal, androidx.core.provider.FontsContractCompat$FontInfo[], int):android.graphics.Typeface");
    }

    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, Resources resources, int i) {
        try {
            FontFamily.Builder builder = null;
            for (FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry : fontFamilyFilesResourceEntry.getEntries()) {
                try {
                    Font fontBuild = new Font.Builder(resources, fontFileResourceEntry.getResourceId()).setWeight(fontFileResourceEntry.getWeight()).setSlant(fontFileResourceEntry.isItalic() ? 1 : 0).setTtcIndex(fontFileResourceEntry.getTtcIndex()).setFontVariationSettings(fontFileResourceEntry.getVariationSettings()).build();
                    if (builder == null) {
                        builder = new FontFamily.Builder(fontBuild);
                    } else {
                        builder.addFont(fontBuild);
                    }
                } catch (IOException unused) {
                }
            }
            if (builder == null) {
                return null;
            }
            return new Typeface.CustomFallbackBuilder(builder.build()).setStyle(new FontStyle((i & 1) != 0 ? 700 : HttpStatus.SC_BAD_REQUEST, (i & 2) != 0 ? 1 : 0)).build();
        } catch (Exception unused2) {
            return null;
        }
    }

    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    public Typeface createFromResourcesFontFile(Context context, Resources resources, int i, String str, int i2) {
        try {
            Font fontBuild = new Font.Builder(resources, i).build();
            return new Typeface.CustomFallbackBuilder(new FontFamily.Builder(fontBuild).build()).setStyle(fontBuild.getStyle()).build();
        } catch (Exception unused) {
            return null;
        }
    }
}
