package com.desktopip.exploriztic.tootanium.models;

public class ModRequestGroup {

    private String groupId, groupName, userId, isApproved, requestDate;

    public ModRequestGroup() {
    }

    public ModRequestGroup(String groupId, String groupName, String userId, String isApproved, String requestDate) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.userId = userId;
        this.isApproved = isApproved;
        this.requestDate = requestDate;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    @Override
    public String toString() {
        return "ModRequestGroup{" +
                "groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", userId='" + userId + '\'' +
                ", isApproved='" + isApproved + '\'' +
                ", requestDate='" + requestDate + '\'' +
                '}';
    }
}
