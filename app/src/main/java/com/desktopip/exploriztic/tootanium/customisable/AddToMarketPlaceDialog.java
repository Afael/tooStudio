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
import android.widget.TextView;

import com.desktopip.exploriztic.tootanium.R;

public class AddToMarketPlaceDialog extends DialogFragment implements View.OnClickListener{

    View view;

    public AddToMarketPlaceDialog() {
        // Required empty public constructor
    }

    TextView add_to_marketplace_chosen_image_text;
    EditText add_to_marketplace_title, add_to_marketplace_price;
    Button add_to_marketplace_set_image_btn, add_to_marketplace_btn_close, add_to_marketplace_btn_save;
    Spinner add_to_marketplace_category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(inflater != null){
            view = inflater.inflate(R.layout.frag_add_to_market_place_dialog, container, false);

            add_to_marketplace_title = view.findViewById(R.id.add_to_marketplace_title);
            add_to_marketplace_price = view.findViewById(R.id.add_to_marketplace_price);
            add_to_marketplace_set_image_btn = view.findViewById(R.id.add_to_marketplace_set_image_btn);
            add_to_marketplace_chosen_image_text = view.findViewById(R.id.add_to_marketplace_chosen_image_text);
            add_to_marketplace_category = view.findViewById(R.id.add_to_marketplace_category);
            add_to_marketplace_btn_close = view.findViewById(R.id.add_to_marketplace_btn_close);
            add_to_marketplace_btn_save = view.findViewById(R.id.add_to_marketplace_btn_save);

            add_to_marketplace_set_image_btn.setOnClickListener(this);
            add_to_marketplace_btn_close.setOnClickListener(this);
            add_to_marketplace_btn_save.setOnClickListener(this);

            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setCancelable(false);

        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_to_marketplace_btn_close:
                getDialog().dismiss();
                break;
            case R.id.add_to_marketplace_btn_save:
                getDialog().dismiss();
                break;
            case R.id.add_to_marketplace_set_image_btn:
                getDialog().dismiss();
                break;
        }
    }
}
