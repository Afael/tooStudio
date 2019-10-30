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
import com.desktopip.exploriztic.tootanium.fragment.FragUploadChannel;
import com.desktopip.exploriztic.tootanium.interfaces.IChannel;
import com.desktopip.exploriztic.tootanium.models.ModUploadChannel;
import com.desktopip.exploriztic.tootanium.utilities.CustomAlertDialogManager;
import com.desktopip.exploriztic.tootanium.utilities.PreferenceConfig;
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

public class UploadChannelAdapter extends RecyclerSwipeAdapter<UploadChannelAdapter.UploadChannelViewHolder> {

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
    private List<ModUploadChannel> mUploadChannel;

    PreferenceConfig cooki;

    private IChannel iChannel;

    public void setIUploadChannel(IChannel iUpload){
        iChannel = iUpload;
    }

    SwipeItemRecyclerMangerImpl swipeItemManger;

    // Injection activity
    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public UploadChannelAdapter(Context context, List<ModUploadChannel> mUploadChannel, Fragment fragment) {
        this.context = context;
        this.mUploadChannel = mUploadChannel;
        this.fragment = fragment;

        session = new SessionManager(context);
        session.checkLogin();

        //Get user data from session
        HashMap<String, String> user =  session.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);

        alert = new CustomAlertDialogManager();

