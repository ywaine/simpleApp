package androidx.appcompat.app;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.C0005R;
import androidx.appcompat.app.AlertController;

public class AlertDialog extends AppCompatDialog implements DialogInterface {
    static final int LAYOUT_HINT_NONE = 0;
    static final int LAYOUT_HINT_SIDE = 1;
    final AlertController mAlert;

    protected AlertDialog(Context context) {
        this(context, 0);
    }

    protected AlertDialog(Context context, int themeResId) {
        super(context, resolveDialogTheme(context, themeResId));
        this.mAlert = new AlertController(getContext(), this, getWindow());
    }

    protected AlertDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        this(context, 0);
        setCancelable(cancelable);
        setOnCancelListener(cancelListener);
    }

    static int resolveDialogTheme(Context context, int resid) {
        if (((resid >>> 24) & 255) >= 1) {
            return resid;
        }
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(C0005R.attr.alertDialogTheme, outValue, true);
        return outValue.resourceId;
    }

    public Button getButton(int whichButton) {
        return this.mAlert.getButton(whichButton);
    }

    public ListView getListView() {
        return this.mAlert.getListView();
    }

    public void setTitle(CharSequence title) {
        super.setTitle(title);
        this.mAlert.setTitle(title);
    }

    public void setCustomTitle(View customTitleView) {
        this.mAlert.setCustomTitle(customTitleView);
    }

    public void setMessage(CharSequence message) {
        this.mAlert.setMessage(message);
    }

    public void setView(View view) {
        this.mAlert.setView(view);
    }

    public void setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
        this.mAlert.setView(view, viewSpacingLeft, viewSpacingTop, viewSpacingRight, viewSpacingBottom);
    }

    /* access modifiers changed from: package-private */
    public void setButtonPanelLayoutHint(int layoutHint) {
        this.mAlert.setButtonPanelLayoutHint(layoutHint);
    }

    public void setButton(int whichButton, CharSequence text, Message msg) {
        this.mAlert.setButton(whichButton, text, (DialogInterface.OnClickListener) null, msg, (Drawable) null);
    }

    public void setButton(int whichButton, CharSequence text, DialogInterface.OnClickListener listener) {
        this.mAlert.setButton(whichButton, text, listener, (Message) null, (Drawable) null);
    }

    public void setButton(int whichButton, CharSequence text, Drawable icon, DialogInterface.OnClickListener listener) {
        this.mAlert.setButton(whichButton, text, listener, (Message) null, icon);
    }

    public void setIcon(int resId) {
        this.mAlert.setIcon(resId);
    }

    public void setIcon(Drawable icon) {
        this.mAlert.setIcon(icon);
    }

    public void setIconAttribute(int attrId) {
        TypedValue out = new TypedValue();
        getContext().getTheme().resolveAttribute(attrId, out, true);
        this.mAlert.setIcon(out.resourceId);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mAlert.installContent();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.mAlert.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (this.mAlert.onKeyUp(keyCode, event)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public static class Builder {

        /* renamed from: P */
        private final AlertController.AlertParams f45P;
        private final int mTheme;

        public Builder(Context context) {
            this(context, AlertDialog.resolveDialogTheme(context, 0));
        }

        public Builder(Context context, int themeResId) {
            this.f45P = new AlertController.AlertParams(new ContextThemeWrapper(context, AlertDialog.resolveDialogTheme(context, themeResId)));
            this.mTheme = themeResId;
        }

        public Context getContext() {
            return this.f45P.mContext;
        }

        public Builder setTitle(int titleId) {
            AlertController.AlertParams alertParams = this.f45P;
            alertParams.mTitle = alertParams.mContext.getText(titleId);
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.f45P.mTitle = title;
            return this;
        }

        public Builder setCustomTitle(View customTitleView) {
            this.f45P.mCustomTitleView = customTitleView;
            return this;
        }

        public Builder setMessage(int messageId) {
            AlertController.AlertParams alertParams = this.f45P;
            alertParams.mMessage = alertParams.mContext.getText(messageId);
            return this;
        }

        public Builder setMessage(CharSequence message) {
            this.f45P.mMessage = message;
            return this;
        }

        public Builder setIcon(int iconId) {
            this.f45P.mIconId = iconId;
            return this;
        }

        public Builder setIcon(Drawable icon) {
            this.f45P.mIcon = icon;
            return this;
        }

        public Builder setIconAttribute(int attrId) {
            TypedValue out = new TypedValue();
            this.f45P.mContext.getTheme().resolveAttribute(attrId, out, true);
            this.f45P.mIconId = out.resourceId;
            return this;
        }

        public Builder setPositiveButton(int textId, DialogInterface.OnClickListener listener) {
            AlertController.AlertParams alertParams = this.f45P;
            alertParams.mPositiveButtonText = alertParams.mContext.getText(textId);
            this.f45P.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
            this.f45P.mPositiveButtonText = text;
            this.f45P.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButtonIcon(Drawable icon) {
            this.f45P.mPositiveButtonIcon = icon;
            return this;
        }

        public Builder setNegativeButton(int textId, DialogInterface.OnClickListener listener) {
            AlertController.AlertParams alertParams = this.f45P;
            alertParams.mNegativeButtonText = alertParams.mContext.getText(textId);
            this.f45P.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener) {
            this.f45P.mNegativeButtonText = text;
            this.f45P.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButtonIcon(Drawable icon) {
            this.f45P.mNegativeButtonIcon = icon;
            return this;
        }

        public Builder setNeutralButton(int textId, DialogInterface.OnClickListener listener) {
            AlertController.AlertParams alertParams = this.f45P;
            alertParams.mNeutralButtonText = alertParams.mContext.getText(textId);
            this.f45P.mNeutralButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(CharSequence text, DialogInterface.OnClickListener listener) {
            this.f45P.mNeutralButtonText = text;
            this.f45P.mNeutralButtonListener = listener;
            return this;
        }

        public Builder setNeutralButtonIcon(Drawable icon) {
            this.f45P.mNeutralButtonIcon = icon;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.f45P.mCancelable = cancelable;
            return this;
        }

        public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            this.f45P.mOnCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.f45P.mOnDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
            this.f45P.mOnKeyListener = onKeyListener;
            return this;
        }

        public Builder setItems(int itemsId, DialogInterface.OnClickListener listener) {
            AlertController.AlertParams alertParams = this.f45P;
            alertParams.mItems = alertParams.mContext.getResources().getTextArray(itemsId);
            this.f45P.mOnClickListener = listener;
            return this;
        }

        public Builder setItems(CharSequence[] items, DialogInterface.OnClickListener listener) {
            this.f45P.mItems = items;
            this.f45P.mOnClickListener = listener;
            return this;
        }

        public Builder setAdapter(ListAdapter adapter, DialogInterface.OnClickListener listener) {
            this.f45P.mAdapter = adapter;
            this.f45P.mOnClickListener = listener;
            return this;
        }

        public Builder setCursor(Cursor cursor, DialogInterface.OnClickListener listener, String labelColumn) {
            this.f45P.mCursor = cursor;
            this.f45P.mLabelColumn = labelColumn;
            this.f45P.mOnClickListener = listener;
            return this;
        }

        public Builder setMultiChoiceItems(int itemsId, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
            AlertController.AlertParams alertParams = this.f45P;
            alertParams.mItems = alertParams.mContext.getResources().getTextArray(itemsId);
            this.f45P.mOnCheckboxClickListener = listener;
            this.f45P.mCheckedItems = checkedItems;
            this.f45P.mIsMultiChoice = true;
            return this;
        }

        public Builder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
            this.f45P.mItems = items;
            this.f45P.mOnCheckboxClickListener = listener;
            this.f45P.mCheckedItems = checkedItems;
            this.f45P.mIsMultiChoice = true;
            return this;
        }

        public Builder setMultiChoiceItems(Cursor cursor, String isCheckedColumn, String labelColumn, DialogInterface.OnMultiChoiceClickListener listener) {
            this.f45P.mCursor = cursor;
            this.f45P.mOnCheckboxClickListener = listener;
            this.f45P.mIsCheckedColumn = isCheckedColumn;
            this.f45P.mLabelColumn = labelColumn;
            this.f45P.mIsMultiChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(int itemsId, int checkedItem, DialogInterface.OnClickListener listener) {
            AlertController.AlertParams alertParams = this.f45P;
            alertParams.mItems = alertParams.mContext.getResources().getTextArray(itemsId);
            this.f45P.mOnClickListener = listener;
            this.f45P.mCheckedItem = checkedItem;
            this.f45P.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(Cursor cursor, int checkedItem, String labelColumn, DialogInterface.OnClickListener listener) {
            this.f45P.mCursor = cursor;
            this.f45P.mOnClickListener = listener;
            this.f45P.mCheckedItem = checkedItem;
            this.f45P.mLabelColumn = labelColumn;
            this.f45P.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(CharSequence[] items, int checkedItem, DialogInterface.OnClickListener listener) {
            this.f45P.mItems = items;
            this.f45P.mOnClickListener = listener;
            this.f45P.mCheckedItem = checkedItem;
            this.f45P.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(ListAdapter adapter, int checkedItem, DialogInterface.OnClickListener listener) {
            this.f45P.mAdapter = adapter;
            this.f45P.mOnClickListener = listener;
            this.f45P.mCheckedItem = checkedItem;
            this.f45P.mIsSingleChoice = true;
            return this;
        }

        public Builder setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
            this.f45P.mOnItemSelectedListener = listener;
            return this;
        }

        public Builder setView(int layoutResId) {
            this.f45P.mView = null;
            this.f45P.mViewLayoutResId = layoutResId;
            this.f45P.mViewSpacingSpecified = false;
            return this;
        }

        public Builder setView(View view) {
            this.f45P.mView = view;
            this.f45P.mViewLayoutResId = 0;
            this.f45P.mViewSpacingSpecified = false;
            return this;
        }

        @Deprecated
        public Builder setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
            this.f45P.mView = view;
            this.f45P.mViewLayoutResId = 0;
            this.f45P.mViewSpacingSpecified = true;
            this.f45P.mViewSpacingLeft = viewSpacingLeft;
            this.f45P.mViewSpacingTop = viewSpacingTop;
            this.f45P.mViewSpacingRight = viewSpacingRight;
            this.f45P.mViewSpacingBottom = viewSpacingBottom;
            return this;
        }

        @Deprecated
        public Builder setInverseBackgroundForced(boolean useInverseBackground) {
            this.f45P.mForceInverseBackground = useInverseBackground;
            return this;
        }

        public Builder setRecycleOnMeasureEnabled(boolean enabled) {
            this.f45P.mRecycleOnMeasure = enabled;
            return this;
        }

        public AlertDialog create() {
            AlertDialog dialog = new AlertDialog(this.f45P.mContext, this.mTheme);
            this.f45P.apply(dialog.mAlert);
            dialog.setCancelable(this.f45P.mCancelable);
            if (this.f45P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(this.f45P.mOnCancelListener);
            dialog.setOnDismissListener(this.f45P.mOnDismissListener);
            if (this.f45P.mOnKeyListener != null) {
                dialog.setOnKeyListener(this.f45P.mOnKeyListener);
            }
            return dialog;
        }

        public AlertDialog show() {
            AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
