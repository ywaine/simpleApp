package com.google.android.material.datepicker;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import com.google.android.material.C0077R;
import com.google.android.material.textfield.TextInputLayout;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

abstract class DateFormatTextWatcher implements TextWatcher {
    private final CalendarConstraints constraints;
    private final DateFormat dateFormat;
    private final String formatHint;
    private final String outOfRange;
    private final TextInputLayout textInputLayout;

    /* access modifiers changed from: package-private */
    public abstract void onValidDate(Long l);

    DateFormatTextWatcher(String formatHint2, DateFormat dateFormat2, TextInputLayout textInputLayout2, CalendarConstraints constraints2) {
        this.formatHint = formatHint2;
        this.dateFormat = dateFormat2;
        this.textInputLayout = textInputLayout2;
        this.constraints = constraints2;
        this.outOfRange = textInputLayout2.getContext().getString(C0077R.string.mtrl_picker_out_of_range);
    }

    /* access modifiers changed from: package-private */
    public void onInvalidDate() {
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s)) {
            this.textInputLayout.setError((CharSequence) null);
            onValidDate((Long) null);
            return;
        }
        try {
            Date date = this.dateFormat.parse(s.toString());
            this.textInputLayout.setError((CharSequence) null);
            long milliseconds = date.getTime();
            if (!this.constraints.getDateValidator().isValid(milliseconds) || !this.constraints.isWithinBounds(milliseconds)) {
                this.textInputLayout.setError(String.format(this.outOfRange, new Object[]{DateStrings.getDateString(milliseconds)}));
                onInvalidDate();
                return;
            }
            onValidDate(Long.valueOf(date.getTime()));
        } catch (ParseException e) {
            String invalidFormat = this.textInputLayout.getContext().getString(C0077R.string.mtrl_picker_invalid_format);
            String useLine = String.format(this.textInputLayout.getContext().getString(C0077R.string.mtrl_picker_invalid_format_use), new Object[]{this.formatHint});
            String exampleLine = String.format(this.textInputLayout.getContext().getString(C0077R.string.mtrl_picker_invalid_format_example), new Object[]{this.dateFormat.format(new Date(UtcDates.getTodayCalendar().getTimeInMillis()))});
            TextInputLayout textInputLayout2 = this.textInputLayout;
            textInputLayout2.setError(invalidFormat + "\n" + useLine + "\n" + exampleLine);
            onInvalidDate();
        }
    }

    public void afterTextChanged(Editable s) {
    }
}
