package com.desktopip.exploriztic.tootanium.customisable;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.desktopip.exploriztic.tootanium.adapters.FileExploreAdapter;
import com.desktopip.exploriztic.tootanium.fragment.FragFileExplore;
import com.desktopip.exploriztic.tootanium.fragment.FragFileExplorerDownloaded;
import com.desktopip.exploriztic.tootanium.interfaces.IFileExplore;
import com.desktopip.exploriztic.tootanium.utilities.CustomAlertDialogManager;
import com.desktopip.exploriztic.tootanium.utilities.StringHelper;
import com.desktopip.exploriztic.tootanium.volley.FileExplorerServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.desktopip.exploriztic.tootanium.adapters.FileExploreAdapter.fragment;

public class NewAndRename extends DialogFragment implements View.OnClickListener {

    View newAndRenameFile;
    CustomAlertDialogManager alert;

    TextView fe_rename_create_folder;
    EditText fe_new_name;
    Button rename_save, rename_cancel;

    Bundle newAndRenameBundle;

    String id, userName, password, baseUrl, baseNameFile, type, renameType, pathSpinner, anuAn, currDirectory;
    int listSize;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (inflater != null) {
            newAndRenameFile = inflater.inflate(R.layout.fe_rename, container, false);
            fe_rename_create_folder = newAndRenameFile.findViewById(R.id.fe_rename_create_folder);
            fe_new_name = newAndRenameFile.findViewById(R.id.fe_new_name);
            rename_save = newAndRenameFile.findViewById(R.id.rename_save);
            rename_cancel = newAndRenameFile.findViewById(R.id.rename_cancel);

            newAndRenameBundle = getArguments();
            if(newAndRenameBundle != null){

                renameType = newAndRenameBundle.getString("rt");

                if(renameType.equals("rename")){
                    fe_rename_create_folder.setText("Change Name");
                    id = newAndRenameBundle.getString("id");
                    userName = newAndRenameBundle.getString("userName");
                    password = newAndRenameBundle.getString("password");
                    baseUrl = newAndRenameBundle.getString("baseUrl");
                    type = newAndRenameBundle.getString("type");
                    baseNameFile = newAndRenameBundle.getString("baseNameFile");

                    fe_new_name.setText(baseNameFile);

                } else if(renameType.equals("newFolderDownloaded")){
                    fe_rename_create_folder.setText("New Folder");
                    anuAn = newAndRenameBundle.getString("anuAn");
                    currDirectory = newAndRenameBundle.getString("currDirectory");
                } else {
                    fe_rename_create_folder.setText("New Folder");
                    listSize = newAndRenameBundle.getInt("listSize");
                    userName = newAndRenameBundle.getString("userName");
                    password = newAndRenameBundle.getString("password");
                    pathSpinner = newAndRenameBundle.getString("pathSpinner");
                }

            }

            rename_save.setOnClickListener(this);
            rename_cancel.setOnClickListener(this);

            alert = new CustomAlertDialogManager();

            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setCancelable(false);
        }

        return newAndRenameFile;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rename_save:

                String newName = fe_new_name.getText().toString();

                if(newAndRenameBundle != null){

                    if(renameType.equals("rename")){
                        FileExplorerServices.rename(new IFileExplore() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            getDialog().dismiss();
                            fe_new_name.setText(null);
                            try {
                                JSONObject jsonObject = result.getJSONObject("message");
                                Toast.makeText(FileExploreAdapter.context, "" + jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                                ((FragFileExplore) fragment).loadData(StringHelper.substringBeforeLast(id, "/"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            handlingError(error);
                        }

                        }, FileExploreAdapter.context, "rename", userName, password, id
                                    , StringHelper.substringBeforeLast(id, "/") + "/" + newName);

                    } else if(renameType.equals("newFolderDownloaded")){
                        handleNewFolderDownloaded(newName);
                    } else {

                        if (listSize > 0) {
                            handleNewFolder(newName);

                        } else {

                            if (pathSpinner != null) {
                                handleNewFolder(newName);
                            }
                        }
                    }
                }

                break;

            case R.id.rename_cancel:
                getDialog().dismiss();
                break;
        }
    }

    private void handleNewFolderDownloaded(String newFolder){
        if (!newFolder.trim().equals("")) {
            if (anuAn.equals("1")) {
                File m_newPath = new File(currDirectory, newFolder);
                //Log.d("cur dir", currDirectory);
                if (!m_newPath.exists()) {
                    getDialog().dismiss();
                    m_newPath.mkdirs();
                    FragFileExplorerDownloaded.getInstance().getDirFromRoot(currDirectory);
                }
            } else {
                try {
                    FileOutputStream m_Output = new FileOutputStream((currDirectory + File.separator + newFolder), false);
                    m_Output.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(getActivity(), "Folder name can't be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleNewFolder(String newName){
        FileExplorerServices.newFolder(new IFileExplore() {
            @Override
            public void onSuccess(JSONObject result) {
                getDialog().dismiss();
                fe_new_name.setText(null);
                try {
                    JSONObject jsonObject = result.getJSONObject("message");

                    Toast.makeText(FileExploreAdapter.context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    ((FragFileExplore) fragment).loadData(pathSpinner);

                } catch (JSONException e) {
                    alert.showAlertDialog(FileExploreAdapter.context, "Error", e.getMessage(), "Close");
                }
            }

            @Override
            public void onError(VolleyError error) {
                handlingError(error);
            }
        }, getActivity(), "createDirectory", userName, password, pathSpinner, newName);
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
