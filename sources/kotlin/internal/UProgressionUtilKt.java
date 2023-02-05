package kotlin.internal;

import kotlin.Metadata;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UnsignedKt;

@Metadata(mo10496d1 = {"\u0000 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001H\u0002ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006\u001a*\u0010\u0000\u001a\u00020\u00072\u0006\u0010\u0002\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0007H\u0002ø\u0001\u0000¢\u0006\u0004\b\b\u0010\t\u001a*\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000eH\u0001ø\u0001\u0000¢\u0006\u0004\b\u000f\u0010\u0006\u001a*\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0010H\u0001ø\u0001\u0000¢\u0006\u0004\b\u0011\u0010\t\u0002\u0004\n\u0002\b\u0019¨\u0006\u0012"}, mo10497d2 = {"differenceModulo", "Lkotlin/UInt;", "a", "b", "c", "differenceModulo-WZ9TVnA", "(III)I", "Lkotlin/ULong;", "differenceModulo-sambcqE", "(JJJ)J", "getProgressionLastElement", "start", "end", "step", "", "getProgressionLastElement-Nkh28Cs", "", "getProgressionLastElement-7ftBX0g", "kotlin-stdlib"}, mo10498k = 2, mo10499mv = {1, 7, 1}, mo10501xi = 48)
/* compiled from: UProgressionUtil.kt */
public final class UProgressionUtilKt {
    /* renamed from: differenceModulo-WZ9TVnA  reason: not valid java name */
    private static final int m1257differenceModuloWZ9TVnA(int a, int b, int c) {
        int ac = UnsignedKt.m397uintRemainderJ1ME1BU(a, c);
        int bc = UnsignedKt.m397uintRemainderJ1ME1BU(b, c);
        return UInt.m143constructorimpl(UnsignedKt.uintCompare(ac, bc) >= 0 ? ac - bc : UInt.m143constructorimpl(ac - bc) + c);
    }

    /* renamed from: differenceModulo-sambcqE  reason: not valid java name */
    private static final long m1258differenceModulosambcqE(long a, long b, long c) {
        long ac = UnsignedKt.m399ulongRemaindereb3DHEI(a, c);
        long bc = UnsignedKt.m399ulongRemaindereb3DHEI(b, c);
        return ULong.m221constructorimpl(UnsignedKt.ulongCompare(ac, bc) >= 0 ? ac - bc : ULong.m221constructorimpl(ac - bc) + c);
    }

    /* renamed from: getProgressionLastElement-Nkh28Cs  reason: not valid java name */
    public static final int m1260getProgressionLastElementNkh28Cs(int start, int end, int step) {
        if (step > 0) {
            if (UnsignedKt.uintCompare(start, end) < 0) {
                return UInt.m143constructorimpl(end - m1257differenceModuloWZ9TVnA(end, start, UInt.m143constructorimpl(step)));
            }
        } else if (step >= 0) {
            throw new IllegalArgumentException("Step is zero.");
        } else if (UnsignedKt.uintCompare(start, end) > 0) {
            return UInt.m143constructorimpl(m1257differenceModuloWZ9TVnA(start, end, UInt.m143constructorimpl(-step)) + end);
        }
        return end;
    }

    /* renamed from: getProgressionLastElement-7ftBX0g  reason: not valid java name */
    public static final long m1259getProgressionLastElement7ftBX0g(long start, long end, long step) {
        if (step > 0) {
            if (UnsignedKt.ulongCompare(start, end) < 0) {
                return ULong.m221constructorimpl(end - m1258differenceModulosambcqE(end, start, ULong.m221constructorimpl(step)));
            }
        } else if (step >= 0) {
            throw new IllegalArgumentException("Step is zero.");
        } else if (UnsignedKt.ulongCompare(start, end) > 0) {
            return ULong.m221constructorimpl(m1258differenceModulosambcqE(start, end, ULong.m221constructorimpl(-step)) + end);
        }
        return end;
    }
}
