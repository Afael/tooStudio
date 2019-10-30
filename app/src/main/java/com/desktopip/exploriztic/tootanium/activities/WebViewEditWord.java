package com.desktopip.exploriztic.tootanium.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.utilities.HTML5WebView;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class WebViewEditWord extends AppCompatActivity {

    HTML5WebView mWebView;

    private WebView dip_webview;
    private static SessionManager sessionManager;
    private static HashMap<String, String> user;
    String baseUrl, userName, password, userBase64, passwordBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dip_web_view);

        sessionManager = new SessionManager(this);

        //Get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);
        baseUrl = user.get(SessionManager.BASE_URL);
        try {
            byte[] bytUser = userName.getBytes("UTF-8");
            userBase64 = Base64.encodeToString(bytUser, Base64.DEFAULT);
            byte[] bytPassword = password.getBytes("UTF-8");
            passwordBase64 = Base64.encodeToString(bytPassword, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Intent openWord = getIntent();
        String param = openWord.getStringExtra("param");

        dip_webview = findViewById(R.id.dip_webview);
        EditText web_edit = findViewById(R.id.web_edit);
        web_edit.requestFocus();
        web_edit.setFocusable(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if (savedInstanceState != null) {
            dip_webview.restoreState(savedInstanceState);
        } else {

            dip_webview.getSettings().setJavaScriptEnabled(true);
            dip_webview.getSettings().setPluginState(WebSettings.PluginState.ON);
            dip_webview.getSettings().setAppCacheEnabled(true);
            dip_webview.getSettings().setBuiltInZoomControls(true);
            dip_webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            dip_webview.setWebChromeClient(new WebChromeClient());

            CookieSyncManager.createInstance(dip_webview.getContext());
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
                cookieManager.getInstance().setAcceptThirdPartyCookies(dip_webview, true );
                setCookie(baseUrl, "DIPSTORAGE_U", userBase64);
                setCookie(baseUrl, "DIPSTORAGE_P", passwordBase64);
                dip_webview.loadUrl(baseUrl+param);

            } else {
                cookieManager.setAcceptCookie(true);
                cookieManager.getInstance().setCookie(baseUrl,"DIPSTORAGE_U="+userBase64);
                cookieManager.getInstance().setCookie(baseUrl,"DIPSTORAGE_P="+passwordBase64);

                String cookie = getCookie(baseUrl);
                String[] cookies = cookie.split(";");
                //Log.d("Cookie", "onCreate: "+ cookies);
                final HashMap<String, String> cookieStrings = new HashMap<>();
                for (String cook : cookies) {
                    String[] cs = cook.split("=");
                    cookieStrings.put(cs[0], cs[1]);
                }
                cookieManager.getInstance().setAcceptCookie(true);
                cookieManager.getInstance().removeSessionCookie();
                CookieSyncManager.getInstance().sync();

                dip_webview.loadUrl(baseUrl+param);
            }
        }

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("Desktop IP");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public static final void setCookie(String url, String cookieName, String cookieContent) {
        CookieManager.getInstance().setCookie(url, cookieName + "=" + cookieContent);
        if (Build.VERSION.SDK_INT >= 21) CookieManager.getInstance().flush();
    }

    public static final String getCookie(String url) {
        Log.d("COOKIES", "HAS COOKIE?: " + CookieManager.getInstance().hasCookies());
        return CookieManager.getInstance().getCookie(url);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        dip_webview.saveState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CookieSyncManager.getInstance().stopSync();
    }

    @Override
    public void onStop() {
        super.onStop();
        dip_webview.stopLoading();
    }

}
