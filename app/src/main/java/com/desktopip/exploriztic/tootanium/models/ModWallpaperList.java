package com.desktopip.exploriztic.tootanium.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModWallpaperList {

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<Data> listWallpaper;

    public static class Data{

        @SerializedName("id")
        private int id;
        @SerializedName("wallpaper")
        private String wallpaper;
        @SerializedName("userName")
        private String userName;
        @SerializedName("dateCreated")
        private String dateCreated;
        @SerializedName("dateModified")
        private String dateModified;
    }

}
