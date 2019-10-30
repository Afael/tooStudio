package com.desktopip.exploriztic.tootanium.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.desktopip.exploriztic.tootanium.MainActivity;
import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.customisable.SettingsHostDialog;
import com.desktopip.exploriztic.tootanium.presenter.LoginPresenter;
import com.desktopip.exploriztic.tootanium.presenter.LoginPresenterImp;
import com.desktopip.exploriztic.tootanium.presenter.LoginView;
import com.desktopip.exploriztic.tootanium.presenter.SetupPresenter;
import com.desktopip.exploriztic.tootanium.presenter.SetupPresenterImp;
import com.desktopip.exploriztic.tootanium.presenter.SetupView;
import com.desktopip.exploriztic.tootanium.utilities.CheckInternetConnection;
import com.desktopip.exploriztic.tootanium.utilities.CustomAlertDialogManager;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.utilities.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import okhttp3.internal.Util;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class Login extends AppCompatActivity implements /*EasyPermissions.PermissionCallbacks, */View.OnClickListener
        , LoginView, SetupView, ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    final String TAG = Login.class.getSimpleName();

    public static SetupPresenter setupPresenter;
    LoginPresenter loginPresenter;
    EditText edit_username, edit_password;
    Button btn_login, dialog_button, dialog_confirm_cancel, dialog_confirm_ok, btn_to_setup;
    TextView dialog_title, dialog_message, dialog_confirm_message;

    Dialog customDialog;
    CustomAlertDialogManager alert;
    SessionManager session;

    SettingsHostDialog settingsHostDialog;

    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    String sLatitude, sLongitude, ipAddress, hostName, port, ssl, http = "http:";
    boolean checkSSL = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        requestWindowFeature( Window.FEATURE_NO_TITLE );

        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );

        setContentView(R.layout.dip_login);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

