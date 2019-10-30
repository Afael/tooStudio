package com.desktopip.exploriztic.tootanium.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.MainActivity;
import com.desktopip.exploriztic.tootanium.fragment.FragWorkingStudio;
import com.desktopip.exploriztic.tootanium.models.ModAppList;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.utilities.StringHelper;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

public class FragCategoryAppAdapter extends RecyclerView.Adapter<FragCategoryAppAdapter.FragCategoryAppsViewHolder> {

    View view;
    private Context context;
    private List<ModAppList.APPS> appsList;

    static SessionManager session;

    static String userName;

    static String password;

    String baseUrl;

    public FragCategoryAppAdapter(Context context, List<ModAppList.APPS> appsList) {
        this.context = context;
        this.appsList = appsList;
        session = new SessionManager(context);
        HashMap<String, String> user = session.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);
        baseUrl = user.get(SessionManager.BASE_URL);
    }

    @NonNull
    @Override
    public FragCategoryAppsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.app_list_items, parent, false);
        return new FragCategoryAppsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FragCategoryAppsViewHolder holder, final int position) {
        //holder.iv_app_list_icon.setImageResource(R.drawable.ic_about_24dp);
        String appIcon = appsList.get(position).ICONS;
        String imageUrl = baseUrl+"/backend/assets/icons/"+appIcon;
        Picasso.with(context)
                .load(imageUrl)
                .resize(1500, 0)
                .placeholder(R.drawable.loading_shape)
                .error(R.drawable.ic_error_outline_black_24dp)
                .into(holder.iv_app_list_icon);

        holder.tv_app_list_title.setText(appsList.get(position).APPS_NAME);
        holder.apps_list_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appsPath = appsList.get(position).APPS_PATH;
                String baseNameFile = appsList.get(position).APPS_NAME;
                try {
                    String hasilEncode = StringHelper.base64Encode(appsPath);
                    String baseParam = "apps-edit/?pt="+hasilEncode;
                    FragWorkingStudio openTab = MainActivity.fragmentContainer;
                    openTab.openNewWebView(baseParam, baseNameFile);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return appsList.size();
    }

    static class FragCategoryAppsViewHolder extends RecyclerView.ViewHolder{

        LinearLayout apps_list_layout;
        ImageView iv_app_list_icon;
        TextView tv_app_list_title;

        public FragCategoryAppsViewHolder(View itemView) {
            super(itemView);
            apps_list_layout = itemView.findViewById(R.id.apps_list_layout);
            iv_app_list_icon = itemView.findViewById(R.id.iv_app_list_icon);
            tv_app_list_title = itemView.findViewById(R.id.tv_app_list_title);
        }
    }
}
