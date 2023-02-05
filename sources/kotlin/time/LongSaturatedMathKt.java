package kotlin.time;

import kotlin.Metadata;
import kotlin.jvm.internal.LongCompanionObject;
import kotlin.time.Duration;

@Metadata(mo10496d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0001H\u0002ø\u0001\u0000¢\u0006\u0004\b\u0006\u0010\u0007\u001a\"\u0010\b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0000ø\u0001\u0000¢\u0006\u0004\b\t\u0010\n\u001a\"\u0010\u000b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0002ø\u0001\u0000¢\u0006\u0004\b\f\u0010\n\u001a \u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0001H\u0000ø\u0001\u0000¢\u0006\u0002\u0010\n\u0002\u0004\n\u0002\b\u0019¨\u0006\u0010"}, mo10497d2 = {"checkInfiniteSumDefined", "", "longNs", "duration", "Lkotlin/time/Duration;", "durationNs", "checkInfiniteSumDefined-PjuGub4", "(JJJ)J", "saturatingAdd", "saturatingAdd-pTJri5U", "(JJ)J", "saturatingAddInHalves", "saturatingAddInHalves-pTJri5U", "saturatingDiff", "valueNs", "originNs", "kotlin-stdlib"}, mo10498k = 2, mo10499mv = {1, 7, 1}, mo10501xi = 48)
/* compiled from: longSaturatedMath.kt */
public final class LongSaturatedMathKt {
    /* renamed from: saturatingAdd-pTJri5U  reason: not valid java name */
    public static final long m1490saturatingAddpTJri5U(long longNs, long duration) {
        long durationNs = Duration.m1382getInWholeNanosecondsimpl(duration);
        if (((longNs - 1) | 1) == LongCompanionObject.MAX_VALUE) {
            return m1489checkInfiniteSumDefinedPjuGub4(longNs, duration, durationNs);
        }
        if ((1 | (durationNs - 1)) == LongCompanionObject.MAX_VALUE) {
            return m1491saturatingAddInHalvespTJri5U(longNs, duration);
        }
        long result = longNs + durationNs;
        if (((longNs ^ result) & (durationNs ^ result)) >= 0) {
            return result;
        }
        if (longNs < 0) {
            return Long.MIN_VALUE;
        }
        return LongCompanionObject.MAX_VALUE;
    }

    /* renamed from: checkInfiniteSumDefined-PjuGub4  reason: not valid java name */
    private static final long m1489checkInfiniteSumDefinedPjuGub4(long longNs, long duration, long durationNs) {
        if (!Duration.m1394isInfiniteimpl(duration) || (longNs ^ durationNs) >= 0) {
            return longNs;
        }
        throw new IllegalArgumentException("Summing infinities of different signs");
    }

    /* renamed from: saturatingAddInHalves-pTJri5U  reason: not valid java name */
    private static final long m1491saturatingAddInHalvespTJri5U(long longNs, long duration) {
        long half = Duration.m1365divUwyO8pc(duration, 2);
        if (((Duration.m1382getInWholeNanosecondsimpl(half) - 1) | 1) == LongCompanionObject.MAX_VALUE) {
            return (long) (((double) longNs) + Duration.m1405toDoubleimpl(duration, DurationUnit.NANOSECONDS));
        }
        return m1490saturatingAddpTJri5U(m1490saturatingAddpTJri5U(longNs, half), half);
    }

    public static final long saturatingDiff(long valueNs, long originNs) {
        if ((1 | (originNs - 1)) == LongCompanionObject.MAX_VALUE) {
            return Duration.m1414unaryMinusUwyO8pc(DurationKt.toDuration(originNs, DurationUnit.DAYS));
        }
        long result = valueNs - originNs;
        if (((result ^ valueNs) & (~(result ^ originNs))) < 0) {
            long j = (long) DurationKt.NANOS_IN_MILLIS;
            long resultMs = (valueNs / j) - (originNs / j);
            long resultNs = (valueNs % j) - (originNs % j);
            Duration.Companion companion = Duration.Companion;
            long duration = DurationKt.toDuration(resultMs, DurationUnit.MILLISECONDS);
            Duration.Companion companion2 = Duration.Companion;
            return Duration.m1398plusLRDsOJo(duration, DurationKt.toDuration(resultNs, DurationUnit.NANOSECONDS));
        }
        Duration.Companion companion3 = Duration.Companion;
        return DurationKt.toDuration(result, DurationUnit.NANOSECONDS);
    }
}
