package com.desktopip.exploriztic.tootanium.adapters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl;
import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.fragment.FragDownloadChannel;
import com.desktopip.exploriztic.tootanium.interfaces.IChannel;
import com.desktopip.exploriztic.tootanium.models.ModDownloadChannel;
import com.desktopip.exploriztic.tootanium.utilities.CustomAlertDialogManager;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.volley.ChannelServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

//import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

/**
 * Created by Jayus on 03/07/2018.
 */

public class DownloadChannelAdapter extends RecyclerSwipeAdapter<DownloadChannelAdapter.DownloadChannelViewHolder> {

    CustomAlertDialogManager alert;

    // Use for usability component
    private AppCompatActivity activity;
    // Use for fragment class
    Fragment fragment;

    // Session manager
    static SessionManager session;
    // username
    static String userName;
    // password
    static String password;

    private Context context;
    private List<ModDownloadChannel> modDownloadChannels;
    SwipeItemRecyclerMangerImpl swipeItemManger;

    // Injection activity
    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public DownloadChannelAdapter(Context context, List<ModDownloadChannel> modDownloadChannels, Fragment fragment) {
        this.context = context;
        this.modDownloadChannels = modDownloadChannels;
        this.fragment = fragment;

        session = new SessionManager(context);
        session.checkLogin();

        //Get user data from session
        HashMap<String, String> user = session.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);

        alert = new CustomAlertDialogManager();

