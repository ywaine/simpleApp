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

public class TypefaceCompatApi29Impl extends TypefaceCompatBaseImpl {
    /* access modifiers changed from: protected */
    public FontsContractCompat.FontInfo findBestInfo(FontsContractCompat.FontInfo[] fonts, int style) {
        throw new RuntimeException("Do not use this function in API 29 or later.");
    }

    /* access modifiers changed from: protected */
    public Typeface createFromInputStream(Context context, InputStream is) {
        throw new RuntimeException("Do not use this function in API 29 or later.");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0058, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0059, code lost:
        if (r7 != null) goto L_0x005b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        r7.close();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.Typeface createFromFontInfo(android.content.Context r11, android.os.CancellationSignal r12, androidx.core.provider.FontsContractCompat.FontInfo[] r13, int r14) {
        /*
            r10 = this;
            r0 = 0
            android.content.ContentResolver r1 = r11.getContentResolver()
            int r2 = r13.length
            r3 = 0
            r4 = 0
        L_0x0008:
            r5 = 1
            if (r4 >= r2) goto L_0x0068
            r6 = r13[r4]
            android.net.Uri r7 = r6.getUri()     // Catch:{ IOException -> 0x0064 }
            java.lang.String r8 = "r"
            android.os.ParcelFileDescriptor r7 = r1.openFileDescriptor(r7, r8, r12)     // Catch:{ IOException -> 0x0064 }
            if (r7 != 0) goto L_0x001f
            if (r7 == 0) goto L_0x0065
            r7.close()     // Catch:{ IOException -> 0x0064 }
            goto L_0x0065
        L_0x001f:
            android.graphics.fonts.Font$Builder r8 = new android.graphics.fonts.Font$Builder     // Catch:{ all -> 0x0056 }
            r8.<init>(r7)     // Catch:{ all -> 0x0056 }
            int r9 = r6.getWeight()     // Catch:{ all -> 0x0056 }
            android.graphics.fonts.Font$Builder r8 = r8.setWeight(r9)     // Catch:{ all -> 0x0056 }
            boolean r9 = r6.isItalic()     // Catch:{ all -> 0x0056 }
            if (r9 == 0) goto L_0x0033
            goto L_0x0034
        L_0x0033:
            r5 = 0
        L_0x0034:
            android.graphics.fonts.Font$Builder r5 = r8.setSlant(r5)     // Catch:{ all -> 0x0056 }
            int r8 = r6.getTtcIndex()     // Catch:{ all -> 0x0056 }
            android.graphics.fonts.Font$Builder r5 = r5.setTtcIndex(r8)     // Catch:{ all -> 0x0056 }
            android.graphics.fonts.Font r5 = r5.build()     // Catch:{ all -> 0x0056 }
            if (r0 != 0) goto L_0x004d
            android.graphics.fonts.FontFamily$Builder r8 = new android.graphics.fonts.FontFamily$Builder     // Catch:{ all -> 0x0056 }
            r8.<init>(r5)     // Catch:{ all -> 0x0056 }
            r0 = r8
            goto L_0x0050
        L_0x004d:
            r0.addFont(r5)     // Catch:{ all -> 0x0056 }
        L_0x0050:
            if (r7 == 0) goto L_0x0055
            r7.close()     // Catch:{ IOException -> 0x0064 }
        L_0x0055:
            goto L_0x0065
        L_0x0056:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x0058 }
        L_0x0058:
            r8 = move-exception
            if (r7 == 0) goto L_0x0063
            r7.close()     // Catch:{ all -> 0x005f }
            goto L_0x0063
        L_0x005f:
            r9 = move-exception
            r5.addSuppressed(r9)     // Catch:{ IOException -> 0x0064 }
        L_0x0063:
            throw r8     // Catch:{ IOException -> 0x0064 }
        L_0x0064:
            r5 = move-exception
        L_0x0065:
            int r4 = r4 + 1
            goto L_0x0008
        L_0x0068:
            if (r0 != 0) goto L_0x006c
            r2 = 0
            return r2
        L_0x006c:
            android.graphics.fonts.FontStyle r2 = new android.graphics.fonts.FontStyle
            r4 = r14 & 1
            if (r4 == 0) goto L_0x0075
            r4 = 700(0x2bc, float:9.81E-43)
            goto L_0x0077
        L_0x0075:
            r4 = 400(0x190, float:5.6E-43)
        L_0x0077:
            r6 = r14 & 2
            if (r6 == 0) goto L_0x007c
            r3 = 1
        L_0x007c:
            r2.<init>(r4, r3)
            android.graphics.Typeface$CustomFallbackBuilder r3 = new android.graphics.Typeface$CustomFallbackBuilder
            android.graphics.fonts.FontFamily r4 = r0.build()
            r3.<init>(r4)
            android.graphics.Typeface$CustomFallbackBuilder r3 = r3.setStyle(r2)
            android.graphics.Typeface r3 = r3.build()
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.graphics.TypefaceCompatApi29Impl.createFromFontInfo(android.content.Context, android.os.CancellationSignal, androidx.core.provider.FontsContractCompat$FontInfo[], int):android.graphics.Typeface");
    }

    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontResourcesParserCompat.FontFamilyFilesResourceEntry familyEntry, Resources resources, int style) {
        FontFamily.Builder familyBuilder = null;
        FontResourcesParserCompat.FontFileResourceEntry[] entries = familyEntry.getEntries();
        int length = entries.length;
        int i = 0;
        int i2 = 0;
        while (true) {
            int i3 = 1;
            if (i2 >= length) {
                break;
            }
            FontResourcesParserCompat.FontFileResourceEntry entry = entries[i2];
            try {
                Font.Builder weight = new Font.Builder(resources, entry.getResourceId()).setWeight(entry.getWeight());
                if (!entry.isItalic()) {
                    i3 = 0;
                }
                Font platformFont = weight.setSlant(i3).setTtcIndex(entry.getTtcIndex()).setFontVariationSettings(entry.getVariationSettings()).build();
                if (familyBuilder == null) {
                    familyBuilder = new FontFamily.Builder(platformFont);
                } else {
                    familyBuilder.addFont(platformFont);
                }
            } catch (IOException e) {
            }
            i2++;
        }
        if (familyBuilder == null) {
            return null;
        }
        int i4 = (style & 1) != 0 ? 700 : 400;
        if ((style & 2) != 0) {
            i = 1;
        }
        return new Typeface.CustomFallbackBuilder(familyBuilder.build()).setStyle(new FontStyle(i4, i)).build();
    }

    public Typeface createFromResourcesFontFile(Context context, Resources resources, int id, String path, int style) {
        try {
            return new Typeface.CustomFallbackBuilder(new FontFamily.Builder(new Font.Builder(resources, id).build()).build()).setStyle(new FontStyle((style & 1) != 0 ? 700 : 400, (style & 2) != 0 ? 1 : 0)).build();
        } catch (IOException e) {
            return null;
        }
    }
}
