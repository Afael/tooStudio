package com.desktopip.exploriztic.tootanium.models;

public class ModWebWorkingStudio {
    private int id;
    private String fragmentName;
    private String fileName;

    public ModWebWorkingStudio() {
    }

    public ModWebWorkingStudio(int id, String fragmentName, String fileName) {
        this.id = id;
        this.fragmentName = fragmentName;
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFragmentName() {
        return fragmentName;
    }

    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
