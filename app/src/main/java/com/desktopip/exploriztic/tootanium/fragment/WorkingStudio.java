package com.desktopip.exploriztic.tootanium.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class WorkingStudio extends Fragment {

    View view;

    public static int total = 0;
    private WebView spatial_webview;
    private static SessionManager sessionManager;
    String baseUrl, userName, password, userBase64, passwordBase64;

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    public WorkingStudio() {
        // Required empty public constructor
        total++;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(inflater != null) {
            view = inflater.inflate(R.layout.fragment_working_studio, container, false);
            sessionManager = new SessionManager(getActivity());

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
            Bundle bundle = getArguments();
            String param = bundle.getString(FragWorkingStudio.PARAM);

            spatial_webview = view.findViewById(R.id.spatial_webview);

            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            setWebView(spatial_webview, param, savedInstanceState);
        }

        return view;
    }

    private void setWebView(WebView spatial_webview, String param, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            spatial_webview.restoreState(savedInstanceState);
        } else {

            spatial_webview.getSettings().setJavaScriptEnabled(true);
            spatial_webview.getSettings().setPluginState(WebSettings.PluginState.ON);
            spatial_webview.getSettings().setAppCacheEnabled(true);
            spatial_webview.getSettings().setBuiltInZoomControls(true);
            spatial_webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            spatial_webview.setWebChromeClient(new WebChromeClient());

            CookieSyncManager.createInstance(spatial_webview.getContext());
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
                cookieManager.getInstance().setAcceptThirdPartyCookies(spatial_webview, true );
                setCookie(baseUrl, getString(R.string.cache_user), userBase64);
                setCookie(baseUrl, getString(R.string.cache_password), passwordBase64);
                spatial_webview.loadUrl(baseUrl+param);

            } else {
                cookieManager.setAcceptCookie(true);
                cookieManager.getInstance().setCookie(baseUrl,getString(R.string.cache_user)+"="+userBase64);
                cookieManager.getInstance().setCookie(baseUrl,getString(R.string.cache_password)+"="+passwordBase64);

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

                spatial_webview.loadUrl(baseUrl+param);
            }
        }
    }

    public static final void setCookie(String url, String cookieName, String cookieContent) {
        CookieManager.getInstance().setCookie(url, cookieName + "=" + cookieContent);
        if (Build.VERSION.SDK_INT >= 21) CookieManager.getInstance().flush();
    }

    public static final String getCookie(String url) {
        Log.d("COOKIES", "HAS COOKIE?: " + CookieManager.getInstance().hasCookies());
        return CookieManager.getInstance().getCookie(url);
    }

}