        swipeItemManger = new SwipeItemRecyclerMangerImpl(this);
    }

    @NonNull
    @Override
    public DownloadChannelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.frag_download_channel_item, parent, false);
        return new DownloadChannelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DownloadChannelViewHolder holder, final int position) {
        swipeItemManger.bindView(holder.itemView, position);
        //Header
        holder.dcName.setText(modDownloadChannels.get(position).getBaseFile());

        if (modDownloadChannels.get(position).getIs_expired().equals("1")) {
            holder.dcStatus.setText("Expired");
            holder.dcEdit.setText("Set Activate");
            holder.dcStatus.setTextColor(Color.parseColor("#FF5252"));
        } else {
            holder.dcStatus.setText("Active");
            holder.dcEdit.setText("Set Expire");
            holder.dcStatus.setTextColor(Color.parseColor("#8BC34A"));
        }

        holder.dcSwipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        //From left
        holder.dcSwipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.dcSwipeLayout.findViewById(R.id.swipe_left));
        //From right
        holder.dcSwipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.dcSwipeLayout.findViewById(R.id.swipe_right));

        holder.dcSwipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout swipeLayout) {
                swipeItemManger.closeAllExcept(swipeLayout);
            }

            @Override
            public void onOpen(SwipeLayout swipeLayout) {

            }

            @Override
            public void onStartClose(SwipeLayout swipeLayout) {

            }

            @Override
            public void onClose(SwipeLayout swipeLayout) {

            }

            @Override
            public void onUpdate(SwipeLayout swipeLayout, int i, int i1) {

            }

            @Override
            public void onHandRelease(SwipeLayout swipeLayout, float v, float v1) {

            }
        });

        holder.dcSwipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.dcBtnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                final View dialogView = inflater.inflate(R.layout.channel_dialog, null);
                dialogBuilder.setView(dialogView);

                final TextView etBaseFile, etLocPath, etUrl, etCreatedDate, etExpiredDate, etStatus, etType;

                etBaseFile = dialogView.findViewById(R.id.etUpBaseFile);
                etLocPath = dialogView.findViewById(R.id.etUpLocPath);
                etUrl = dialogView.findViewById(R.id.etUpUrl);
                etCreatedDate = dialogView.findViewById(R.id.etUpCreatedDate);
                etExpiredDate = dialogView.findViewById(R.id.etUpExpiredDate);
                etStatus = dialogView.findViewById(R.id.etUpStatus);
                etType = dialogView.findViewById(R.id.etType);

                etBaseFile.setText(modDownloadChannels.get(position).getBaseFile());
                etLocPath.setText(modDownloadChannels.get(position).getDirpath());
                etUrl.setText(modDownloadChannels.get(position).getChannel_id());
                etCreatedDate.setText(modDownloadChannels.get(position).getGenerate_date());
                etExpiredDate.setText(modDownloadChannels.get(position).getExpired());

                if (modDownloadChannels.get(position).getIs_expired().equals("1")) {
                    etStatus.setText("Expired");
                    etStatus.setTextColor(Color.parseColor("#FF5252"));
                } else {
                    etStatus.setText("Active");
                    etStatus.setTextColor(Color.parseColor("#8BC34A"));
                }
                String fileSize = modDownloadChannels.get(position).getFileSize();

                String fileType = modDownloadChannels.get(position).getPathtype();
                if(fileType.equals("FILE")){
                    etType.setText(fileType+"("+fileSize+" kB)");
                }else{
                    etType.setText(fileType);
                }

                dialogBuilder.setPositiveButton("Share URL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //shareTextUrl(context, userName+" Share Link Download Channel", etUrl.getText().toString());
                        shareChannelTo(context, userName+" Share Link Download Channel", etUrl.getText().toString());
                    }
                });

                dialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();

                //notifyDataSetChanged();
            }
        });

        String baseFile = modDownloadChannels.get(position).getBaseFile();
        String type = modDownloadChannels.get(position).getPathtype();

        switch (type) {
            case "FOLDER":
                holder.dcThumbnail.setImageResource(R.mipmap.ic_folder);
                break;
            case "FILE":
                //String ext = android.webkit.MimeTypeMap.getFileExtensionFromUrl(baseFile);

                //Log.d("extension", ""+ext);
                if(baseFile.contains(".")) {
                    String ext = baseFile.substring(baseFile.lastIndexOf("."));
                    switch (ext) {
                        case ".txt":
                            holder.dcThumbnail.setImageResource(R.mipmap.ic_file);
                            break;
                        case ".docx":
                            holder.dcThumbnail.setImageResource(R.mipmap.ic_docx);
                            break;
                        case ".doc":
                            holder.dcThumbnail.setImageResource(R.mipmap.ic_docx);
                            break;
                        case ".xls":
                            holder.dcThumbnail.setImageResource(R.mipmap.ic_excel);
                            break;
                        case ".xlsx":
                            holder.dcThumbnail.setImageResource(R.mipmap.ic_excel);
                            break;
                        case ".ppt":
                            holder.dcThumbnail.setImageResource(R.mipmap.ic_powerpoint);
                            break;
                        case ".pdf":
                            holder.dcThumbnail.setImageResource(R.mipmap.ic_pdf);
                            break;
                        case ".zip":
                            holder.dcThumbnail.setImageResource(R.mipmap.ic_archive);
                            break;
                        case ".rar":
                            holder.dcThumbnail.setImageResource(R.mipmap.ic_archive);
                            break;
                        case ".mp3":
                            holder.dcThumbnail.setImageResource(R.mipmap.ic_music);
                            break;
                        case ".3gp":
                            holder.dcThumbnail.setImageResource(R.mipmap.ic_video);
                            break;
                        case ".png":
                            holder.dcThumbnail.setImageResource(R.mipmap.ic_images);
                            break;
                        case ".jpg":
                            holder.dcThumbnail.setImageResource(R.mipmap.ic_images);
                            break;
                        default:
                            holder.dcThumbnail.setImageResource(R.mipmap.ic_file);
                            break;

                    }
                }
                break;
        }

        holder.dcEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.dcEdit.getText().equals("Set Activate")) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    final View dialogView = inflater.inflate(R.layout.channel_set_layout, null);
                    dialogBuilder.setView(dialogView);



                    //final DatePicker datePicker = dialogView.findViewById(R.id.datePicker);
                    final TextView channel_date;
                    channel_date = dialogView.findViewById(R.id.channel_date);
                    LinearLayout channel_date_picker;

                    channel_date_picker = dialogView.findViewById(R.id.channel_date_picker);
                    final Calendar newCalendar = Calendar.getInstance();
                    final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                    channel_date.setText(dateFormatter.format(newCalendar.getTime()));

                    channel_date_picker.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    newCalendar.set(year, monthOfYear, dayOfMonth);
                                    String strDate = dateFormatter.format(newCalendar.getTime());
                                    channel_date.setText(strDate);
                                }
                            },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//                                final CalendarView cal = datePickerDialog.getDatePicker().getCalendarView();
