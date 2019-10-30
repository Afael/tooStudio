package com.desktopip.exploriztic.tootanium.models;

public class ModNotificationList {

    private String objectId, groupId, groupName, userId, title, message, createDate, createdBy, isJoin;

    public ModNotificationList() {
    }

    public ModNotificationList(String objectId, String groupId, String groupName, String userId, String title, String message, String createDate, String createdBy, String isJoin) {
        this.objectId = objectId;
        this.groupId = groupId;
        this.groupName = groupName;
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.isJoin = isJoin;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(String isJoin) {
        this.isJoin = isJoin;
    }

    @Override
    public String toString() {
        return "ModNotificationList{" +
                "objectId='" + objectId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", createDate='" + createDate + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", isJoin='" + isJoin + '\'' +
                '}';
    }
}
