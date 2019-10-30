package com.desktopip.exploriztic.tootanium.models;

import android.os.Parcel;

public class StorageMenuAction extends StorageMenuItem {

    public StorageMenuAction(int iconId, String label) {
        super(iconId, label);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected StorageMenuAction(Parcel in) {
        super(in);
    }

    public static final Creator<StorageMenuAction> CREATOR = new Creator<StorageMenuAction>() {
        @Override
        public StorageMenuAction createFromParcel(Parcel source) {
            return new StorageMenuAction(source);
        }

        @Override
        public StorageMenuAction[] newArray(int size) {
            return new StorageMenuAction[size];
        }
    };
}