//                                if (cal != null) {
//                                    newCalendar.add(Calendar.MONTH, 24);
//                                    cal.setDate(newCalendar.getTimeInMillis(), false, true);
//                                    newCalendar.add(Calendar.MONTH, -24);
//                                    cal.setDate(newCalendar.getTimeInMillis(), false, true);
//                                }
//                            } else {
//                                // Do something for lollipop and above versions
//                            }
                            datePickerDialog.show();
                        }
                    });

                    //dialogBuilder.setTitle("Select date");
                    dialogBuilder.setPositiveButton("Set Active", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
//                            int day = datePicker.getDayOfMonth();
//                            int month = datePicker.getMonth() + 1;
//                            int year = datePicker.getYear();
//
//                            calendar.set(Calendar.YEAR, year);
//                            calendar.set(Calendar.MONTH, month);
//                            calendar.set(Calendar.DAY_OF_MONTH, day);
//                            Date date = calendar.getTime();
//                            String strDate = dateFormatter.format(date);
//
//                            datePicker.setMinDate(calendar.getTimeInMillis());
//                            final CalendarView cal = datePicker.getCalendarView();
//                            if (cal != null) {
//                                calendar.add(Calendar.MONTH, 24);
//                                cal.setDate(calendar.getTimeInMillis(), false, true);
//                                calendar.add(Calendar.MONTH, -24);
//                                cal.setDate(calendar.getTimeInMillis(), false, true);
//                            }

                            ChannelServices.setChannelActive(new IChannel() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    try {
                                        String message = response.getString("message");
                                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
//                                        refreshData();
                                        ((FragDownloadChannel) fragment).loadChannel();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(VolleyError error) {
                                    Log.d("Error Actived: ", "" + error);
                                    Toast.makeText(context, "Error: " + error, Toast.LENGTH_SHORT).show();
                                }
                            }, context, "downloadChannelActived", modDownloadChannels.get(position).getPrimary(), channel_date.getText().toString(), userName);
                        }
                    });
                    dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            notifyDataSetChanged();
                        }
                    });

                    AlertDialog channel = dialogBuilder.create();
                    channel.show();
                } else {
                    android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(context);
                    builder1.setMessage("Do you want to set expired ?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ChannelServices.setChannelDelDeactExp(new IChannel() {
                                        @Override
                                        public void onSuccess(JSONObject response) {
                                            try {
                                                String message = response.getString("message");
                                                Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                                                //refreshData();
                                                ((FragDownloadChannel) fragment).loadChannel();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onError(VolleyError error) {
                                            //Log.d("Error Actived: ", ""+error);
                                            Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();
                                        }
                                    }, context, "downloadChannelExpired", modDownloadChannels.get(position).getPrimary(), userName);
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    notifyDataSetChanged();
                                    notifyItemChanged(position);
                                }
                            });
                    android.support.v7.app.AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });

        holder.dcRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(context);
                builder1.setMessage("Do you want to remove this channel ?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ChannelServices.setChannelDelDeactExp(new IChannel() {
                                    @Override
                                    public void onSuccess(JSONObject response) {
                                        try {
                                            String message = response.getString("message");
                                            Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                                            //refreshData();
                                            ((FragDownloadChannel) fragment).loadChannel();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(VolleyError error) {
                                        Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();
                                    }
                                }, context, "downloadChannelDelete", modDownloadChannels.get(position).getPrimary(), userName);
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                notifyDataSetChanged();
                                notifyItemChanged(position);
                            }
                        });
                android.support.v7.app.AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return modDownloadChannels.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe;
    }

    public static class DownloadChannelViewHolder extends RecyclerView.ViewHolder {

        TextView dcName, dcStatus, dcEdit, dcRemove;
        ImageView dcThumbnail;
        LinearLayout dc_header_layout;
        public SwipeLayout dcSwipeLayout;
        public ImageButton dcBtnDetails;

        public DownloadChannelViewHolder(View itemView) {
            super(itemView);

            dc_header_layout = itemView.findViewById(R.id.dc_header_layout);
            dcSwipeLayout = itemView.findViewById(R.id.swipe);
            dcName = itemView.findViewById(R.id.dc_name);
            dcStatus = itemView.findViewById(R.id.dc_status);
            dcEdit = itemView.findViewById(R.id.dc_edit);
            dcRemove = itemView.findViewById(R.id.dc_remove);
            dcBtnDetails = itemView.findViewById(R.id.dc_btn_details);
            dcThumbnail = itemView.findViewById(R.id.dc_thumbnail);
        }
    }

    public void refreshData() {

        ChannelServices.loadChannel(new IChannel() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray message = response.getJSONArray("message");
                    modDownloadChannels.clear();
                    for (int i = 0; i < message.length(); i++) {

                        ModDownloadChannel downloadChannel = new ModDownloadChannel();

                        JSONObject data = message.getJSONObject(i);
                        downloadChannel.setPrimary(data.getString("primary"));
                        downloadChannel.setBaseFile(data.getString("basefile"));
                        downloadChannel.setChannel_id(data.getString("channel_id"));
                        downloadChannel.setDirpath(data.getString("dirpath"));
                        downloadChannel.setGenerate_date(data.getString("generate_date"));
                        downloadChannel.setExpired(data.getString("expired"));
                        downloadChannel.setIs_expired(data.getString("is_expired"));
                        downloadChannel.setPathtype(data.getString("pathtype"));
                        downloadChannel.setFileSize(data.getString("filesize"));
                        modDownloadChannels.add(downloadChannel);
                        notifyDataSetChanged();

                    }
                    //setupRecyclerView(listUploadChannel);
                } catch (JSONException e) {
                    alert.showAlertDialog(context, "Json Exception", ""+e.getMessage(), "Close");
                }
            }

            @Override
            public void onError(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    alert.showAlertDialog(context, "Network Error", "Request Time-Out", "Close");
                } else if (error instanceof AuthFailureError) {
                    alert.showAlertDialog(context, "Authentication Error", "Authentication Failed" + error.toString(), "Close");
                } else if (error instanceof ServerError) {
                    alert.showAlertDialog(context, "Server Error", "Internal Server Error", "Close");
                } else if (error instanceof NetworkError) {
                    alert.showAlertDialog(context, "Network Error", "Unable To Access The Network" + error.toString(), "Close");
                } else if (error instanceof ParseError) {
                    alert.showAlertDialog(context, "Parse Error", "There was a problem parsing the package" + error.toString(), "Close");
                }
            }
        }, context, "downloadChannelList", userName);

    }

    public void searchChannel(List<ModDownloadChannel> newChannel) {
        modDownloadChannels = new ArrayList<>();
        modDownloadChannels.addAll(newChannel);
        notifyDataSetChanged();
    }

    // Method to share either text or URL.
    private void shareTextUrl(Context context, String titleShare, String urlShare) {
//        Intent share = new Intent(android.content.Intent.ACTION_SEND);
//        share.setType("text/plain");
//        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//
//        share.putExtra(Intent.EXTRA_SUBJECT, titleShare);
//        share.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(urlShare));
//
//        context.startActivity(Intent.createChooser(share, "Share link!"));

        try {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            share.putExtra(Intent.EXTRA_SUBJECT, titleShare);
            share.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(urlShare));

            context.startActivity(Intent.createChooser(share, "Share link!"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "Some error: "+ex.getMessage(), Toast.LENGTH_SHORT).show();
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
            //Log.d("shareLog", "Package Name : " + packageName);
            //Log.d("shareLog", "Name : " + name);

            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setComponent(new ComponentName(packageName, name));
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, titleShare);
            intent.putExtra(Intent.EXTRA_TEXT, urlShare/*Html.fromHtml(urlShare)*/);
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
}