        swipeItemManger = new SwipeItemRecyclerMangerImpl(this);
    }

    @NonNull
    @Override
    public UploadChannelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.frag_upload_channel_item, parent, false);
        return new UploadChannelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UploadChannelViewHolder holder, final int position) {
        swipeItemManger.bindView(holder.itemView, position);
        holder.upName.setText(mUploadChannel.get(position).getBaseFile());

        if (mUploadChannel.get(position).getIs_expired().equals("1")) {
            holder.upStatus.setText("Expired");
            holder.upEdit.setText("Set Activate");
            holder.upStatus.setTextColor(Color.parseColor("#FF5252"));
        } else {
            holder.upStatus.setText("Active");
            holder.upEdit.setText("Set Expire");
            holder.upStatus.setTextColor(Color.parseColor("#8BC34A"));
        }
        holder.upThumbnail.setImageResource(R.mipmap.ic_folder);

        holder.upSwipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        //From left
        holder.upSwipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.upSwipeLayout.findViewById(R.id.swipe_left));
        //From right
        holder.upSwipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.upSwipeLayout.findViewById(R.id.swipe_right));
        holder.upSwipeLayout.setClickToClose(true);

        holder.upSwipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
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
                if(holder.upSwipeLayout==null) {
                    holder.upSwipeLayout=swipeLayout;
                }else
                {
                    holder.upSwipeLayout.close();
                    holder.upSwipeLayout=swipeLayout;
                }
            }
        });

        holder.upSwipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        holder.upBtnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                final View dialogView = inflater.inflate(R.layout.channel_dialog, null);
                dialogBuilder.setView(dialogView);

                final TextView etUpBaseFile, etUpLocPath, etUpUrl, etUpCreatedDate
                        , etUpExpiredDate, etUpStatus;

                etUpBaseFile = dialogView.findViewById(R.id.etUpBaseFile);
                etUpLocPath = dialogView.findViewById(R.id.etUpLocPath);
                etUpUrl = dialogView.findViewById(R.id.etUpUrl);
                etUpCreatedDate = dialogView.findViewById(R.id.etUpCreatedDate);
                etUpExpiredDate = dialogView.findViewById(R.id.etUpExpiredDate);
                etUpStatus = dialogView.findViewById(R.id.etUpStatus);

                etUpBaseFile.setText(mUploadChannel.get(position).getBaseFile());
                etUpLocPath.setText(mUploadChannel.get(position).getDirpath());
                etUpUrl.setText(mUploadChannel.get(position).getChannel_id());
                etUpCreatedDate.setText(mUploadChannel.get(position).getGenerate_date());
                etUpExpiredDate.setText(mUploadChannel.get(position).getExpired());

                if (mUploadChannel.get(position).getIs_expired().equals("1")) {
                    etUpStatus.setText("Expired");
                    etUpStatus.setTextColor(Color.parseColor("#FF5252"));
                } else {
                    etUpStatus.setText("Active");
                    etUpStatus.setTextColor(Color.parseColor("#8BC34A"));
                }

                dialogBuilder.setPositiveButton("Share URL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //shareTextUrl(context, userName+" Share Link Upload Channel", etUpUrl.getText().toString());
                        shareChannelTo(context, userName+" Share Link Upload Channel", etUpUrl.getText().toString());
                    }
                });

                dialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();

                //notifyDataSetChanged();
            }
        });
        
        holder.upEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.upEdit.getText().equals("Set Activate")) {
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
//                            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
//                            Calendar calendar = Calendar.getInstance();
//                            calendar.set(Calendar.YEAR, year);
//                            calendar.set(Calendar.MONTH, month);
//                            calendar.set(Calendar.DAY_OF_MONTH, day);
//                            Date date = calendar.getTime();
//                            String strDate = dateFormatter.format(date);

                            ChannelServices.setChannelActive(new IChannel() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    //Log.d("Success: ", ""+response);
                                    try {
                                        String message = response.getString("message");
                                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                                        //refreshData();
                                        ((FragUploadChannel) fragment).loadChannel();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(VolleyError error) {
                                    Log.d("Error Actived: ", ""+error);
                                    Toast.makeText(context, "Error: "+error, Toast.LENGTH_SHORT).show();
                                }
                            }, context, "uploadChannelActived", mUploadChannel.get(position).getPrimary(), channel_date.getText().toString(), userName);
                        }
                    });
                    dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            notifyDataSetChanged();
                            notifyItemChanged(position);
                        }
                    });
                    final AlertDialog channel = dialogBuilder.create();
                    channel.show();

                }
                else {
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
                                                ((FragUploadChannel) fragment).loadChannel();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onError(VolleyError error) {
                                            //Log.d("Error Actived: ", ""+error);
                                            Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
                                        }
                                    }, context, "uploadChannelExpired", mUploadChannel.get(position).getPrimary(), userName);

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
                notifyDataSetChanged();

            }
        });

        holder.upRemove.setOnClickListener(new View.OnClickListener() {
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
                                            ((FragUploadChannel) fragment).loadChannel();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(VolleyError error) {
                                        Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
                                    }
                                }, context, "uploadChannelDelete", mUploadChannel.get(position).getPrimary(), userName);
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
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUploadChannel.size();
    }

    public void refreshData() {

        ChannelServices.loadChannel(new IChannel() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray message = response.getJSONArray("message");
                    mUploadChannel.clear();
                    for (int i = 0; i < message.length(); i++) {

                        ModUploadChannel uploadChannel = new ModUploadChannel();

                        JSONObject data = message.getJSONObject(i);
                        uploadChannel.setPrimary(data.getString("primary"));
                        uploadChannel.setBaseFile(data.getString("basefile"));
                        uploadChannel.setChannel_id(data.getString("channel_id"));
                        uploadChannel.setDirpath(data.getString("dirpath"));
                        uploadChannel.setGenerate_date(data.getString("generate_date"));
                        uploadChannel.setExpired(data.getString("expired"));
                        uploadChannel.setIs_expired(data.getString("is_expired"));
                        uploadChannel.setShare_by(data.getString("share_by"));
                        mUploadChannel.add(uploadChannel);
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
        }, context, "uploadChannelList", userName);

    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe;
    }

    public static class UploadChannelViewHolder extends RecyclerView.ViewHolder{

        TextView upName, upStatus, upEdit, upRemove;
        ImageView upThumbnail;
        LinearLayout up_header_layout;
        public SwipeLayout upSwipeLayout;
        public ImageButton upBtnDetails;

        public UploadChannelViewHolder(View itemView) {
            super(itemView);

            up_header_layout = itemView.findViewById(R.id.up_header_layout);
            upSwipeLayout = itemView.findViewById(R.id.swipe);
            upName = itemView.findViewById(R.id.up_name);
            upStatus = itemView.findViewById(R.id.up_status);
            upEdit = itemView.findViewById(R.id.up_edit);
            upRemove = itemView.findViewById(R.id.up_delete);
            upBtnDetails = itemView.findViewById(R.id.btn_details);
            upThumbnail = itemView.findViewById(R.id.up_thumbnail);

        }
    }

    public void searchChannel(List<ModUploadChannel> newChannel) {
        mUploadChannel = new ArrayList<>();
        mUploadChannel.addAll(newChannel);
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

        List<Intent> intentShareList = new ArrayList<>();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(shareIntent, 0);
        for (ResolveInfo resInfo : resolveInfoList) {
            String packageName = resInfo.activityInfo.packageName;
            String name = resInfo.activityInfo.name;
            //Log.d("shareLog", "Package Name : " + packageName);
            //Log.d("shareLog", "Name : " + name);

            if (packageName.contains("com.whatsapp")) {

                Intent intent = new Intent();;
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.setComponent(new ComponentName(packageName, name));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, titleShare);
                intent.putExtra(Intent.EXTRA_TEXT, urlShare/*Html.fromHtml(urlShare)*/);
                intentShareList.add(intent);
            }

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
