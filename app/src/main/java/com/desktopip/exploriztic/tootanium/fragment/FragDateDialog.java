package com.desktopip.exploriztic.tootanium.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by Jayus on 1/18/2017.
 */

@SuppressLint("ValidFragment")
public class FragDateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    EditText etDate;

    public FragDateDialog(View view) {
        etDate = (EditText) view;
    }

    public Dialog onCreateDialog(Bundle saveInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        @SuppressLint("WrongConstant") int year = calendar.get(Calendar.YEAR);
        @SuppressLint("WrongConstant") int month = calendar.get(Calendar.MONTH);
        @SuppressLint("WrongConstant") int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = year + "-" + (month + 1) + "-" + dayOfMonth;
        etDate.setText(date);
    }
}
