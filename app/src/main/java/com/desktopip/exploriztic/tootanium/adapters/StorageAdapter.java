package com.desktopip.exploriztic.tootanium.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.desktopip.exploriztic.tootanium.models.StorageMenuItem;

import java.util.List;

public class StorageAdapter extends BaseAdapter {

    protected List<?> items;
    protected LayoutInflater inflater;

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        StorageMenuItem item = (StorageMenuItem) items.get(position);
        return item.getIconId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    static class StorageAdapterViewHolder{
        ImageView icon;
        TextView label;
    }
}
