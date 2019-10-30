package com.desktopip.exploriztic.tootanium.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jayus on 03/07/2018.
 */

public class ModDownloadChannel implements Parcelable{

    private String primary;
    private String channel_id;
    private String baseFile;
    private String dirpath;
    private String generate_date;
    private String expired;
    private String is_expired;
    private String pathtype;
    private String fileSize;

    public ModDownloadChannel() {
    }

    public ModDownloadChannel(String primary, String channel_id, String baseFile, String dirpath
            , String generate_date, String expired, String is_expired, String pathtype, String share_by) {
        this.primary = primary;
        this.channel_id = channel_id;
        this.baseFile = baseFile;
        this.dirpath = dirpath;
        this.generate_date = generate_date;
        this.expired = expired;
        this.is_expired = is_expired;
        this.pathtype = pathtype;
    }

    protected ModDownloadChannel(Parcel in) {
        primary = in.readString();
        channel_id = in.readString();
        baseFile = in.readString();
        dirpath = in.readString();
        generate_date = in.readString();
        expired = in.readString();
        is_expired = in.readString();
        pathtype = in.readString();
    }

    public static final Creator<ModDownloadChannel> CREATOR = new Creator<ModDownloadChannel>() {
        @Override
        public ModDownloadChannel createFromParcel(Parcel in) {
            return new ModDownloadChannel(in);
        }

        @Override
        public ModDownloadChannel[] newArray(int size) {
            return new ModDownloadChannel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(primary);
        parcel.writeString(channel_id);
        parcel.writeString(baseFile);
        parcel.writeString(dirpath);
        parcel.writeString(generate_date);
        parcel.writeString(expired);
        parcel.writeString(is_expired);
        parcel.writeString(pathtype);
    }

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

    public String getBaseFile() {
        return baseFile;
    }

    public void setBaseFile(String baseFile) {
        this.baseFile = baseFile;
    }

    public String getDirpath() {
        return dirpath;
    }

    public void setDirpath(String dirpath) {
        this.dirpath = dirpath;
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

    public String getIs_expired() {
        return is_expired;
    }

    public void setIs_expired(String is_expired) {
        this.is_expired = is_expired;
    }

    public String getPathtype() {
        return pathtype;
    }

    public void setPathtype(String pathtype) {
        this.pathtype = pathtype;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
}
