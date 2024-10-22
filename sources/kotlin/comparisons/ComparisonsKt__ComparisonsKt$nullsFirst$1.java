package kotlin.comparisons;

import java.util.Comparator;
import kotlin.Metadata;

@Metadata(mo10496d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0004\u0010\u0000\u001a\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u0001H\u00022\b\u0010\u0005\u001a\u0004\u0018\u0001H\u0002H\n¢\u0006\u0004\b\u0006\u0010\u0007"}, mo10497d2 = {"<anonymous>", "", "T", "", "a", "b", "compare", "(Ljava/lang/Object;Ljava/lang/Object;)I"}, mo10498k = 3, mo10499mv = {1, 7, 1}, mo10501xi = 48)
/* compiled from: Comparisons.kt */
final class ComparisonsKt__ComparisonsKt$nullsFirst$1<T> implements Comparator {
    final /* synthetic */ Comparator<? super T> $comparator;

    ComparisonsKt__ComparisonsKt$nullsFirst$1(Comparator<? super T> comparator) {
        this.$comparator = comparator;
    }

    public final int compare(T a, T b) {
        if (a == b) {
            return 0;
        }
        if (a == null) {
            return -1;
        }
        if (b == null) {
            return 1;
        }
        return this.$comparator.compare(a, b);
    }
}
