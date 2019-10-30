package com.desktopip.exploriztic.tootanium.models;

import android.os.Parcel;
import android.os.Parcelable;

public class StorageMenuItem implements Parcelable {
    public static final String STORAGE_MENU_ITEMS_KEY = "com.desktopip.exploriztic.spatialworkingstudio.models.StorageMenuItem";

    private int iconId;
    private String label;

    public StorageMenuItem(int iconId, String label) {
        this.iconId = iconId;
        this.label = label;
    }

    public int getIconId() {
        return iconId;
    }

    public String getLabel() {
        return label;
    }

    protected StorageMenuItem(Parcel in) {
        this.iconId = in.readInt();
        this.label = in.readString();
    }

    public static final Creator<StorageMenuItem> CREATOR = new Creator<StorageMenuItem>() {
        @Override
        public StorageMenuItem createFromParcel(Parcel in) {
            return new StorageMenuItem(in);
        }

        @Override
        public StorageMenuItem[] newArray(int size) {
            return new StorageMenuItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.iconId);
        dest.writeString(this.label);
    }
}
