package com.desktopip.exploriztic.tootanium.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.models.NewProfile;
import com.desktopip.exploriztic.tootanium.models.PostNewProfile;
import com.desktopip.exploriztic.tootanium.utilities.ImageInject;
import com.fxn.pix.Pix;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Profile extends SpatialSubAppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Profile";

    private final int REQUEST_CODE_IMAGE_PICKER = 1;

    private ImageView profileImage;

    private String selectedProfileImagePath;
    private boolean isImageChange;

    private String  defaultFullName,
            defaultEmail,
            defaultPosition,
            defaultImageUrl;

    private EditText usernameET,
            emailET,
            fullNameET,
            positionET;

    private Button imagePickerBtn;

    private MenuItem editMenu,
            saveMenu;

    private TextView imageFileName;

    private Bitmap bitmap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        isImageChange = false;
        setTitle(getString(R.string.profile_title));

        setUpParam();
        setUpDefaultContent();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setDataToCompare();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE_PICKER && resultCode == RESULT_OK) {

            selectedProfileImagePath = data.getStringArrayListExtra(Pix.IMAGE_RESULTS).get(0);
            File displayImageFile = new File(selectedProfileImagePath);
            ImageInject.setWithGlide(this, displayImageFile, profileImage);
            imageFileName.setText(displayImageFile.getName());
            isImageChange = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.profil_menu, menu);
        editMenu = menu.findItem(R.id.profil_menu_edit);
        saveMenu = menu.findItem(R.id.profil_menu_save);
        return true;
    }

    //Semua Klik Event
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profil_menu_edit:
                editData(true);
                editMenu.setVisible(false);
                saveMenu.setVisible(true);
                break;
            case R.id.profil_menu_save:
                saveData();
                editMenu.setVisible(true);
                saveMenu.setVisible(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.profile_setimage_btn:
                openFilePicker();
                break;
        }
    }

    private void setDataToCompare() {
        defaultFullName = etText(fullNameET);
        defaultEmail = etText(emailET);
        defaultPosition = etText(positionET);
    }

    //content default
    private void setUpDefaultContent() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ImageInject.setWithGlide(this, R.drawable.dummy_profil_pic, profileImage);
        usernameET.setText(R.string.dummy_username);
        //emailET.setText();
        //fullNameET.setText();
        //positionET.setText();
    }

    private boolean isDataHasChange(){
        boolean isFullNameChange = !defaultFullName.equals(etText(fullNameET));
        boolean isEmailChange = !defaultEmail.equals(etText(emailET));
        boolean isPositionChange = !defaultPosition.equals(positionET);

        return (isFullNameChange || isEmailChange || isPositionChange || isImageChange);
    }

    private String etText(EditText et){
        return et.getText().toString();
    }

    private void editData(boolean enable) {
        fullNameET.setEnabled(enable);
        emailET.setEnabled(enable);
        positionET.setEnabled(enable);
        imageFileName.setVisibility(enable ? View.VISIBLE : View.GONE);
        imagePickerBtn.setVisibility(enable ? View.VISIBLE : View.GONE);
        editMenu.setVisible(!enable);
        saveMenu.setVisible(enable);

        setDataToCompare();
    }


    //init parameter
    private void setUpParam() {
        profileImage = findViewById(R.id.profil_image);
        usernameET = findViewById(R.id.profile_username);
        emailET = findViewById(R.id.profile_email);
        fullNameET = findViewById(R.id.profile_fullname);
        positionET = findViewById(R.id.profile_position);
        imagePickerBtn = findViewById(R.id.profile_setimage_btn);
        imageFileName = findViewById(R.id.profile_chosen_image_text);
        imagePickerBtn.setOnClickListener(this);
    }

    public void openFilePicker() {
        Pix.start(this,
                REQUEST_CODE_IMAGE_PICKER,
                1);
    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }

    //save data ke server
    private void saveData() {

        try{
            if(!isDataHasChange())
                return;

            NewProfile newProfile = NewProfile.build()
                    .fullName(etText(fullNameET))
                    .position(etText(positionET))
                    .email(etText(emailET))
                    .imageUrl(selectedProfileImagePath)
                    .create();

//        Log.d(TAG, " passData URL : " + selectedProfileImagePath);

            postNewProfile(newProfile);

//        Toast.makeText(this,"data diubah",Toast.LENGTH_SHORT).show();

            isImageChange = false;
            editData(false);
            setDataToCompare();
            defaultImageUrl = selectedProfileImagePath;
        }catch (Exception e){
            Log.d("save", e.toString());
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
        }

    }

    private void postNewProfile(NewProfile newProfile){
        String baseUrl = "action/user_profile_save.php";
        PostNewProfile postNewProfile = new PostNewProfile(getBaseContext());
        postNewProfile.posNewSetting(newProfile, baseUrl);

    }
}
