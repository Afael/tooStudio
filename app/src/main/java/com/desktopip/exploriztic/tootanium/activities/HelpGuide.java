package com.desktopip.exploriztic.tootanium.activities;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.desktopip.exploriztic.tootanium.R;

public class HelpGuide extends SpatialSubAppCompatActivity {

    TooWebView webView;
    ProgressBar progressBar;
    final String HELP_GUID_URL = "https://desktopip.com/wp-content/uploads/2019/04/tooStudio-Use-Guide-user-eng-for-Public.pdf";
    //final String HELP_GUID_URL = "https://desktopip.com/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_guide);

        webView = findViewById(R.id.help_guid);
        progressBar = findViewById(R.id.progressBar);
        setWebView();
    }

    private void setWebView(){
        webView.setWebViewClient(new myWebClient());
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url="+HELP_GUID_URL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setWebView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        webView.stopLoading();
    }

    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            //progressBar.setVisibility(View.VISIBLE);
            //view.loadUrl(url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
        }
    }
}
