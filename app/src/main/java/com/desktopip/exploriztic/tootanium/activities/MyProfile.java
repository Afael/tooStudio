package com.desktopip.exploriztic.tootanium.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.models.NewProfile;
import com.desktopip.exploriztic.tootanium.models.PostNewProfile;
import com.desktopip.exploriztic.tootanium.utilities.ImageInject;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.utilities.Utils;
import com.fxn.pix.Pix;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MyProfile extends Fragment implements View.OnClickListener, Response.Listener<String>, Response.ErrorListener {

    private static final String TAG = "Profile";

    private View view;

    private static SessionManager session;
    private static String baseUrl, urlAPI, uid, userId;
    private static HashMap<String, String> user;

    private final String GET_PROFILE_URL = "/action/get_user_profile.php";

    private ImageView profileImage;

    private String selectedProfileImagePath;
    private boolean isImageChange;

    private String  defaultFullName,
            defaultEmail,
            defaultPosition,
            defaultImageUrl;

    private TextView usernameET,
            emailET,
            fullNameET,
            positionET;

    private Button imagePickerBtn;

    private MenuItem editMenu,
            saveMenu;

    private TextView imageFileName;

    private Bitmap bitmap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (inflater != null) {

            view = inflater.inflate(R.layout.activity_my_profile, container, false);

            init();
            getProfile();
        }
        return view;
    }

    //init parameter
    private void init() {

        session = new SessionManager(getActivity());

        user = session.getUserDetails();

        userId = Utils.base64Helper(user.get(SessionManager.KEY_USERNAME));
        baseUrl = user.get(SessionManager.BASE_URL);

        profileImage = view.findViewById(R.id.profil_image);
        usernameET = view.findViewById(R.id.profile_username);
        emailET = view.findViewById(R.id.profile_email);
        fullNameET = view.findViewById(R.id.profile_fullname);
        positionET = view.findViewById(R.id.profile_position);
        imagePickerBtn = view.findViewById(R.id.profile_setimage_btn);
        imageFileName = view.findViewById(R.id.profile_chosen_image_text);
        imagePickerBtn.setOnClickListener(this);
    }

    private void getProfile(){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//
        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl + GET_PROFILE_URL, this, this) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user", userId);

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
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.profile_setimage_btn:
                break;
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String result) {

        try {
            JSONObject jsonObject = new JSONObject(result);
            Log.d("my_profile", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
