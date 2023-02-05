package com.google.android.material.datepicker;

import android.content.Context;
import android.widget.BaseAdapter;

class MonthAdapter extends BaseAdapter {
    static final int MAXIMUM_WEEKS = UtcDates.getUtcCalendar().getMaximum(4);
    final CalendarConstraints calendarConstraints;
    CalendarStyle calendarStyle;
    final DateSelector<?> dateSelector;
    final Month month;

    MonthAdapter(Month month2, DateSelector<?> dateSelector2, CalendarConstraints calendarConstraints2) {
        this.month = month2;
        this.dateSelector = dateSelector2;
        this.calendarConstraints = calendarConstraints2;
    }

    public boolean hasStableIds() {
        return true;
    }

    public Long getItem(int position) {
        if (position < this.month.daysFromStartOfWeekToFirstOfMonth() || position > lastPositionInMonth()) {
            return null;
        }
        return Long.valueOf(this.month.getDay(positionToDay(position)));
    }

    public long getItemId(int position) {
        return (long) (position / this.month.daysInWeek);
    }

    public int getCount() {
        return this.month.daysInMonth + firstPositionInMonth();
    }

    /* JADX WARNING: type inference failed for: r3v6, types: [android.view.View] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.widget.TextView getView(int r12, android.view.View r13, android.view.ViewGroup r14) {
        /*
            r11 = this;
            android.content.Context r0 = r14.getContext()
            r11.initializeStyles(r0)
            r0 = r13
            android.widget.TextView r0 = (android.widget.TextView) r0
            r1 = 0
            if (r13 != 0) goto L_0x001e
            android.content.Context r2 = r14.getContext()
            android.view.LayoutInflater r2 = android.view.LayoutInflater.from(r2)
            int r3 = com.google.android.material.C0077R.layout.mtrl_calendar_day
            android.view.View r3 = r2.inflate(r3, r14, r1)
            r0 = r3
            android.widget.TextView r0 = (android.widget.TextView) r0
        L_0x001e:
            int r2 = r11.firstPositionInMonth()
            int r2 = r12 - r2
            r3 = 1
            if (r2 < 0) goto L_0x0064
            com.google.android.material.datepicker.Month r4 = r11.month
            int r4 = r4.daysInMonth
            if (r2 < r4) goto L_0x002e
            goto L_0x0064
        L_0x002e:
            int r4 = r2 + 1
            com.google.android.material.datepicker.Month r5 = r11.month
            r0.setTag(r5)
            java.lang.String r5 = java.lang.String.valueOf(r4)
            r0.setText(r5)
            com.google.android.material.datepicker.Month r5 = r11.month
            long r5 = r5.getDay(r4)
            com.google.android.material.datepicker.Month r7 = r11.month
            int r7 = r7.year
            com.google.android.material.datepicker.Month r8 = com.google.android.material.datepicker.Month.current()
            int r8 = r8.year
            if (r7 != r8) goto L_0x0056
            java.lang.String r7 = com.google.android.material.datepicker.DateStrings.getMonthDayOfWeekDay(r5)
            r0.setContentDescription(r7)
            goto L_0x005d
        L_0x0056:
            java.lang.String r7 = com.google.android.material.datepicker.DateStrings.getYearMonthDayOfWeekDay(r5)
            r0.setContentDescription(r7)
        L_0x005d:
            r0.setVisibility(r1)
            r0.setEnabled(r3)
            goto L_0x006c
        L_0x0064:
            r4 = 8
            r0.setVisibility(r4)
            r0.setEnabled(r1)
        L_0x006c:
            java.lang.Long r4 = r11.getItem((int) r12)
            if (r4 != 0) goto L_0x0073
            return r0
        L_0x0073:
            com.google.android.material.datepicker.CalendarConstraints r5 = r11.calendarConstraints
            com.google.android.material.datepicker.CalendarConstraints$DateValidator r5 = r5.getDateValidator()
            long r6 = r4.longValue()
            boolean r5 = r5.isValid(r6)
            if (r5 == 0) goto L_0x00d9
            r0.setEnabled(r3)
            com.google.android.material.datepicker.DateSelector<?> r1 = r11.dateSelector
            java.util.Collection r1 = r1.getSelectedDays()
            java.util.Iterator r1 = r1.iterator()
        L_0x0090:
            boolean r3 = r1.hasNext()
            if (r3 == 0) goto L_0x00b9
            java.lang.Object r3 = r1.next()
            java.lang.Long r3 = (java.lang.Long) r3
            long r5 = r3.longValue()
            long r7 = r4.longValue()
            long r7 = com.google.android.material.datepicker.UtcDates.canonicalYearMonthDay(r7)
            long r9 = com.google.android.material.datepicker.UtcDates.canonicalYearMonthDay(r5)
            int r3 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r3 != 0) goto L_0x00b8
            com.google.android.material.datepicker.CalendarStyle r1 = r11.calendarStyle
            com.google.android.material.datepicker.CalendarItemStyle r1 = r1.selectedDay
            r1.styleItem(r0)
            return r0
        L_0x00b8:
            goto L_0x0090
        L_0x00b9:
            java.util.Calendar r1 = com.google.android.material.datepicker.UtcDates.getTodayCalendar()
            long r5 = r1.getTimeInMillis()
            long r7 = r4.longValue()
            int r1 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r1 != 0) goto L_0x00d1
            com.google.android.material.datepicker.CalendarStyle r1 = r11.calendarStyle
            com.google.android.material.datepicker.CalendarItemStyle r1 = r1.todayDay
            r1.styleItem(r0)
            return r0
        L_0x00d1:
            com.google.android.material.datepicker.CalendarStyle r1 = r11.calendarStyle
            com.google.android.material.datepicker.CalendarItemStyle r1 = r1.day
            r1.styleItem(r0)
            return r0
        L_0x00d9:
            r0.setEnabled(r1)
            com.google.android.material.datepicker.CalendarStyle r1 = r11.calendarStyle
            com.google.android.material.datepicker.CalendarItemStyle r1 = r1.invalidDay
            r1.styleItem(r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.datepicker.MonthAdapter.getView(int, android.view.View, android.view.ViewGroup):android.widget.TextView");
    }

    private void initializeStyles(Context context) {
        if (this.calendarStyle == null) {
            this.calendarStyle = new CalendarStyle(context);
        }
    }

    /* access modifiers changed from: package-private */
    public int firstPositionInMonth() {
        return this.month.daysFromStartOfWeekToFirstOfMonth();
    }

    /* access modifiers changed from: package-private */
    public int lastPositionInMonth() {
        return (this.month.daysFromStartOfWeekToFirstOfMonth() + this.month.daysInMonth) - 1;
    }

    /* access modifiers changed from: package-private */
    public int positionToDay(int position) {
        return (position - this.month.daysFromStartOfWeekToFirstOfMonth()) + 1;
    }

    /* access modifiers changed from: package-private */
    public int dayToPosition(int day) {
        return firstPositionInMonth() + (day - 1);
    }

    /* access modifiers changed from: package-private */
    public boolean withinMonth(int position) {
        return position >= firstPositionInMonth() && position <= lastPositionInMonth();
    }

    /* access modifiers changed from: package-private */
    public boolean isFirstInRow(int position) {
        return position % this.month.daysInWeek == 0;
    }

    /* access modifiers changed from: package-private */
    public boolean isLastInRow(int position) {
        return (position + 1) % this.month.daysInWeek == 0;
    }
}
