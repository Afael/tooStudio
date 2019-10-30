package com.desktopip.exploriztic.tootanium.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.customisable.OptionDialog;
import com.desktopip.exploriztic.tootanium.fragment.FragFileExplore;
import com.desktopip.exploriztic.tootanium.models.ModFileExplore;
import com.desktopip.exploriztic.tootanium.utilities.CheckInternetConnection;
import com.desktopip.exploriztic.tootanium.utilities.CustomAlertDialogManager;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.utilities.StringHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jayus on 03/07/2018.
 */

public class FileExploreAdapter extends RecyclerView.Adapter<FileExploreAdapter.FileExploreViewHolder> {

    View view;

    private AppCompatActivity activity;

    public static Fragment fragment;

    static SessionManager session;

    static String userName;

    static String password;

    String baseUrl;

    String urlDownload;

    public static Context context;

    private List<ModFileExplore> mFileExplore;

    CustomAlertDialogManager alert;

    public static FragmentActivity myContext;

    OptionDialog dialogOptionFragment;



    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public FileExploreAdapter(Context context, List<ModFileExplore> mFileExplore, Fragment fragment) {
        this.context = context;
        myContext = (FragmentActivity) context;
        this.mFileExplore = mFileExplore;
        this.fragment = fragment;

        session = new SessionManager(context);
        session.checkLogin();

        //Get user data from session
        HashMap<String, String> user = session.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);
        baseUrl = user.get(SessionManager.BASE_URL);
        urlDownload = user.get(SessionManager.BASE_URL_DOWNLOAD);
        alert = new CustomAlertDialogManager();

        dialogOptionFragment = new OptionDialog();
    }

    @NonNull
    @Override
    public FileExploreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.frag_file_explore_item, parent, false);
        return new FileExploreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FileExploreViewHolder holder, final int position) {

        if (!mFileExplore.get(position).getName().equals("")) {
            holder.feName.setText(mFileExplore.get(position).getName());
        }

        String baseFile = mFileExplore.get(position).getName();
        String type = mFileExplore.get(position).getType();

        if (type != null) {
            switch (type) {
                case "shortcut":
                    holder.feThumbnail.setImageResource(R.mipmap.ic_folder_shortcut);
                    break;
                case "directory":
                    holder.feThumbnail.setImageResource(R.mipmap.ic_folder);
                    break;
                case "file":
                    holder.feThumbnail.setImageResource(setFileImageType(baseFile));
                    break;
            }
        }

        holder.fe_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fileOrFolder = mFileExplore.get(position).getType();

                if (!fileOrFolder.equals("file") && !fileOrFolder.equals("shortcut")) {
                    ((FragFileExplore) fragment).loadData(mFileExplore.get(position).getId());
                }

            }
        });

        holder.feOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CheckInternetConnection.checknetwork(context)) {
                    final String id = mFileExplore.get(position).getId();
                    final String fileOrFolder = mFileExplore.get(position).getType();
                    final String baseNameFile = mFileExplore.get(position).getName();
                    final String path = mFileExplore.get(position).getPath();
                    String isRoot = StringHelper.substringBeforeLast(mFileExplore.get(position).getId(), "/");
                    String typeFile = mFileExplore.get(position).getType();
                    String type = mFileExplore.get(position).getType();

                    Bundle optionBundle = new Bundle();
                    optionBundle.putString("id", id);
                    optionBundle.putString("userName", userName);
                    optionBundle.putString("password", password);
                    optionBundle.putString("baseUrl", baseUrl);
                    optionBundle.putString("urlDownload", urlDownload);
                    optionBundle.putString("urlDownload", urlDownload);
                    optionBundle.putString("fileOrFolder", fileOrFolder);
                    optionBundle.putString("baseNameFile", baseNameFile);
                    optionBundle.putString("path", path);
                    optionBundle.putString("isRoot", isRoot);
                    optionBundle.putString("typeFile", typeFile);
                    optionBundle.putString("type", type);

                    if(!dialogOptionFragment.isVisible()){
                        dialogOptionFragment.show(myContext.getSupportFragmentManager(), "OptionDialog");
                        dialogOptionFragment.setArguments(optionBundle);
                    }

                } else {
                    Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void searchFileExplorer(List<ModFileExplore> newChannel) {
        mFileExplore = new ArrayList<>();
        mFileExplore.addAll(newChannel);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mFileExplore.size();
    }

    public static class FileExploreViewHolder extends RecyclerView.ViewHolder {

        TextView feName, feOption;
        ImageView feThumbnail;
        //SwipeLayout feSwipeLayout;
        LinearLayout fe_content;
        RelativeLayout feNotFound;
        RecyclerView rc_file_explore;

        public FileExploreViewHolder(View itemView) {
            super(itemView);
            fe_content = itemView.findViewById(R.id.fe_content);
            rc_file_explore = itemView.findViewById(R.id.rc_file_explore);
            feNotFound = itemView.findViewById(R.id.layout_not_found);
            feName = itemView.findViewById(R.id.fe_name);
            feOption = itemView.findViewById(R.id.fe_option);
            feThumbnail = itemView.findViewById(R.id.fe_thumbnail);
            //feSwipeLayout = itemView.findViewById(R.id.swipe);
        }
    }

    private int setFileImageType(String m_file) {
        int m_lastIndex = m_file.lastIndexOf(".");

        if (m_file.contains(".")) {
            if (m_file.substring(m_lastIndex).equalsIgnoreCase(".png")) {
                return R.mipmap.ic_images;
            } else if (m_file.substring(m_lastIndex).equalsIgnoreCase(".jpg")) {
                return R.mipmap.ic_images;
            } else if (m_file.substring(m_lastIndex).equalsIgnoreCase(".jpeg")) {
                return R.mipmap.ic_images;
            } else if (m_file.substring(m_lastIndex).equalsIgnoreCase(".txt")) {
                return R.mipmap.ic_file;
            } else if (m_file.substring(m_lastIndex).equalsIgnoreCase(".doc")) {
                return R.mipmap.ic_docx;
            } else if (m_file.substring(m_lastIndex).equalsIgnoreCase(".docx")) {
                return R.mipmap.ic_docx;
            } else if (m_file.substring(m_lastIndex).equalsIgnoreCase(".xls")) {
                return R.mipmap.ic_excel;
            } else if (m_file.substring(m_lastIndex).equalsIgnoreCase(".xlsx")) {
                return R.mipmap.ic_excel;
            } else if (m_file.substring(m_lastIndex).equalsIgnoreCase(".ppt")) {
                return R.mipmap.ic_powerpoint;
            } else if (m_file.substring(m_lastIndex).equalsIgnoreCase(".pptx")) {
                return R.mipmap.ic_powerpoint;
            } else if (m_file.substring(m_lastIndex).equalsIgnoreCase(".pdf")) {
                return R.mipmap.ic_pdf;
            } else if (m_file.substring(m_lastIndex).equalsIgnoreCase(".zip")) {
                return R.mipmap.ic_archive;
            } else if (m_file.substring(m_lastIndex).equalsIgnoreCase(".rar")) {
                return R.mipmap.ic_archive;
            } else if (m_file.substring(m_lastIndex).equalsIgnoreCase(".mp3")) {
                return R.mipmap.ic_music;
            } else if (m_file.substring(m_lastIndex).equalsIgnoreCase(".mp4")) {
                return R.mipmap.ic_music;
            } else if (m_file.substring(m_lastIndex).equalsIgnoreCase(".3gp")) {
                return R.mipmap.ic_video;
            } else {
                return R.mipmap.ic_file;
            }
        }
        return R.mipmap.ic_file;
    }
}
