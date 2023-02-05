package kotlin;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/* renamed from: kotlin.SafePublicationLazyImpl$$ExternalSyntheticBackportWithForwarding0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0633xe90471fe {
    /* renamed from: m */
    public static /* synthetic */ boolean m21m(AtomicReferenceFieldUpdater atomicReferenceFieldUpdater, Object obj, Object obj2, Object obj3) {
        while (!atomicReferenceFieldUpdater.compareAndSet(obj, obj2, obj3)) {
            if (atomicReferenceFieldUpdater.get(obj) != obj2) {
                return false;
            }
        }
        return true;
    }
}
