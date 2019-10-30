package com.desktopip.exploriztic.tootanium.models;

public class ModGroupAccess {

    private String groupAccessId, groupAccessName, groupAccessDescription, createdDate, createdBy;

    public ModGroupAccess() {
    }

    public ModGroupAccess(String groupAccessId, String groupAccessName, String groupAccessDescription, String createdDate, String createdBy) {
        this.groupAccessId = groupAccessId;
        this.groupAccessName = groupAccessName;
        this.groupAccessDescription = groupAccessDescription;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
    }

    public String getGroupAccessId() {
        return groupAccessId;
    }

    public void setGroupAccessId(String groupAccessId) {
        this.groupAccessId = groupAccessId;
    }

    public String getGroupAccessName() {
        return groupAccessName;
    }

    public void setGroupAccessName(String groupAccessName) {
        this.groupAccessName = groupAccessName;
    }

    public String getGroupAccessDescription() {
        return groupAccessDescription;
    }

    public void setGroupAccessDescription(String groupAccessDescription) {
        this.groupAccessDescription = groupAccessDescription;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "ModGroupAccess{" +
                "groupAccessId='" + groupAccessId + '\'' +
                ", groupAccessName='" + groupAccessName + '\'' +
                ", groupAccessDescription='" + groupAccessDescription + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