//        if(checkPermission()){
//            init();
//        }
        init();
    }

    private void init(){

        setupPresenter = new SetupPresenterImp(this, Login.this);
        loginPresenter = new LoginPresenterImp(this, Login.this);

        btn_login = findViewById(R.id.btn_login);
        btn_to_setup = findViewById(R.id.btn_to_setup);
        edit_username = findViewById(R.id.edit_username);
        edit_password = findViewById(R.id.edit_password);
        btn_to_setup.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        session = new SessionManager(getApplicationContext());

        HashMap<String, String> setup = session.getSetupDetails();
        hostName = setup.get(SessionManager.HOST_NAME);
        port = setup.get(SessionManager.PORT);
        ssl = setup.get(SessionManager.SSL);

        customDialog = new Dialog(this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        alert = new CustomAlertDialogManager();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        settingsHostDialog = new SettingsHostDialog();
    }

    @AfterPermissionGranted(123)
    private boolean checkPermission() {
        String[] perms = {
                Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.ACCESS_NETWORK_STATE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.CAMERA
        };
        if (EasyPermissions.hasPermissions(this, perms)) {
            return true;
        } else {
            EasyPermissions.requestPermissions(this, "We need permissions because this apps should be access your local storage",
                    123, perms);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        permissionManager.checkResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_to_setup:
                showHostSettingsDialog();
                break;
            case R.id.btn_login:
                if (CheckInternetConnection.checknetwork(this)) {
                    ipAddress = getLocalIpAddress();
                    sLatitude = String.valueOf(currentLatitude);
                    sLongitude = String.valueOf(currentLongitude);
                    String sUserName = edit_username.getText().toString();
                    String sPassword = edit_password.getText().toString();

                    HashMap<String, String> setup = session.getSetupDetails();
                    hostName = setup.get(SessionManager.HOST_NAME);
                    port = setup.get(SessionManager.PORT);
                    ssl = setup.get(SessionManager.SSL);

                    if (checkSSL)
                        http = "https:";
                    else
                        http = "http:";

                    try {
                        if(Utils.pingServer(new URL(http+"//"+hostName+":"+port+"/"), this)){
                            loginPresenter.login(
                                    sUserName
                                    , sPassword
                                    , ipAddress
                                    , sLatitude
                                    , sLongitude
                                    , hostName
                                    , port
                                    , checkSSL
                            );
                        } else {
                            //alert.showAlertDialog(this, "Sign in Failed", getString(R.string.warning_setup_url_wrong), "Close");
                        }

                    } catch (MalformedURLException e) {
                        //e.printStackTrace();
                        Log.d("Login: ", e.toString() + "Port: "+port);
                        alert.showAlertDialog(this, "Connection Error", e.getMessage(), "Close");
                    }

                } else {
                    Toast.makeText(Login.this, getString(R.string.warning_no_internet), Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showAlertDialog("Confirm Exit", getString(R.string.warning_confirm_exit), "Yes Exit", false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        if(checkPermission())
            mGoogleApiClient.connect();


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if(checkPermission()){
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
        }

    }

    @Override
    public void showValidationError() {
        alert.showAlertDialog(this, "Sign in Failed", getString(R.string.validation_input_login), "Close");
    }

    @Override
    public void showValidationSetupError() {
        alert.showAlertDialog(this, "Setup Failed", getString(R.string.validation_input_setup), "Close");
    }

    @Override
    public void setupSuccess(String success) {
        customDialog.cancel();
        alert.showAlertDialog(this, "Setup Completed", success, "Close");
    }

    @Override
    public void setupFailed(String failed) {
        alert.showAlertDialog(this, "Setup Failed", failed, "Close");
    }

    @Override
    public void setupError(String error) {

    }

    @Override
    public void loginSuccess(JSONObject result) {
        Log.d("login", result.toString());
        try {
            JSONObject jsonObject = result.getJSONObject("message");
            String level = jsonObject.getString("level");
            if (level.equals("USER")) {
                String uid = jsonObject.getString("uid");
                String user = jsonObject.getString("user");
                String pass = jsonObject.getString("password");
                String decodeUser = new String(Base64.decode(user, Base64.DEFAULT));
                String decodePassword = new String(Base64.decode(pass, Base64.DEFAULT));

                session = new SessionManager(this);
                session.createSessionLogin(uid, decodeUser, decodePassword, sLatitude, sLongitude);
                showAlertDialog("Login Success", getString(R.string.message_login_success) +" "+ decodeUser, "Continue", true);
                Log.d("uid_login", uid);
            } else {
                showAlertDialog("Warning!", getString(R.string.warning_login_admin_account), "Close", true);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void loginFailed(JSONObject failed) {
        try {
            String failedMsg = failed.getString("message");
            alert.showAlertDialog(this, "Sign in Failed", getString(R.string.warning_setup_url_wrong), "Close");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loginError(VolleyError error) {
        Utils.handlingVolleyError(this, error);
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    private void showHostSettingsDialog() {

        Bundle hostSettingsBundle = new Bundle();
        hostSettingsBundle.putString("hostName", hostName);
        hostSettingsBundle.putString("port", port);
        hostSettingsBundle.putString("ssl", ssl);

        if(!settingsHostDialog.isVisible()){
            settingsHostDialog.setArguments(hostSettingsBundle);
            settingsHostDialog.show(getSupportFragmentManager(), "SettingsHostDialog");
        }

    }

    private void showAlertDialog(String title, String message, String button, boolean status) {

        if (status) {

            customDialog.setContentView(R.layout.custom_popup);
            dialog_title = customDialog.findViewById(R.id.dialog_title);
            dialog_message = customDialog.findViewById(R.id.dialog_message);
            dialog_button = customDialog.findViewById(R.id.dialog_button);
            dialog_title.setText(title);
            dialog_message.setText(message);
            dialog_button.setText(button);

            customDialog.setCancelable(false);
            customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customDialog.show();

            dialog_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialog_button.getText().toString().equals("Close")) {
                        customDialog.dismiss();
                    } else {
                        if (customDialog.isShowing()) {
                            customDialog.cancel();

                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            });

        } else {

            customDialog.setContentView(R.layout.custom_popup_confirm);
            dialog_confirm_message = customDialog.findViewById(R.id.dialog_confirm_message);
            dialog_confirm_cancel = customDialog.findViewById(R.id.dialog_confirm_cancel);
            dialog_confirm_ok = customDialog.findViewById(R.id.dialog_confirm_ok);
            dialog_confirm_message.setText(message);
            dialog_confirm_ok.setText(button);

            customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customDialog.setCancelable(false);
            customDialog.show();

            dialog_confirm_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customDialog.dismiss();
                }
            });

            dialog_confirm_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (customDialog.isShowing()) {
                        customDialog.cancel();
                        finish();
                    }
                }
            });

        }
    }

}
