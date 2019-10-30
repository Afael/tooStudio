package com.desktopip.exploriztic.tootanium.models;

public class ModAdvertiseGroupUser {

    private String userId;
    private String groupName;
    private String createdBy;


    public ModAdvertiseGroupUser() {
    }

    public ModAdvertiseGroupUser(String userId, String groupName, String createdBy) {
        this.userId = userId;
        this.groupName = groupName;
        this.createdBy = createdBy;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "ModAdvertiseGroupUser{" +
                "userId='" + userId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
