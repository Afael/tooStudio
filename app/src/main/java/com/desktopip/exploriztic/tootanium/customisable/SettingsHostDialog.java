package com.desktopip.exploriztic.tootanium.customisable;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.activities.Login;
import com.desktopip.exploriztic.tootanium.utilities.Utils;

import java.net.MalformedURLException;
import java.net.URL;

import dmax.dialog.SpotsDialog;

public class SettingsHostDialog extends DialogFragment implements View.OnClickListener{

    View view;
    EditText session_server, session_port;
    CheckBox session_ssl;
    Button btn_save_setup_server, close_setup, test_connection;
    ProgressBar test_progress;

    String hostName, port, ssl;
    String http = "http:";

    boolean checkSSL = false;

    AlertDialog alertDialog;

    public SettingsHostDialog(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(inflater != null){
            view = inflater.inflate(R.layout.dip_setting, container, false);

            session_server = view.findViewById(R.id.session_server);
            session_port = view.findViewById(R.id.session_port);
            session_ssl = view.findViewById(R.id.session_ssl);
            test_connection = view.findViewById(R.id.test_connection);
            close_setup = view.findViewById(R.id.close_setup);
            btn_save_setup_server = view.findViewById(R.id.btn_save_setup_server);
            test_progress = view.findViewById(R.id.test_progress);

            Bundle hostSettingBundle = getArguments();
            hostName = hostSettingBundle.getString("hostName");
            port = hostSettingBundle.getString("port");
            ssl = hostSettingBundle.getString("ssl");

            session_server.setText(hostName);
            session_port.setText(port);
            if (ssl.equals("true")){
                session_ssl.setChecked(true);
            }

            setCancelable(false);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            test_connection.setOnClickListener(this);
            close_setup.setOnClickListener(this);
            btn_save_setup_server.setOnClickListener(this);
        }

        return view;
    }

    private void setVisibilityConnection(boolean status){
        if(status){
            test_connection.setVisibility(View.GONE);
            test_progress.setVisibility(View.VISIBLE);
        } else {
            test_connection.setVisibility(View.VISIBLE);
            test_progress.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.test_connection:

                try {

                    setVisibilityConnection(true);

                    if (session_ssl.isChecked())
                        http = "https:";
                    else
                        http = "http:";

                    hostName = session_server.getText().toString();
                    port = session_port.getText().toString();
                    Log.d("ping_util", "ping prepare");

                    if(Utils.pingServer(new URL(http+"//"+hostName+":"+port+"/"), getActivity())){
                        Log.d("ping_util", "ping success");
                        setVisibilityConnection(false);
                        Toast.makeText(getActivity(), "Connection Success", Toast.LENGTH_LONG).show();
                    } else {
                        setVisibilityConnection(false);
                        Log.d("ping_util", "ping refused");
                        Toast.makeText(getActivity(), "Connection refused", Toast.LENGTH_LONG).show();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.d("ping_util", "ping refused");
                    setVisibilityConnection(false);
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.close_setup:
                getDialog().dismiss();
                break;

            case R.id.btn_save_setup_server:
                hostName = session_server.getText().toString();
                port = session_port.getText().toString();
                if (session_ssl.isChecked()) {
                    checkSSL = true;
                } else {
                    checkSSL = false;
                }
                Log.d("ping_util", "port: "+ port);
                Login.setupPresenter.setup(hostName, port, checkSSL);
                break;
        }
    }
}
