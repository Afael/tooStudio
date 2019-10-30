package com.desktopip.exploriztic.tootanium.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.desktopip.exploriztic.tootanium.activities.SpatialAppCompat;
import com.desktopip.exploriztic.tootanium.adapters.FileExploreAdapter;
import com.desktopip.exploriztic.tootanium.customisable.NewAndRename;
import com.desktopip.exploriztic.tootanium.interfaces.ICallBack;
import com.desktopip.exploriztic.tootanium.interfaces.IFileExplore;
import com.desktopip.exploriztic.tootanium.models.ModDropdown;
import com.desktopip.exploriztic.tootanium.models.ModFileExplore;
import com.desktopip.exploriztic.tootanium.utilities.CheckInternetConnection;
import com.desktopip.exploriztic.tootanium.utilities.CustomAlertDialogManager;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.utilities.StringHelper;
import com.desktopip.exploriztic.tootanium.utilities.UploadTask;
import com.desktopip.exploriztic.tootanium.volley.FileExplorerServices;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Jayus on 02/07/2018.
 */

public class FragFileExplore extends BottomSheetDialogFragment implements ICallBack {

    private final int UPLOAD_FILE = 10;
    private static String currentPath;
    private SharedPreferences mPrefs;
    private int PRIVATE_MODE = 0;
    SharedPreferences.Editor ed;
    private static final String PREF_NAME = "DipFileExplorerPref";
    private static final String PREF_COPY_NAME = "OptionDialog";
    static final String SESSION_ID_DEFAULT = "SESSION_ID_DEFAULT";

    CustomAlertDialogManager alert;

    UploadTask uploadTask;

    // Session manager
    SessionManager session;
    // uid
    String uid;
    // username
    String userName;
    // password
    String password;

    final String LOG = FragFileExplore.class.getSimpleName();

    RelativeLayout layout_not_found;
    TextView tv_volley_error;
    ImageView iv_volley_error;

    List<ModFileExplore> listFileExplores;
    RecyclerView fileExploreRecyclerView;
    FileExploreAdapter recyclerAdapter;

    Toolbar feToolbar;
    Spinner pathSpinner;
    ModDropdown modDropdown;

    String fileSize, fileName;
    TextView txt_file_size, txt_file_name;
    Intent intentData;

    Dialog customDialog;
    int currentSpinnerPosition = 0;
    FrameLayout rootFeLayout;
    Snackbar snackbar;

    Bundle newAndRenameBundle;
    NewAndRename newAndRenameDialog;

    public FragFileExplore(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            String pathSpin = pathSpinner.getSelectedItem().toString();
            if(pathSpin != null){
                currentPath = pathSpinner.getSelectedItem().toString();
                ed = mPrefs.edit();
                ed.putString("CURRENT_PATH", currentPath);
                ed.commit();
            } else {
                currentPath = userName;
                ed = mPrefs.edit();
                ed.putString("CURRENT_PATH", userName);
                ed.commit();
            }
        }catch (Exception e){
            Log.d("e", e.toString());
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_file_explore_list, container, false);
        fileExploreRecyclerView = view.findViewById(R.id.rc_file_explore);
        rootFeLayout = view.findViewById(R.id.rootFeLayout);

        session = new SessionManager(getActivity());
        session.checkLogin();

        alert = new CustomAlertDialogManager();
        customDialog = new Dialog(getActivity());
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before

