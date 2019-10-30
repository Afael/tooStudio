package com.desktopip.exploriztic.tootanium.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.desktopip.exploriztic.tootanium.adapters.AdvertiseFileExplorerAdapter;
import com.desktopip.exploriztic.tootanium.adapters.GroupAdvertiseFolderAdapter;
import com.desktopip.exploriztic.tootanium.interfaces.IAdvertise;
import com.desktopip.exploriztic.tootanium.interfaces.IFileExplore;
import com.desktopip.exploriztic.tootanium.models.ModAdvertiseFileExplorer;
import com.desktopip.exploriztic.tootanium.models.ModDropdown;
import com.desktopip.exploriztic.tootanium.models.ModFileExplore;
import com.desktopip.exploriztic.tootanium.presenter.AdvCreateAdvertiseFolderPresenter;
import com.desktopip.exploriztic.tootanium.presenter.AdvCreateAdvertiseFolderPresenterImp;
import com.desktopip.exploriztic.tootanium.presenter.AdvCreateAdvertiseFolderView;
import com.desktopip.exploriztic.tootanium.utilities.CheckInternetConnection;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.utilities.StringHelper;
import com.desktopip.exploriztic.tootanium.volley.FileExplorerAdvertiseFolderServices;
import com.desktopip.exploriztic.tootanium.volley.FileExplorerServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

//import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

@SuppressLint("ValidFragment")
public class FragGroupAdvertiseFolder extends Fragment implements AdvCreateAdvertiseFolderView {

    private static final String TAG = "FragAdvertiseFolder";

    public boolean isInActionMode = false;

    // Session manager
    SessionManager session;
    // uid
    String uid;
    // username
    String userName;
    // password
    String password;

    String pathAdvertise = null;

    String groupId, groupAccessId;

    Toolbar groupAdvFolderToolbar;
    RecyclerView rc_file_explorer_advertise_folder;
    Spinner adv_path_spinner;

    RelativeLayout layout_not_found, lay_spinner;
    TextView tv_volley_error;
    ImageView iv_volley_error;
    FloatingActionButton fab_add_advertise;

    List<ModAdvertiseFileExplorer> modAdvertiseFileExplorerList;
    List<ModFileExplore> listFileExplores;
    GroupAdvertiseFolderAdapter groupAdvertiseFolderAdapter;
    AdvCreateAdvertiseFolderPresenter createAdvertiseFolderPresenter;

    Dialog customDialog;
    int currentSpinnerPosition = 0;
    ModDropdown modDropdown;

    @SuppressLint("ValidFragment")
    public FragGroupAdvertiseFolder(String groupId, String groupAccessId){
        this.groupId = groupId;
        this.groupAccessId = groupAccessId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.frag_group_advertise_folder_list, container, false);

        session = new SessionManager(getActivity());

