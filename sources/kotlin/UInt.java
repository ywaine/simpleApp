package kotlin;

import kotlin.jvm.JvmInline;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.ranges.UIntRange;

@JvmInline
@Metadata(mo10496d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b!\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\n\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u000e\b@\u0018\u0000 y2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001yB\u0014\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005J\u001b\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\fø\u0001\u0000¢\u0006\u0004\b\n\u0010\u000bJ\u001b\u0010\f\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\rH\nø\u0001\u0000¢\u0006\u0004\b\u000e\u0010\u000fJ\u001b\u0010\f\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0000H\nø\u0001\u0000¢\u0006\u0004\b\u0010\u0010\u000bJ\u001b\u0010\f\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0011H\nø\u0001\u0000¢\u0006\u0004\b\u0012\u0010\u0013J\u001b\u0010\f\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0014H\nø\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016J\u0016\u0010\u0017\u001a\u00020\u0000H\nø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b\u0018\u0010\u0005J\u001b\u0010\u0019\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\nø\u0001\u0000¢\u0006\u0004\b\u001a\u0010\u000fJ\u001b\u0010\u0019\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\nø\u0001\u0000¢\u0006\u0004\b\u001b\u0010\u000bJ\u001b\u0010\u0019\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\nø\u0001\u0000¢\u0006\u0004\b\u001c\u0010\u001dJ\u001b\u0010\u0019\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\nø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u0016J\u001a\u0010\u001f\u001a\u00020 2\b\u0010\t\u001a\u0004\u0018\u00010!HÖ\u0003¢\u0006\u0004\b\"\u0010#J\u001b\u0010$\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\bø\u0001\u0000¢\u0006\u0004\b%\u0010\u000fJ\u001b\u0010$\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\bø\u0001\u0000¢\u0006\u0004\b&\u0010\u000bJ\u001b\u0010$\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\bø\u0001\u0000¢\u0006\u0004\b'\u0010\u001dJ\u001b\u0010$\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\bø\u0001\u0000¢\u0006\u0004\b(\u0010\u0016J\u0010\u0010)\u001a\u00020\u0003HÖ\u0001¢\u0006\u0004\b*\u0010\u0005J\u0016\u0010+\u001a\u00020\u0000H\nø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b,\u0010\u0005J\u0016\u0010-\u001a\u00020\u0000H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b.\u0010\u0005J\u001b\u0010/\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\nø\u0001\u0000¢\u0006\u0004\b0\u0010\u000fJ\u001b\u0010/\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\nø\u0001\u0000¢\u0006\u0004\b1\u0010\u000bJ\u001b\u0010/\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\nø\u0001\u0000¢\u0006\u0004\b2\u0010\u001dJ\u001b\u0010/\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\nø\u0001\u0000¢\u0006\u0004\b3\u0010\u0016J\u001b\u00104\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\rH\bø\u0001\u0000¢\u0006\u0004\b5\u00106J\u001b\u00104\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\bø\u0001\u0000¢\u0006\u0004\b7\u0010\u000bJ\u001b\u00104\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\bø\u0001\u0000¢\u0006\u0004\b8\u0010\u001dJ\u001b\u00104\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\bø\u0001\u0000¢\u0006\u0004\b9\u0010:J\u001b\u0010;\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\fø\u0001\u0000¢\u0006\u0004\b<\u0010\u000bJ\u001b\u0010=\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\nø\u0001\u0000¢\u0006\u0004\b>\u0010\u000fJ\u001b\u0010=\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\nø\u0001\u0000¢\u0006\u0004\b?\u0010\u000bJ\u001b\u0010=\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\nø\u0001\u0000¢\u0006\u0004\b@\u0010\u001dJ\u001b\u0010=\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\nø\u0001\u0000¢\u0006\u0004\bA\u0010\u0016J\u001b\u0010B\u001a\u00020C2\u0006\u0010\t\u001a\u00020\u0000H\nø\u0001\u0000¢\u0006\u0004\bD\u0010EJ\u001b\u0010F\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\nø\u0001\u0000¢\u0006\u0004\bG\u0010\u000fJ\u001b\u0010F\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\nø\u0001\u0000¢\u0006\u0004\bH\u0010\u000bJ\u001b\u0010F\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\nø\u0001\u0000¢\u0006\u0004\bI\u0010\u001dJ\u001b\u0010F\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\nø\u0001\u0000¢\u0006\u0004\bJ\u0010\u0016J\u001e\u0010K\u001a\u00020\u00002\u0006\u0010L\u001a\u00020\u0003H\fø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\bM\u0010\u000bJ\u001e\u0010N\u001a\u00020\u00002\u0006\u0010L\u001a\u00020\u0003H\fø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\bO\u0010\u000bJ\u001b\u0010P\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\nø\u0001\u0000¢\u0006\u0004\bQ\u0010\u000fJ\u001b\u0010P\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\nø\u0001\u0000¢\u0006\u0004\bR\u0010\u000bJ\u001b\u0010P\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\nø\u0001\u0000¢\u0006\u0004\bS\u0010\u001dJ\u001b\u0010P\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\nø\u0001\u0000¢\u0006\u0004\bT\u0010\u0016J\u0010\u0010U\u001a\u00020VH\b¢\u0006\u0004\bW\u0010XJ\u0010\u0010Y\u001a\u00020ZH\b¢\u0006\u0004\b[\u0010\\J\u0010\u0010]\u001a\u00020^H\b¢\u0006\u0004\b_\u0010`J\u0010\u0010a\u001a\u00020\u0003H\b¢\u0006\u0004\bb\u0010\u0005J\u0010\u0010c\u001a\u00020dH\b¢\u0006\u0004\be\u0010fJ\u0010\u0010g\u001a\u00020hH\b¢\u0006\u0004\bi\u0010jJ\u000f\u0010k\u001a\u00020lH\u0016¢\u0006\u0004\bm\u0010nJ\u0016\u0010o\u001a\u00020\rH\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\bp\u0010XJ\u0016\u0010q\u001a\u00020\u0000H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\br\u0010\u0005J\u0016\u0010s\u001a\u00020\u0011H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\bt\u0010fJ\u0016\u0010u\u001a\u00020\u0014H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\bv\u0010jJ\u001b\u0010w\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\fø\u0001\u0000¢\u0006\u0004\bx\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0000X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007\u0001\u0002\u0001\u00020\u0003ø\u0001\u0000\u0002\b\n\u0002\b\u0019\n\u0002\b!¨\u0006z"}, mo10497d2 = {"Lkotlin/UInt;", "", "data", "", "constructor-impl", "(I)I", "getData$annotations", "()V", "and", "other", "and-WZ4Q5Ns", "(II)I", "compareTo", "Lkotlin/UByte;", "compareTo-7apg3OU", "(IB)I", "compareTo-WZ4Q5Ns", "Lkotlin/ULong;", "compareTo-VKZWuLQ", "(IJ)I", "Lkotlin/UShort;", "compareTo-xj2QHRw", "(IS)I", "dec", "dec-pVg5ArA", "div", "div-7apg3OU", "div-WZ4Q5Ns", "div-VKZWuLQ", "(IJ)J", "div-xj2QHRw", "equals", "", "", "equals-impl", "(ILjava/lang/Object;)Z", "floorDiv", "floorDiv-7apg3OU", "floorDiv-WZ4Q5Ns", "floorDiv-VKZWuLQ", "floorDiv-xj2QHRw", "hashCode", "hashCode-impl", "inc", "inc-pVg5ArA", "inv", "inv-pVg5ArA", "minus", "minus-7apg3OU", "minus-WZ4Q5Ns", "minus-VKZWuLQ", "minus-xj2QHRw", "mod", "mod-7apg3OU", "(IB)B", "mod-WZ4Q5Ns", "mod-VKZWuLQ", "mod-xj2QHRw", "(IS)S", "or", "or-WZ4Q5Ns", "plus", "plus-7apg3OU", "plus-WZ4Q5Ns", "plus-VKZWuLQ", "plus-xj2QHRw", "rangeTo", "Lkotlin/ranges/UIntRange;", "rangeTo-WZ4Q5Ns", "(II)Lkotlin/ranges/UIntRange;", "rem", "rem-7apg3OU", "rem-WZ4Q5Ns", "rem-VKZWuLQ", "rem-xj2QHRw", "shl", "bitCount", "shl-pVg5ArA", "shr", "shr-pVg5ArA", "times", "times-7apg3OU", "times-WZ4Q5Ns", "times-VKZWuLQ", "times-xj2QHRw", "toByte", "", "toByte-impl", "(I)B", "toDouble", "", "toDouble-impl", "(I)D", "toFloat", "", "toFloat-impl", "(I)F", "toInt", "toInt-impl", "toLong", "", "toLong-impl", "(I)J", "toShort", "", "toShort-impl", "(I)S", "toString", "", "toString-impl", "(I)Ljava/lang/String;", "toUByte", "toUByte-w2LRezQ", "toUInt", "toUInt-pVg5ArA", "toULong", "toULong-s-VKNKU", "toUShort", "toUShort-Mh2AYeg", "xor", "xor-WZ4Q5Ns", "Companion", "kotlin-stdlib"}, mo10498k = 1, mo10499mv = {1, 7, 1}, mo10501xi = 48)
/* compiled from: UInt.kt */
public final class UInt implements Comparable<UInt> {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final int MAX_VALUE = -1;
    public static final int MIN_VALUE = 0;
    public static final int SIZE_BITS = 32;
    public static final int SIZE_BYTES = 4;
    private final int data;

