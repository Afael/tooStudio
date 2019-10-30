package com.desktopip.exploriztic.tootanium.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.desktopip.exploriztic.tootanium.activities.NotificationsList;
import com.desktopip.exploriztic.tootanium.activities.SearchGroup;
import com.desktopip.exploriztic.tootanium.adapters.GroupAdapter;
import com.desktopip.exploriztic.tootanium.interfaces.IGroup;
import com.desktopip.exploriztic.tootanium.interfaces.INotifications;
import com.desktopip.exploriztic.tootanium.models.ModAdvertise;
import com.desktopip.exploriztic.tootanium.models.ModNotificationList;
import com.desktopip.exploriztic.tootanium.presenter.AdvCreateGroupPresenter;
import com.desktopip.exploriztic.tootanium.presenter.AdvCreateGroupPresenterImp;
import com.desktopip.exploriztic.tootanium.presenter.AdvCreateGroupView;
import com.desktopip.exploriztic.tootanium.utilities.Converter;
import com.desktopip.exploriztic.tootanium.utilities.CustomAlertDialogManager;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.volley.GroupServices;
import com.desktopip.exploriztic.tootanium.volley.NotificationsServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragGroup extends Fragment implements AdvCreateGroupView {

    private static final String TAG = "FragGroup";

    CustomAlertDialogManager alert;

    // Session manager
    SessionManager session;
    // username
    String userName;
    // password
    String password;

    FloatingActionButton fab;
    Dialog customDialog;


    ModAdvertise modAdvertise;

    List<ModAdvertise> advertiseList;
    List<ModNotificationList> notificationLists;
    RecyclerView rc_adv_group;
    RelativeLayout search_group;
    //RelativeLayout rl_empty;
    GroupAdapter groupAdapter;
    AdvCreateGroupPresenter advCreateGroupPresenter;

    TextView adv_close_dialog_group, empty_message;
    Button adv_btn_save_group;
    EditText adv_group_name, adv_group_desc;
    Toolbar groupAdvToolbar;

    private static int notification_count = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_advertise_list, container, false);

        session = new SessionManager(getActivity());

        //Get user data from session
        HashMap<String, String> user = session.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);

        groupAdvToolbar = view.findViewById(R.id.groupAdvToolbar);
        rc_adv_group = view.findViewById(R.id.rc_adv_group);
        search_group = view.findViewById(R.id.search_group);
        fab = view.findViewById(R.id.groupAdvAdd);
        //rl_empty = view.findViewById(R.id.rl_empty);
        //empty_message = view.findViewById(R.id.empty_message);

        customDialog = new Dialog(getActivity());
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                 NotificationGenerator.openActivityNotification(getActivity());
//                showCreateGroupDialog();
//                 startActivity(new Intent(getActivity(), GroupActivity.class));
//            }
//        });

        search_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchGroup = new Intent(getActivity(), SearchGroup.class);
                //searchGroup.putExtra("groupName", modAdvertise.getAdvGroupName());
                //searchGroup.putExtra("groupId", modAdvertise.getAdvGroupId());
                getActivity().startActivity(searchGroup);
            }
        });

        advertiseList = new ArrayList<>();
        notificationLists = new ArrayList<>();
        alert = new CustomAlertDialogManager();
        advCreateGroupPresenter = new AdvCreateGroupPresenterImp(this, getActivity());

        loadData();

        ((AppCompatActivity) getActivity()).setSupportActionBar(groupAdvToolbar);
        setHasOptionsMenu(true);

        return view;
    }

    public void loadData() {

        NotificationsServices.loadNotifications(new INotifications() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray message = response.getJSONArray("message");
                    notification_count = message.length();
                    getActivity().invalidateOptionsMenu();
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Error:"+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(JSONObject failed) {
//                try {
//                    JSONArray message = failed.getJSONArray("message");
//                    Toast.makeText(getActivity(), "Error: "+message.toString(), Toast.LENGTH_SHORT).show();
//                } catch (JSONException e) {
//                    Toast.makeText(getActivity(), "Error notification:"+e.toString(), Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onError(VolleyError error) {
                handlingError(error);
            }
        }, getActivity(), "getNotifications", userName);

        GroupServices.loadAdvertise(new IGroup() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "onSuccess: "+response.toString());
                try {
                    JSONArray message = response.getJSONArray("message");
                    advertiseList.clear();
                    for (int i = 0; i < message.length(); i++) {
                        modAdvertise = new ModAdvertise();
                        JSONObject data = message.getJSONObject(i);
                        modAdvertise.setAdvGroupId(data.getString("group_id"));
                        modAdvertise.setAdvGroupName(data.getString("group_name"));
                        modAdvertise.setAdvGroupDesc(data.getString("group_description"));
                        modAdvertise.setAdvGroupCreateDate(data.getString("created_date"));
                        modAdvertise.setAdvGroupCreatedBy(data.getString("created_by"));
                        modAdvertise.setAdvGroupIsActive(data.getString("isActive"));
                        modAdvertise.setGroupAccessId(data.getString("group_access_id"));
                        advertiseList.add(modAdvertise);
                    }
                    setupRecyclerView(advertiseList);
                    handlingPage(advertiseList);
                } catch (JSONException e) {
                    //layout_not_found.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Error Json"+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(JSONObject failed) {
                try {
                    JSONArray message = failed.getJSONArray("message");
                    Toast.makeText(getActivity(), "Error: "+message.toString(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Error:"+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(VolleyError volleyError) {
                handlingError(volleyError);
            }
        }, getActivity(), "getGroupAdvertise", userName);
    }

    private void handlingPage(List<ModAdvertise> advertiseList){
        if(advertiseList.size() > 0) {
            //rl_empty.setVisibility(View.GONE);
            rc_adv_group.setVisibility(View.VISIBLE);
        } else {
            //rl_empty.setVisibility(View.VISIBLE);
            rc_adv_group.setVisibility(View.GONE);
        }
    }

    private void setupRecyclerView(List<ModAdvertise> advertiseList) {
        groupAdapter = new GroupAdapter(getActivity(), advertiseList, FragGroup.this);
        rc_adv_group.setLayoutManager(new LinearLayoutManager(getActivity()));
        rc_adv_group.setAdapter(groupAdapter);
    }

    private void handlingError(VolleyError error) {
        //adv_layout_not_found.setVisibility(View.VISIBLE);
        rc_adv_group.setVisibility(View.GONE);
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Toast.makeText(getActivity(), "" + R.string.error_timeout, Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(getActivity(), "" + R.string.error_auth, Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(getActivity(), "" + R.string.error_server, Toast.LENGTH_SHORT).show();
        } else if (error instanceof NetworkError) {
            Toast.makeText(getActivity(), "" + R.string.error_network, Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(getActivity(), "" + R.string.error_parse, Toast.LENGTH_SHORT).show();
        }
    }

    private void showCreateGroupDialog() {
        customDialog.setContentView(R.layout.adv_create_group_dialog);
        adv_close_dialog_group = customDialog.findViewById(R.id.adv_close_dialog_group);
        adv_group_name = customDialog.findViewById(R.id.adv_group_name);
        adv_group_desc = customDialog.findViewById(R.id.adv_group_desc);
        adv_btn_save_group = customDialog.findViewById(R.id.adv_btn_save_group);

        //Get user setup data from session
        HashMap<String, String> setup = session.getUserDetails();
        final String userName = setup.get(SessionManager.KEY_USERNAME);
        final String password = setup.get(SessionManager.KEY_PASSWORD);

        customDialog.setCancelable(false);

        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        customDialog.show();

        adv_close_dialog_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.cancel();
            }
        });

        adv_btn_save_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupName = adv_group_name.getText().toString();
                String groupDesc = adv_group_desc.getText().toString();
                advCreateGroupPresenter.createGroup(userName, groupName, groupDesc);
            }
        });
    }

    @Override
    public void showValidationError() {
        alert.showAlertDialog(getActivity(), "Create Group Failed", "Please enter valid name and description!", "Close");
    }

    @Override
    public void createGroupSuccess(JSONObject result) {
        // Log.d(TAG, "createGroupSuccess: "+ result.toString());
        try {
            customDialog.cancel();
            String message = result.getString("message");
            Log.d(TAG, "createGroupSuccess: "+message);
            alert.showAlertDialog(getActivity(), "Create Group Success", message, "Close");
            if(message.contains("Successfully")) {
                loadData();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createGroupFailed(JSONObject failed) {
        try {
            String failedMsg = failed.getString("message");
            alert.showAlertDialog(getActivity(), "Create Group Failed ", failedMsg, "Close");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createGroupError(VolleyError error) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            alert.showAlertDialog(getActivity(), "Network Error", "Request Time-Out", "Close");
        } else if (error instanceof AuthFailureError) {
            alert.showAlertDialog(getActivity(), "Authentication Error", "Authentication Failed" + error.toString(), "Close");
        } else if (error instanceof ServerError) {
            alert.showAlertDialog(getActivity(), "Server Error", "Internal Server Error", "Close");
        } else if (error instanceof NetworkError) {
            alert.showAlertDialog(getActivity(), "Network Error", "Unable To Access The Network" + error.toString(), "Close");
        } else if (error instanceof ParseError) {
            alert.showAlertDialog(getActivity(), "Parse Error", "There was a problem parsing the package" + error.toString(), "Close");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_notification, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_notification);
        menuItem.setIcon(Converter.convertLayoutToImage(getActivity(), notification_count, R.drawable.ic_notification_yellow));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_notification:
                Intent toNotifications = new Intent(getActivity(), NotificationsList.class);
                toNotifications.putExtra("userName", userName);
                startActivity(toNotifications);
                break;
            case R.id.menu_create_group:
                showCreateGroupDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}
