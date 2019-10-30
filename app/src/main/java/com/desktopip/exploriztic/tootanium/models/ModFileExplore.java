package com.desktopip.exploriztic.tootanium.models;

/**
 * Created by Jayus on 03/07/2018.
 */

public class ModFileExplore {

    private String id;
    private String name;
    private String type;
    private String extension;
    private String mime;
    private String path;


    public ModFileExplore() { }

    public ModFileExplore(String id, String name, String type, String extension, String mime, String path) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.extension = extension;
        this.mime = mime;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getExtension() {
        return extension;
    }

    public String getMime() {
        return mime;
    }

    public String getPath() {
        return path;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