    /* renamed from: box-impl  reason: not valid java name */
    public static final /* synthetic */ UInt m137boximpl(int i) {
        return new UInt(i);
    }

    /* renamed from: constructor-impl  reason: not valid java name */
    public static int m143constructorimpl(int i) {
        return i;
    }

    /* renamed from: equals-impl  reason: not valid java name */
    public static boolean m149equalsimpl(int i, Object obj) {
        return (obj instanceof UInt) && i == ((UInt) obj).m194unboximpl();
    }

    /* renamed from: equals-impl0  reason: not valid java name */
    public static final boolean m150equalsimpl0(int i, int i2) {
        return i == i2;
    }

    public static /* synthetic */ void getData$annotations() {
    }

    /* renamed from: hashCode-impl  reason: not valid java name */
    public static int m155hashCodeimpl(int i) {
        return i;
    }

    public boolean equals(Object obj) {
        return m149equalsimpl(this.data, obj);
    }

    public int hashCode() {
        return m155hashCodeimpl(this.data);
    }

    /* renamed from: unbox-impl  reason: not valid java name */
    public final /* synthetic */ int m194unboximpl() {
        return this.data;
    }

    public /* bridge */ /* synthetic */ int compareTo(Object other) {
        return UnsignedKt.uintCompare(m194unboximpl(), ((UInt) other).m194unboximpl());
    }

