package kotlin.sequences;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

@Metadata(mo10496d1 = {"\u0000\f\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0010\u0000\u001a\u0002H\u0001\"\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0010\u0003\u001a\u0004\u0018\u0001H\u0001H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, mo10497d2 = {"<anonymous>", "T", "", "it", "invoke", "(Ljava/lang/Object;)Ljava/lang/Object;"}, mo10498k = 3, mo10499mv = {1, 7, 1}, mo10501xi = 48)
/* compiled from: _Sequences.kt */
final class SequencesKt___SequencesKt$requireNoNulls$1 extends Lambda implements Function1<T, T> {
    final /* synthetic */ Sequence<T> $this_requireNoNulls;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    SequencesKt___SequencesKt$requireNoNulls$1(Sequence<? extends T> sequence) {
        super(1);
        this.$this_requireNoNulls = sequence;
    }

    public final T invoke(T it) {
        if (it != null) {
            return it;
        }
        throw new IllegalArgumentException("null element found in " + this.$this_requireNoNulls + '.');
    }
}
