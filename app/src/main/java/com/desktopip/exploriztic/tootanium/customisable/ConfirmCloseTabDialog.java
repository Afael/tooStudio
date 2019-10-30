package com.desktopip.exploriztic.tootanium.customisable;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.fragment.FragWorkingStudio;

public class ConfirmCloseTabDialog extends DialogFragment implements View.OnClickListener{

    View view;
    // widget
    TextView dialog_confirm_message;
    Button dialog_confirm_cancel, dialog_confirm_ok;
    int tabPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if(inflater != null){
            view = inflater.inflate(R.layout.custom_popup_confirm, container, false);
            dialog_confirm_message = view.findViewById(R.id.dialog_confirm_message);
            dialog_confirm_cancel = view.findViewById(R.id.dialog_confirm_cancel);
            dialog_confirm_ok = view.findViewById(R.id.dialog_confirm_ok);

            Bundle bundle = getArguments();
            String message = bundle.getString("message");
            tabPosition = bundle.getInt("position");

            dialog_confirm_message.setText(message);
            dialog_confirm_ok.setText("Yes");

            dialog_confirm_cancel.setOnClickListener(this);
            dialog_confirm_ok.setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.dialog_confirm_cancel:
                getDialog().dismiss();
                break;
            case R.id.dialog_confirm_ok:

                FragWorkingStudio.fragmentList.remove(tabPosition);
                FragWorkingStudio.adapter.deleteFragmentFromList(FragWorkingStudio.fragmentToDelete, FragWorkingStudio.showFragment);
                getDialog().dismiss();
                break;
        }
    }
}