    private /* synthetic */ UInt(int data2) {
        this.data = data2;
    }

    @Metadata(mo10496d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\u00020\u0004XTø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\n\u0002\u0010\u0005R\u0016\u0010\u0006\u001a\u00020\u0004XTø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\n\u0002\u0010\u0005R\u000e\u0010\u0007\u001a\u00020\bXT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bXT¢\u0006\u0002\n\u0000\u0002\b\n\u0002\b\u0019\n\u0002\b!¨\u0006\n"}, mo10497d2 = {"Lkotlin/UInt$Companion;", "", "()V", "MAX_VALUE", "Lkotlin/UInt;", "I", "MIN_VALUE", "SIZE_BITS", "", "SIZE_BYTES", "kotlin-stdlib"}, mo10498k = 1, mo10499mv = {1, 7, 1}, mo10501xi = 48)
    /* compiled from: UInt.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* renamed from: compareTo-7apg3OU  reason: not valid java name */
    private static final int m138compareTo7apg3OU(int arg0, byte other) {
        return UnsignedKt.uintCompare(arg0, m143constructorimpl(other & UByte.MAX_VALUE));
    }

    /* renamed from: compareTo-xj2QHRw  reason: not valid java name */
    private static final int m142compareToxj2QHRw(int arg0, short other) {
        return UnsignedKt.uintCompare(arg0, m143constructorimpl(65535 & other));
    }

    /* renamed from: compareTo-WZ4Q5Ns  reason: not valid java name */
    private int m140compareToWZ4Q5Ns(int other) {
        return UnsignedKt.uintCompare(m194unboximpl(), other);
    }

    /* renamed from: compareTo-WZ4Q5Ns  reason: not valid java name */
    private static int m141compareToWZ4Q5Ns(int arg0, int other) {
        return UnsignedKt.uintCompare(arg0, other);
    }

    /* renamed from: compareTo-VKZWuLQ  reason: not valid java name */
    private static final int m139compareToVKZWuLQ(int arg0, long other) {
        return UnsignedKt.ulongCompare(ULong.m221constructorimpl(((long) arg0) & 4294967295L), other);
    }

    /* renamed from: plus-7apg3OU  reason: not valid java name */
    private static final int m167plus7apg3OU(int arg0, byte other) {
        return m143constructorimpl(m143constructorimpl(other & UByte.MAX_VALUE) + arg0);
    }

    /* renamed from: plus-xj2QHRw  reason: not valid java name */
    private static final int m170plusxj2QHRw(int arg0, short other) {
        return m143constructorimpl(m143constructorimpl(65535 & other) + arg0);
    }

    /* renamed from: plus-WZ4Q5Ns  reason: not valid java name */
    private static final int m169plusWZ4Q5Ns(int arg0, int other) {
        return m143constructorimpl(arg0 + other);
    }

    /* renamed from: plus-VKZWuLQ  reason: not valid java name */
    private static final long m168plusVKZWuLQ(int arg0, long other) {
        return ULong.m221constructorimpl(ULong.m221constructorimpl(((long) arg0) & 4294967295L) + other);
    }

    /* renamed from: minus-7apg3OU  reason: not valid java name */
    private static final int m158minus7apg3OU(int arg0, byte other) {
        return m143constructorimpl(arg0 - m143constructorimpl(other & UByte.MAX_VALUE));
    }

    /* renamed from: minus-xj2QHRw  reason: not valid java name */
    private static final int m161minusxj2QHRw(int arg0, short other) {
        return m143constructorimpl(arg0 - m143constructorimpl(65535 & other));
    }

    /* renamed from: minus-WZ4Q5Ns  reason: not valid java name */
    private static final int m160minusWZ4Q5Ns(int arg0, int other) {
        return m143constructorimpl(arg0 - other);
    }

    /* renamed from: minus-VKZWuLQ  reason: not valid java name */
    private static final long m159minusVKZWuLQ(int arg0, long other) {
        return ULong.m221constructorimpl(ULong.m221constructorimpl(((long) arg0) & 4294967295L) - other);
    }

    /* renamed from: times-7apg3OU  reason: not valid java name */
    private static final int m178times7apg3OU(int arg0, byte other) {
        return m143constructorimpl(m143constructorimpl(other & UByte.MAX_VALUE) * arg0);
    }

    /* renamed from: times-xj2QHRw  reason: not valid java name */
    private static final int m181timesxj2QHRw(int arg0, short other) {
        return m143constructorimpl(m143constructorimpl(65535 & other) * arg0);
    }

    /* renamed from: times-WZ4Q5Ns  reason: not valid java name */
    private static final int m180timesWZ4Q5Ns(int arg0, int other) {
        return m143constructorimpl(arg0 * other);
    }

    /* renamed from: times-VKZWuLQ  reason: not valid java name */
    private static final long m179timesVKZWuLQ(int arg0, long other) {
        return ULong.m221constructorimpl(ULong.m221constructorimpl(((long) arg0) & 4294967295L) * other);
    }

    /* renamed from: div-7apg3OU  reason: not valid java name */
    private static final int m145div7apg3OU(int arg0, byte other) {
        return UnsignedKt.m396uintDivideJ1ME1BU(arg0, m143constructorimpl(other & UByte.MAX_VALUE));
    }

    /* renamed from: div-xj2QHRw  reason: not valid java name */
    private static final int m148divxj2QHRw(int arg0, short other) {
        return UnsignedKt.m396uintDivideJ1ME1BU(arg0, m143constructorimpl(65535 & other));
    }

    /* renamed from: div-WZ4Q5Ns  reason: not valid java name */
    private static final int m147divWZ4Q5Ns(int arg0, int other) {
        return UnsignedKt.m396uintDivideJ1ME1BU(arg0, other);
    }

    /* renamed from: div-VKZWuLQ  reason: not valid java name */
    private static final long m146divVKZWuLQ(int arg0, long other) {
        return UnsignedKt.m398ulongDivideeb3DHEI(ULong.m221constructorimpl(((long) arg0) & 4294967295L), other);
    }

    /* renamed from: rem-7apg3OU  reason: not valid java name */
    private static final int m172rem7apg3OU(int arg0, byte other) {
        return UnsignedKt.m397uintRemainderJ1ME1BU(arg0, m143constructorimpl(other & UByte.MAX_VALUE));
    }

    /* renamed from: rem-xj2QHRw  reason: not valid java name */
    private static final int m175remxj2QHRw(int arg0, short other) {
        return UnsignedKt.m397uintRemainderJ1ME1BU(arg0, m143constructorimpl(65535 & other));
    }

    /* renamed from: rem-WZ4Q5Ns  reason: not valid java name */
    private static final int m174remWZ4Q5Ns(int arg0, int other) {
        return UnsignedKt.m397uintRemainderJ1ME1BU(arg0, other);
    }

    /* renamed from: rem-VKZWuLQ  reason: not valid java name */
    private static final long m173remVKZWuLQ(int arg0, long other) {
        return UnsignedKt.m399ulongRemaindereb3DHEI(ULong.m221constructorimpl(((long) arg0) & 4294967295L), other);
    }

    /* renamed from: floorDiv-7apg3OU  reason: not valid java name */
    private static final int m151floorDiv7apg3OU(int arg0, byte other) {
        return UnsignedKt.m396uintDivideJ1ME1BU(arg0, m143constructorimpl(other & UByte.MAX_VALUE));
    }

    /* renamed from: floorDiv-xj2QHRw  reason: not valid java name */
    private static final int m154floorDivxj2QHRw(int arg0, short other) {
        return UnsignedKt.m396uintDivideJ1ME1BU(arg0, m143constructorimpl(65535 & other));
    }

    /* renamed from: floorDiv-WZ4Q5Ns  reason: not valid java name */
    private static final int m153floorDivWZ4Q5Ns(int arg0, int other) {
        return UnsignedKt.m396uintDivideJ1ME1BU(arg0, other);
    }

    /* renamed from: floorDiv-VKZWuLQ  reason: not valid java name */
    private static final long m152floorDivVKZWuLQ(int arg0, long other) {
        return UnsignedKt.m398ulongDivideeb3DHEI(ULong.m221constructorimpl(((long) arg0) & 4294967295L), other);
    }

    /* renamed from: mod-7apg3OU  reason: not valid java name */
    private static final byte m162mod7apg3OU(int arg0, byte other) {
        return UByte.m67constructorimpl((byte) UnsignedKt.m397uintRemainderJ1ME1BU(arg0, m143constructorimpl(other & UByte.MAX_VALUE)));
    }

    /* renamed from: mod-xj2QHRw  reason: not valid java name */
    private static final short m165modxj2QHRw(int arg0, short other) {
        return UShort.m327constructorimpl((short) UnsignedKt.m397uintRemainderJ1ME1BU(arg0, m143constructorimpl(65535 & other)));
    }

    /* renamed from: mod-WZ4Q5Ns  reason: not valid java name */
    private static final int m164modWZ4Q5Ns(int arg0, int other) {
        return UnsignedKt.m397uintRemainderJ1ME1BU(arg0, other);
    }

    /* renamed from: mod-VKZWuLQ  reason: not valid java name */
    private static final long m163modVKZWuLQ(int arg0, long other) {
        return UnsignedKt.m399ulongRemaindereb3DHEI(ULong.m221constructorimpl(((long) arg0) & 4294967295L), other);
    }

    /* renamed from: inc-pVg5ArA  reason: not valid java name */
    private static final int m156incpVg5ArA(int arg0) {
        return m143constructorimpl(arg0 + 1);
    }

    /* renamed from: dec-pVg5ArA  reason: not valid java name */
    private static final int m144decpVg5ArA(int arg0) {
        return m143constructorimpl(arg0 - 1);
    }

    /* renamed from: rangeTo-WZ4Q5Ns  reason: not valid java name */
    private static final UIntRange m171rangeToWZ4Q5Ns(int arg0, int other) {
        return new UIntRange(arg0, other, (DefaultConstructorMarker) null);
    }

    /* renamed from: shl-pVg5ArA  reason: not valid java name */
    private static final int m176shlpVg5ArA(int arg0, int bitCount) {
        return m143constructorimpl(arg0 << bitCount);
    }

    /* renamed from: shr-pVg5ArA  reason: not valid java name */
    private static final int m177shrpVg5ArA(int arg0, int bitCount) {
        return m143constructorimpl(arg0 >>> bitCount);
    }

    /* renamed from: and-WZ4Q5Ns  reason: not valid java name */
    private static final int m136andWZ4Q5Ns(int arg0, int other) {
        return m143constructorimpl(arg0 & other);
    }

    /* renamed from: or-WZ4Q5Ns  reason: not valid java name */
    private static final int m166orWZ4Q5Ns(int arg0, int other) {
        return m143constructorimpl(arg0 | other);
    }

    /* renamed from: xor-WZ4Q5Ns  reason: not valid java name */
    private static final int m193xorWZ4Q5Ns(int arg0, int other) {
        return m143constructorimpl(arg0 ^ other);
    }

    /* renamed from: inv-pVg5ArA  reason: not valid java name */
    private static final int m157invpVg5ArA(int arg0) {
        return m143constructorimpl(~arg0);
    }

    /* renamed from: toByte-impl  reason: not valid java name */
    private static final byte m182toByteimpl(int arg0) {
        return (byte) arg0;
    }

    /* renamed from: toShort-impl  reason: not valid java name */
    private static final short m187toShortimpl(int arg0) {
        return (short) arg0;
    }

    /* renamed from: toInt-impl  reason: not valid java name */
    private static final int m185toIntimpl(int arg0) {
        return arg0;
    }

    /* renamed from: toLong-impl  reason: not valid java name */
    private static final long m186toLongimpl(int arg0) {
        return ((long) arg0) & 4294967295L;
    }

    /* renamed from: toUByte-w2LRezQ  reason: not valid java name */
    private static final byte m189toUBytew2LRezQ(int arg0) {
        return UByte.m67constructorimpl((byte) arg0);
    }

    /* renamed from: toUShort-Mh2AYeg  reason: not valid java name */
    private static final short m192toUShortMh2AYeg(int arg0) {
        return UShort.m327constructorimpl((short) arg0);
    }

    /* renamed from: toUInt-pVg5ArA  reason: not valid java name */
    private static final int m190toUIntpVg5ArA(int arg0) {
        return arg0;
    }

    /* renamed from: toULong-s-VKNKU  reason: not valid java name */
    private static final long m191toULongsVKNKU(int arg0) {
        return ULong.m221constructorimpl(((long) arg0) & 4294967295L);
    }

    /* renamed from: toFloat-impl  reason: not valid java name */
    private static final float m184toFloatimpl(int arg0) {
        return (float) UnsignedKt.uintToDouble(arg0);
    }

    /* renamed from: toDouble-impl  reason: not valid java name */
    private static final double m183toDoubleimpl(int arg0) {
        return UnsignedKt.uintToDouble(arg0);
    }

    /* renamed from: toString-impl  reason: not valid java name */
    public static String m188toStringimpl(int arg0) {
        return String.valueOf(((long) arg0) & 4294967295L);
    }

    public String toString() {
        return m188toStringimpl(this.data);
    }
}
