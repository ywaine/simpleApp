package kotlin.text;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo10496d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0003"}, mo10497d2 = {"<anonymous>", "", "it", "invoke"}, mo10498k = 3, mo10499mv = {1, 7, 1}, mo10501xi = 48)
/* compiled from: Indent.kt */
final class StringsKt__IndentKt$prependIndent$1 extends Lambda implements Function1<String, String> {
    final /* synthetic */ String $indent;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    StringsKt__IndentKt$prependIndent$1(String str) {
        super(1);
        this.$indent = str;
    }

    public final String invoke(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        if (!StringsKt.isBlank(it)) {
            return this.$indent + it;
        } else if (it.length() < this.$indent.length()) {
            return this.$indent;
        } else {
            return it;
        }
    }
}
