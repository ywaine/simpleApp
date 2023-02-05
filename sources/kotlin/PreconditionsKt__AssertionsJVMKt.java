package kotlin;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo10496d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\b\u001a\"\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\bø\u0001\u0000\u0002\u0007\n\u0005\b20\u0001¨\u0006\u0007"}, mo10497d2 = {"assert", "", "value", "", "lazyMessage", "Lkotlin/Function0;", "", "kotlin-stdlib"}, mo10498k = 5, mo10499mv = {1, 7, 1}, mo10501xi = 49, mo10502xs = "kotlin/PreconditionsKt")
/* compiled from: AssertionsJVM.kt */
class PreconditionsKt__AssertionsJVMKt {
    /* renamed from: assert  reason: not valid java name */
    private static final void m46assert(boolean value) {
        if (!value) {
            throw new AssertionError("Assertion failed");
        }
    }

    /* renamed from: assert  reason: not valid java name */
    private static final void m47assert(boolean value, Function0<? extends Object> lazyMessage) {
        Intrinsics.checkNotNullParameter(lazyMessage, "lazyMessage");
        if (!value) {
            throw new AssertionError(lazyMessage.invoke());
        }
    }
}
