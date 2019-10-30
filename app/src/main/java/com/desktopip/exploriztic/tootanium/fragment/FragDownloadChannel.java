package com.desktopip.exploriztic.tootanium.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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
import com.desktopip.exploriztic.tootanium.adapters.DownloadChannelAdapter;
import com.desktopip.exploriztic.tootanium.interfaces.IChannel;
import com.desktopip.exploriztic.tootanium.models.ModDownloadChannel;
import com.desktopip.exploriztic.tootanium.utilities.CheckInternetConnection;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
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

public class FragDownloadChannel extends Fragment {

    // Session manager
    SessionManager session;
    // username
    String userName;
    // password
    String password;

    Toolbar downToolbar;

    final String LOG = FragDownloadChannel.class.getSimpleName();

    RelativeLayout layout_not_found;
    TextView tv_volley_error;
    ImageView iv_volley_error;

    List<ModDownloadChannel> listDownloadChannel;

    RecyclerView downloadChannelRecyclerView;
    DownloadChannelAdapter recyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_download_channel_list, container, false);

        session = new SessionManager(getActivity());
        session.checkLogin();

        //Get user data from session
        HashMap<String, String> user =  session.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);

        downToolbar = view.findViewById(R.id.up_toolbar);
        downToolbar.setTitle("Download Channel");
        downToolbar.setSubtitle("Manage your Download Channel here");

        SpatialAppCompat.setActionBarColor(downToolbar, getContext());

        ((AppCompatActivity) getActivity()).setSupportActionBar(downToolbar);
        setHasOptionsMenu(true);

        listDownloadChannel = new ArrayList<>();
        downloadChannelRecyclerView = view.findViewById(R.id.rc_download_channel);
        layout_not_found = view.findViewById(R.id.layout_not_found);
        iv_volley_error = view.findViewById(R.id.iv_volley_error);
        tv_volley_error = view.findViewById(R.id.tv_volley_error);

        loadChannel();

        return view;

    }

    public void loadChannel() {
        if (CheckInternetConnection.checknetwork(getActivity())) {
            ChannelServices.loadChannel(new IChannel() {
                @Override
                public void onSuccess(JSONObject response) {
                    layout_not_found.setVisibility(View.GONE);
                    downloadChannelRecyclerView.setVisibility(View.VISIBLE);
                    try {
                        JSONArray message = response.getJSONArray("message");
                        if(message != null && message.length() > 0){
                            listDownloadChannel.clear();
                            for (int i = 0; i < message.length(); i++) {

                                ModDownloadChannel downloadChannel = new ModDownloadChannel();

                                JSONObject data = message.getJSONObject(i);
                                downloadChannel.setPrimary(data.getString("primary"));
                                downloadChannel.setBaseFile(data.getString("basefile"));
                                downloadChannel.setChannel_id(data.getString("channel_id"));
                                downloadChannel.setDirpath(data.getString("dirpath"));
                                downloadChannel.setGenerate_date(data.getString("generate_date"));
                                downloadChannel.setExpired(data.getString("expired"));
                                downloadChannel.setIs_expired(data.getString("is_expired"));
                                downloadChannel.setPathtype(data.getString("pathtype"));
                                downloadChannel.setFileSize(data.getString("filesize"));

                                listDownloadChannel.add(downloadChannel);

                            }

                            setupRecyclerView(listDownloadChannel);
                        } else {
                            layout_not_found.setVisibility(View.VISIBLE);
                            downloadChannelRecyclerView.setVisibility(View.GONE);
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
            }, getActivity(), "downloadChannelList", userName);
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void handlingError(VolleyError error) {
        layout_not_found.setVisibility(View.VISIBLE);
        downloadChannelRecyclerView.setVisibility(View.GONE);
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

    private void setupRecyclerView(List<ModDownloadChannel> listDownloadChannel) {
        recyclerAdapter = new DownloadChannelAdapter(getActivity(), listDownloadChannel, FragDownloadChannel.this);
        downloadChannelRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        downloadChannelRecyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setActivity((AppCompatActivity) getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.menu_channel, menu);
        downToolbar.getMenu().clear();
        downToolbar.inflateMenu(R.menu.menu_channel);
        for (int i = 0; i < downToolbar.getMenu().size(); i++) {
            Drawable yourdrawable = downToolbar.getMenu().getItem(i).getIcon(); // change 0 with 1,2 ...
            yourdrawable.mutate();
            yourdrawable.setColorFilter(getResources().getColor(R.color.color_white), PorterDuff.Mode.SRC_IN);
        }
        MenuItem searchItem = downToolbar.getMenu().findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) searchItem.getActionView();

        SearchManager searchManager = (SearchManager)  getActivity().getSystemService(Context.SEARCH_SERVICE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String userSearch = newText.toLowerCase();
                List<ModDownloadChannel> newList = new ArrayList<>();
                if(listDownloadChannel.size()>0) {
                    for(ModDownloadChannel search : listDownloadChannel) {
                        if(search.getBaseFile().toString().toLowerCase().contains(userSearch)) {
                            newList.add(search);
                        }
                    }
                    recyclerAdapter.searchChannel(newList);
                }
                return true;
            }
        });
    }
}
