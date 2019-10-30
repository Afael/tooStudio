package com.desktopip.exploriztic.tootanium.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.models.SpartialColor;
import com.desktopip.exploriztic.tootanium.utilities.ColorHelper;
import com.desktopip.exploriztic.tootanium.utilities.MySingleton;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.utilities.SharedPrefManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SpatialAppCompat extends AppCompatActivity {

    private SessionManager sessionManager;

    protected String baseUrl, userName;

    protected Toolbar mainToolbar;
    protected void setToolbar(Toolbar mainToolbar){
        this.mainToolbar = mainToolbar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        userName = user.get(SessionManager.KEY_USERNAME);
        baseUrl = user.get(SessionManager.BASE_URL);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setActionBarColor();
    }

    protected void getImageUrl(final ImageView root){

        String sBaseUrl = baseUrl+"action/themes_list.php";
        final String[] url = new String[1];

        StringRequest request = new StringRequest(Request.Method.POST, sBaseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String mImageUrl;
                try {
                    JSONObject jsonObject = new JSONObject(response);


                    if(jsonObject.getString("status").equals("1")){
                        JSONArray data = jsonObject.getJSONArray("data");
                        JSONObject imageUrl = data.getJSONObject(0);
                        url[0] = imageUrl.getString("wallpaper");
                        mImageUrl = baseUrl+ url[0];

                        Picasso.with(SpatialAppCompat.this).load(mImageUrl).error(R.drawable.background).into(root);
                    }
                    Picasso.with(SpatialAppCompat.this).load(R.drawable.background).error(R.drawable.background).into(root);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Picasso.with(SpatialAppCompat.this).load(R.drawable.background).into(root);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SpatialAppCompat.this,"ERROR", Toast.LENGTH_LONG).show();
                Log.d("TAG", "onResponse: Gagal");
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> param = new HashMap<>();
                param.put("user",userName);
                return param;
            }


        };
        MySingleton.getInstance(this).addToRequestQueue(request);
        //requestQueue.add(request);
    }

    public static void setColorView(View view, Context context){
        SpartialColor spartialColor = SpartialColor.getColor(context, getValueContext(R.string.default_action_bar_color_key, R.string.system_default_action_bar_color_key, context));
        view.setBackgroundColor(Color.parseColor(spartialColor.getColorAlpha()));
    }

    public static String getValueContext(@StringRes int keyValue, @StringRes int defaultValue, Context context){

        String a = context.getResources().getString(keyValue);
        String b = context.getResources().getString(defaultValue);

        String data = SharedPrefManager.getString(context, a, b);
        return data;
    }

    protected void setActionBarColor(){
        //int allColor = SharedPrefManager.getInt(this, getResources().getString(R.string.action_bar_color_key), getResources().getColor(R.color.colorPrimary));

        SpartialColor spartialColor = SpartialColor.getColor(getBaseContext(), getValue(R.string.default_action_bar_color_key, R.string.system_default_action_bar_color_key));

        mainToolbar.setBackgroundColor( spartialColor.colorResId());

        getWindow().setStatusBarColor(Color.parseColor(spartialColor.getColorAlpha()));
    }

    public static void setActionBarColor(Toolbar toolbar, Context context){
        //int allColor = SharedPrefManager.getInt(this, getResources().getString(R.string.action_bar_color_key), getResources().getColor(R.color.colorPrimary));

        SpartialColor spartialColor = SpartialColor.getColor(context, getValueContext(R.string.default_action_bar_color_key, R.string.system_default_action_bar_color_key, context));

        toolbar.setBackgroundColor( spartialColor.colorResId());
    }

    private String getValue(@StringRes int keyValue, @StringRes int defaultValue){

        String a = getResources().getString(keyValue);
        String b = getResources().getString(defaultValue);

        String data = SharedPrefManager.getString(this, a, b);
        return data;
    }

    private int getActionBarValue(@StringRes int keyValue){
        return SharedPrefManager.getInt(this, getResources().getString(keyValue), ColorHelper.fromResources(this,R.color.colorPrimary));
    }
}
