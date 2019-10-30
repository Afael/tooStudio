package com.desktopip.exploriztic.tootanium.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.models.StorageMenuAction;

import java.util.List;

public class StorageMenuAdapter extends StorageAdapter {

    public StorageMenuAdapter(Context context, List<?> actions) {
        this.items = actions;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StorageAdapterViewHolder holder;
        StorageMenuAction action = (StorageMenuAction) items.get(position);

        if(convertView == null){
            holder = new StorageAdapterViewHolder();
            convertView = inflater.inflate(R.layout.storage_bottom_sheet_menu_items, parent, false);
            convertView.setTag(holder);

            holder.icon = convertView.findViewById(R.id.iv_storage_menu);
            holder.label = convertView.findViewById(R.id.tv_storage_menu);
        } else {
            holder = (StorageAdapterViewHolder) convertView.getTag();
        }

        holder.icon.setImageResource(action.getIconId());
        holder.label.setText(action.getLabel());

        return convertView;
    }
}
