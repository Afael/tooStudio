package com.desktopip.exploriztic.tootanium.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.desktopip.exploriztic.tootanium.interfaces.ICallBack;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.OkHttpClient;


/**
 * Created by Jayus on 17/07/2018.
 */

public class UploadTask {

    AlertDialogManager alertDialogManager;

    private Context context;
    // Session manager
    SessionManager session;
    // username
    String userName, uid;
    // password
    String password;

    private String path;
    Intent data;
    private ICallBack iCallBack;

    // URL UPLOAD
    String urlUpload;
    ProgressDialog dialog;
    UploadingTask uploadingTask;

    OkHttpClient client = null;
    private String TAG_UPLOAD = "upload";

    long totalSize = 0;

    public UploadTask(Context context, Intent data, String path, ICallBack callBack) {

        this.context = context;
        this.iCallBack = callBack;
        this.data = data;
        this.path = path;

        session = new SessionManager(context);

        HashMap<String, String> user = session.getUserDetails();

        uid = user.get(SessionManager.KEY_UID);
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);
        urlUpload = user.get(SessionManager.BASE_URL_UPLOAD);
        alertDialogManager = new AlertDialogManager();

        dialog = new ProgressDialog(context);

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.cancel();
                if (uploadingTask != null && uploadingTask.getStatus() != AsyncTask.Status.FINISHED) {
                    uploadingTask.cancel(true);
                    abortUpload(client, TAG_UPLOAD);
                }
            }
        });

        uploadingTask = new UploadingTask();
        uploadingTask.execute();
    }

    private class UploadingTask extends AsyncTask<Void, Integer, String> {

        int progress;
        String message;

        @Override
        protected void onPreExecute() {

            dialog.setTitle("Upload Progress");
            dialog.setMessage("Please wait your file is uploading");
            dialog.setCancelable(false);
            dialog.setMax(100);
            dialog.setProgress(0);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setIndeterminate(false);
            dialog.show();

            progress = 0;

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {

            dialog.dismiss();
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

            iCallBack.refreshList();

            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // Update Progress the dialog
            dialog.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(Void... arg0) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile(){

            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urlUpload);


            try {
                SpatialUploadProgress entity = new SpatialUploadProgress(
                        new SpatialUploadProgress.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                String contentType = getMimeType(sourceFile.getPath());
                String passwordBase64 = StringHelper.base64Encode(password);
                String pathBase64 = StringHelper.base64Encode(path);
                // Adding file data to http body
                entity.addPart("file", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("uid", new StringBody(uid));
                entity.addPart("password", new StringBody(passwordBase64));
                entity.addPart("path", new StringBody(pathBase64));
                entity.addPart("type", new StringBody(contentType));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                    JSONObject jsonObject = new JSONObject(responseString);
                    message = jsonObject.getString("message");
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                    message = responseString;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {

            }

            return responseString;

        }

        private String getMimeType(String path) {
            String ext = MimeTypeMap.getFileExtensionFromUrl(path);
            return ext;//MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        }

    }

    public void abortUpload(OkHttpClient client, String tag) {
        client.dispatcher().cancelAll();
    }

}
