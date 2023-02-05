package kotlin.comparisons;

import java.util.Comparator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;

@Metadata(mo10496d1 = {"\u0000\n\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u000e\u0010\u0003\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u00022\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u0002H\n¢\u0006\u0004\b\u0006\u0010\u0007"}, mo10497d2 = {"<anonymous>", "", "T", "a", "kotlin.jvm.PlatformType", "b", "compare", "(Ljava/lang/Object;Ljava/lang/Object;)I"}, mo10498k = 3, mo10499mv = {1, 7, 1}, mo10501xi = 176)
/* compiled from: Comparisons.kt */
public final class ComparisonsKt__ComparisonsKt$compareBy$2<T> implements Comparator {
    final /* synthetic */ Function1<T, Comparable<?>> $selector;

    public ComparisonsKt__ComparisonsKt$compareBy$2(Function1<? super T, ? extends Comparable<?>> function1) {
        this.$selector = function1;
    }

    public final int compare(T a, T b) {
        Function1<T, Comparable<?>> function1 = this.$selector;
        return ComparisonsKt.compareValues(function1.invoke(a), function1.invoke(b));
    }
}