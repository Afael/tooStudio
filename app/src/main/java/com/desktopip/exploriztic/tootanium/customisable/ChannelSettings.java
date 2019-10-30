package com.desktopip.exploriztic.tootanium.customisable;

import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
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
import com.desktopip.exploriztic.tootanium.interfaces.IFileExplore;
import com.desktopip.exploriztic.tootanium.utilities.CustomAlertDialogManager;
import com.desktopip.exploriztic.tootanium.volley.FileExplorerServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChannelSettings extends DialogFragment implements View.OnClickListener{

    View dialogChannelSettingView;

    TextView header_channel, channel_content_name, channel_content_type, channel_date, channel_url, channel_content_directory_name;
    Button channel_generate_share, btn_channel_close;
    LinearLayout channel_date_picker, channel_generate_url, channel_close, lay_content_type, lay_content_type_name;

    Calendar newCalendar;
    SimpleDateFormat dateFormatter;

    String channelType, id, userName, password, baseUrl, type, baseNameFile, apiName, shareTitle;
    CustomAlertDialogManager alert;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(inflater != null){
            dialogChannelSettingView = inflater.inflate(R.layout.fe_create_channel, container, false);

            header_channel = dialogChannelSettingView.findViewById(R.id.header_channel);
            channel_content_name = dialogChannelSettingView.findViewById(R.id.channel_content_name);
            channel_content_type = dialogChannelSettingView.findViewById(R.id.channel_content_type);
            lay_content_type = dialogChannelSettingView.findViewById(R.id.lay_content_type);
            lay_content_type_name = dialogChannelSettingView.findViewById(R.id.lay_content_type_name);
            channel_content_directory_name = dialogChannelSettingView.findViewById(R.id.channel_content_directory_name);
            channel_date = dialogChannelSettingView.findViewById(R.id.channel_date);
            channel_url = dialogChannelSettingView.findViewById(R.id.channel_url);
            channel_date_picker = dialogChannelSettingView.findViewById(R.id.channel_date_picker);
            channel_generate_url = dialogChannelSettingView.findViewById(R.id.channel_generate_url);
            channel_close = dialogChannelSettingView.findViewById(R.id.channel_close);
            channel_generate_share = dialogChannelSettingView.findViewById(R.id.channel_generate_share);
            btn_channel_close = dialogChannelSettingView.findViewById(R.id.btn_channel_close);

            Bundle channelSettingsBundle = getArguments();
            channelType = channelSettingsBundle.getString("ct");
            id = channelSettingsBundle.getString("id");
            type = channelSettingsBundle.getString("type");
            baseNameFile = channelSettingsBundle.getString("baseNameFile");
            userName = channelSettingsBundle.getString("userName");
            password = channelSettingsBundle.getString("password");
            baseUrl = channelSettingsBundle.getString("baseUrl");

            if(channelType.equals("dc")){
                header_channel.setText("Create Download Channel");
                apiName = "createDownloadChannel";
                shareTitle = " Share Download Channel";
                if (type.equals("directory")) {
                    type = "folder";
                }
            } else if(channelType.equals("uc")){
                header_channel.setText("Create Upload Channel");
                apiName = "createUploadChannel";
                shareTitle = " Share Upload Channel";
                channel_content_directory_name.setText("Directory Name:");
                lay_content_type.setVisibility(View.GONE);
                lay_content_type_name.setVisibility(View.GONE);
            } else {
                header_channel.setText("Create Apps Channel");
                apiName = "createAppsChannel";
                shareTitle = " Share Apps Channel";
                channel_content_directory_name.setText("File Name:");
                lay_content_type.setVisibility(View.GONE);
                lay_content_type_name.setVisibility(View.GONE);
            }

            channel_content_name.setText(baseNameFile);

            newCalendar = Calendar.getInstance();
            dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            channel_content_name.setText(baseNameFile);
            channel_content_type.setText(type.toUpperCase());
            channel_date.setText(dateFormatter.format(newCalendar.getTime()));

            channel_date_picker.setOnClickListener(this);
            channel_generate_share.setOnClickListener(this);
            btn_channel_close.setOnClickListener(this);

            alert = new CustomAlertDialogManager();
        }
        return dialogChannelSettingView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.channel_date_picker:

                DatePickerDialog datePickerDialog = new DatePickerDialog(FileExploreAdapter.context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        newCalendar.set(year, monthOfYear, dayOfMonth);
                        String strDate = dateFormatter.format(newCalendar.getTime());
                        channel_date.setText(strDate);
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                datePickerDialog.show();

                break;

            case R.id.channel_generate_share:
                if (channel_generate_share.getText().toString().equals("Generate URL")) {
                    if(channelType.equals("dc")){
                        FileExplorerServices.createDownloadChannel(new IFileExplore() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                try {
                                    JSONObject jsonObject = result.getJSONObject("message");
                                    channel_url.setText(jsonObject.getString("url"));
                                    channel_generate_share.setText("Share URL");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(VolleyError error) {
                                handlingError(error);
                            }

                            }, FileExploreAdapter.context, apiName, userName, password, channel_date.getText().toString(), id
                                , baseUrl, type);
                    } else if(channelType.equals("uc")){
                        FileExplorerServices.createUploadChannel(new IFileExplore() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                try {
                                    JSONObject jsonObject = result.getJSONObject("message");
                                    channel_url.setText(jsonObject.getString("url"));
                                    channel_generate_share.setText("Share URL");
                                } catch (JSONException e) {
                                    alert.showAlertDialog(FileExploreAdapter.context, "Error", e.getMessage(), "Close");
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                handlingError(error);
                            }

                            }, FileExploreAdapter.context, apiName, userName, password, channel_date.getText().toString(), id
                                , baseUrl);
                    } else {
                        FileExplorerServices.createAppsChannel(new IFileExplore() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                //Log.d("url", result.toString());
                                try {
                                    JSONObject jsonObject = result.getJSONObject("message");
                                    channel_url.setText(jsonObject.getString("url"));
                                    channel_generate_share.setText("Share URL");
                                } catch (JSONException e) {
                                    alert.showAlertDialog(FileExploreAdapter.context, "Error", e.getMessage(), "Close");
                                }

                            }

                            @Override
                            public void onError(VolleyError error) {
                                handlingError(error);
                            }

                            }, FileExploreAdapter.context, apiName, userName, password, channel_date.getText().toString(), id
                                , baseUrl);
                    }

                } else {
                    shareChannelTo(FileExploreAdapter.context, userName + shareTitle, channel_url.getText().toString());
                }
                break;

            case R.id.btn_channel_close:
                getDialog().dismiss();
                break;
        }
    }

    public void shareChannelTo(Context context, String titleShare, String urlShare) {

        List<Intent> intentShareList = new ArrayList<Intent>();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(shareIntent, 0);
        for (ResolveInfo resInfo : resolveInfoList) {
            String packageName = resInfo.activityInfo.packageName;
            String name = resInfo.activityInfo.name;

            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setComponent(new ComponentName(packageName, name));
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, titleShare);
            intent.putExtra(Intent.EXTRA_TEXT, urlShare);
            intentShareList.add(intent);

        }

        if (intentShareList.isEmpty()) {
            Toast.makeText(context, "No apps to share !", Toast.LENGTH_SHORT).show();
        } else {
            Intent chooserIntent = Intent.createChooser(intentShareList.remove(0), "Share via");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentShareList.toArray(new Parcelable[]{}));
            context.startActivity(chooserIntent);
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
