package com.desktopip.exploriztic.tootanium.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.desktopip.exploriztic.tootanium.interfaces.ILogin;
import com.desktopip.exploriztic.tootanium.volley.LoginServices;

import org.json.JSONObject;

/**
 * Created by Jayus on 23/07/2018.
 */

public class LoginPresenterImp implements LoginPresenter {
    /**
     * The login view
     */
    LoginView loginView;

    Context context;

    /**
     * Membuat sebuah login presenter baru
     * @param loginView the login view
     */
    public  LoginPresenterImp(LoginView loginView, Context context) {
        this.loginView = loginView;
        this.context = context;

    }

    @Override
    public void login(
            String username
            , String password
            , String ipAddress
            , final String latitude
            , final String longitude
            , String hostName
            , String port
            , boolean ssl) {

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            loginView.showValidationError();
        }
        else {

            LoginServices.loginUser(new ILogin() {
                @Override
                public void onSuccess(JSONObject result) {
                    //Log.d("loginSuccess", result.toString());
                    loginView.loginSuccess(result);
                }

                @Override
                public void onFailed(JSONObject failed) {
                    //Log.d("failed", failed.toString());
                    //JSONObject jsonObject = failed.getJSONObject("message");
                    loginView.loginFailed(failed);
                }

                @Override
                public void onError(VolleyError error) {
                    //Log.d("loginSuccess", error.toString());
                    loginView.loginError(error);
                }
            }, context, "login", username, password, ipAddress, latitude, longitude);
        }
    }
}
