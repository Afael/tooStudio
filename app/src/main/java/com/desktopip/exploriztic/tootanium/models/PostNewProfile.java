package com.desktopip.exploriztic.tootanium.models;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PostNewProfile implements Response.Listener<String>, Response.ErrorListener {

    private Context context;

    private SessionManager sessionManager;
    private String userName;
    private String baseUrl;

    private final String USER_NAME = "userid",
            FULL_NAME = "fullname",
            EMAIL = "email",
            POSITION = "position",
            PROFILE_IMAGE_PATH = "image";


    public PostNewProfile(Context context) {

        this.context = context;
        sessionManager = new SessionManager(context);
        HashMap<String, String> userDetail = sessionManager.getUserDetails();
        userName = userDetail.get(SessionManager.KEY_USERNAME);
        baseUrl = userDetail.get(SessionManager.BASE_URL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
    }

    public void posNewSetting(final NewProfile personalization, String uri) {

        Log.d("profile", personalization.getEmail()
                +"-"+personalization.getFullName()
                +"-"+personalization.getPosition()
                +"-"+personalization.getImageUrl()
        );
//        String uploadId = UUID.randomUUID().toString();
//        try {
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
////                return;
////            else
//                new MultipartUploadRequest(context, uploadId, baseUrl + uri)
//                        .addFileToUpload(personalization.getImageUrl(), PROFILE_IMAGE_PATH) //Adding file
//                        .addParameter(USER_NAME, userName) //Adding text parameter to the request
//                        .addParameter(FULL_NAME, personalization.getFullName()) //Adding text parameter to the request
//                        .addParameter(EMAIL, personalization.getEmail()) //Adding text parameter to the request
//                        .addParameter(POSITION, personalization.getPosition()) //Adding text parameter to the request
//                        .setNotificationConfig(new UploadNotificationConfig()
//                                .setErrorMessage("Something wrong")
//                                .setTitle("Notification")
//                                .setInProgressMessage("Please wait")
//                                .setCompletedMessage("Data was updated")
//                                .setAutoClearOnSuccess(true))
//                        .setMaxRetries(2)
//                        .startUpload(); //Starting the upload
//
//        } catch (Exception exc) {
//            Toast.makeText(context, exc.getMessage(), Toast.LENGTH_SHORT).show();
//        }


        RequestQueue requestQueue = Volley.newRequestQueue(context);
//
        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl + uri, this, this) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(USER_NAME, userName);
                params.put(FULL_NAME, personalization.getFullName());
                params.put(EMAIL, personalization.getEmail());
                params.put(POSITION, personalization.getPosition());
                params.put(PROFILE_IMAGE_PATH, personalization.getImageUrl());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("content-type", "multipart/form-data");
                return params;
            }
        };
        requestQueue.add(stringRequest);

//        SimpleMultiPartRequest req = new SimpleMultiPartRequest(Request.Method.POST, baseUrl + uri, this, this);
//        req.addStringParam(USER_NAME, userName);
//        req.addStringParam(FULL_NAME, personalization.getFullName());
//        req.addStringParam(EMAIL, personalization.getEmail());
//        req.addStringParam(POSITION, personalization.getPosition());
//        req.addFile(PROFILE_IMAGE_PATH, personalization.getImageUrl());
//        requestQueue.add(req);
    }

    @Override
    public void onResponse(String response) {
        Log.d("VolleyProfile", "onResponse: "+response);
        String data = "";
        try {

            JSONObject jsonObject = new JSONObject(response);
            data = jsonObject.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("VolleyProfile", "onError: "+error.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.desktopip.com";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_chat)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        //startMyOwnForeground(2, notification);
    }

    public interface PostCommentResponseListener {
        void passData(String response);
    }
}
