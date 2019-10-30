package com.desktopip.exploriztic.tootanium.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.activities.Personalization;
import com.desktopip.exploriztic.tootanium.activities.Profile;
import com.desktopip.exploriztic.tootanium.activities.UserProfile;
import com.desktopip.exploriztic.tootanium.models.SystemPreferencesMenu;

import java.util.List;

public class SystemPreferenceAdapter extends RecyclerView.Adapter<SystemPreferenceAdapter.SystemPreferenceViewHolder> {

    View view;
    private Fragment fragment = null;
    GridLayout mainGrid;
    private Context context;
    private List<SystemPreferencesMenu> list;

    public SystemPreferenceAdapter(Context context, GridLayout mainGrid, List<SystemPreferencesMenu> list) {
        this.context = context;
        this.list = list;
        this.mainGrid = mainGrid;
    }

    @NonNull
    @Override
    public SystemPreferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.system_preferences_card, parent, false);
        return new SystemPreferenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SystemPreferenceViewHolder holder, final int position) {
        holder.iv_system_preference_card.setImageResource(list.get(position).getImage());
        holder.system_preference_title.setText(list.get(position).getTitle());
        holder.cv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position){
                    case 0:
                        Intent workspace = new Intent(context, Profile.class);
                        workspace.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(workspace);
                        break;
                    case 1:
                        Intent personalization = new Intent(context, Personalization.class);
                        personalization.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(personalization);
                        break;
                    case 2:
                        Intent profile = new Intent(context, Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(profile);
                        break;
                    case 3:
                        Intent marketplace = new Intent(context, UserProfile.class);
                        marketplace.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(marketplace);
                        break;
                    case 4:
                        Intent language = new Intent(context, UserProfile.class);
                        language.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(language);
                        break;
                    case 5:
                        Intent help = new Intent(context, UserProfile.class);
                        help.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(help);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SystemPreferenceViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout cv_menu;
        ImageView iv_system_preference_card;
        TextView system_preference_title;

        public SystemPreferenceViewHolder(View itemView) {
            super(itemView);

            cv_menu = itemView.findViewById(R.id.cv_menu);
            iv_system_preference_card = itemView.findViewById(R.id.iv_system_preference_card);
            system_preference_title = itemView.findViewById(R.id.system_preference_title);
        }
    }
}