        //Get user data from session
        HashMap<String, String> user =  session.getUserDetails();
        uid = user.get(SessionManager.KEY_UID);
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);

        rc_file_explorer_advertise_folder = view.findViewById(R.id.rc_file_explorer_advertise_folder);
        groupAdvFolderToolbar = view.findViewById(R.id.groupAdvFolderToolbar);
        groupAdvFolderToolbar.setTitle("");
        groupAdvFolderToolbar.setSubtitle("Manage your advertise here");
        ((AppCompatActivity) getActivity()).setSupportActionBar(groupAdvFolderToolbar);
        setHasOptionsMenu(true);

        layout_not_found = view.findViewById(R.id.layout_not_found);
        lay_spinner = view.findViewById(R.id.lay_spinner);
        iv_volley_error = view.findViewById(R.id.iv_volley_error);
        tv_volley_error = view.findViewById(R.id.tv_volley_error);
        fab_add_advertise = view.findViewById(R.id.fab_add_advertise);
        adv_path_spinner = view.findViewById(R.id.adv_path_spinner);

        customDialog = new Dialog(getActivity());
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        modAdvertiseFileExplorerList = new ArrayList<>();
        listFileExplores = new ArrayList<>();
        createAdvertiseFolderPresenter = new AdvCreateAdvertiseFolderPresenterImp(getActivity(), FragGroupAdvertiseFolder.this);

        loadAdvertiseFolder(pathAdvertise);

        adv_path_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(currentSpinnerPosition == position){
                    return;
                }
                else {
                    modDropdown = (ModDropdown) adapterView.getSelectedItem();
                    loadData(modDropdown.getPath());
                    //Toast.makeText(getActivity(), modDropdown.getPath(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fab_add_advertise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddAdvertiseDialog();
            }
        });

        return view;
    }

    private void addToSpinner(String path) {

        ArrayList<ModDropdown> dropdownList = new ArrayList<>();

        String[] split = path.split("/");
        ArrayList<String> arrayPath = new ArrayList<>();
        for(String baseDir : split) {
            if(baseDir != userName) {
                arrayPath.add(baseDir);
            }
            dropdownList.add(new ModDropdown(TextUtils.join("/", arrayPath), baseDir));
        }


        Collections.sort(dropdownList, new Comparator<ModDropdown>() {
            @Override
            public int compare(ModDropdown modDropdown, ModDropdown t1) {
                return t1.getPath().compareToIgnoreCase(modDropdown.getPath());
            }
        });

        ArrayAdapter<ModDropdown> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, dropdownList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adv_path_spinner.setAdapter(adapter);
        adv_path_spinner.setSelection(-1);

    }

    public void loadData(String path) {

        if (path.equals("") || path.isEmpty() || path == null) {
            path = userName;
        }

        if (CheckInternetConnection.checknetwork(getActivity())) {
            final String finalPath = path;
            FileExplorerServices.load(new IFileExplore() {
                @Override
                public void onSuccess(JSONObject result) {
                    Log.d("TAG", result.toString());
                    rc_file_explorer_advertise_folder.setVisibility(View.VISIBLE);
                    layout_not_found.setVisibility(View.GONE);
                    String status = result.toString();
                    String type = "";
                    ModAdvertiseFileExplorer modFileExplorer;
                    JSONObject jsonObject = null;
                    try {
                        if (status.contains("error")) {
                            jsonObject = new JSONObject(result.getString("error"));
                            //alert.showAlertDialog(getActivity(), "No data", jsonObject.getString("message"), "Close");
                        }
                        ModAdvertiseFileExplorer fileExplorer = null;
                        JSONObject message = result.getJSONObject("message");
                        JSONArray data = message.getJSONArray("data");
                        if(data != null && data.length() > 0) {
                            setHasOptionsMenu(true);
                            //lay_spinner.setVisibility(View.VISIBLE);
                            modAdvertiseFileExplorerList.clear();
                            for (int i = 0; i < data.length(); i++) {

                                modFileExplorer = new ModAdvertiseFileExplorer();
                                JSONObject inData = data.getJSONObject(i);
                                modFileExplorer.setId(inData.getString("id"));
                                modFileExplorer.setName(inData.getString("name"));
                                modFileExplorer.setType(inData.getString("type"));
                                modFileExplorer.setExtension(inData.getString("extension"));
                                modFileExplorer.setMime(inData.getString("mime"));
                                modFileExplorer.setPath(inData.getString("path"));
                                modAdvertiseFileExplorerList.add(modFileExplorer);

                                String path = StringHelper.substringBeforeLast(modFileExplorer.getId(), "/");
                                //type = modFileExplorer.getType().toString();
                                //Log.d("PATH", path);
                                //feToolbar.setSubtitle(path);
                                addToSpinner(path);
                            }
                        }
                        else {
                            modAdvertiseFileExplorerList.clear();
                            fileExplorer = new ModAdvertiseFileExplorer();
                            fileExplorer.setPath(finalPath);
                            //feToolbar.setSubtitle(finalPath);
                            //addToSpinner(finalPath);
                            rc_file_explorer_advertise_folder.setVisibility(View.GONE);
                            layout_not_found.setVisibility(View.VISIBLE);
                            tv_volley_error.setText("No Content");
                            //Log.d(LOG, fileExplorer.getPath());
                        }
                    } catch (JSONException e) {

                    }
                    setupRecyclerView(modAdvertiseFileExplorerList);
//                    if(type.equals("shortcut")){
//                        return;
//                    }else {
//                        setupRecyclerView(listFileExplores);
//                    }
                }

                @Override
                public void onError(VolleyError error) {
                    handlingError(error);
                }
            }, getActivity(), "getDirectory", userName, password, path);
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void setupRecyclerView(List<ModAdvertiseFileExplorer> listFileExplores) {
        AdvertiseFileExplorerAdapter recyclerAdapter = new AdvertiseFileExplorerAdapter(getActivity(), listFileExplores, FragGroupAdvertiseFolder.this);
        rc_file_explorer_advertise_folder.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        rc_file_explorer_advertise_folder.setItemAnimator(itemAnimator);
        rc_file_explorer_advertise_folder.setAdapter(recyclerAdapter);
        recyclerAdapter.setActivity((AppCompatActivity) getActivity());
    }

    public void loadAdvertiseFolder(String pathAdvertise) {
        if(pathAdvertise != null) {
            FileExplorerAdvertiseFolderServices.getAdvertiseFolder(new IAdvertise() {
                @Override
                public void onSuccess(JSONObject response) {
                    //Log.d(TAG, "onSuccess: "+response.toString());
                    rc_file_explorer_advertise_folder.setVisibility(View.VISIBLE);
                    layout_not_found.setVisibility(View.GONE);
                    try {
                        JSONArray message = response.getJSONArray("message");
                        ModAdvertiseFileExplorer modAdvertiseFileExplorer = null;
                        modAdvertiseFileExplorerList.clear();
                        for(int i = 0; i < message.length(); i++) {
                            modAdvertiseFileExplorer = new ModAdvertiseFileExplorer();
                            JSONObject data = message.getJSONObject(i);
                            modAdvertiseFileExplorer.setName(data.getString("advertise_name"));
                            modAdvertiseFileExplorer.setPath(data.getString("advertise_path"));
                            //modAdvertiseFileExplorer.setCreatedBy(data.getString("created_by"));
                            modAdvertiseFileExplorerList.add(modAdvertiseFileExplorer);
                        }
                        setupRecyclerViewAdvertiseFolder(modAdvertiseFileExplorerList);

                    } catch (JSONException e) {
                        //layout_not_found.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    Log.d(TAG, "onError: "+error.toString());
                }
            }, getActivity(), "getAdvertiseFolder", userName, groupId, groupAccessId, pathAdvertise);
        } else {
            FileExplorerAdvertiseFolderServices.getAdvertiseFolder(new IAdvertise() {
                @Override
                public void onSuccess(JSONObject response) {
                    Log.d(TAG, "onSuccess: "+response.toString());
                    rc_file_explorer_advertise_folder.setVisibility(View.VISIBLE);
                    layout_not_found.setVisibility(View.GONE);
                    try {
                        JSONArray message = response.getJSONArray("message");
                        ModAdvertiseFileExplorer modAdvertiseFileExplorer = null;
                        modAdvertiseFileExplorerList.clear();
                        for(int i = 0; i < message.length(); i++) {
                            modAdvertiseFileExplorer = new ModAdvertiseFileExplorer();
                            JSONObject data = message.getJSONObject(i);
                            modAdvertiseFileExplorer.setName(data.getString("advertise_name"));
                            modAdvertiseFileExplorer.setPath(data.getString("advertise_path"));
                            //modAdvertiseFileExplorer.setCreatedBy(data.getString("created_by"));
                            modAdvertiseFileExplorerList.add(modAdvertiseFileExplorer);
                        }
                        setupRecyclerViewAdvertiseFolder(modAdvertiseFileExplorerList);

                    } catch (JSONException e) {
                        //layout_not_found.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    Log.d(TAG, "onError: "+error.toString());
                    handlingError(error);
                }
            }, getActivity(), "getAdvertiseFolder", userName, groupId, groupAccessId, pathAdvertise);
        }

    }

    private void setupRecyclerViewAdvertiseFolder(List<ModAdvertiseFileExplorer> advertiseList){
        groupAdvertiseFolderAdapter = new GroupAdvertiseFolderAdapter(getActivity(), advertiseList, FragGroupAdvertiseFolder.this);
        rc_file_explorer_advertise_folder.setLayoutManager(new LinearLayoutManager(getActivity()));
        rc_file_explorer_advertise_folder.setAdapter(groupAdvertiseFolderAdapter);
    }

    private void handlingError(VolleyError error) {
        layout_not_found.setVisibility(View.VISIBLE);
        rc_file_explorer_advertise_folder.setVisibility(View.GONE);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_advertise_folder, menu);

        if(modAdvertiseFileExplorerList.size() > 0) {
            setHasOptionsMenu(true);
            lay_spinner.setVisibility(View.VISIBLE);
        } else {
            setHasOptionsMenu(false);
            lay_spinner.setVisibility(View.GONE);
        }
//        for (int i = 0; i < menu.size(); i++) {
//            Drawable iconToolbar = menu.getItem(i).getIcon(); // change 0 with 1,2 ...
//            iconToolbar.mutate();
//            iconToolbar.setColorFilter(getResources().getColor(R.color.color_white), PorterDuff.Mode.SRC_IN);
//        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ((item.getItemId())) {
            case R.id.adv_folder_back:
                if (modAdvertiseFileExplorerList.size() > 0) {
                    String newPath = null;
                    String firstPath = null;

                    for (ModAdvertiseFileExplorer path : modAdvertiseFileExplorerList) {
                        newPath = path.getPath();
                        int slashFirst = newPath.lastIndexOf('/');
                        firstPath = newPath.substring(0, slashFirst);
                        Log.d("pathBack", firstPath);
                        Log.d("pathBack", newPath);
                    }

                    if (!firstPath.equals(newPath)) {
                        //Log.d("ROOT", firtsPath);
                        int slashSecond = firstPath.lastIndexOf('/');
                        String sPath = firstPath.substring(0, slashSecond);
                        loadData(sPath);
                    } else {
                        loadAdvertiseFolder(null);
                    }
                } else {
                    if(adv_path_spinner.getSelectedItem() != null/*feToolbar.getSubtitle().toString().equals("empty")*/) {
                        String pathEmpty = adv_path_spinner.getSelectedItem().toString();
                        //String pathEmpty = feToolbar.getSubtitle().toString();
                        loadData(StringHelper.substringBeforeLast(pathEmpty, "/"));
                        //Log.d("pathback", "" + pathEmpty);
                    } else {
                        loadData(userName);
                    }

                }
                break;

            case R.id.adv_folder_new_folder:
                String isRoot = null;
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                final View newDirectory = inflater.inflate(R.layout.fe_rename, null);
                dialogBuilder.setView(newDirectory);

                final EditText etNewFolder = newDirectory.findViewById(R.id.fe_new_name);
                etNewFolder.setHint("New Folder");
                TextView fe_rename_create_folder = newDirectory.findViewById(R.id.fe_rename_create_folder);
                fe_rename_create_folder.setText("Create New Folder");

                if (modAdvertiseFileExplorerList.size() > 0) {

//                    for (ModAdvertiseFileExplorer path : modAdvertiseFileExplorerList) {
//                        isRoot = path.getId();//StringHelper.substringBeforeLast(path.getId(), "/");
//                    }
                    //Log.d("isRoot", "Hello "+isRoot);
//                    if (!isRoot.equals(userName)) {
//                        //Log.d("isRoot", isRoot.toString());
                        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                if(!etNewFolder.getText().toString().isEmpty() || !etNewFolder.getText().toString().equals("")) {
//                                    FileExplorerAdvertiseFolderServices.createAdvertiseFolder(new IAdvertise() {
//                                        @Override
//                                        public void onSuccess(JSONObject result) {
//                                            try {
//                                                JSONObject jsonObject = result.getJSONObject("message");
//                                                Toast.makeText(getActivity(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                                                //loadData(feToolbar.getSubtitle().toString());
//                                                //loadData(pathSpinner.getSelectedItem().toString());
//                                            } catch (JSONException e) {
//                                                //alert.showAlertDialog(getActivity(), "Error", e.getMessage(), "close");
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onError(VolleyError error) {
//                                            handlingError(error);
//                                        }
//                                    }, getActivity(), "createAdvertiseFolder", userName, password, /*pathSpinner.getSelectedItem().toString()feToolbar.getSubtitle().toString(),*/ etNewFolder.getText().toString());
//
//                                }
//                                else {
//                                    //alert.showAlertDialog(getActivity(), "Warning", "The name of directory can't empty", "Close");
//                                }

                            }
                        });

                        dialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog rename = dialogBuilder.create();
                        rename.show();
//                    }
//                    else {
//                        //alert.showAlertDialog(getActivity(), "Error", "Can't create folder in parent directories", "Close");
//                    }

                }
                else {
                    dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

//                            if(pathSpinner.getSelectedItem() != null) {
//                                FileExplorerServices.newFolder(new IFileExplore() {
//                                    @Override
//                                    public void onSuccess(JSONObject result) {
//                                        try {
//                                            JSONObject jsonObject = result.getJSONObject("message");
//                                            Toast.makeText(getActivity(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                                            //loadData(feToolbar.getSubtitle().toString());
//                                            loadData(pathSpinner.getSelectedItem().toString());
//                                        } catch (JSONException e) {
//                                            alert.showAlertDialog(getActivity(), "Error", e.getMessage(), "Close");
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onError(VolleyError error) {
//                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                                            alert.showAlertDialog(getActivity(), "Network Error", "Request Time-Out", "Close");
//                                        } else if (error instanceof AuthFailureError) {
//                                            alert.showAlertDialog(getActivity(), "Authentication Error", "Authentication Failed" + error.toString(), "Close");
//                                        } else if (error instanceof ServerError) {
//                                            alert.showAlertDialog(getActivity(), "Server Error", "Internal Server Error", "Close");
//                                        } else if (error instanceof NetworkError) {
//                                            alert.showAlertDialog(getActivity(), "Network Error", "Unable To Access The Network" + error.toString(), "Close");
//                                        } else if (error instanceof ParseError) {
//                                            alert.showAlertDialog(getActivity(), "Parse Error", "There was a problem parsing the package" + error.toString(), "Close");
//                                        }
//                                    }
//                                }, getActivity(), "createDirectory", userName, password, pathSpinner.getSelectedItem().toString()/*feToolbar.getSubtitle().toString()*/, etNewFolder.getText().toString());
//                            }
//                            else {
//                                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
//                            }
                        }
                    });

                    dialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog rename = dialogBuilder.create();
                    rename.show();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddAdvertiseDialog() {
        customDialog.setContentView(R.layout.new_folder);
        Button btn_save_new_folder = customDialog.findViewById(R.id.btn_save_new_folder);
        final EditText new_folder_name = customDialog.findViewById(R.id.new_folder_name);
        TextView close_new_folder = customDialog.findViewById(R.id.close_new_folder);

        customDialog.setCancelable(false);

        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        customDialog.show();

        close_new_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.cancel();
            }
        });

        btn_save_new_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckInternetConnection.checknetwork(getActivity())) {
                    String folderName = new_folder_name.getText().toString();
                    createAdvertiseFolderPresenter.createAdvertise(userName, password, folderName, groupId);
                } else {
                    Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void showValidationError() {
        Toast.makeText(getActivity(), "Please enter valid folder name!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void createAdvertiseSuccess(JSONObject result) {
        try {

            String message = result.getString("message");
            if(message.contains("Successfully")) {
                customDialog.cancel();
                loadAdvertiseFolder(pathAdvertise);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            } else {
                customDialog.cancel();
                //Log.d(TAG, "createAdvertiseSuccess: "+message);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createAdvertiseFailed(JSONObject failed) {
        try {
            String failedMsg = failed.getString("message");
            Toast.makeText(getActivity(), "Create folder failed "+failedMsg, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createAdvertiseError(VolleyError volleyError) {
        handlingError(volleyError);
    }

}
