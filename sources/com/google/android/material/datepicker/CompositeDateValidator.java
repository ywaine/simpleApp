package com.google.android.material.datepicker;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.util.Preconditions;
import com.google.android.material.datepicker.CalendarConstraints;
import java.util.List;

public final class CompositeDateValidator implements CalendarConstraints.DateValidator {
    public static final Parcelable.Creator<CompositeDateValidator> CREATOR = new Parcelable.Creator<CompositeDateValidator>() {
        public CompositeDateValidator createFromParcel(Parcel source) {
            return new CompositeDateValidator((List) Preconditions.checkNotNull(source.readArrayList(CalendarConstraints.DateValidator.class.getClassLoader())));
        }

        public CompositeDateValidator[] newArray(int size) {
            return new CompositeDateValidator[size];
        }
    };
    private final List<CalendarConstraints.DateValidator> validators;

    private CompositeDateValidator(List<CalendarConstraints.DateValidator> validators2) {
        this.validators = validators2;
    }

    public static CalendarConstraints.DateValidator allOf(List<CalendarConstraints.DateValidator> validators2) {
        return new CompositeDateValidator(validators2);
    }

    public boolean isValid(long date) {
        for (CalendarConstraints.DateValidator validator : this.validators) {
            if (validator != null && !validator.isValid(date)) {
                return false;
            }
        }
        return true;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.validators);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompositeDateValidator)) {
            return false;
        }
        return this.validators.equals(((CompositeDateValidator) o).validators);
    }

    public int hashCode() {
        return this.validators.hashCode();
    }
}
