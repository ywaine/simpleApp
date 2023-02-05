package com.sst.myapplication;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.sst.myapplication.databinding.ActivityMainMenuBinding;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo10496d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0002¢\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\b¨\u0006\u000f"}, mo10497d2 = {"Lcom/sst/myapplication/MainMenuActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "mMessageReceiver", "Landroid/content/BroadcastReceiver;", "viewBinding", "Lcom/sst/myapplication/databinding/ActivityMainMenuBinding;", "getViewBinding", "()Lcom/sst/myapplication/databinding/ActivityMainMenuBinding;", "viewBinding$delegate", "Lkotlin/Lazy;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "app_debug"}, mo10498k = 1, mo10499mv = {1, 7, 1}, mo10501xi = 48)
/* compiled from: MainMenuActivity.kt */
public final class MainMenuActivity extends AppCompatActivity {
    private BroadcastReceiver mMessageReceiver = new MainMenuActivity$mMessageReceiver$1(this);
    private final Lazy viewBinding$delegate = LazyKt.lazy(new MainMenuActivity$viewBinding$2(this));

    /* access modifiers changed from: private */
    public final ActivityMainMenuBinding getViewBinding() {
        return (ActivityMainMenuBinding) this.viewBinding$delegate.getValue();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((View) getViewBinding().getRoot());
        getViewBinding().rvMenu.setHasFixedSize(true);
        getViewBinding().rvMenu.setAdapter(new MenuListAdapter(this, ArraysKt.asList((T[]) Constants.INSTANCE.getMainMenu())));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(this.mMessageReceiver, new IntentFilter("DataChange"));
        getViewBinding().tvMenuTitle.setVisibility(4);
        getViewBinding().txtCancel.setOnClickListener(new MainMenuActivity$$ExternalSyntheticLambda0(this));
        getViewBinding().txtcancel.setOnClickListener(new MainMenuActivity$$ExternalSyntheticLambda1(this));
        getViewBinding().txtMainMenu.setOnClickListener(new MainMenuActivity$$ExternalSyntheticLambda4(this));
        getViewBinding().txtEnter.setOnClickListener(new MainMenuActivity$$ExternalSyntheticLambda2(this));
        getViewBinding().txtOK.setOnClickListener(new MainMenuActivity$$ExternalSyntheticLambda3(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: onCreate$lambda-0  reason: not valid java name */
    public static final void m39onCreate$lambda0(MainMenuActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (Constants.INSTANCE.getMenuType() == 0) {
            this$0.finish();
            return;
        }
        Constants constants = Constants.INSTANCE;
        constants.setMenuType(constants.getMenuType() - 1);
        Intent intent = new Intent("DataChange");
        intent.putExtra("menuType", Constants.INSTANCE.getMenuType());
        LocalBroadcastManager.getInstance(this$0.getApplicationContext()).sendBroadcast(intent);
    }

    /* access modifiers changed from: private */
    /* renamed from: onCreate$lambda-1  reason: not valid java name */
    public static final void m40onCreate$lambda1(MainMenuActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Constants constants = Constants.INSTANCE;
        constants.setMenuType(constants.getMenuType() - 1);
        Intent intent = new Intent("DataChange");
        intent.putExtra("menuType", Constants.INSTANCE.getMenuType());
        LocalBroadcastManager.getInstance(this$0.getApplicationContext()).sendBroadcast(intent);
    }

    /* access modifiers changed from: private */
    /* renamed from: onCreate$lambda-2  reason: not valid java name */
    public static final void m41onCreate$lambda2(MainMenuActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Constants.INSTANCE.setMenuType(0);
        Intent intent = new Intent("DataChange");
        intent.putExtra("menuType", Constants.INSTANCE.getMenuType());
        LocalBroadcastManager.getInstance(this$0.getApplicationContext()).sendBroadcast(intent);
    }

    /* access modifiers changed from: private */
    /* renamed from: onCreate$lambda-3  reason: not valid java name */
    public static final void m42onCreate$lambda3(MainMenuActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (Constants.INSTANCE.getMenuType() == 4) {
            this$0.getViewBinding().cvTransactioncard.setVisibility(0);
            this$0.getViewBinding().cardView.setVisibility(8);
            return;
        }
        Constants constants = Constants.INSTANCE;
        constants.setMenuType(constants.getMenuType() + 1);
        Intent intent = new Intent("DataChange");
        intent.putExtra("menuType", Constants.INSTANCE.getMenuType());
        LocalBroadcastManager.getInstance(this$0.getApplicationContext()).sendBroadcast(intent);
    }

    /* access modifiers changed from: private */
    /* renamed from: onCreate$lambda-4  reason: not valid java name */
    public static final void m43onCreate$lambda4(MainMenuActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Constants.INSTANCE.setMenuType(0);
        this$0.finish();
    }
}
