package com.desktopip.exploriztic.tootanium.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jayus on 03/07/2018.
 */

public class ModAppsChannel implements Parcelable{

    private String primary;
    private String channel_id;
    private String basefile;
    private String basePath;
    private String is_expired;
    private String generate_date;
    private String expired;

    public ModAppsChannel() {
    }

    public ModAppsChannel(String primary, String channel_id, String basefile, String basePath
            , String is_expired, String generate_date, String expired) {
        this.primary = primary;
        this.channel_id = channel_id;
        this.basefile = basefile;
        this.basePath = basePath;
        this.is_expired = is_expired;
        this.generate_date = generate_date;
        this.expired = expired;
    }

    protected ModAppsChannel(Parcel in) {
        primary = in.readString();
        channel_id = in.readString();
        basefile = in.readString();
        basePath = in.readString();
        is_expired = in.readString();
        generate_date = in.readString();
        expired = in.readString();
    }

    public static final Creator<ModAppsChannel> CREATOR = new Creator<ModAppsChannel>() {
        @Override
        public ModAppsChannel createFromParcel(Parcel in) {
            return new ModAppsChannel(in);
        }

        @Override
        public ModAppsChannel[] newArray(int size) {
            return new ModAppsChannel[size];
        }
    };

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getBasefile() {
        return basefile;
    }

    public void setBasefile(String basefile) {
        this.basefile = basefile;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getIs_expired() {
        return is_expired;
    }

    public void setIs_expired(String is_expired) {
        this.is_expired = is_expired;
    }

    public String getGenerate_date() {
        return generate_date;
    }

    public void setGenerate_date(String generate_date) {
        this.generate_date = generate_date;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(primary);
        parcel.writeString(channel_id);
        parcel.writeString(basefile);
        parcel.writeString(basePath);
        parcel.writeString(is_expired);
        parcel.writeString(generate_date);
        parcel.writeString(expired);
    }
}
