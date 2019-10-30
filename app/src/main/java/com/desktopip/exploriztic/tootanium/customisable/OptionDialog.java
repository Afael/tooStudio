package com.desktopip.exploriztic.tootanium.customisable;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.desktopip.exploriztic.tootanium.MainActivity;
import com.desktopip.exploriztic.tootanium.adapters.FileExploreAdapter;
import com.desktopip.exploriztic.tootanium.fragment.FragFileExplore;
import com.desktopip.exploriztic.tootanium.fragment.FragWorkingStudio;
import com.desktopip.exploriztic.tootanium.interfaces.IFileExplore;
import com.desktopip.exploriztic.tootanium.interfaces.IRequestResult;
import com.desktopip.exploriztic.tootanium.utilities.CustomAlertDialogManager;
import com.desktopip.exploriztic.tootanium.utilities.DownloadTask;
import com.desktopip.exploriztic.tootanium.utilities.NetworkManager;
import com.desktopip.exploriztic.tootanium.utilities.PlayMusicTask;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.utilities.StringHelper;
import com.desktopip.exploriztic.tootanium.utilities.Utils;
import com.desktopip.exploriztic.tootanium.volley.FileExplorerServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class OptionDialog extends DialogFragment implements View.OnClickListener {

    View dialogOptionView;
    DownloadTask downloadTask;
    CustomAlertDialogManager alert;
    Bundle channelSettingsBundle, newAndRenameBundle, findHelperBundle, addToMarketplaceBundle;
    ChannelSettings channelSettingsDialog;
    FindHelperDialog findHelperDialog;
    AddToMarketPlaceDialog addToMarketPlaceDialog;
    NewAndRename newAndRenameDialog;
    ImagePreview ImagePreviewDialog;

    LinearLayout fe_edit
            , fe_download
            , fe_rename
            , fe_copy
            , fe_paste
            , fe_delete
            , fe_create_dc
            , fe_create_uc
            , fe_create_apps
            , fe_find_helper
            , fe_add_to_marketplace;

    TextView fe_file;
    ImageView fe_file_img;

    String ext
            , id
            , userName
            , password
            , baseUrl
            , urlAPI
            , urlDownload
            , fileOrFolder
            , baseNameFile
            , path
            , isRoot
            , typeFile
            , type
            , uid;

    private static SessionManager sessionManager;
    private static HashMap<String, String> user;

    static final String SESSION_ID_DEFAULT = "SESSION_ID_DEFAULT";
    private SharedPreferences mPrefs;
    private int PRIVATE_MODE = 0;
    private SharedPreferences.Editor ed;

    private static final String PREF_NAME = "OptionDialog";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (inflater != null) {

            dialogOptionView = inflater.inflate(R.layout.fe_option_dialog, container, false);
            fe_edit = dialogOptionView.findViewById(R.id.fe_edit);
            fe_file = dialogOptionView.findViewById(R.id.fe_file);
            fe_file_img = dialogOptionView.findViewById(R.id.fe_file_img);
            fe_download = dialogOptionView.findViewById(R.id.fe_download);
            fe_rename = dialogOptionView.findViewById(R.id.fe_rename);
            fe_copy = dialogOptionView.findViewById(R.id.fe_copy);
            fe_paste = dialogOptionView.findViewById(R.id.fe_paste);
            fe_delete = dialogOptionView.findViewById(R.id.fe_delete);
            fe_create_dc = dialogOptionView.findViewById(R.id.fe_create_dc);
            fe_create_uc = dialogOptionView.findViewById(R.id.fe_create_uc);
            fe_create_apps = dialogOptionView.findViewById(R.id.fe_create_apps);
            fe_find_helper = dialogOptionView.findViewById(R.id.fe_find_helper);
            fe_add_to_marketplace = dialogOptionView.findViewById(R.id.fe_add_to_marketplace);

            Bundle optionBundle = getArguments();
            id = optionBundle.getString("id");
            userName = optionBundle.getString("userName");
            password = optionBundle.getString("password");
            baseUrl = optionBundle.getString("baseUrl");
            urlDownload = optionBundle.getString("urlDownload");
            fileOrFolder = optionBundle.getString("fileOrFolder");
            baseNameFile = optionBundle.getString("baseNameFile");
            path = optionBundle.getString("path");
            isRoot = optionBundle.getString("isRoot");
            typeFile = optionBundle.getString("typeFile");
            type = optionBundle.getString("type");

            if (fileOrFolder.equals("file")) {
                fe_create_uc.setVisibility(View.GONE);
                fe_paste.setVisibility(View.GONE);
                if (baseNameFile.contains(".")) {
                    ext = baseNameFile.substring(baseNameFile.lastIndexOf("."));

                    if (ext.equalsIgnoreCase(".mp3") || ext.equalsIgnoreCase(".png")
                            || ext.equalsIgnoreCase(".jpg")
                            || ext.equalsIgnoreCase(".jpeg")) {
                        fe_edit.setVisibility(View.VISIBLE);
                        fe_file.setText("Open");
                        fe_file_img.setImageResource(R.drawable.ic_open_in);
                    } else {
                        fe_edit.setVisibility(View.VISIBLE);
                        fe_file.setText("Edit");
                        fe_file_img.setImageResource(R.drawable.ic_edit);
                    }
                }
            } else {

                fe_edit.setVisibility(View.GONE);
                fe_create_apps.setVisibility(View.GONE);
                fe_find_helper.setVisibility(View.GONE);
                fe_add_to_marketplace.setVisibility(View.GONE);
            }

            fe_edit.setOnClickListener(this);
            fe_file.setOnClickListener(this);
            fe_file_img.setOnClickListener(this);
            fe_download.setOnClickListener(this);
            fe_rename.setOnClickListener(this);
            fe_copy.setOnClickListener(this);
            fe_paste.setOnClickListener(this);
            fe_delete.setOnClickListener(this);
            fe_create_dc.setOnClickListener(this);
            fe_create_uc.setOnClickListener(this);
            fe_create_apps.setOnClickListener(this);
            fe_find_helper.setOnClickListener(this);
            fe_add_to_marketplace.setOnClickListener(this);
            alert = new CustomAlertDialogManager();

            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setCancelable(true);
            ImagePreviewDialog = new ImagePreview();
            newAndRenameDialog = new NewAndRename();

            mPrefs = getActivity().getSharedPreferences(PREF_NAME, PRIVATE_MODE);

            sessionManager = new SessionManager(FileExploreAdapter.context);
            user = sessionManager.getUserDetails();
            uid = user.get(SessionManager.KEY_UID);
            urlAPI = user.get(SessionManager.BASE_URL_API);

            NetworkManager.getInstance(FileExploreAdapter.context);
        }

        return dialogOptionView;
    }

    @Override
    public void onClick(View v) {
        android.app.AlertDialog alertDialog = new SpotsDialog(FileExploreAdapter.context);
        switch (v.getId()) {
            case R.id.fe_edit:
                getDialog().dismiss();
                if (fe_file.getText().toString().equalsIgnoreCase("Open")) {

                    FileExplorerServices.playAudio(new IFileExplore() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            //String fileName = null;
                            Log.d("option_play", result.toString());
                            try {
                                String message = result.getString("message");
                                if (ext.equalsIgnoreCase(".mp3")) {
                                    new PlayMusicTask(FileExploreAdapter.context, urlDownload, message, id, baseNameFile);
                                } else if (ext.equalsIgnoreCase(".png")
                                        || ext.equalsIgnoreCase(".jpg")
                                        || ext.equalsIgnoreCase(".jpeg")) {
                                    Bundle ImagePreviewBundle = new Bundle();
                                    ImagePreviewBundle.putString("id", id);
                                    ImagePreviewBundle.putString("userName", userName);
                                    ImagePreviewBundle.putString("password", password);
                                    ImagePreviewBundle.putString("urlDownload", urlDownload);
                                    ImagePreviewBundle.putString("fileName", message);

                                    if(!ImagePreviewDialog.isVisible()){
                                        ImagePreviewDialog.setArguments(ImagePreviewBundle);
                                        ImagePreviewDialog.show(FileExploreAdapter.myContext.getSupportFragmentManager(), "ImagePreviewDialog");
                                    }

                                } else {
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            handlingError(error);
                        }

                    }, FileExploreAdapter.context, "playAudio", userName, password, path);

                } else {

                    if (!ext.equalsIgnoreCase(".mp3") || !ext.equalsIgnoreCase(".png")
                            || !ext.equalsIgnoreCase(".jpg")) {

                        FileExplorerServices.openWord(new IFileExplore() {
                            @Override
                            public void onSuccess(JSONObject result) {

                                try {
                                    String param = result.getString("message");

                                    FragWorkingStudio openTab = MainActivity.fragmentContainer;

                                    openTab.openNewWebView(param, baseNameFile);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                handlingError(error);
                            }
                        }, FileExploreAdapter.context, "openWord", userName, password, path);

                    } else {

                        FileExplorerServices.download(new IFileExplore() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                try {
                                    String file = result.getString("message");
                                    downloadTask = new DownloadTask(FileExploreAdapter.context, urlDownload + file);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                handlingError(error);
                            }
                        }, FileExploreAdapter.context, "downloadFile", userName, password, id);

                    }
                }

                break;

            case R.id.fe_download:

                getDialog().dismiss();
                String apiName = getString(R.string.api_download_file_method);

                if (typeFile.equals(getString(R.string.api_type_file_directory))) {
                    apiName = getString(R.string.api_download_directory_method);

                    FileExplorerServices.download(new IFileExplore() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            //Log.d("file download", result.toString());
                            try {
                                String file = result.getString("message");
                                downloadTask = new DownloadTask(FileExploreAdapter.context, urlDownload + file);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            handlingError(error);
                        }
                    }, FileExploreAdapter.context, apiName, userName, password, id);

                } else {

                    FileExplorerServices.download(new IFileExplore() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            //Log.d("file download", result.toString());
                            try {
                                String file = result.getString("message");
                                downloadTask = new DownloadTask(FileExploreAdapter.context, urlDownload + StringHelper.substringAfterLast(file, "/"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            handlingError(error);
                        }
                    }, FileExploreAdapter.context, apiName, userName, password, id);

                }

                break;

            case R.id.fe_rename:

                getDialog().dismiss();
                newAndRenameBundle = new Bundle();
                newAndRenameBundle.putString("rt", "rename");
                newAndRenameBundle.putString("id", id);
                newAndRenameBundle.putString("userName", userName);
                newAndRenameBundle.putString("password", password);
                newAndRenameBundle.putString("baseUrl", baseUrl);
                newAndRenameBundle.putString("type", type);
                newAndRenameBundle.putString("baseNameFile", baseNameFile);

                if(!newAndRenameDialog.isVisible()){
                    newAndRenameDialog.setArguments(newAndRenameBundle);
                    newAndRenameDialog.show(FileExploreAdapter.myContext.getSupportFragmentManager(), "ChannelSettingsDialog");
                }

                break;

            case R.id.fe_copy:
                getDialog().dismiss();
                alertDialog.show();
                alertDialog.setMessage("Copying...");

                if (!isRoot.equals(userName.toString())) {

                    if (typeFile.equals(getString(R.string.api_type_file_directory))) {
                        type = getString(R.string.api_type_file_folder);
                    }

                    final JSONObject rawJson = new JSONObject();
                    JSONObject param = new JSONObject();
                    String caps = type.toUpperCase();

                    try {
                        String pathBase64 = Utils.base64Helper(id);
                        String typeBase64 = Utils.base64Helper(caps);

                        rawJson.put("name", "copy");
                        param.put("uid", uid);
                        param.put("path", pathBase64);
                        param.put("type", typeBase64);
                        rawJson.put("param", param);
                        //Log.d("copy", pathBase64+"-"+typeBase64);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final android.app.AlertDialog finalAlertDialog = alertDialog;
                    NetworkManager.getInstance(FileExploreAdapter.context).lazyJsonObjectRequest(rawJson, new IRequestResult<JSONObject>()
                    {
                        @Override
                        public void getResult(JSONObject object) {
                            finalAlertDialog.dismiss();
                            ed = mPrefs.edit();
                            Log.d("result_copy_option", object.toString());
                            try {
                                JSONObject jsonObject = object.getJSONObject("message");
                                String sessionId = jsonObject.getString("session_id");
                                ed.putString("SESSION_ID_KEY", sessionId);
                                ed.commit();
                                Log.d("session_id", sessionId);
                                Toast.makeText(FileExploreAdapter.context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void getError(VolleyError object) {
                            finalAlertDialog.dismiss();
                            handlingError(object);
                        }
                    });

//                    FileExplorerServices.copyFile(new IFileExplore() {
//                        @Override
//                        public void onSuccess(JSONObject result) {
//                            ed = mPrefs.edit();
//                            Log.d("result_copy_option", result.toString());
//                            try {
//                                JSONObject jsonObject = result.getJSONObject("message");
//                                String sessionId = jsonObject.getString("session_id");
//                                ed.putString("SESSION_ID_KEY", sessionId);
//                                ed.commit();
//                                Log.d("session_id", sessionId);
//                                Toast.makeText(FileExploreAdapter.context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onError(VolleyError error) {
//                            handlingError(error);
//                        }
//                    }, FileExploreAdapter.context, getString(R.string.api_copy_method), userName, id, type);

                } else {
                    alert.showAlertDialog(FileExploreAdapter.context, "Error", getString(R.string.api_warning_copy_in_root_directory), "Close");
                }

                break;

            case R.id.fe_paste:
                getDialog().dismiss();
                alertDialog.show();
                alertDialog.setMessage("Please wait...");
                String sessionId = mPrefs.getString("SESSION_ID_KEY", SESSION_ID_DEFAULT);

                String pathBase64 = Utils.base64Helper(id);
                String passwordBase64 = Utils.base64Helper(password);
                JSONObject rawJson = new JSONObject();

                Map data = new HashMap();
                data.put("uid", uid);
                data.put("locs", pathBase64);
                data.put("password", passwordBase64);
                data.put("session_id", sessionId);

                rawJson = Utils.makingJSON("paste", data);
                //Log.d("paste", rawJson.toString());

//                JSONObject param = new JSONObject();
//                try {
//                    String pathBase64 = Utils.base64Helper(id);
//                    String passwordBase64 = Utils.base64Helper(password);
//
//                    Map data = new HashMap();
//                    data.put("name", "paste");
//                    data.put("uid", uid);
//                    data.put("locs", pathBase64);
//                    data.put("password", passwordBase64);
//                    data.put("session_id", sessionId);
//
//                    rawJson = Utils.makingJSON("paste", data);
//
////                    rawJson.put("name", "paste");
////                    param.put("uid", uid);
////                    param.put("locs", pathBase64);
////                    param.put("password", passwordBase64);
////                    param.put("session_id", sessionId);
////                    rawJson.put("param", param);
//                    Log.d("paste", pathBase64+"-"+password+"-"+userName);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                final android.app.AlertDialog finalAlertDialog = alertDialog;
                NetworkManager.getInstance(FileExploreAdapter.context).lazyJsonObjectRequest(rawJson, new IRequestResult<JSONObject>()
                {
                    @Override
                    public void getResult(JSONObject object) {
                        finalAlertDialog.dismiss();
                        //ed = mPrefs.edit();
                        try {
                            JSONObject jsonObject = object.getJSONObject("message");
                            //ed.putString("SESSION_ID_KEY", null);
                            //ed.commit();
                            Toast.makeText(FileExploreAdapter.context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void getError(VolleyError object) {
                        finalAlertDialog.dismiss();
                        handlingError(object);
                    }
                });

//                FileExplorerServices.paste(new IFileExplore() {
//                    @Override
//                    public void onSuccess(JSONObject result) {
//                        try {
//                            JSONObject jsonObject = result.getJSONObject("message");
//                            Toast.makeText(FileExploreAdapter.context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                            //Log.d("PASTEAPI", "onSuccess: "+jsonObject.getString("message"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(VolleyError error) {
//                        handlingError(error);
//                    }
//
//                }, FileExploreAdapter.context, getString(R.string.api_paste_method), userName, id, password, sessionId);

                break;

            case R.id.fe_delete:
                getDialog().dismiss();

                AlertDialog.Builder confirmDelete = new AlertDialog.Builder(FileExploreAdapter.context);
                confirmDelete.setMessage("Are you sure want to delete " + baseNameFile + " ?");
                confirmDelete.setCancelable(false);

                confirmDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String apiName = "deleteDirectory";

                        if (typeFile.equals("file")) {
                            apiName = "deleteFile";
                        }

                        FileExplorerServices.delete(new IFileExplore() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                try {
                                    JSONObject jsonObject = result.getJSONObject("message");
                                    Toast.makeText(FileExploreAdapter.context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    ((FragFileExplore) FileExploreAdapter.fragment).loadData(StringHelper.substringBeforeLast(id, "/"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                handlingError(error);
                            }
                        }, FileExploreAdapter.context, apiName, userName, id, password);
                    }
                });

                confirmDelete.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog confirmDeleteShow = confirmDelete.create();
                confirmDeleteShow.show();

                break;

            case R.id.fe_create_dc:

                getDialog().dismiss();
                channelSettingsBundle = new Bundle();
                channelSettingsBundle.putString("ct", "dc");
                channelSettingsBundle.putString("id", id);
                channelSettingsBundle.putString("userName", userName);
                channelSettingsBundle.putString("password", password);
                channelSettingsBundle.putString("baseUrl", baseUrl);
                channelSettingsBundle.putString("type", type);
                channelSettingsBundle.putString("baseNameFile", baseNameFile);
                channelSettingsDialog = new ChannelSettings();
                channelSettingsDialog.setArguments(channelSettingsBundle);
                channelSettingsDialog.show(FileExploreAdapter.myContext.getSupportFragmentManager(), "ChannelSettingsDialog");

                break;

            case R.id.fe_create_uc:

                getDialog().dismiss();
                channelSettingsBundle = new Bundle();
                channelSettingsBundle.putString("ct", "uc");
                channelSettingsBundle.putString("id", id);
                channelSettingsBundle.putString("userName", userName);
                channelSettingsBundle.putString("password", password);
                channelSettingsBundle.putString("baseUrl", baseUrl);
                channelSettingsBundle.putString("type", type);
                channelSettingsBundle.putString("baseNameFile", baseNameFile);
                channelSettingsDialog = new ChannelSettings();
                channelSettingsDialog.setArguments(channelSettingsBundle);
                channelSettingsDialog.show(FileExploreAdapter.myContext.getSupportFragmentManager(), "ChannelSettingsDialog");

                break;

            case R.id.fe_create_apps:

                getDialog().dismiss();
                channelSettingsBundle = new Bundle();
                channelSettingsBundle.putString("ct", "apps");
                channelSettingsBundle.putString("id", id);
                channelSettingsBundle.putString("userName", userName);
                channelSettingsBundle.putString("password", password);
                channelSettingsBundle.putString("baseUrl", baseUrl);
                channelSettingsBundle.putString("type", type);
                channelSettingsBundle.putString("baseNameFile", baseNameFile);
                channelSettingsDialog = new ChannelSettings();
                channelSettingsDialog.setArguments(channelSettingsBundle);
                channelSettingsDialog.show(FileExploreAdapter.myContext.getSupportFragmentManager(), "ChannelSettingsDialog");

                break;

            case R.id.fe_find_helper:
                getDialog().dismiss();
                findHelperBundle = new Bundle();
                findHelperBundle.putString("ct", "apps");
                findHelperBundle.putString("id", id);
                findHelperBundle.putString("userName", userName);
                findHelperBundle.putString("password", password);
                findHelperBundle.putString("baseUrl", baseUrl);
                findHelperBundle.putString("type", type);
                findHelperBundle.putString("baseNameFile", baseNameFile);
                findHelperDialog = new FindHelperDialog();
                findHelperDialog.setArguments(findHelperBundle);
                findHelperDialog.show(FileExploreAdapter.myContext.getSupportFragmentManager(), "FindHelperDialog");

                break;

            case R.id.fe_add_to_marketplace:

                getDialog().dismiss();
                addToMarketplaceBundle = new Bundle();
                addToMarketplaceBundle.putString("ct", "apps");
                addToMarketplaceBundle.putString("id", id);
                addToMarketplaceBundle.putString("userName", userName);
                addToMarketplaceBundle.putString("password", password);
                addToMarketplaceBundle.putString("baseUrl", baseUrl);
                addToMarketplaceBundle.putString("type", type);
                addToMarketplaceBundle.putString("baseNameFile", baseNameFile);
                addToMarketPlaceDialog = new AddToMarketPlaceDialog();
                addToMarketPlaceDialog.setArguments(addToMarketplaceBundle);
                addToMarketPlaceDialog.show(FileExploreAdapter.myContext.getSupportFragmentManager(), "AddToMarketPlaceDialog");

                break;
        }
    }

    private void handlingError(VolleyError error) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            alert.showAlertDialog(FileExploreAdapter.context, "Network Error", "Request Time-Out", "Close");
        } else if (error instanceof AuthFailureError) {
            alert.showAlertDialog(FileExploreAdapter.context, "Authentication Error", "Authentication Failed" + error.toString(), "Close");
        } else if (error instanceof ServerError) {
            alert.showAlertDialog(FileExploreAdapter.context, "Server Error", "Internal Server Error", "Close");
        } else if (error instanceof NetworkError) {
            alert.showAlertDialog(FileExploreAdapter.context, "Network Error", "Unable To Access The Network" + error.toString(), "Close");
        } else if (error instanceof ParseError) {
            alert.showAlertDialog(FileExploreAdapter.context, "Parse Error", "There was a problem parsing the package" + error.toString(), "Close");
        }
    }
}
