package com.desktopip.exploriztic.tootanium.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.unstopable.WebFragment;
import com.desktopip.exploriztic.tootanium.unstopable.WebRVAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragWorkingStudio extends Fragment implements WebRVAdapter.onCellClick {

    View view;

    public static final String PARAM = "PARAM";
    private static FragWorkingStudio sFragWorkingStudioInstance;

    private RecyclerView rv;
    public static WebRVAdapter adapter;
    public static List<Fragment> fragmentList;
    private Fragment currentShowFragment;
    public static WebFragment fragmentToDelete;
    public static WebFragment showFragment;
    private int count;
    private Context context;

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    public FragWorkingStudio() {
        // Required empty public constructor
        if (sFragWorkingStudioInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public void setContext(Context context){
        this.context = context;
    }

//    public static WorkingStudioAdapter workingStudioAdapter;
//    public static ViewPager mViewPager;
//    public static CustomTab mTabLayout;

    public static FragWorkingStudio getInstance() {
        if (sFragWorkingStudioInstance == null) { //if there is no instance available... create new one
            synchronized (FragWorkingStudio.class) {
                if (sFragWorkingStudioInstance == null)
                    sFragWorkingStudioInstance = new FragWorkingStudio();
            }
        }
        return sFragWorkingStudioInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //SpatialAppCompat.setColorView(rv, getContext());
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (inflater != null) {

            view = inflater.inflate(R.layout.coba_activity_web_view_container, container, false);
            rv = view.findViewById(R.id.rv);

            init();
        }
        return view;
    }

    public void openNewWebView(String url, String fileName) {

        WebFragment webFragment = WebFragment.newInstance(url, fileName);
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
        rv.scrollToPosition(fragmentList.size()-1);
    }

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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);

        adapter = new WebRVAdapter(getContext(), this);

        rv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(false);
    }

    private void setUpVar() {
        count = 0;
        fragmentList = new ArrayList<>();
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
        fragmentToDelete = (WebFragment) fragmentList.get(pos);
        showFragment = null;

        if (!fragmentToDelete.equals(currentShowFragment)) {
            return;
        }

        if (pos > 0) {
            showFragment = (WebFragment) fragmentList.get(pos - 1);
        } else if (getSize() > 1) {
            showFragment = (WebFragment) fragmentList.get(pos + 1);
        }

        currentShowFragment = showFragment;
        fragmentList.remove(pos);

//        Bundle bundle = new Bundle();
//        bundle.putString("message", "Are you sure, want to close?");
//        bundle.putInt("position", pos);
//        bundle.putInt("viewPosition", pos);
//        ConfirmCloseTabDialog dialogFragment = new ConfirmCloseTabDialog();
//        dialogFragment.setArguments(bundle);
//        dialogFragment.show(getActivity().getSupportFragmentManager(), "ConfirmCloseTabDialog");
        adapter.deleteFragmentFromList(fragmentToDelete, showFragment);
    }
}
