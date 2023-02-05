package com.sst.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.sst.myapplication.databinding.ActivityMainBinding;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo10496d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0014R\u001b\u0010\u0003\u001a\u00020\u00048BX\u0002¢\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006¨\u0006\r"}, mo10497d2 = {"Lcom/sst/myapplication/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "viewBinding", "Lcom/sst/myapplication/databinding/ActivityMainBinding;", "getViewBinding", "()Lcom/sst/myapplication/databinding/ActivityMainBinding;", "viewBinding$delegate", "Lkotlin/Lazy;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "app_debug"}, mo10498k = 1, mo10499mv = {1, 7, 1}, mo10501xi = 48)
/* compiled from: MainActivity.kt */
public final class MainActivity extends AppCompatActivity {
    private final Lazy viewBinding$delegate = LazyKt.lazy(new MainActivity$viewBinding$2(this));

    private final ActivityMainBinding getViewBinding() {
        return (ActivityMainBinding) this.viewBinding$delegate.getValue();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((View) getViewBinding().getRoot());
        getViewBinding().tvAirtelCode.setOnClickListener(new MainActivity$$ExternalSyntheticLambda1(this));
        getViewBinding().icAirtel.setOnClickListener(new MainActivity$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: onCreate$lambda-0  reason: not valid java name */
    public static final void m36onCreate$lambda0(MainActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.startActivity(new Intent(this$0, MainMenuActivity.class));
        this$0.finish();
    }

    /* access modifiers changed from: private */
    /* renamed from: onCreate$lambda-1  reason: not valid java name */
    public static final void m37onCreate$lambda1(MainActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.startActivity(new Intent(this$0, MainMenuActivity.class));
        this$0.finish();
    }
}
