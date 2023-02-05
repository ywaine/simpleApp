package com.sst.myapplication.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.sst.myapplication.C0082R;

public final class ActivityMainMenuBinding implements ViewBinding {
    public final CardView cardView;
    public final ConstraintLayout clAmount;
    public final ConstraintLayout clMainmenu;
    public final CardView cvTransactioncard;
    public final EditText editText;
    public final ConstraintLayout mainVieasw;
    public final ConstraintLayout mainView;
    private final ConstraintLayout rootView;
    public final RecyclerView rvMenu;
    public final TextView textView;
    public final TextView tvMenuTitle;
    public final TextView txtCancel;
    public final TextView txtEnter;
    public final TextView txtMainMenu;
    public final TextView txtOK;
    public final TextView txtTitles;
    public final TextView txtcancel;

    private ActivityMainMenuBinding(ConstraintLayout rootView2, CardView cardView2, ConstraintLayout clAmount2, ConstraintLayout clMainmenu2, CardView cvTransactioncard2, EditText editText2, ConstraintLayout mainVieasw2, ConstraintLayout mainView2, RecyclerView rvMenu2, TextView textView2, TextView tvMenuTitle2, TextView txtCancel2, TextView txtEnter2, TextView txtMainMenu2, TextView txtOK2, TextView txtTitles2, TextView txtcancel2) {
        this.rootView = rootView2;
        this.cardView = cardView2;
        this.clAmount = clAmount2;
        this.clMainmenu = clMainmenu2;
        this.cvTransactioncard = cvTransactioncard2;
        this.editText = editText2;
        this.mainVieasw = mainVieasw2;
        this.mainView = mainView2;
        this.rvMenu = rvMenu2;
        this.textView = textView2;
        this.tvMenuTitle = tvMenuTitle2;
        this.txtCancel = txtCancel2;
        this.txtEnter = txtEnter2;
        this.txtMainMenu = txtMainMenu2;
        this.txtOK = txtOK2;
        this.txtTitles = txtTitles2;
        this.txtcancel = txtcancel2;
    }

    public ConstraintLayout getRoot() {
        return this.rootView;
    }

    public static ActivityMainMenuBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, (ViewGroup) null, false);
    }

    public static ActivityMainMenuBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(C0082R.layout.activity_main_menu, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ActivityMainMenuBinding bind(View rootView2) {
        View view = rootView2;
        int id = C0082R.C0085id.cardView;
        CardView cardView2 = (CardView) ViewBindings.findChildViewById(view, C0082R.C0085id.cardView);
        if (cardView2 != null) {
            id = C0082R.C0085id.clAmount;
            ConstraintLayout clAmount2 = (ConstraintLayout) ViewBindings.findChildViewById(view, C0082R.C0085id.clAmount);
            if (clAmount2 != null) {
                id = C0082R.C0085id.clMainmenu;
                ConstraintLayout clMainmenu2 = (ConstraintLayout) ViewBindings.findChildViewById(view, C0082R.C0085id.clMainmenu);
                if (clMainmenu2 != null) {
                    id = C0082R.C0085id.cvTransactioncard;
                    CardView cvTransactioncard2 = (CardView) ViewBindings.findChildViewById(view, C0082R.C0085id.cvTransactioncard);
                    if (cvTransactioncard2 != null) {
                        id = C0082R.C0085id.editText;
                        EditText editText2 = (EditText) ViewBindings.findChildViewById(view, C0082R.C0085id.editText);
                        if (editText2 != null) {
                            id = C0082R.C0085id.mainVieasw;
                            ConstraintLayout mainVieasw2 = (ConstraintLayout) ViewBindings.findChildViewById(view, C0082R.C0085id.mainVieasw);
                            if (mainVieasw2 != null) {
                                id = C0082R.C0085id.mainView;
                                ConstraintLayout mainView2 = (ConstraintLayout) ViewBindings.findChildViewById(view, C0082R.C0085id.mainView);
                                if (mainView2 != null) {
                                    id = C0082R.C0085id.rvMenu;
                                    RecyclerView rvMenu2 = (RecyclerView) ViewBindings.findChildViewById(view, C0082R.C0085id.rvMenu);
                                    if (rvMenu2 != null) {
                                        id = C0082R.C0085id.textView;
                                        TextView textView2 = (TextView) ViewBindings.findChildViewById(view, C0082R.C0085id.textView);
                                        if (textView2 != null) {
                                            id = C0082R.C0085id.tvMenuTitle;
                                            TextView tvMenuTitle2 = (TextView) ViewBindings.findChildViewById(view, C0082R.C0085id.tvMenuTitle);
                                            if (tvMenuTitle2 != null) {
                                                id = C0082R.C0085id.txtCancel;
                                                TextView txtCancel2 = (TextView) ViewBindings.findChildViewById(view, C0082R.C0085id.txtCancel);
                                                if (txtCancel2 != null) {
                                                    id = C0082R.C0085id.txtEnter;
                                                    TextView txtEnter2 = (TextView) ViewBindings.findChildViewById(view, C0082R.C0085id.txtEnter);
                                                    if (txtEnter2 != null) {
                                                        id = C0082R.C0085id.txtMainMenu;
                                                        TextView txtMainMenu2 = (TextView) ViewBindings.findChildViewById(view, C0082R.C0085id.txtMainMenu);
                                                        if (txtMainMenu2 != null) {
                                                            id = C0082R.C0085id.txtOK;
                                                            TextView txtOK2 = (TextView) ViewBindings.findChildViewById(view, C0082R.C0085id.txtOK);
                                                            if (txtOK2 != null) {
                                                                id = C0082R.C0085id.txtTitles;
                                                                TextView txtTitles2 = (TextView) ViewBindings.findChildViewById(view, C0082R.C0085id.txtTitles);
                                                                if (txtTitles2 != null) {
                                                                    id = C0082R.C0085id.txtcancel;
                                                                    TextView txtcancel2 = (TextView) ViewBindings.findChildViewById(view, C0082R.C0085id.txtcancel);
                                                                    if (txtcancel2 != null) {
                                                                        return new ActivityMainMenuBinding((ConstraintLayout) view, cardView2, clAmount2, clMainmenu2, cvTransactioncard2, editText2, mainVieasw2, mainView2, rvMenu2, textView2, tvMenuTitle2, txtCancel2, txtEnter2, txtMainMenu2, txtOK2, txtTitles2, txtcancel2);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(rootView2.getResources().getResourceName(id)));
    }
}
