package com.desktopip.exploriztic.tootanium.unstopable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.activities.SpatialAppCompat;

import java.util.ArrayList;
import java.util.List;

public class WebViewContainer extends SpatialAppCompat implements WebRVAdapter.onCellClick {

    private RecyclerView rv;
    private WebRVAdapter adapter;
    private List<Fragment> fragmentList;
    private Fragment currentShowFragment;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coba_activity_web_view_container);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.profil_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.profil_menu_edit:
                openNewWebView();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openNewWebView() {

        count++;
        String url;
        String title;

        if (count % 2 == 0) {
            url = "google.com";
            title = "Goo";
        } else {
            url = "facebook.com";
            title = "face";
        }

        WebFragment webFragment = WebFragment.newInstance(url, title);
        fragmentList.add(webFragment);

        //Jika Tidak ada Fragment Didalam Container
        if (fragmentList.size() == 0) {
            adapter.addNewFragmentToList(webFragment, null);
        } else {
            //Jika Sudah Ada Fragment di dalam Container
            adapter.addNewFragmentToList(webFragment, currentShowFragment);
        }

        currentShowFragment = webFragment;
        adapter.notifyDataSetChanged();
    }

    /*private void openNewWebView(String url, String title) {

        WebFragment webFragment = WebFragment.newInstance(url, title);
        fragmentList.add(webFragment);

        //Jika Tidak ada Fragment Didalam Container
        if (fragmentList.size() == 0) {
            adapter.addNewFragmentToList(webFragment, null);
            currentShowFragment = webFragment;
        } else {
            //Jika Sudah Ada Fragment di dalam Container
            adapter.addNewFragmentToList(webFragment, currentShowFragment);
            currentShowFragment = webFragment;
        }

        adapter.notifyDataSetChanged();
    }*/

    public int getSize() {
        return fragmentList.size();
    }

    public String getFragmentTitle(int pos) {
        if (getSize() != 0) {
            WebFragment webFragment = (WebFragment) fragmentList.get(pos);
            return webFragment.getTabTitle();
        } else {
            return null;
        }
    }

    private void init() {
        setUpVar();
        setUpRV();
    }

    private void setUpRV() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);

        adapter = new WebRVAdapter(this, this);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(false);
    }

    private void setUpVar() {

        rv = findViewById(R.id.rv);
        count = 0;
        fragmentList = new ArrayList<>();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemClick(int pos) {

        if (!currentShowFragment.equals(fragmentList.get(pos))) {
            adapter.showFragment(fragmentList.get(pos), currentShowFragment);
            currentShowFragment = fragmentList.get(pos);
        }
    }

    @Override
    public void onClosedClick(int pos) {
        WebFragment fragmentToDelete = (WebFragment) fragmentList.get(pos);
        WebFragment showFragment = null;

        if(!fragmentToDelete.equals(currentShowFragment)){
            return;
        }

        if (pos > 0) {
            showFragment = (WebFragment) fragmentList.get(pos - 1);
        }else if(getSize()>1){
            showFragment = (WebFragment) fragmentList.get(pos + 1 );
        }

        currentShowFragment = showFragment;
        fragmentList.remove(pos);
        adapter.deleteFragmentFromList(fragmentToDelete, showFragment);
    }
}
