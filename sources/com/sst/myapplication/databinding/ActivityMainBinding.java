package com.sst.myapplication.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.sst.myapplication.C0082R;

public final class ActivityMainBinding implements ViewBinding {
    public final Guideline guideline;
    public final Guideline guideline2;
    public final ImageView icAirtel;
    public final ImageView icMTN;
    private final ConstraintLayout rootView;
    public final TextView tvAirtelCode;
    public final TextView tvMTNCode;

    private ActivityMainBinding(ConstraintLayout rootView2, Guideline guideline3, Guideline guideline22, ImageView icAirtel2, ImageView icMTN2, TextView tvAirtelCode2, TextView tvMTNCode2) {
        this.rootView = rootView2;
        this.guideline = guideline3;
        this.guideline2 = guideline22;
        this.icAirtel = icAirtel2;
        this.icMTN = icMTN2;
        this.tvAirtelCode = tvAirtelCode2;
        this.tvMTNCode = tvMTNCode2;
    }

    public ConstraintLayout getRoot() {
        return this.rootView;
    }

    public static ActivityMainBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, (ViewGroup) null, false);
    }

    public static ActivityMainBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(C0082R.layout.activity_main, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ActivityMainBinding bind(View rootView2) {
        View view = rootView2;
        int id = C0082R.C0085id.guideline;
        Guideline guideline3 = (Guideline) ViewBindings.findChildViewById(view, C0082R.C0085id.guideline);
        if (guideline3 != null) {
            id = C0082R.C0085id.guideline2;
            Guideline guideline22 = (Guideline) ViewBindings.findChildViewById(view, C0082R.C0085id.guideline2);
            if (guideline22 != null) {
                id = C0082R.C0085id.icAirtel;
                ImageView icAirtel2 = (ImageView) ViewBindings.findChildViewById(view, C0082R.C0085id.icAirtel);
                if (icAirtel2 != null) {
                    id = C0082R.C0085id.icMTN;
                    ImageView icMTN2 = (ImageView) ViewBindings.findChildViewById(view, C0082R.C0085id.icMTN);
                    if (icMTN2 != null) {
                        id = C0082R.C0085id.tvAirtelCode;
                        TextView tvAirtelCode2 = (TextView) ViewBindings.findChildViewById(view, C0082R.C0085id.tvAirtelCode);
                        if (tvAirtelCode2 != null) {
                            id = C0082R.C0085id.tvMTNCode;
                            TextView tvMTNCode2 = (TextView) ViewBindings.findChildViewById(view, C0082R.C0085id.tvMTNCode);
                            if (tvMTNCode2 != null) {
                                return new ActivityMainBinding((ConstraintLayout) view, guideline3, guideline22, icAirtel2, icMTN2, tvAirtelCode2, tvMTNCode2);
                            }
                        }
                    }
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(rootView2.getResources().getResourceName(id)));
    }
}
