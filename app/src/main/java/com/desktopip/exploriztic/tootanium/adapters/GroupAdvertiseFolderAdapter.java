package com.desktopip.exploriztic.tootanium.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.fragment.FragGroupAdvertiseFolder;
import com.desktopip.exploriztic.tootanium.models.ModAdvertiseFileExplorer;
import com.desktopip.exploriztic.tootanium.utilities.DownloadTask;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;

import java.util.HashMap;
import java.util.List;

public class GroupAdvertiseFolderAdapter extends RecyclerView.Adapter<GroupAdvertiseFolderAdapter.FileExplorerAdvertiseViewHolder> {

    private static final String TAG = "FileExplorerAdvertise";



    // Use for usability component
    private AppCompatActivity activity;
    // Use for fragment class
    Fragment fragment;

    // Download manager
    DownloadTask downloadTask;
    // Session manager
    static SessionManager session;
    // username
    static String userName;
    // password
    static String password;
    // URL_API
    String baseUrl;
    // URL DOWNLOAD
    String urlDownload;

    private Context context;
    private List<ModAdvertiseFileExplorer> advertiseFileExplorerList;

    // Injection activity
    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public GroupAdvertiseFolderAdapter(Context context, List<ModAdvertiseFileExplorer> advertiseFileExplorerList, Fragment fragment) {
        this.context = context;
        this.advertiseFileExplorerList = advertiseFileExplorerList;
        this.fragment = fragment;

        session = new SessionManager(context);
        session.checkLogin();

        //Get user data from session
        HashMap<String, String> user = session.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);
        baseUrl = user.get(SessionManager.BASE_URL);
        urlDownload = user.get(SessionManager.BASE_URL_DOWNLOAD);
    }

    @NonNull
    @Override
    public FileExplorerAdvertiseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.frag_group_advertise_folder_item, parent, false);
        return new FileExplorerAdvertiseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileExplorerAdvertiseViewHolder holder, final int position) {
        holder.adv_name.setText(advertiseFileExplorerList.get(position).getName());
        holder.adv_thumbnail.setImageResource(R.mipmap.ic_folder);

        holder.adv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FragGroupAdvertiseFolder) fragment).loadData(advertiseFileExplorerList.get(position).getPath());
                Log.d("path", advertiseFileExplorerList.get(position).getPath());
                //String fileOrFolder = advertiseFileExplorerList.get(position).getType();
                //Log.d("fileType", fileOrFolder);
//                if (!fileOrFolder.equals("file") && !fileOrFolder.equals("shortcut")) {
//                    //refreshData(mFileExplore.get(position).getPath());
//                    ((FragGroupAdvertiseFolder) fragment).loadAdvertiseFolder(advertiseFileExplorerList.get(position).getPath());
//                    Log.d("path", advertiseFileExplorerList.get(position).getPath());
//                }
            }
        });

        holder.adv_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogOptionBuilder = new AlertDialog.Builder(context);
                final LayoutInflater inflater = LayoutInflater.from(context);
                final View dialogOptionView = inflater.inflate(R.layout.adv_option_dialog, null);
                dialogOptionBuilder.setView(dialogOptionView);

                final AlertDialog option = dialogOptionBuilder.create();
                option.show();

                LinearLayout adv_set_advertise, adv_share_to, adv_rename, adv_delete;

                adv_set_advertise = dialogOptionView.findViewById(R.id.adv_set_advertise);
                adv_share_to = dialogOptionView.findViewById(R.id.adv_share_to);
                adv_rename = dialogOptionView.findViewById(R.id.adv_rename);
                adv_delete = dialogOptionView.findViewById(R.id.adv_delete);

                adv_set_advertise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                adv_share_to.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                adv_rename.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                adv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return advertiseFileExplorerList.size();
    }

    public class FileExplorerAdvertiseViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout adv_content;
        ImageView adv_thumbnail;
        TextView adv_name, adv_option;
        public FileExplorerAdvertiseViewHolder(View itemView) {
            super(itemView);

            adv_content = itemView.findViewById(R.id.adv_content);
            adv_thumbnail = itemView.findViewById(R.id.adv_thumbnail);
            adv_name = itemView.findViewById(R.id.adv_name);
            adv_option = itemView.findViewById(R.id.adv_option);
        }
    }
}
