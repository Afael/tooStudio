package com.desktopip.exploriztic.tootanium.unstopable;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.desktopip.exploriztic.tootanium.utilities.SessionManager;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class WebFragment extends Fragment {

    private final static String URL_KEY = "WEB_URL_KEY";
    private final static String TITLE_KEY = "WEB_TITLE_KEY";

    public static int total = 0;
    private WebView spatial_webview;
    private static SessionManager sessionManager;
    String baseUrl, userName, password, userBase64, passwordBase64;


    public static WebFragment newInstance(String url, String title) {

        WebFragment newWebFragment = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString(URL_KEY, url);
        bundle.putString(TITLE_KEY, title);
        newWebFragment.setArguments(bundle);

        return newWebFragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        assert bundle != null;
        String mUrl = bundle.getString(URL_KEY);
        assert mUrl != null;

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
        spatial_webview = new WebView(getContext());
        setWebView(spatial_webview, mUrl, savedInstanceState);

        return spatial_webview;
    }


    public String getTabTitle() {
        return getArguments().getString(TITLE_KEY);
    }

    public String getUrl() {
        return getArguments().getString(URL_KEY);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.getInstance().setAcceptThirdPartyCookies(spatial_webview, true);
                setCookie(baseUrl, "DIPSTORAGE_U", userBase64);
                setCookie(baseUrl, "DIPSTORAGE_P", passwordBase64);
                spatial_webview.loadUrl(baseUrl + param);

            } else {
                cookieManager.setAcceptCookie(true);
                cookieManager.getInstance().setCookie(baseUrl, "DIPSTORAGE_U=" + userBase64);
                cookieManager.getInstance().setCookie(baseUrl, "DIPSTORAGE_P=" + passwordBase64);

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

                spatial_webview.loadUrl(baseUrl + param);
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
