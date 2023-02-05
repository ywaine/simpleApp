package com.sst.myapplication;

import com.sst.myapplication.databinding.ActivityMainMenuBinding;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo10496d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo10497d2 = {"<anonymous>", "Lcom/sst/myapplication/databinding/ActivityMainMenuBinding;", "invoke"}, mo10498k = 3, mo10499mv = {1, 7, 1}, mo10501xi = 48)
/* compiled from: MainMenuActivity.kt */
final class MainMenuActivity$viewBinding$2 extends Lambda implements Function0<ActivityMainMenuBinding> {
    final /* synthetic */ MainMenuActivity this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    MainMenuActivity$viewBinding$2(MainMenuActivity mainMenuActivity) {
        super(0);
        this.this$0 = mainMenuActivity;
    }

    public final ActivityMainMenuBinding invoke() {
        ActivityMainMenuBinding inflate = ActivityMainMenuBinding.inflate(this.this$0.getLayoutInflater());
        Intrinsics.checkNotNullExpressionValue(inflate, "inflate(layoutInflater)");
        return inflate;
    }
}
