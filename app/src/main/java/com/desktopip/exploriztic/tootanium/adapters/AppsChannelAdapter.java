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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl;
import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.fragment.FragAppsChannel;
import com.desktopip.exploriztic.tootanium.interfaces.IChannel;
import com.desktopip.exploriztic.tootanium.models.ModAppsChannel;
import com.desktopip.exploriztic.tootanium.utilities.CustomAlertDialogManager;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.volley.ChannelServices;

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

public class AppsChannelAdapter extends RecyclerSwipeAdapter<AppsChannelAdapter.AppsChannelViewHolder> {

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
    private List<ModAppsChannel> mAppsChannels;
    SwipeItemRecyclerMangerImpl swipeItemManger;

    // Injection activity
    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public AppsChannelAdapter(Context context, List<ModAppsChannel> modAppsChannels, Fragment fragment) {
        this.context = context;
        this.mAppsChannels = modAppsChannels;
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
    public AppsChannelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.frag_apps_channel_item, parent, false);
        return new AppsChannelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AppsChannelViewHolder holder, final int position) {
        swipeItemManger.bindView(holder.itemView, position);
        holder.apName.setText(mAppsChannels.get(position).getBasefile());

        if (mAppsChannels.get(position).getIs_expired().equals("Expired")) {
            holder.apStatus.setText("Expired");
            holder.apEdit.setText("Set Activate");
            holder.apStatus.setTextColor(Color.parseColor("#FF5252"));
        } else {
            holder.apStatus.setText("Active");
            holder.apEdit.setText("Set Expire");
            holder.apStatus.setTextColor(Color.parseColor("#8BC34A"));
        }

        holder.apSwipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        //From left
        holder.apSwipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.apSwipeLayout.findViewById(R.id.swipe_left));
        //From right
        holder.apSwipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.apSwipeLayout.findViewById(R.id.swipe_right));

        holder.apSwipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
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

        String baseFile = mAppsChannels.get(position).getBasefile();

        //String ext = android.webkit.MimeTypeMap.getFileExtensionFromUrl(baseFile);

        if(baseFile.contains(".")) {
            String ext = baseFile.substring(baseFile.lastIndexOf("."));
            switch (ext) {
                case ".txt":
                    holder.appThumbnail.setImageResource(R.mipmap.ic_file);
                    break;
                case ".docx":
                    holder.appThumbnail.setImageResource(R.mipmap.ic_docx);
                    break;
                case ".doc":
                    holder.appThumbnail.setImageResource(R.mipmap.ic_docx);
                    break;
                case ".xls":
                    holder.appThumbnail.setImageResource(R.mipmap.ic_excel);
                    break;
                case ".xlsx":
                    holder.appThumbnail.setImageResource(R.mipmap.ic_excel);
                    break;
                case ".ppt":
                    holder.appThumbnail.setImageResource(R.mipmap.ic_powerpoint);
                    break;
                case ".pdf":
                    holder.appThumbnail.setImageResource(R.mipmap.ic_pdf);
                    break;
                case ".zip":
                    holder.appThumbnail.setImageResource(R.mipmap.ic_archive);
                    break;
                case ".rar":
                    holder.appThumbnail.setImageResource(R.mipmap.ic_archive);
                    break;
                case ".mp3":
                    holder.appThumbnail.setImageResource(R.mipmap.ic_music);
                    break;
                case ".3gp":
                    holder.appThumbnail.setImageResource(R.mipmap.ic_video);
                    break;
                case ".png":
                    holder.appThumbnail.setImageResource(R.mipmap.ic_images);
                    break;
                case ".jpg":
                    holder.appThumbnail.setImageResource(R.mipmap.ic_images);
                    break;

                default:
                    holder.appThumbnail.setImageResource(R.mipmap.ic_file);
                    break;

            }
        }

        holder.apSwipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.apBtnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                final View dialogView = inflater.inflate(R.layout.channel_dialog, null);
                dialogBuilder.setView(dialogView);

                final TextView etBaseFile, etLocPath, etUrl, etCreatedDate
                        , etExpiredDate, etStatus, etTypeHeader, etType;

                etBaseFile = dialogView.findViewById(R.id.etUpBaseFile);
                etLocPath = dialogView.findViewById(R.id.etUpLocPath);
                etUrl = dialogView.findViewById(R.id.etUpUrl);
                etCreatedDate = dialogView.findViewById(R.id.etUpCreatedDate);
                etExpiredDate = dialogView.findViewById(R.id.etUpExpiredDate);
                etStatus = dialogView.findViewById(R.id.etUpStatus);
                etTypeHeader = dialogView.findViewById(R.id.etTypeHeader);
                etType = dialogView.findViewById(R.id.etType);
                etTypeHeader.setVisibility(View.GONE);
                etType.setVisibility(View.GONE);

                etBaseFile.setText(mAppsChannels.get(position).getBasefile());
                etLocPath.setText(mAppsChannels.get(position).getBasePath());
                etUrl.setText(mAppsChannels.get(position).getChannel_id());
                etCreatedDate.setText(mAppsChannels.get(position).getGenerate_date());
                etExpiredDate.setText(mAppsChannels.get(position).getExpired());
                etStatus.setText(mAppsChannels.get(position).getIs_expired());

                if (mAppsChannels.get(position).getIs_expired().equals("Expired")) {
                    etStatus.setTextColor(Color.parseColor("#FF5252"));
                } else {
                    etStatus.setTextColor(Color.parseColor("#8BC34A"));
                }

                dialogBuilder.setPositiveButton("Share URL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //shareTextUrl(context, userName+" Share Link Apps Channel", etUrl.getText().toString());
                        shareChannelTo(context, userName+" Share Link Apps Channel", etUrl.getText().toString());
                    }
                });

                dialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();

                //notifyDataSetChanged();
            }
        });

        holder.apEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.apEdit.getText().equals("Set Activate")) {
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

                    dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            ChannelServices.setChannelActive(new IChannel() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    try {
                                        String message = response.getString("message");
                                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                                        //refreshData();
                                        ((FragAppsChannel) fragment).loadChannel();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(VolleyError error) {
                                    Toast.makeText(context, "Error: "+error, Toast.LENGTH_SHORT).show();
                                }
                            }, context, "appChannelActived", mAppsChannels.get(position).getPrimary(), channel_date.getText().toString(), userName);

                        }
                    });
                    dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            notifyDataSetChanged();
                        }
                    });
                    AlertDialog channel = dialogBuilder.create();
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
                                                ((FragAppsChannel) fragment).loadChannel();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onError(VolleyError error) {
                                            //Log.d("Error Actived: ", ""+error);
                                            Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
                                        }
                                    }, context, "appChannelExpired", mAppsChannels.get(position).getPrimary(), userName);
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

        holder.apRemove.setOnClickListener(new View.OnClickListener() {
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
                                            ((FragAppsChannel) fragment).loadChannel();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(VolleyError error) {
                                        Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
                                    }
                                }, context, "appChannelDelete", mAppsChannels.get(position).getPrimary(), userName);
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                notifyDataSetChanged();
                                notifyItemRemoved(position);
                            }
                        });
                android.support.v7.app.AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAppsChannels.size();
    }

    public void searchChannel(List<ModAppsChannel> newChannel) {
        mAppsChannels = new ArrayList<>();
        mAppsChannels.addAll(newChannel);
        notifyDataSetChanged();
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe;
    }

    public static class AppsChannelViewHolder extends RecyclerView.ViewHolder{

        TextView apName, apStatus, apEdit, apRemove;
        ImageView appThumbnail;
        LinearLayout up_header_layout;
        public SwipeLayout apSwipeLayout;
        public ImageButton apBtnDetails;

        public AppsChannelViewHolder(View itemView) {
            super(itemView);

            up_header_layout = itemView.findViewById(R.id.up_header_layout);
            apSwipeLayout = itemView.findViewById(R.id.swipe);
            apName = itemView.findViewById(R.id.app_name);
            apStatus = itemView.findViewById(R.id.app_status);
            apEdit = itemView.findViewById(R.id.app_edit);
            apRemove = itemView.findViewById(R.id.app_remove);
            apBtnDetails = itemView.findViewById(R.id.app_btn_details);
            appThumbnail = itemView.findViewById(R.id.app_thumbnail);
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
