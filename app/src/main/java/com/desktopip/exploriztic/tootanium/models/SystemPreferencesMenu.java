package com.desktopip.exploriztic.tootanium.models;

public class SystemPreferencesMenu {
    private String title;
    private int image;

    public SystemPreferencesMenu(){}

    public SystemPreferencesMenu(String title, int image){
        this.title = title;
        this.image = image;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public void setImage(int image){
        this.image = image;
    }

    public int getImage(){
        return this.image;
    }
}
