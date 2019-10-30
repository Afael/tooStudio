package com.desktopip.exploriztic.tootanium.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


/**
 * Created by Jayus on 17/07/2018.
 */

public class DownloadTask {
    private static final String TAG = "Download Task";
    private Context context;

    private String downloadUrl = "", downloadFileName = "";
    // Progress Dialog to show progress on dialog


    // Session manager
    SessionManager session;
    // URL DOWNLOAD
    String baseUrlDownload = "";

    DownloadingTask downloadingTask;
    ProgressDialog dialog;

    public DownloadTask(Context context, String urlDownload) {
        this.context = context;
        this.downloadUrl = urlDownload;

        session = new SessionManager(context);

        HashMap<String, String> user = session.getUserDetails();
        baseUrlDownload = user.get(SessionManager.BASE_URL_DOWNLOAD);
        //Log.e("SessionUrl", baseUrlDownload);
        downloadFileName = downloadUrl.replace(StringHelper.substringBeforeLast(downloadUrl, "/"), "");//Create file name by picking download file name from URL
        //Log.e("fileName", downloadUrl);

        dialog = new ProgressDialog(context);

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (downloadingTask != null && downloadingTask.getStatus() != AsyncTask.Status.FINISHED)
                    downloadingTask.cancel(true);
                //Log.d(TAG, "onClick: Cancel task");
            }
        });

        //Start Downloading Task
        downloadingTask = new DownloadingTask();
        downloadingTask.execute();
    }


    private class DownloadingTask extends AsyncTask<Void, Integer, Void> {

        File apkStorage = null;
        File outputFile = null;

        int progress;
        String message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Setting title to dialog
            //dialog.setTitle("Downloading File");
            dialog.setMessage("Please wait your file is downloading");

            dialog.setCancelable(false);

            dialog.setMax(100);

            dialog.setProgress(0);

            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            dialog.setIndeterminate(false);

            // Showing dialog
            dialog.show();

            progress = 0;
        }

        @Override
        protected void onPostExecute(Void result) {

            try {

                if(isCancelled())
                    message = "Canceled";

                if (outputFile != null) {
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        }
                    }, 3000);

                    Log.e(TAG, "Download Failed");
                    message = "Download Failed";
                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs
                //buttonText.setText(R.string.downloadFailed);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //buttonText.setEnabled(true);
                        //buttonText.setText(R.string.downloadAgain);
                    }
                }, 3000);
                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());
                message = "Download Failed with Exception - "+ e.getLocalizedMessage();
            }
            dialog.dismiss();
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // Update Progress the dialog
            dialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                int fileLength = c.getContentLength();

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());
                    message = "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage();
                }

                //Get File if SD card is present
                if (new CheckForSDCard().isSDCardPresent()) {

                    apkStorage = new File(
                            Environment.getExternalStorageDirectory() + "/"
                                    + Utils.downloadDirectory);
                } else
                    message = "Oops!! There is no SD Card.";

                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e(TAG, "Directory Created.");
                }

                outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File
                int i = 1;
                String fileName = StringHelper.substringBeforeLast(downloadFileName, ".");
                String fileExt = StringHelper.substringAfterLast(downloadFileName, ".");
                while (outputFile.exists()){
                    //downloadFileName += "(" + (i++) +")";
                    downloadFileName.split(".");

                    downloadFileName = fileName + "(" + (i++) +")" + "."+fileExt;
                    outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File
                    //Log.e(TAG, "File Created."+downloadFileName);
                }

                //Create New File if not present
                if (!outputFile.exists()) {
                    FileOutputStream fos;
                    InputStream is;

                    byte[] buffer = new byte[1024];//Set buffer type
                    long total = 0;
                    int count;//init length

                    outputFile.createNewFile();
                    fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location
                    is = c.getInputStream();//Get InputStream for connection

                    while ((count = is.read(buffer)) != -1) {
                        if (isCancelled())
                        {
                            is.close();
                            outputFile.delete();
                            return null;
                        }
                        total += count;
                        if(fileLength > 0 )
                            publishProgress((int) (total * 100) / fileLength);
                        fos.write(buffer, 0, count);//Write new file
                    }

                    //Close all connection after doing task
                    fos.close();
                    is.close();

                    Log.e(TAG, "File Downloaded");
                    message = "File Downloaded";

                }
                else{
                    message = "File Already Exists";
                    //Log.e(TAG, "File Already Exists");
                }

            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
                message = "Download Error Exception " + e.getMessage();
            }

            return null;
        }
    }

}