        //Get user data from session
        HashMap<String, String> user = session.getUserDetails();
        uid = user.get(SessionManager.KEY_UID);
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);

        feToolbar = view.findViewById(R.id.fe_toolbar);
        feToolbar.setTitle("File Explorer");
        feToolbar.setSubtitle("Browse your File here");

        SpatialAppCompat.setActionBarColor(feToolbar, getContext());

        ((AppCompatActivity) getActivity()).setSupportActionBar(feToolbar);
        setHasOptionsMenu(true);

        pathSpinner = view.findViewById(R.id.fe_path_spinner);

        layout_not_found = view.findViewById(R.id.layout_not_found);
        iv_volley_error = view.findViewById(R.id.iv_volley_error);
        tv_volley_error = view.findViewById(R.id.tv_volley_error);

        listFileExplores = new ArrayList<>();

        pathSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(currentSpinnerPosition == position){
                    return;
                }
                else {
                    modDropdown = (ModDropdown) adapterView.getSelectedItem();
                    loadData(modDropdown.getPath());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        newAndRenameDialog = new NewAndRename();
        mPrefs = getActivity().getSharedPreferences(PREF_NAME, PRIVATE_MODE);

        if(currentPath != null && currentPath != userName){
            //currentPath = mPrefs.getString("CURRENT_PATH", userName);
            loadData(currentPath);
        } else {
            loadData(userName);
        }

        return view;

    }

    private void addToSpinner(String path) {
        try {
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
            pathSpinner.setAdapter(adapter);
            pathSpinner.setSelection(-1);
        } catch (Exception e){
            Log.d("spinner", e.toString());
        }

    }

    public void loadData(String path) {

//        if (path.equals("") || path.isEmpty() || path == null) {
//            path = userName;
//        }

        if (CheckInternetConnection.checknetwork(getActivity())) {
            final String finalPath = path;
            FileExplorerServices.load(new IFileExplore() {
                @Override
                public void onSuccess(JSONObject result) {
                    Log.d("explore_res", result.toString());
                    fileExploreRecyclerView.setVisibility(View.VISIBLE);
                    layout_not_found.setVisibility(View.GONE);
                    String status = result.toString();
                    //String type = "";
                    ModFileExplore modFileExplorer;
                    JSONObject jsonObject;
                    try {
                        if (status.contains("error")) {
                            jsonObject = new JSONObject(result.getString("error"));
                            alert.showAlertDialog(getActivity(), "No data", jsonObject.getString("message"), "Close");
                        }
                        ModFileExplore fileExplorer;
                        JSONObject message = result.getJSONObject("message");
                        JSONArray data = message.getJSONArray("data");
                        if(data != null && data.length() > 0) {
                            listFileExplores.clear();
                            for (int i = 0; i < data.length(); i++) {

                                modFileExplorer = new ModFileExplore();
                                JSONObject inData = data.getJSONObject(i);
                                modFileExplorer.setId(inData.getString("id"));
                                modFileExplorer.setName(inData.getString("name"));
                                modFileExplorer.setType(inData.getString("type"));
                                modFileExplorer.setExtension(inData.getString("extension"));
                                modFileExplorer.setMime(inData.getString("mime"));
                                modFileExplorer.setPath(inData.getString("path"));
                                listFileExplores.add(modFileExplorer);

                                String path = StringHelper.substringBeforeLast(modFileExplorer.getId(), "/");

                                addToSpinner(path);
                                ed = mPrefs.edit();
                                ed.putString("CURRENT_PATH", currentPath);
                                ed.commit();
                            }
                        }
                        else {
                            listFileExplores.clear();
                            fileExplorer = new ModFileExplore();
                            fileExplorer.setPath(finalPath);

                            addToSpinner(finalPath);
                            ed = mPrefs.edit();
                            ed.putString("CURRENT_PATH", currentPath);
                            ed.commit();

                            fileExploreRecyclerView.setVisibility(View.GONE);
                            layout_not_found.setVisibility(View.VISIBLE);
                            tv_volley_error.setText("No Content");

                        }
                    } catch (JSONException e) {
                        Log.d("explore_e", e.toString());
                    }

                    setupRecyclerView(listFileExplores);

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

    private void setupRecyclerView(List<ModFileExplore> listFileExplores) {

        try {
            recyclerAdapter = new FileExploreAdapter(getActivity(), listFileExplores, FragFileExplore.this);
            fileExploreRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
            fileExploreRecyclerView.setItemAnimator(itemAnimator);
            fileExploreRecyclerView.setAdapter(recyclerAdapter);
            recyclerAdapter.setActivity((AppCompatActivity) getActivity());
        } catch (Exception e){
            Log.d("spinner", e.toString());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_file_explorer, menu);
        for (int i = 0; i < menu.size(); i++) {
            Drawable iconToolbar = menu.getItem(i).getIcon(); // change 0 with 1,2 ...
            iconToolbar.mutate();
            iconToolbar.setColorFilter(getResources().getColor(R.color.color_white), PorterDuff.Mode.SRC_IN);
        }

        MenuItem searchItem = feToolbar.getMenu().findItem(R.id.fe_search);
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
                List<ModFileExplore> newList = new ArrayList<>();
                if(listFileExplores.size()>0) {
                    for(ModFileExplore search : listFileExplores) {
                        if(search.getName().toString().toLowerCase().contains(userSearch)) {
                            newList.add(search);
                        }
                    }
                    recyclerAdapter.searchFileExplorer(newList);
                }
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.fe_nav_back:

                if (listFileExplores.size() > 0) {
                    String newPath;
                    String firstPath = null;

                    for (ModFileExplore path : listFileExplores) {
                        newPath = path.getId();
                        firstPath = StringHelper.substringBeforeLast(newPath, "/");
                    }

                    if (!firstPath.equals(userName.toString())) {
                        String path = StringHelper.substringBeforeLast(firstPath, "/");
                        loadData(path);
                    }

                } else {

                    if(pathSpinner.getSelectedItem() != null) {
                        String pathEmpty = pathSpinner.getSelectedItem().toString();
                        loadData(StringHelper.substringBeforeLast(pathEmpty, "/"));
                    } else {
                        loadData(userName);
                    }

                }

                break;

            case R.id.fe_nav_new_folder:

                if (CheckInternetConnection.checknetwork(getActivity())) {

                    newAndRenameBundle = new Bundle();
                    newAndRenameBundle.putString("rt", "newFolder");
                    newAndRenameBundle.putInt("listSize", listFileExplores.size());
                    newAndRenameBundle.putString("userName", userName);
                    newAndRenameBundle.putString("password", password);
                    newAndRenameBundle.putString("pathSpinner", pathSpinner.getSelectedItem().toString());
                    newAndRenameDialog.setArguments(newAndRenameBundle);

                    String isRoot = null;

                    if(listFileExplores.size() > 0){

                        for (ModFileExplore path : listFileExplores) {
                            isRoot = StringHelper.substringBeforeLast(path.getId(), "/");
                        }

                        if (!isRoot.equals(userName)) {

                            if(!newAndRenameDialog.isVisible()){
                                newAndRenameDialog.show(FileExploreAdapter.myContext.getSupportFragmentManager(), "RenameDialog");
                            }
                        }
                        else {
                            alert.showAlertDialog(getActivity(), "Error", "Can't create folder in parent directories", "Close");
                        }

                    } else {
                        if(!newAndRenameDialog.isVisible()){
                            newAndRenameDialog.show(FileExploreAdapter.myContext.getSupportFragmentManager(), "RenameDialog");
                        }
                    }

                } else {
                    Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.fe_nav_paste:
                if (CheckInternetConnection.checknetwork(getActivity())) {
                    String newPath = null;
                    mPrefs = getActivity().getSharedPreferences(PREF_COPY_NAME, PRIVATE_MODE);
                    String sessionId = mPrefs.getString("SESSION_ID_KEY", SESSION_ID_DEFAULT);
                    if (listFileExplores.size() > 0) {

                        for (ModFileExplore path : listFileExplores) {
                            newPath = StringHelper.substringBeforeLast(path.getId(), "/");
                        }

                        if (!newPath.equals(userName.toString())) {
                            final String finalNewPath = newPath;

                            FileExplorerServices.paste(new IFileExplore() {
                                @Override
                                public void onSuccess(JSONObject result) {
                                    try {
                                        JSONObject jsonObject = result.getJSONObject("message");
                                        Toast.makeText(getActivity(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                        loadData(finalNewPath);
                                        Log.d("PASTEAPI", "onSuccess: "+jsonObject.getString("message"));
                                    } catch (JSONException e) {
                                        alert.showAlertDialog(getActivity(), "Error", ""+e, "Close");
                                    }
                                }

                                @Override
                                public void onError(VolleyError error) {
                                    handlingError(error);
                                }
                            }, getActivity(), "paste", userName, newPath, password, sessionId);

                        } else {
                            alert.showAlertDialog(getActivity(), "Error", "Can't paste in parent directory", "close");
                        }

                    } else {
                        if(pathSpinner.getSelectedItem() != null) {
                            //final String pathEmpty = feToolbar.getSubtitle().toString();
                            mPrefs = getActivity().getSharedPreferences(PREF_COPY_NAME, PRIVATE_MODE);
                            String sessionIdPathNull = mPrefs.getString("SESSION_ID_KEY", SESSION_ID_DEFAULT);
                            final String pathEmpty = pathSpinner.getSelectedItem().toString();
                            FileExplorerServices.paste(new IFileExplore() {
                                @Override
                                public void onSuccess(JSONObject result) {
                                    try {
                                        JSONObject jsonObject = result.getJSONObject("message");
                                        Toast.makeText(getActivity(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                        loadData(pathEmpty);
                                    } catch (JSONException e) {
                                        alert.showAlertDialog(getActivity(), "Error", e.getMessage(), "Close");
                                    }
                                }

                                @Override
                                public void onError(VolleyError error) {
                                    handlingError(error);
                                }
                            }, getActivity(), "paste", userName, pathEmpty, password, sessionIdPathNull);
                            // Log.d("pathback", "" + pathEmpty);
                        }
                        else {
                            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.fe_nav_reload:
                //String pathSpinnerEmpty = pathSpinner.getSelectedItem().toString();
                if (listFileExplores.size() > 0) {
                    String pathReload = null;
                    for (ModFileExplore path : listFileExplores) {
                        pathReload = StringHelper.substringBeforeLast(path.getId(), "/");
                    }
                    loadData(pathReload);
                } else if(pathSpinner.getSelectedItem() == null/*feToolbar.getSubtitle().toString().equals("empty")*/){
                    loadData(userName.toString());
                } else {
                    //loadData(feToolbar.getSubtitle().toString());
                    loadData(pathSpinner.getSelectedItem().toString());
                }
                break;

            case R.id.fe_nav_upload:
                String pathUpload = null;
                if (CheckInternetConnection.checknetwork(getActivity())) {
                    if (listFileExplores.size() > 0) {

                        for (ModFileExplore path : listFileExplores) {
                            pathUpload = StringHelper.substringBeforeLast(path.getId(), "/");
                        }

                        if (!pathUpload.equals(userName.toString())) {
                            customDialog.setContentView(R.layout.fe_upload);
                            txt_file_size = customDialog.findViewById(R.id.txt_file_size);
                            txt_file_name = customDialog.findViewById(R.id.txt_file_name);
                            LinearLayout lay_browse_upload = customDialog.findViewById(R.id.lay_browse_upload);
                            Button btn_upload = customDialog.findViewById(R.id.btn_upload);
                            Button btn_upload_cancel = customDialog.findViewById(R.id.btn_upload_cancel);
                            customDialog.setCancelable(false);

                            customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            customDialog.show();

                            lay_browse_upload.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
//                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                                    intent.setType("*/*");
//                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                                    intent = Intent.createChooser(intent, "Choose a file");
//                                    startActivityForResult(intent, UPLOAD_FILE);
                                    new MaterialFilePicker().withActivity(getActivity()).withRequestCode(UPLOAD_FILE).start();
                                }
                            });

                            btn_upload.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (intentData != null) {

                                        String txtSize = txt_file_size.getText().toString();
                                        //String sSize = txtSize.substring(0, txtSize.length() - 3);
                                        long fileInSizeMB = Long.parseLong(txtSize);
                                        if (fileInSizeMB >= (800 * 100)) {
                                            Toast.makeText(getActivity(), "Size of file is too large", Toast.LENGTH_SHORT).show();
                                        } else {
                                            customDialog.cancel();
                                            uploadTask = new UploadTask(getActivity(), intentData, pathSpinner.getSelectedItem().toString()/*feToolbar.getSubtitle().toString()*/, FragFileExplore.this);
                                            intentData = null;
                                        }

                                    } else {
                                        Toast.makeText(getActivity(), "Please choose file first", Toast.LENGTH_SHORT).show();
                                        //alert.showAlertDialog(getActivity(), "Error", "Please choose file first", "close");
                                    }
                                }
                            });

                            btn_upload_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    intentData = null;
                                    customDialog.cancel();
                                }
                            });

                        } else {
                            alert.showAlertDialog(getActivity(), "Error", "Can't upload file in parent directory", "close");
                        }
                    } else {
                        if (pathSpinner.getSelectedItem() != null) {
                            if (!/*feToolbar.getSubtitle().toString()*/pathSpinner.getSelectedItem().toString().equals(userName.toString())) {
                                customDialog.setContentView(R.layout.fe_upload);
                                txt_file_size = customDialog.findViewById(R.id.txt_file_size);
                                txt_file_name = customDialog.findViewById(R.id.txt_file_name);
                                LinearLayout lay_browse_upload = customDialog.findViewById(R.id.lay_browse_upload);
                                Button btn_upload = customDialog.findViewById(R.id.btn_upload);
                                Button btn_upload_cancel = customDialog.findViewById(R.id.btn_upload_cancel);
                                customDialog.setCancelable(false);

                                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                customDialog.show();

                                lay_browse_upload.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
//                                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                                        intent.setType("*/*");
//                                        intent.addCategory(Intent.CATEGORY_OPENABLE);
//                                        intent = Intent.createChooser(intent, "Choose a file");
//                                        startActivityForResult(intent, UPLOAD_FILE);
                                        new MaterialFilePicker().withActivity(getActivity()).withRequestCode(UPLOAD_FILE).start();
                                    }
                                });

                                btn_upload.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (intentData != null) {

                                            String txtSize = txt_file_size.getText().toString();
                                            //String sSize = txtSize.substring(0, txtSize.length() - 3);
                                            long fileInSizeMB = Long.parseLong(txtSize);
                                            if (fileInSizeMB >= (800 * 100)) {
                                                Toast.makeText(getActivity(), "Size of file is too large", Toast.LENGTH_SHORT).show();
                                            } else {
                                                customDialog.cancel();
                                                uploadTask = new UploadTask(getActivity(), intentData, pathSpinner.getSelectedItem().toString()/*feToolbar.getSubtitle().toString()*/, FragFileExplore.this);
                                                intentData = null;
                                            }

                                        } else {
                                            Toast.makeText(getActivity(), "Please choose file first", Toast.LENGTH_SHORT).show();
                                            //alert.showAlertDialog(getActivity(), "Error", "Please choose file first", "close");
                                        }
                                    }
                                });

                                btn_upload_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        intentData = null;
                                        customDialog.cancel();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                else {
                    Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                }

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("FILES", "onActivityResult: "+data);
        if(requestCode == UPLOAD_FILE && resultCode == RESULT_OK) {

            intentData = new Intent(data);
            if(intentData != null) {
                File file = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                //String fileName = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                //txt_file_name.setText(StringHelper.substringAfterLast(fileName, "/"));
                txt_file_name.setText(file.getName());
                long sFileSize = getFileSize(file);
                txt_file_size.setText(String.valueOf(sFileSize));
            }

        }
    }

    public long getFileSize(File file) {
        long result;
        long size = file.length();
        result = size / 1024;

        return result;
    }

    @Override
    public void refreshList() {
        loadData(pathSpinner.getSelectedItem().toString()/*feToolbar.getSubtitle().toString()*/);
    }

    private void handlingError(VolleyError error) {
        layout_not_found.setVisibility(View.VISIBLE);
        fileExploreRecyclerView.setVisibility(View.GONE);
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

}
