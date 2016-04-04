package com.misura.dontforget.create;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import java.util.Calendar;

/**
 * Created by kmisura on 04/04/16.
 */
public class DatePickerFragment extends DialogFragment {
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    public void setListener(DatePickerDialog.OnDateSetListener dateSetListener) {
        mDateSetListener = dateSetListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), mDateSetListener, year, month, day);
    }
}
