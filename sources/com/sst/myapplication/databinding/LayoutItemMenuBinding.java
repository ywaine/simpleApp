package com.sst.myapplication.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.sst.myapplication.C0082R;

public final class LayoutItemMenuBinding implements ViewBinding {
    private final ConstraintLayout rootView;
    public final TextView txtMenuname;

    private LayoutItemMenuBinding(ConstraintLayout rootView2, TextView txtMenuname2) {
        this.rootView = rootView2;
        this.txtMenuname = txtMenuname2;
    }

    public ConstraintLayout getRoot() {
        return this.rootView;
    }

    public static LayoutItemMenuBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, (ViewGroup) null, false);
    }

    public static LayoutItemMenuBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(C0082R.layout.layout_item_menu, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static LayoutItemMenuBinding bind(View rootView2) {
        TextView txtMenuname2 = (TextView) ViewBindings.findChildViewById(rootView2, C0082R.C0085id.txtMenuname);
        if (txtMenuname2 != null) {
            return new LayoutItemMenuBinding((ConstraintLayout) rootView2, txtMenuname2);
        }
        throw new NullPointerException("Missing required view with ID: ".concat(rootView2.getResources().getResourceName(C0082R.C0085id.txtMenuname)));
    }
}
