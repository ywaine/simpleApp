package com.google.android.material.datepicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;
import androidx.core.util.Pair;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.C0077R;
import java.util.Calendar;
import java.util.Iterator;

final class MaterialCalendarGridView extends GridView {
    private final Calendar dayCompute;

    public MaterialCalendarGridView(Context context) {
        this(context, (AttributeSet) null);
    }

    public MaterialCalendarGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialCalendarGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.dayCompute = UtcDates.getUtcCalendar();
        if (MaterialDatePicker.isFullscreen(getContext())) {
            setNextFocusLeftId(C0077R.C0080id.cancel_button);
            setNextFocusRightId(C0077R.C0080id.confirm_button);
        }
        ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegateCompat() {
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                accessibilityNodeInfoCompat.setCollectionInfo((Object) null);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getAdapter().notifyDataSetChanged();
    }

    public void setSelection(int position) {
        if (position < getAdapter().firstPositionInMonth()) {
            super.setSelection(getAdapter().firstPositionInMonth());
        } else {
            super.setSelection(position);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!super.onKeyDown(keyCode, event)) {
            return false;
        }
        if (getSelectedItemPosition() == -1 || getSelectedItemPosition() >= getAdapter().firstPositionInMonth()) {
            return true;
        }
        if (19 != keyCode) {
            return false;
        }
        setSelection(getAdapter().firstPositionInMonth());
        return true;
    }

    public MonthAdapter getAdapter() {
        return (MonthAdapter) super.getAdapter();
    }

    public final void setAdapter(ListAdapter adapter) {
        if (adapter instanceof MonthAdapter) {
            super.setAdapter(adapter);
        } else {
            throw new IllegalArgumentException(String.format("%1$s must have its Adapter set to a %2$s", new Object[]{MaterialCalendarGridView.class.getCanonicalName(), MonthAdapter.class.getCanonicalName()}));
        }
    }

