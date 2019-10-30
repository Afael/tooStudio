package com.desktopip.exploriztic.tootanium;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.desktopip.exploriztic.tootanium.activities.About;
import com.desktopip.exploriztic.tootanium.activities.HelpGuide;
import com.desktopip.exploriztic.tootanium.activities.MyProfile;
import com.desktopip.exploriztic.tootanium.activities.Personalization;
import com.desktopip.exploriztic.tootanium.activities.Profile;
import com.desktopip.exploriztic.tootanium.activities.SpatialAppCompat;
import com.desktopip.exploriztic.tootanium.fragment.FragAppList;
import com.desktopip.exploriztic.tootanium.fragment.FragFileExplore;
import com.desktopip.exploriztic.tootanium.fragment.FragMainOnlineStorage;
import com.desktopip.exploriztic.tootanium.fragment.FragSystemPreferences;
import com.desktopip.exploriztic.tootanium.fragment.FragWorkingStudio;
import com.desktopip.exploriztic.tootanium.models.ModAppList;
import com.desktopip.exploriztic.tootanium.models.Wallpaper;
import com.desktopip.exploriztic.tootanium.unstopable.SpatialWebView;
import com.desktopip.exploriztic.tootanium.unstopable.WebFragment;
import com.desktopip.exploriztic.tootanium.utilities.AppController;
import com.desktopip.exploriztic.tootanium.utilities.CheckInternetConnection;
import com.desktopip.exploriztic.tootanium.utilities.GetLastVersion;
import com.desktopip.exploriztic.tootanium.utilities.MySingleton;
import com.desktopip.exploriztic.tootanium.utilities.NetworkManager;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.utilities.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

//import com.facebook.accountkit.ui.AccountKitActivity;
//import com.facebook.accountkit.ui.AccountKitConfiguration;
//import com.facebook.accountkit.ui.LoginType;

