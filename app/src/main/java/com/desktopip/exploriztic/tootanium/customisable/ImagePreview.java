package com.desktopip.exploriztic.tootanium.customisable;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.adapters.FileExploreAdapter;
import com.desktopip.exploriztic.tootanium.interfaces.IFileExplore;
import com.desktopip.exploriztic.tootanium.utilities.CustomAlertDialogManager;
import com.desktopip.exploriztic.tootanium.utilities.DownloadTask;
import com.desktopip.exploriztic.tootanium.volley.FileExplorerServices;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class ImagePreview extends DialogFragment implements View.OnClickListener {

    private View dialogImagePreviewView;
    private ImageView fe_image_view;
    private Button btn_image_viewer_close, btn_image_download;

    private DownloadTask downloadTask;
    private CustomAlertDialogManager alert;

    private String id, userName, password, urlDownload, fileName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (inflater != null) {
            dialogImagePreviewView = inflater.inflate(R.layout.frag_image_viewer, container, false);

            fe_image_view = dialogImagePreviewView.findViewById(R.id.fe_image_view);
            btn_image_viewer_close = dialogImagePreviewView.findViewById(R.id.btn_image_viewer_close);
            btn_image_download = dialogImagePreviewView.findViewById(R.id.btn_image_download);

            Bundle ImagePreviewBundle = getArguments();
            urlDownload = ImagePreviewBundle.getString("urlDownload");
            fileName = ImagePreviewBundle.getString("fileName");
            id = ImagePreviewBundle.getString("id");
            userName = ImagePreviewBundle.getString("userName");
            password = ImagePreviewBundle.getString("password");

            Picasso.with(FileExploreAdapter.context)
                    .load(urlDownload + fileName)
                    .resize(1500, 0)
                    .placeholder(R.drawable.loading_shape)
                    .error(R.drawable.ic_error_outline_black_24dp)
                    .into(fe_image_view);

            btn_image_viewer_close.setOnClickListener(this);

            btn_image_download.setOnClickListener(this);

            alert = new CustomAlertDialogManager();
        }

        return dialogImagePreviewView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_image_viewer_close:
                getDialog().dismiss();
                FileExplorerServices.clearTempMusicPlayer(new IFileExplore() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        //Log.d("clear", result.toString());
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                }, FileExploreAdapter.context, "clearTempAudioPlay", userName, fileName);
                break;
            case R.id.btn_image_download:
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
