package com.desktopip.exploriztic.tootanium.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.fragment.FragGroupAdvertiseFolder;
import com.desktopip.exploriztic.tootanium.models.ModAdvertiseFileExplorer;
import com.desktopip.exploriztic.tootanium.utilities.DownloadTask;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;

import java.util.HashMap;
import java.util.List;

public class AdvertiseFileExplorerAdapter extends RecyclerView.Adapter<AdvertiseFileExplorerAdapter.AdvertiseFileExplorerViewHolder> {

    AppCompatActivity activity;
    Fragment fragment;
    FragmentManager fragmentManager;
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
    FragGroupAdvertiseFolder fragGroupAdvertiseFolder;

    private MediaPlayer mediaPlayer = null;

    // Injection activity
    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    private void updateToolbar(boolean hide) {
        if(!hide){
            activity.getSupportActionBar().hide();
        }
    }

    public AdvertiseFileExplorerAdapter(Context context, List<ModAdvertiseFileExplorer> advertiseFileExplorerList, Fragment fragment) {
        this.context = context;
        this.advertiseFileExplorerList = advertiseFileExplorerList;
        this.fragment = fragment;

        fragGroupAdvertiseFolder = (FragGroupAdvertiseFolder) fragment;
        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();

        session = new SessionManager(context);
        session.checkLogin();

        //Get user data from session
        HashMap<String, String> user = session.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);
        baseUrl = user.get(SessionManager.BASE_URL);
        urlDownload = user.get(SessionManager.BASE_URL_DOWNLOAD);

        mediaPlayer = new MediaPlayer();

    }

    @NonNull
    @Override
    public AdvertiseFileExplorerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.frag_group_advertise_folder_item, parent, false);
        AdvertiseFileExplorerViewHolder viewHolder = new AdvertiseFileExplorerViewHolder(view, fragGroupAdvertiseFolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertiseFileExplorerViewHolder holder, final int position) {
        String baseFile = advertiseFileExplorerList.get(position).getName();
        String type = advertiseFileExplorerList.get(position).getType();
        String contentName = advertiseFileExplorerList.get(position).getName();

        if (!contentName.equals("") || contentName != null) {
            holder.adv_name.setText(advertiseFileExplorerList.get(position).getName());
            holder.adv_thumbnail.setImageResource(setFileImageType(baseFile, type));
        }

        holder.adv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileOrFolder = advertiseFileExplorerList.get(position).getType();
                //Log.d("fileType", fileOrFolder);
                if (!fileOrFolder.equals("file") && !fileOrFolder.equals("shortcut")) {
                    //refreshData(mFileExplore.get(position).getPath());
                    ((FragGroupAdvertiseFolder) fragment).loadData(advertiseFileExplorerList.get(position).getId());
                    //Log.d("pathkosong", mFileExplore.get(position).getPath());
                }
                //FragFileExplore fileExplore = new FragFileExplore();
                //fragmentManager.beginTransaction().replace(R.id.relLayoutContain, fileExplore).commit();
            }
        });

        holder.adv_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if(fragGroupAdvertiseFolder.isInActionMode) {
            updateToolbar(true);
        }
    }

    @Override
    public int getItemCount() {
        return advertiseFileExplorerList.size();
    }

    public static class AdvertiseFileExplorerViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout adv_content;
        ImageView adv_thumbnail;
        TextView adv_name, adv_option;
        FragGroupAdvertiseFolder fragGroupAdvertiseFolder;

        public AdvertiseFileExplorerViewHolder(View itemView, FragGroupAdvertiseFolder fragGroupAdvertiseFolder) {
            super(itemView);

            adv_content = itemView.findViewById(R.id.adv_content);
            adv_thumbnail = itemView.findViewById(R.id.adv_thumbnail);
            adv_name = itemView.findViewById(R.id.adv_name);
            adv_option = itemView.findViewById(R.id.adv_option);
            this.fragGroupAdvertiseFolder = fragGroupAdvertiseFolder;
        }

    }

    private int setFileImageType(String fileName, String fileType) {

        int lastIndex = fileName.lastIndexOf(".");

        if (fileType != null) {
            if(fileType.equalsIgnoreCase("shortcut")) {
                return R.mipmap.ic_folder_shortcut;
            } else if(fileType.equalsIgnoreCase("directory")) {
                return R.mipmap.ic_folder;
            } else {
                if (fileName.contains(".")) {
                    if(fileName.substring(lastIndex).equalsIgnoreCase(".txt")) {
                        return R.mipmap.ic_file;
                    } else if (fileName.substring(lastIndex).equalsIgnoreCase(".docx")) {
                        return R.mipmap.ic_docx;
                    } else if (fileName.substring(lastIndex).equalsIgnoreCase(".doc")) {
                        return R.mipmap.ic_docx;
                    } else if (fileName.substring(lastIndex).equalsIgnoreCase(".xls")) {
                        return R.mipmap.ic_excel;
                    } else if (fileName.substring(lastIndex).equalsIgnoreCase(".xlsx")) {
                        return R.mipmap.ic_excel;
                    } else if (fileName.substring(lastIndex).equalsIgnoreCase(".ppt")) {
                        return R.mipmap.ic_powerpoint;
                    } else if (fileName.substring(lastIndex).equalsIgnoreCase(".pdf")) {
                        return R.mipmap.ic_pdf;
                    } else if (fileName.substring(lastIndex).equalsIgnoreCase(".zip")) {
                        return R.mipmap.ic_archive;
                    } else if (fileName.substring(lastIndex).equalsIgnoreCase(".rar")) {
                        return R.mipmap.ic_archive;
                    } else if (fileName.substring(lastIndex).equalsIgnoreCase(".mp3")) {
                        return R.mipmap.ic_music;
                    } else if (fileName.substring(lastIndex).equalsIgnoreCase(".3gp")) {
                        return R.mipmap.ic_video;
                    } else if (fileName.substring(lastIndex).equalsIgnoreCase(".png")) {
                        return R.mipmap.ic_images;
                    } else if (fileName.substring(lastIndex).equalsIgnoreCase(".jpg")) {
                        return R.mipmap.ic_images;
                    } else {
                        return R.mipmap.ic_file;
                    }

                } else {
                    return R.mipmap.ic_file;
                }
            }
        }
        return R.mipmap.ic_file;
    }
}
