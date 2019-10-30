package com.desktopip.exploriztic.tootanium.utilities;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class GetLastVersion extends AsyncTask<String, String, String> {
    String latestVersion;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            //It retrieves the latest version by scraping the content of current version from play store at runtime
            String urlOfAppFromPlayStore = "https://play.google.com/store/apps/details?id=com.desktopip.exploriztic.tootanium";
            Document doc = Jsoup.connect(urlOfAppFromPlayStore).get();
            Element element = doc.getElementsByClass("BgcNfc").get(3);
            latestVersion = element.parent().children().get(1).children().text();

        }catch (Exception e){
            e.printStackTrace();
        }

        return latestVersion;
    }
}