    /* access modifiers changed from: protected */
    public final void onDraw(Canvas canvas) {
        int rangeHighlightStart;
        int firstHighlightPosition;
        int rangeHighlightEnd;
        int lastHighlightPosition;
        MaterialCalendarGridView materialCalendarGridView = this;
        super.onDraw(canvas);
        MonthAdapter monthAdapter = getAdapter();
        DateSelector<?> dateSelector = monthAdapter.dateSelector;
        CalendarStyle calendarStyle = monthAdapter.calendarStyle;
        Long firstOfMonth = monthAdapter.getItem(monthAdapter.firstPositionInMonth());
        Long lastOfMonth = monthAdapter.getItem(monthAdapter.lastPositionInMonth());
        Iterator<Pair<Long, Long>> it = dateSelector.getSelectedRanges().iterator();
        while (it.hasNext()) {
            Pair<Long, Long> range = it.next();
            if (range.first == null) {
                DateSelector<?> dateSelector2 = dateSelector;
                Long l = firstOfMonth;
                Long l2 = lastOfMonth;
                Iterator<Pair<Long, Long>> it2 = it;
                Pair<Long, Long> pair = range;
                materialCalendarGridView = this;
            } else if (range.second == null) {
                continue;
            } else {
                long startItem = ((Long) range.first).longValue();
                long endItem = ((Long) range.second).longValue();
                if (!skipMonth(firstOfMonth, lastOfMonth, Long.valueOf(startItem), Long.valueOf(endItem))) {
                    if (startItem < firstOfMonth.longValue()) {
                        firstHighlightPosition = monthAdapter.firstPositionInMonth();
                        if (monthAdapter.isFirstInRow(firstHighlightPosition)) {
                            rangeHighlightStart = 0;
                        } else {
                            rangeHighlightStart = materialCalendarGridView.getChildAt(firstHighlightPosition - 1).getRight();
                        }
                    } else {
                        materialCalendarGridView.dayCompute.setTimeInMillis(startItem);
                        firstHighlightPosition = monthAdapter.dayToPosition(materialCalendarGridView.dayCompute.get(5));
                        rangeHighlightStart = horizontalMidPoint(materialCalendarGridView.getChildAt(firstHighlightPosition));
                    }
                    if (endItem > lastOfMonth.longValue()) {
                        lastHighlightPosition = Math.min(monthAdapter.lastPositionInMonth(), getChildCount() - 1);
                        if (monthAdapter.isLastInRow(lastHighlightPosition)) {
                            rangeHighlightEnd = getWidth();
                        } else {
                            rangeHighlightEnd = materialCalendarGridView.getChildAt(lastHighlightPosition).getRight();
                        }
                    } else {
                        materialCalendarGridView.dayCompute.setTimeInMillis(endItem);
                        lastHighlightPosition = monthAdapter.dayToPosition(materialCalendarGridView.dayCompute.get(5));
                        rangeHighlightEnd = horizontalMidPoint(materialCalendarGridView.getChildAt(lastHighlightPosition));
                    }
                    Long firstOfMonth2 = firstOfMonth;
                    Long lastOfMonth2 = lastOfMonth;
                    int firstRow = (int) monthAdapter.getItemId(firstHighlightPosition);
                    Iterator<Pair<Long, Long>> it3 = it;
                    Pair<Long, Long> pair2 = range;
                    int lastRow = (int) monthAdapter.getItemId(lastHighlightPosition);
                    int row = firstRow;
                    while (row <= lastRow) {
                        MonthAdapter monthAdapter2 = monthAdapter;
                        int firstPositionInRow = row * getNumColumns();
                        DateSelector<?> dateSelector3 = dateSelector;
                        int lastPositionInRow = (firstPositionInRow + getNumColumns()) - 1;
                        View firstView = materialCalendarGridView.getChildAt(firstPositionInRow);
                        int top = firstView.getTop() + calendarStyle.day.getTopInset();
                        Iterator<Pair<Long, Long>> it4 = it3;
                        int bottom = firstView.getBottom() - calendarStyle.day.getBottomInset();
                        int left = firstPositionInRow > firstHighlightPosition ? 0 : rangeHighlightStart;
                        int right = lastHighlightPosition > lastPositionInRow ? getWidth() : rangeHighlightEnd;
                        int i = firstPositionInRow;
                        int left2 = left;
                        int left3 = lastPositionInRow;
                        float f = (float) left2;
                        int i2 = left2;
                        float f2 = (float) top;
                        int i3 = top;
                        int top2 = right;
                        int i4 = top2;
                        int i5 = bottom;
                        canvas.drawRect(f, f2, (float) top2, (float) bottom, calendarStyle.rangeFill);
                        row++;
                        materialCalendarGridView = this;
                        monthAdapter = monthAdapter2;
                        dateSelector = dateSelector3;
                        it3 = it4;
                        firstRow = firstRow;
                    }
                    DateSelector<?> dateSelector4 = dateSelector;
                    Iterator<Pair<Long, Long>> it5 = it3;
                    int i6 = firstRow;
                    materialCalendarGridView = this;
                    firstOfMonth = firstOfMonth2;
                    lastOfMonth = lastOfMonth2;
                    it = it5;
                } else {
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        if (gainFocus) {
            gainFocus(direction, previouslyFocusedRect);
        } else {
            super.onFocusChanged(false, direction, previouslyFocusedRect);
        }
    }

    private void gainFocus(int direction, Rect previouslyFocusedRect) {
        if (direction == 33) {
            setSelection(getAdapter().lastPositionInMonth());
        } else if (direction == 130) {
            setSelection(getAdapter().firstPositionInMonth());
        } else {
            super.onFocusChanged(true, direction, previouslyFocusedRect);
        }
    }

    private static boolean skipMonth(Long firstOfMonth, Long lastOfMonth, Long startDay, Long endDay) {
        if (firstOfMonth == null || lastOfMonth == null || startDay == null || endDay == null || startDay.longValue() > lastOfMonth.longValue() || endDay.longValue() < firstOfMonth.longValue()) {
            return true;
        }
        return false;
    }

    private static int horizontalMidPoint(View view) {
        return view.getLeft() + (view.getWidth() / 2);
    }
}
