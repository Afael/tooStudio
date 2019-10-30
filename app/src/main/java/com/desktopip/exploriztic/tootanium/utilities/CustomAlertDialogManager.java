package com.desktopip.exploriztic.tootanium.utilities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.desktopip.exploriztic.tootanium.R;

/**
 * Created by Jayus on 26/07/2018.
 */

public class CustomAlertDialogManager {

    Button dialog_button;
    TextView dialog_close, dialog_title, dialog_message;
    Dialog customDialog;

    public void showAlertDialog(Context context, String title, String message, String button) {

        customDialog = new Dialog(context);
        customDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.custom_popup);
        //dialog_close = customDialog.findViewById(R.id.dialog_close);
        dialog_title = customDialog.findViewById(R.id.dialog_title);
        dialog_message = customDialog.findViewById(R.id.dialog_message);
        dialog_button = customDialog.findViewById(R.id.dialog_button);
        //dialog_close.setVisibility(View.GONE);
        dialog_title.setText(title);
        dialog_message.setText(message);
        dialog_button.setText(button);

        customDialog.setCancelable(false);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.show();

        dialog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });

    }
}
