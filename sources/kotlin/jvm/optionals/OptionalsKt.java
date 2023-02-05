package kotlin.jvm.optionals;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

@Metadata(mo10496d1 = {"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u001f\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0010\"\n\u0000\u001a$\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u0003*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0004H\u0007\u001a4\u0010\u0005\u001a\u0002H\u0006\"\u0004\b\u0000\u0010\u0006\"\n\b\u0001\u0010\u0002*\u0004\b\u0002H\u0006*\b\u0012\u0004\u0012\u0002H\u00020\u00042\u0006\u0010\u0007\u001a\u0002H\u0006H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\b\u001a>\u0010\t\u001a\u0002H\u0006\"\u0004\b\u0000\u0010\u0006\"\n\b\u0001\u0010\u0002*\u0004\b\u0002H\u0006*\b\u0012\u0004\u0012\u0002H\u00020\u00042\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00060\nH\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0002\u0010\u000b\u001a#\u0010\f\u001a\u0004\u0018\u0001H\u0002\"\b\b\u0000\u0010\u0002*\u00020\u0003*\b\u0012\u0004\u0012\u0002H\u00020\u0004H\u0007¢\u0006\u0002\u0010\r\u001a;\u0010\u000e\u001a\u0002H\u000f\"\b\b\u0000\u0010\u0002*\u00020\u0003\"\u0010\b\u0001\u0010\u000f*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0010*\b\u0012\u0004\u0012\u0002H\u00020\u00042\u0006\u0010\u0011\u001a\u0002H\u000fH\u0007¢\u0006\u0002\u0010\u0012\u001a$\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0014\"\b\b\u0000\u0010\u0002*\u00020\u0003*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0004H\u0007\u001a$\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0016\"\b\b\u0000\u0010\u0002*\u00020\u0003*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0004H\u0007\u0002\u000b\n\u0002\b9\n\u0005\b20\u0001¨\u0006\u0017"}, mo10497d2 = {"asSequence", "Lkotlin/sequences/Sequence;", "T", "", "Ljava/util/Optional;", "getOrDefault", "R", "defaultValue", "(Ljava/util/Optional;Ljava/lang/Object;)Ljava/lang/Object;", "getOrElse", "Lkotlin/Function0;", "(Ljava/util/Optional;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "getOrNull", "(Ljava/util/Optional;)Ljava/lang/Object;", "toCollection", "C", "", "destination", "(Ljava/util/Optional;Ljava/util/Collection;)Ljava/util/Collection;", "toList", "", "toSet", "", "kotlin-stdlib-jdk8"}, mo10498k = 2, mo10499mv = {1, 7, 1}, mo10501xi = 48)
/* compiled from: Optionals.kt */
public final class OptionalsKt {
    public static final <T> T getOrNull(Optional<T> $this$getOrNull) {
        Intrinsics.checkNotNullParameter($this$getOrNull, "<this>");
        return $this$getOrNull.orElse((Object) null);
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<T>, java.util.Optional, java.lang.Object] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final <R, T extends R> R getOrDefault(java.util.Optional<T> r1, R r2) {
        /*
            java.lang.String r0 = "<this>"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r1, r0)
            boolean r0 = r1.isPresent()
            if (r0 == 0) goto L_0x0010
            java.lang.Object r0 = r1.get()
            goto L_0x0011
        L_0x0010:
            r0 = r2
        L_0x0011:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.jvm.optionals.OptionalsKt.getOrDefault(java.util.Optional, java.lang.Object):java.lang.Object");
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [java.util.Optional<T>, java.util.Optional, java.lang.Object] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final <R, T extends R> R getOrElse(java.util.Optional<T> r2, kotlin.jvm.functions.Function0<? extends R> r3) {
        /*
            java.lang.String r0 = "<this>"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r2, r0)
            java.lang.String r0 = "defaultValue"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r3, r0)
            r0 = 0
            boolean r1 = r2.isPresent()
            if (r1 == 0) goto L_0x0016
            java.lang.Object r1 = r2.get()
            goto L_0x001a
        L_0x0016:
            java.lang.Object r1 = r3.invoke()
        L_0x001a:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.jvm.optionals.OptionalsKt.getOrElse(java.util.Optional, kotlin.jvm.functions.Function0):java.lang.Object");
    }

    public static final <T, C extends Collection<? super T>> C toCollection(Optional<T> $this$toCollection, C destination) {
        Intrinsics.checkNotNullParameter($this$toCollection, "<this>");
        Intrinsics.checkNotNullParameter(destination, "destination");
        if ($this$toCollection.isPresent()) {
            T t = $this$toCollection.get();
            Intrinsics.checkNotNullExpressionValue(t, "get()");
            destination.add(t);
        }
        return destination;
    }

    public static final <T> List<T> toList(Optional<? extends T> $this$toList) {
        Intrinsics.checkNotNullParameter($this$toList, "<this>");
        return $this$toList.isPresent() ? CollectionsKt.listOf($this$toList.get()) : CollectionsKt.emptyList();
    }

    public static final <T> Set<T> toSet(Optional<? extends T> $this$toSet) {
        Intrinsics.checkNotNullParameter($this$toSet, "<this>");
        return $this$toSet.isPresent() ? SetsKt.setOf($this$toSet.get()) : SetsKt.emptySet();
    }

    public static final <T> Sequence<T> asSequence(Optional<? extends T> $this$asSequence) {
        Intrinsics.checkNotNullParameter($this$asSequence, "<this>");
        if (!$this$asSequence.isPresent()) {
            return SequencesKt.emptySequence();
        }
        return SequencesKt.sequenceOf($this$asSequence.get());
    }
}
