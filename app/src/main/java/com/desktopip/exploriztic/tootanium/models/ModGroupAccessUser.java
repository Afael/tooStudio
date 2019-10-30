package com.desktopip.exploriztic.tootanium.models;

public class ModGroupAccessUser {

    private String userId, groupAccessName;

    public ModGroupAccessUser() {
    }

    public ModGroupAccessUser(String userId, String groupAccessName) {
        this.userId = userId;
        this.groupAccessName = groupAccessName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupAccessName() {
        return groupAccessName;
    }

    public void setGroupAccessName(String groupAccessName) {
        this.groupAccessName = groupAccessName;
    }

    @Override
    public String toString() {
        return "ModGroupAccessUser{" +
                "userId='" + userId + '\'' +
                ", groupAccessName='" + groupAccessName + '\'' +
                '}';
    }
}
