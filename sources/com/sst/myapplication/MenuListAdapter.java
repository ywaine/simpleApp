package com.sst.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sst.myapplication.databinding.LayoutItemMenuBinding;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo10496d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0013B\u001d\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006¢\u0006\u0002\u0010\bJ\b\u0010\t\u001a\u00020\nH\u0016J\u0018\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\nH\u0016J\u0018\u0010\u000f\u001a\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\nH\u0016R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, mo10497d2 = {"Lcom/sst/myapplication/MenuListAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/sst/myapplication/MenuListAdapter$ItemViewHolder;", "mContext", "Landroid/app/Activity;", "arrayList", "", "", "(Landroid/app/Activity;Ljava/util/List;)V", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "ItemViewHolder", "app_debug"}, mo10498k = 1, mo10499mv = {1, 7, 1}, mo10501xi = 48)
/* compiled from: MenuListAdapter.kt */
public final class MenuListAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private List<String> arrayList;
    private final Activity mContext;

    public MenuListAdapter(Activity mContext2, List<String> arrayList2) {
        Intrinsics.checkNotNullParameter(arrayList2, "arrayList");
        this.mContext = mContext2;
        this.arrayList = arrayList2;
    }

    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        LayoutItemMenuBinding inflate = LayoutItemMenuBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        Intrinsics.checkNotNullExpressionValue(inflate, "inflate(\n               …      false\n            )");
        return new ItemViewHolder(inflate);
    }

    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        holder.getItemViewHolder().txtMenuname.setText(this.arrayList.get(position));
        holder.itemView.setOnClickListener(new MenuListAdapter$$ExternalSyntheticLambda0(this, position));
    }

    /* access modifiers changed from: private */
    /* renamed from: onBindViewHolder$lambda-0  reason: not valid java name */
    public static final void m44onBindViewHolder$lambda0(MenuListAdapter this$0, int $position, View it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (this$0.arrayList.get($position).equals("00. Main Menu")) {
            Constants.INSTANCE.setMenuType(0);
            Intent intent = new Intent("DataChange");
            intent.putExtra("menuType", Constants.INSTANCE.getMenuType());
            intent.putExtra("menuTitle", "");
            Activity activity = this$0.mContext;
            Intrinsics.checkNotNull(activity, "null cannot be cast to non-null type android.app.Activity");
            LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
            return;
        }
        Constants constants = Constants.INSTANCE;
        constants.setMenuType(constants.getMenuType() + 1);
        Intent intent2 = new Intent("DataChange");
        intent2.putExtra("menuType", Constants.INSTANCE.getMenuType());
        intent2.putExtra("menuTitle", this$0.arrayList.get($position));
        Activity activity2 = this$0.mContext;
        Intrinsics.checkNotNull(activity2, "null cannot be cast to non-null type android.app.Activity");
        LocalBroadcastManager.getInstance(activity2).sendBroadcast(intent2);
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    @Metadata(mo10496d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u001a\u0010\u0005\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\u0004¨\u0006\t"}, mo10497d2 = {"Lcom/sst/myapplication/MenuListAdapter$ItemViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemLayoutBinding", "Lcom/sst/myapplication/databinding/LayoutItemMenuBinding;", "(Lcom/sst/myapplication/databinding/LayoutItemMenuBinding;)V", "itemViewHolder", "getItemViewHolder", "()Lcom/sst/myapplication/databinding/LayoutItemMenuBinding;", "setItemViewHolder", "app_debug"}, mo10498k = 1, mo10499mv = {1, 7, 1}, mo10501xi = 48)
    /* compiled from: MenuListAdapter.kt */
    public static final class ItemViewHolder extends RecyclerView.ViewHolder {
        private LayoutItemMenuBinding itemViewHolder;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public ItemViewHolder(LayoutItemMenuBinding itemLayoutBinding) {
            super(itemLayoutBinding.getRoot());
            Intrinsics.checkNotNullParameter(itemLayoutBinding, "itemLayoutBinding");
            this.itemViewHolder = itemLayoutBinding;
        }

        public final LayoutItemMenuBinding getItemViewHolder() {
            return this.itemViewHolder;
        }

        public final void setItemViewHolder(LayoutItemMenuBinding layoutItemMenuBinding) {
            Intrinsics.checkNotNullParameter(layoutItemMenuBinding, "<set-?>");
            this.itemViewHolder = layoutItemMenuBinding;
        }
    }
}
