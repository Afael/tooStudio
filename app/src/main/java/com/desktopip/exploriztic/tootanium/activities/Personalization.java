package com.desktopip.exploriztic.tootanium.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.desktopip.exploriztic.tootanium.MainActivity;
import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.adapters.ColorAdapter;
import com.desktopip.exploriztic.tootanium.adapters.WallpaperAdapter;
import com.desktopip.exploriztic.tootanium.models.SpartialColor;
import com.desktopip.exploriztic.tootanium.models.Wallpaper;
import com.desktopip.exploriztic.tootanium.utilities.Glide4Engine;
import com.desktopip.exploriztic.tootanium.utilities.ImageInject;
import com.desktopip.exploriztic.tootanium.utilities.SharedPrefManager;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Personalization extends SpatialSubAppCompatActivity
        implements WallpaperAdapter.WallpaperOnClickListener,
        WallpaperAdapter.OnAddNewWallpaperClickListener,
        ColorAdapter.ColorOnClick{

    private static final int REQUEST_CODE_CHOOSE = 9283;
    private static final String TAG = "Personalization";

    private TextView previewActionBarTextView;

    private RecyclerView appearanceRv,
            wallpaperRv;

    private ImageView previewWallpaper;

    private ArrayList<Wallpaper> listWallpaper;

    private ArrayList<SpartialColor> listColor;

    private List<String> dataFromImagePicker;

    private String selectedWallpaperURL;

    private SpartialColor selectedSpatialColor;

    private WallpaperAdapter wallpaperAdapter;

    private static final int NO_NEW_COLOR_ID = -1234567890;

    private int colorId = NO_NEW_COLOR_ID;

    private String colorAlpha;

    private boolean hasPersonalizationChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalization);
        setTitle(getString(R.string.personalization_title));

        if(MainActivity.listWallpaper == null){
            listWallpaper = new ArrayList<>();
            listWallpaper.add(new Wallpaper(Wallpaper.ADD_BUTTON));
        } else {
            listWallpaper = MainActivity.listWallpaper;
        }
//        selectedWallpaperURL ;
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        previewActionBarTextView.setBackgroundColor(getActionBarColor());
    }

    @Override
    protected void onResume() {
        previewActionBarTextView.setBackgroundColor(getActionBarColor());
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.personalization_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.personalization_save_btn:
                saveData();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {

            dataFromImagePicker = Matisse.obtainPathResult(data);
            Uri uri = Uri.fromFile(new File(dataFromImagePicker.get(0)));

            wallpaperAdapter.addNewWallPaper(new Wallpaper(uri.toString()));
            wallpaperAdapter.notifyDataSetChanged();

        }
    }

    private void init() {
        setUpParam();
        defaultData();
        setUpRecycler();
    }

    private void openImagePicker(){

        Matisse.from(Personalization.this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .countable(false)
                .maxSelectable(1)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    private void defaultData() {

        ImageInject.setWithGlide(this, R.drawable.dummy_background, previewWallpaper);
    }

    private void setUpParam() {

        previewActionBarTextView = findViewById(R.id.personalization_preview_actionbar);
        wallpaperRv = findViewById(R.id.personalization_wallpaper_rv);
        previewWallpaper = findViewById(R.id.personalization_preview_wallpaper);
        appearanceRv = findViewById(R.id.personalization_appearance_rv);

        listColor = new ArrayList<>(SpartialColor.getPredefineColor(this).values());

        //listWallpaper = new ArrayList<>();


        hasPersonalizationChange = false;
    }

    private void setUpRecycler() {
        setUpAppearanceRV();
        setUpWallpaperRV();
    }

    private void setUpAppearanceRV() {
        appearanceRv = findViewById(R.id.personalization_appearance_rv);

        ColorAdapter colorAdapter = new ColorAdapter(this, listColor, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        appearanceRv.setLayoutManager(linearLayoutManager);
        appearanceRv.setAdapter(colorAdapter);
    }

    private void setUpWallpaperRV() {

        wallpaperAdapter = new WallpaperAdapter(this, listWallpaper, this,this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        wallpaperRv.setLayoutManager(layoutManager);
        wallpaperRv.setAdapter(wallpaperAdapter);
        wallpaperRv.setHasFixedSize(false);
    }

    //Saat Item Wallpaper di click
    @Override
    public void onWallpaperClick(int pos) {
        selectedWallpaperURL = listWallpaper.get(pos).getUri();
        int[] size = {1700,600};
        ImageInject.setWithGlide(this,selectedWallpaperURL,previewWallpaper,size);
        hasPersonalizationChange = true;
    }

    //Saat Item Warna di click
    @Override
    public void onColorIconClick(View view, int pos) {
        selectedSpatialColor = listColor.get(pos);
        int mColor = selectedSpatialColor.colorResId();
        Drawable actionBarColor = new ColorDrawable(mColor);
        previewActionBarTextView.setBackground(actionBarColor);


        if(getSupportActionBar() != null){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(mColor));
            //getWindow().setStatusBarColor(Color.parseColor(colorAlpha));
        }

        if(!hasPersonalizationChange){
            hasPersonalizationChange = true;
        }
    }

    @Override
    public void onAddWallpaperClick(View v) {
        openImagePicker();
    }

    private void saveData() {

        if(!hasPersonalizationChange){

            return;
        }

        SharedPrefManager.saveString(this,getResources().getString(R.string.default_action_bar_color_key),selectedSpatialColor.getIdColor());
        SharedPrefManager.saveString(this, getResources().getString(R.string.PERSONALIZATION_STATUS_BAR_COLOR_KEY), selectedSpatialColor.getColorAlpha());
        SharedPrefManager.saveString(this, getResources().getString(R.string.default_wallpaper_key), selectedWallpaperURL);
        hasPersonalizationChange = false;

        Toast.makeText(this,"Appearance Has Been Change", Toast.LENGTH_SHORT).show();
    }
}