public class MainActivity extends SpatialAppCompat implements
        NavigationView.OnNavigationItemSelectedListener,
        CheckInternetConnection.ConnectivityReceiverListener {

    public static FragWorkingStudio fragmentContainer;
    private static MyProfile fragmentMyProfile;

    // Session manager
    SessionManager session;
    // username
    String userName;
    // password
    String password;
    //Base URL
    String baseURL;

    Button dialog_confirm_cancel, dialog_confirm_ok;
    TextView dialog_confirm_message, user_login;

    // Alert dialog manager
    Dialog customDialog;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    String activityTitle;
    String[] items;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Toolbar mainToolbar;

    public static ArrayList<Wallpaper> listWallpaper;

    FragMainOnlineStorage fragMainOnlineStorage;
    FragFileExplore fragFileExplore;
    FragSystemPreferences fragSystemPreferences;
    FragAppList fragAppList;

    List<ModAppList.Template> templateApps;
    List<ModAppList.AppsRecent> appsRecents;

    String latestVersion;
    String currentVersion;
    private SpatialWebView SpatialWebView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //NetworkManager.getInstance(this);

//        CalligraphyConfig.initDefault(new CalligraphyConfig
//                .Builder()
//                .setDefaultFontPath("fonts/ClearSans-Medium.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build());

        setContentView(R.layout.main_activity);

        session = new SessionManager(this);
        if (session.checkLogin()) {

            initApp();
            if (savedInstanceState == null) {
                //selectFirstItemAsDefault();
                Bundle bundle = new Bundle();
                fragmentContainer = new FragWorkingStudio();
//                fragmentMyProfile = new MyProfile();
                bundle.putString("Test", "Cocok");
                fragmentContainer.setArguments(bundle);
                callFragment(fragmentContainer);
                if(!checkingUpdate()){
                    return;
                }
            }

        } else {
            finish();
        }

    }

    private boolean checkingUpdate(){
        currentVersion = getCurrentVersion();
        try {
            latestVersion = new GetLastVersion().execute().get();
            if(latestVersion != null){
                if (!currentVersion.equalsIgnoreCase(latestVersion)){
                    showUpdateDialog();
                    return true;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getCurrentVersion(){
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo =  pm.getPackageInfo(this.getPackageName(),0);

        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        String currentVersion = pInfo.versionName;

        return currentVersion;
    }

    private void showUpdateDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("A New Update is Available");
        builder.setMessage("Before using this application, You must update to the latest version");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("https://play.google.com/store/apps/details?id=com.desktopip.exploriztic.tootanium")));
                dialog.dismiss();
                MainActivity.this.finish();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MainActivity.this.finish();
            }
        });

        builder.setCancelable(false);
        customDialog = builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Untuk handle onActivityResult dari fragment
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment != null) {
                //Log.d("thread", ""+fragment);
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void getAppList(){
        String pathUrl = "backend/?c=apps_group&m=load_apps";
        String url = baseURL + pathUrl;//"railiztic-admin/index.php?p=apps&c=User&a=checkUser";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonObject;
                ModAppList.Template templateList;
                ModAppList.AppsRecent appsRecent;
                List<ModAppList.APPS> appChild = new ArrayList<>();

                try {
                    jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if(status.equals("success")){

                        JSONArray jsonTemplate = jsonObject.getJSONArray("template");

                        for(int iTemplate = 0; iTemplate<jsonTemplate.length(); iTemplate++){

                            JSONObject data = jsonTemplate.getJSONObject(iTemplate);

                            templateList = new ModAppList.Template();
                            templateList.setU_APP_ID(data.getString("U_APP_ID"));
                            templateList.setGID(data.getString("GID"));
                            templateList.setGROUP_NAME(data.getString("GROUP_NAME"));

                            String[] splitAppsId = data.getString("APP_IDS").split("@");
                            String[] splitAppsName = data.getString("APPS_NAME").split("@");
                            String[] splitAppsPath = data.getString("APPS_PATH").split("@");
                            String[] splitAppsIcons = data.getString("ICONS").split("@");
                            String[] splitAppsActive = data.getString("APP_ACTIVE").split("@");

                            for(int iApps = 0; iApps<splitAppsId.length; iApps++){
                                templateList.addAppList(new ModAppList.APPS(splitAppsId[iApps], splitAppsName[iApps], splitAppsPath[iApps], splitAppsIcons[iApps], splitAppsActive[iApps]));
                            }

                            //templateList.setAPPS_LIST(appChild);
                            templateApps.add(templateList);

                        }

                        JSONArray appRecents = jsonObject.getJSONArray("apps_recents");
                        for(int iAppRecent = 0; iAppRecent<appRecents.length();iAppRecent++){
                            JSONObject dataRecents = appRecents.getJSONObject(iAppRecent);
                            appsRecent = new ModAppList.AppsRecent();
                            appsRecent.setID(dataRecents.getString("ID"));
                            appsRecent.setAPP_ID(dataRecents.getString("APP_ID"));
                            appsRecent.setUSER(dataRecents.getString("USER"));
                            appsRecent.setCLICK(dataRecents.getString("CLICK"));
                            appsRecent.setDATE_CREATED(dataRecents.getString("DATE_CREATED"));
                            appsRecent.setDATE_MODIFIED(dataRecents.getString("DATE_MODIFIED"));
                            appsRecent.setAPP_NAME(dataRecents.getString("APP_NAME"));
                            appsRecent.setAPP_PATH(dataRecents.getString("APP_PATH"));
                            appsRecent.setICON(dataRecents.getString("ICON"));

                            appsRecents.add(appsRecent);
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "Something wrong!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handlingError(error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("datas[user]", userName);
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void getUserPersonalizationSettings(){

        String url = baseURL + "assets/apps/image/wp_users/"+userName+".json";
        Log.d("user_personal", url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("user_personal", response);
                    JSONObject jsonObject = new JSONObject(response);

                    if(!response.contains("status")){
                        String lockScreen = jsonObject.getString("lockscreen");
                        String defaultColor = jsonObject.getString("basic_color");
                        String defaultWallpaper = jsonObject.getString("wallpaper");

                        SharedPrefManager.saveString(getBaseContext(), getString(R.string.default_lock_screen_key), lockScreen);
                        SharedPrefManager.saveString(getBaseContext(), getString(R.string.default_action_bar_color_key), defaultColor);
                        SharedPrefManager.saveString(getBaseContext(), getString(R.string.default_wallpaper_key), defaultWallpaper);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Wallpaper", "onErrorResponse: "+e.toString());
                }

                Log.d("Wallpaper", "onResponse: "+response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handlingError(error);
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void getPersonalizationList(){
        String url = baseURL + "action/themes_list.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    listWallpaper = new ArrayList<>();
                    listWallpaper.add(new Wallpaper(Wallpaper.ADD_BUTTON));
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for(int i = 0; i< jsonArray.length(); i++){
                            JSONObject data = jsonArray.getJSONObject(i);
                            String urlJason = data.getString("wallpaper").substring(1);
                            listWallpaper.add(new Wallpaper(baseURL+urlJason));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("Theme", "onResponse: "+response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handlingError(error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user", userName);
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    private void initApp() {

        getImageUrl((ImageView) findViewById(R.id.background));

        customDialog = new Dialog(this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before

        //Get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // username
        userName = user.get(SessionManager.KEY_USERNAME);
        // password
        password = user.get(SessionManager.KEY_PASSWORD);
        // base URL
        baseURL = user.get(SessionManager.BASE_URL);

        mainToolbar = findViewById(R.id.main_toolbar);
        mainToolbar.setTitle(getString(R.string.app_name));

        setSupportActionBar(mainToolbar);
        setToolbar(mainToolbar);

        checkConnection();
        // Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        activityTitle = getTitle().toString();

        View listHeaderView = getLayoutInflater().inflate(R.layout.main_nav_header, null, false);
        user_login = listHeaderView.findViewById(R.id.user_login);
        user_login.setText("you log in as ("+userName+")");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, mainToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        //get property from nav_header
        View header = navigationView.getHeaderView(0);
        //Initializing textView
        user_login = header.findViewById(R.id.user_login);
        user_login.setText("you log in as ("+userName+")");
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        fragSystemPreferences = new FragSystemPreferences();
        fragMainOnlineStorage = new FragMainOnlineStorage();
        fragFileExplore = new FragFileExplore();
        fragAppList = new FragAppList();
        templateApps = new ArrayList<>();
        appsRecents = new ArrayList<>();

        getAppList();
        getPersonalizationList();
        getUserPersonalizationSettings();
    }

    public void callFragment(Fragment fragment) {
        if (fragment != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.remove(fragment);
            fragmentTransaction.replace(R.id.frame_container, fragment);
            fragmentTransaction.commit();
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:

                if(!fragmentContainer.isVisible()){
                    Bundle bundle = new Bundle();
                    fragmentContainer = new FragWorkingStudio();
                    bundle.putString("Test", "Cocok");
                    fragmentContainer.setArguments(bundle);
                    callFragment(fragmentContainer);
                }

                break;

            case R.id.nav_personalization:
                startActivity(new Intent(MainActivity.this, Personalization.class));
                break;

            case R.id.nav_profile:
                fragmentMyProfile = new MyProfile();
                callFragment(fragmentContainer);
                //startActivity(new Intent(MainActivity.this, Profile.class));
                break;

//            case R.id.nav_marketplace:
                //startActivity(new Intent(MainActivity.this, MarketPlace.class));
//                break;

            //case R.id.nav_language:
                //startActivity(new Intent(MainActivity.this, MarketPlace.class));
                //break;

            case R.id.nav_help_guide:
                startActivity(new Intent(MainActivity.this, HelpGuide.class));
                break;

            case R.id.nav_about:
                startActivity(new Intent(MainActivity.this, About.class));
                break;

            case R.id.nav_logout:
                showAlertDialog("Logout", "Are you sure, want to logout?", "logout");
                break;
        }
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(Gravity.START);
        return true;
    }

    private void showSnack(boolean isConnected) {
        String message = null;
        int color = 0;
        if (!isConnected) {
            message = "Sorry! Not connected to internet";
            color = Color.YELLOW;
            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            //TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            //textView.setTextColor(color);
            snackbar.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        AppController.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = CheckInternetConnection.isConnected();
        showSnack(isConnected);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog("Exit", "Are you sure, want to exit?", "back_exit");
    }

    private void showAlertDialog(String button, String message, final String tag) {

        customDialog.setContentView(R.layout.custom_popup_confirm);
        dialog_confirm_message = customDialog.findViewById(R.id.dialog_confirm_message);
        dialog_confirm_cancel = customDialog.findViewById(R.id.dialog_confirm_cancel);
        dialog_confirm_ok = customDialog.findViewById(R.id.dialog_confirm_ok);
        dialog_confirm_message.setText(message);
        dialog_confirm_ok.setText(button);

        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.setCancelable(false);
        customDialog.show();

        dialog_confirm_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });

        dialog_confirm_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tag.equals("logout")) {
                    if (customDialog.isShowing())
                        customDialog.cancel();

                    session.logoutUser();
                    finish();
                } else {
                    if (customDialog.isShowing())
                        customDialog.cancel();

                    moveTaskToBack(true);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        mainToolbar.getMenu().clear();
        mainToolbar.inflateMenu(R.menu.menu_main);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_file_explorer:

                if(!fragMainOnlineStorage.isVisible()){
                    fragMainOnlineStorage.show(getSupportFragmentManager(), fragMainOnlineStorage.getTag());
                }
                return true;

            case R.id.menu_application:

                if(!fragAppList.isVisible()){
                    FragAppList appList = FragAppList.newInstance(MainActivity.this, templateApps);
                    appList.show(getSupportFragmentManager(), appList.getTag());
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handlingError(VolleyError error) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Toast.makeText(this, ""+R.string.error_timeout, Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(this, ""+R.string.error_auth, Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(this, ""+R.string.error_server, Toast.LENGTH_SHORT).show();
        } else if (error instanceof NetworkError) {
            Toast.makeText(this, ""+R.string.error_network, Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(this, ""+R.string.error_parse, Toast.LENGTH_SHORT).show();
        }
    }
}
