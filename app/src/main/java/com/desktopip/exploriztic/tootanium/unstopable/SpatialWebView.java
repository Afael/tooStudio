package com.desktopip.exploriztic.tootanium.unstopable;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SpatialWebView extends WebView{

    private String mUrl;
    private Context context;

    public static SpatialWebView newInstance(Context context , String url){
        SpatialWebView newSpatialWV = new SpatialWebView(context);

        StringBuilder fixedUrl = new StringBuilder("http://");

        if(url.toLowerCase().contains("http://")|| url.toLowerCase().contains("https://")){
            newSpatialWV.mUrl = url;
        }else{
            fixedUrl.append(url);
            newSpatialWV.mUrl = fixedUrl.toString();
        }

        newSpatialWV.context = context;
        return newSpatialWV;
    }

    public String getUrl() {
        return mUrl;
    }

    public SpatialWebView go(){
        WebViewClient webClient = new WebViewClient();
        setWebViewClient(webClient);
        loadUrl(mUrl);

        return  this;
    }

    public SpatialWebView(Context context) {
        super(context);
    }

    public SpatialWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpatialWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SpatialWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SpatialWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
    }

}
