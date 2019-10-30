package com.desktopip.exploriztic.tootanium.customisable;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.desktopip.exploriztic.tootanium.R;

public class FindHelperDialog extends DialogFragment implements View.OnClickListener{

    View view;

    EditText find_helper_job_title, find_helper_job_price, find_helper_timeline;
    Spinner find_helper_skill_category;
    Button find_helper_btn_close, find_helper_btn_save;

    public FindHelperDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(inflater != null){

            view = inflater.inflate(R.layout.frag_find_helper_dialog, container, false);

            find_helper_job_title = view.findViewById(R.id.find_helper_job_title);
            find_helper_job_price = view.findViewById(R.id.find_helper_job_price);
            find_helper_timeline = view.findViewById(R.id.find_helper_timeline);
            find_helper_skill_category = view.findViewById(R.id.find_helper_skill_category);
            find_helper_btn_close = view.findViewById(R.id.find_helper_btn_close);
            find_helper_btn_save = view.findViewById(R.id.find_helper_btn_save);

            find_helper_btn_close.setOnClickListener(this);
            find_helper_btn_save.setOnClickListener(this);

            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setCancelable(false);
        }

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.find_helper_btn_close:
                getDialog().dismiss();
                break;
            case R.id.find_helper_btn_save:
                getDialog().dismiss();
                break;
        }
    }
}
