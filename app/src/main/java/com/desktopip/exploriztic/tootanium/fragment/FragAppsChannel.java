package com.desktopip.exploriztic.tootanium.fragment;


import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.activities.SpatialAppCompat;
import com.desktopip.exploriztic.tootanium.adapters.AppsChannelAdapter;
import com.desktopip.exploriztic.tootanium.interfaces.IChannel;
import com.desktopip.exploriztic.tootanium.models.ModAppsChannel;
import com.desktopip.exploriztic.tootanium.utilities.CheckInternetConnection;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.utilities.SwipeController;
import com.desktopip.exploriztic.tootanium.volley.ChannelServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

/**
 * Created by Jayus on 03/07/2018.
 */

public class FragAppsChannel extends Fragment {

    // Session manager
    SessionManager session;
    // username
    String userName;
    // password
    String password;

    Toolbar appToolbar;

    final String JSON_URL = "http://192.168.90.50/online-storage/rest-api/";

    final String LOG = FragAppsChannel.class.getSimpleName();
    RelativeLayout layout_not_found;
    List<ModAppsChannel> listAppChannel;
    RecyclerView appChannelRecyclerView;
    AppsChannelAdapter appsChannelAdapter;
    SwipeController swipeController;
    ItemTouchHelper itemTouchhelper;

    TextView tv_volley_error;
    ImageView iv_volley_error;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_apps_channel_list, container, false);

        session = new SessionManager(getActivity());
        session.checkLogin();

        //Get user data from session
        HashMap<String, String> user =  session.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);

        appToolbar = view.findViewById(R.id.up_toolbar);
        appToolbar.setTitle("Apps Channel");
        appToolbar.setSubtitle("Manage your Apps Channel here");

        SpatialAppCompat.setActionBarColor(appToolbar, getContext());

        ((AppCompatActivity) getActivity()).setSupportActionBar(appToolbar);
        setHasOptionsMenu(true);

        listAppChannel = new ArrayList<>();

        appChannelRecyclerView = view.findViewById(R.id.rc_apps_channel);
        layout_not_found = view.findViewById(R.id.layout_not_found);
        iv_volley_error = view.findViewById(R.id.iv_volley_error);
        tv_volley_error = view.findViewById(R.id.tv_volley_error);

        swipeController = new SwipeController();itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(appChannelRecyclerView);

        loadChannel();

        return view;
    }

    public void loadChannel() {

        if (CheckInternetConnection.checknetwork(getActivity())) {
            ChannelServices.loadChannel(new IChannel() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONArray message = response.getJSONArray("message");
                        if(message != null && message.length() > 0) {
                            listAppChannel.clear();
                            for(int i = 0; i < message.length(); i++) {
                                ModAppsChannel appsChannel = new ModAppsChannel();
                                JSONObject data = message.getJSONObject(i);
                                appsChannel.setPrimary(data.getString("primary"));
                                appsChannel.setChannel_id(data.getString("channel_id"));
                                appsChannel.setBasePath(data.getString("dirpath"));
                                appsChannel.setBasefile(data.getString("basefile"));
                                appsChannel.setGenerate_date(data.getString("generate_date"));
                                appsChannel.setExpired(data.getString("expired"));
                                appsChannel.setIs_expired(data.getString("is_expired"));

                                if(data.getString("is_expired").equals("1"))
                                    appsChannel.setIs_expired("Expired");
                                else
                                    appsChannel.setIs_expired("Active");

                                listAppChannel.add(appsChannel);
                            }
                            setupRecyclerView(listAppChannel);
                        } else {
                            layout_not_found.setVisibility(View.VISIBLE);
                            appChannelRecyclerView.setVisibility(View.GONE);
                            tv_volley_error.setText("No Content");
                        }

                    } catch (JSONException e) {
                        layout_not_found.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(VolleyError volleyError) {
                    handlingError(volleyError);
                }
            }, getActivity(), "appChannelList", userName);

        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void handlingError(VolleyError error) {
        layout_not_found.setVisibility(View.VISIBLE);
        appChannelRecyclerView.setVisibility(View.GONE);
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            iv_volley_error.setImageResource(R.drawable.ic_time_out);
            tv_volley_error.setText(R.string.error_timeout);
        } else if (error instanceof AuthFailureError) {
            iv_volley_error.setImageResource(R.drawable.ic_security_auth);
            tv_volley_error.setText(R.string.error_auth);
        } else if (error instanceof ServerError) {
            iv_volley_error.setImageResource(R.drawable.ic_server);
            tv_volley_error.setText(R.string.error_server);
        } else if (error instanceof NetworkError) {
            iv_volley_error.setImageResource(R.drawable.ic_signal_wifi_off);
            tv_volley_error.setText(R.string.error_network);
        } else if (error instanceof ParseError) {
            iv_volley_error.setImageResource(R.drawable.ic_parse_error);
            tv_volley_error.setText(R.string.error_parse);
        }
    }

    private void setupRecyclerView(List<ModAppsChannel> listAppChannel){
        appsChannelAdapter = new AppsChannelAdapter(getContext(), listAppChannel, FragAppsChannel.this);
        appChannelRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        appChannelRecyclerView.setAdapter(appsChannelAdapter);
        appsChannelAdapter.setActivity((AppCompatActivity) getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        appToolbar.getMenu().clear();
        appToolbar.inflateMenu(R.menu.menu_channel);
        for (int i = 0; i < appToolbar.getMenu().size(); i++) {
            Drawable drawable = appToolbar.getMenu().getItem(i).getIcon();
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(R.color.color_white), PorterDuff.Mode.SRC_IN);
        }

        MenuItem searchItem = appToolbar.getMenu().findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String userSearch = newText.toLowerCase();
                List<ModAppsChannel> newList = new ArrayList<>();
                if(listAppChannel.size()>0) {
                    for(ModAppsChannel search : listAppChannel) {
                        if(search.getBasefile().toString().toLowerCase().contains(userSearch)||
                                search.getIs_expired().toString().toLowerCase().contains(userSearch)) {
                            newList.add(search);
                        }
                    }
                    appsChannelAdapter.searchChannel(newList);
                }
                return true;
            }
        });
    }
}
