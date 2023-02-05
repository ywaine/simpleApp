package com.google.android.material.datepicker;

import android.os.Build;
import android.widget.BaseAdapter;
import java.util.Calendar;

class DaysOfWeekAdapter extends BaseAdapter {
    private static final int CALENDAR_DAY_STYLE = (Build.VERSION.SDK_INT >= 26 ? 4 : 1);
    private static final int NARROW_FORMAT = 4;
    private final Calendar calendar;
    private final int daysInWeek;
    private final int firstDayOfWeek;

    public DaysOfWeekAdapter() {
        Calendar utcCalendar = UtcDates.getUtcCalendar();
        this.calendar = utcCalendar;
        this.daysInWeek = utcCalendar.getMaximum(7);
        this.firstDayOfWeek = utcCalendar.getFirstDayOfWeek();
    }

    public Integer getItem(int position) {
        if (position >= this.daysInWeek) {
            return null;
        }
        return Integer.valueOf(positionToDayOfWeek(position));
    }

    public long getItemId(int position) {
        return 0;
    }

    public int getCount() {
        return this.daysInWeek;
    }

    /* JADX WARNING: type inference failed for: r3v6, types: [android.view.View] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.view.View getView(int r9, android.view.View r10, android.view.ViewGroup r11) {
        /*
            r8 = this;
            r0 = r10
            android.widget.TextView r0 = (android.widget.TextView) r0
            r1 = 0
            if (r10 != 0) goto L_0x0017
            android.content.Context r2 = r11.getContext()
            android.view.LayoutInflater r2 = android.view.LayoutInflater.from(r2)
            int r3 = com.google.android.material.C0077R.layout.mtrl_calendar_day_of_week
            android.view.View r3 = r2.inflate(r3, r11, r1)
            r0 = r3
            android.widget.TextView r0 = (android.widget.TextView) r0
        L_0x0017:
            java.util.Calendar r2 = r8.calendar
            int r3 = r8.positionToDayOfWeek(r9)
            r4 = 7
            r2.set(r4, r3)
            java.util.Calendar r2 = r8.calendar
            int r3 = CALENDAR_DAY_STYLE
            java.util.Locale r5 = java.util.Locale.getDefault()
            java.lang.String r2 = r2.getDisplayName(r4, r3, r5)
            r0.setText(r2)
            android.content.Context r2 = r11.getContext()
            int r3 = com.google.android.material.C0077R.string.mtrl_picker_day_of_week_column_header
            java.lang.String r2 = r2.getString(r3)
            r3 = 1
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.util.Calendar r5 = r8.calendar
            r6 = 2
            java.util.Locale r7 = java.util.Locale.getDefault()
            java.lang.String r4 = r5.getDisplayName(r4, r6, r7)
            r3[r1] = r4
            java.lang.String r1 = java.lang.String.format(r2, r3)
            r0.setContentDescription(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.datepicker.DaysOfWeekAdapter.getView(int, android.view.View, android.view.ViewGroup):android.view.View");
    }

    private int positionToDayOfWeek(int position) {
        int dayConstant = this.firstDayOfWeek + position;
        int i = this.daysInWeek;
        if (dayConstant > i) {
            return dayConstant - i;
        }
        return dayConstant;
    }
}
