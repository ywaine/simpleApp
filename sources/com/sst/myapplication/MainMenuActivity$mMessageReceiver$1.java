package com.sst.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Metadata(mo10496d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u001a\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, mo10497d2 = {"com/sst/myapplication/MainMenuActivity$mMessageReceiver$1", "Landroid/content/BroadcastReceiver;", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "app_debug"}, mo10498k = 1, mo10499mv = {1, 7, 1}, mo10501xi = 48)
/* compiled from: MainMenuActivity.kt */
public final class MainMenuActivity$mMessageReceiver$1 extends BroadcastReceiver {
    final /* synthetic */ MainMenuActivity this$0;

    MainMenuActivity$mMessageReceiver$1(MainMenuActivity $receiver) {
        this.this$0 = $receiver;
    }

    public void onReceive(Context context, Intent intent) {
        Intent intent2 = intent;
        Intrinsics.checkNotNullParameter(intent2, "intent");
        int type = intent2.getIntExtra("menuType", 0);
        String title = intent2.getStringExtra("menuTitle");
        switch (type) {
            case 0:
                this.this$0.getViewBinding().mainView.setVisibility(0);
                this.this$0.getViewBinding().clAmount.setVisibility(8);
                this.this$0.getViewBinding().tvMenuTitle.setVisibility(4);
                this.this$0.getViewBinding().rvMenu.setAdapter(new MenuListAdapter(this.this$0, ArraysKt.asList((T[]) Constants.INSTANCE.getMainMenu())));
                return;
            case 1:
                if (title != null) {
                    Constants.INSTANCE.setMenuTitle(StringsKt.trim((CharSequence) (String) StringsKt.split$default((CharSequence) title, new String[]{"."}, false, 0, 6, (Object) null).get(1)).toString());
                }
                this.this$0.getViewBinding().mainView.setVisibility(0);
                this.this$0.getViewBinding().clAmount.setVisibility(8);
                this.this$0.getViewBinding().tvMenuTitle.setText(Constants.INSTANCE.getMenuTitle());
                this.this$0.getViewBinding().tvMenuTitle.setVisibility(0);
                this.this$0.getViewBinding().rvMenu.setAdapter(new MenuListAdapter(this.this$0, ArraysKt.asList((T[]) Constants.INSTANCE.getAirtime_BundlesMenu())));
                return;
            case 2:
                if (title != null) {
                    Constants.INSTANCE.setMenuTitle(StringsKt.trim((CharSequence) (String) StringsKt.split$default((CharSequence) title, new String[]{"."}, false, 0, 6, (Object) null).get(1)).toString());
                }
                this.this$0.getViewBinding().mainView.setVisibility(0);
                this.this$0.getViewBinding().clAmount.setVisibility(8);
                this.this$0.getViewBinding().tvMenuTitle.setText(Constants.INSTANCE.getMenuTitle());
                this.this$0.getViewBinding().tvMenuTitle.setVisibility(0);
                this.this$0.getViewBinding().rvMenu.setAdapter(new MenuListAdapter(this.this$0, ArraysKt.asList((T[]) Constants.INSTANCE.getBuy_AirtimeSubMenu())));
                return;
            case 3:
                if (title != null) {
                    Constants.INSTANCE.setMenuTitle(StringsKt.trim((CharSequence) (String) StringsKt.split$default((CharSequence) title, new String[]{"."}, false, 0, 6, (Object) null).get(1)).toString());
                }
                this.this$0.getViewBinding().mainView.setVisibility(8);
                this.this$0.getViewBinding().clAmount.setVisibility(0);
                this.this$0.getViewBinding().editText.setText("");
                this.this$0.getViewBinding().txtTitles.setText("Enter Amount");
                this.this$0.getViewBinding().txtEnter.setText("Enter");
                return;
            case 4:
                if (title != null) {
                    Constants.INSTANCE.setMenuTitle(StringsKt.trim((CharSequence) (String) StringsKt.split$default((CharSequence) title, new String[]{"."}, false, 0, 6, (Object) null).get(1)).toString());
                }
                this.this$0.getViewBinding().mainView.setVisibility(8);
                this.this$0.getViewBinding().clAmount.setVisibility(0);
                this.this$0.getViewBinding().editText.setText("");
                this.this$0.getViewBinding().txtTitles.setText("Airtime Top up of UGX#EA for #BA number. Confirm with your PIN");
                this.this$0.getViewBinding().txtEnter.setText("Send");
                return;
            default:
                return;
        }
    }
}
