package com.desktopip.exploriztic.tootanium.models;


/**
 * Created by Jayus on 02/08/2018.
 */

public class ModDropdown{

    private String path;
    private String baseName;

    public ModDropdown(String path, String baseName) {
        this.path = path;
        this.baseName = baseName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    @Override
    public String toString() {
        return path;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ModDropdown){
            ModDropdown c = (ModDropdown)obj;
            if(c.getBaseName().equals(baseName) && c.getPath()==path ) return true;
        }

        return false;
    }

}
