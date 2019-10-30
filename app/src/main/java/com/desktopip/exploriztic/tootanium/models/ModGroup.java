package com.desktopip.exploriztic.tootanium.models;

public class ModGroup {

    private String groupId, groupName, groupDescription, isActive, createdDate, createdBy, isJoin, isInvite;

    public ModGroup() {
    }

    public ModGroup(String groupId, String groupName, String groupDescription, String isActive
            , String createdDate, String createdBy, String isJoin, String isInvite) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.isActive = isActive;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.isJoin = isJoin;
        this.isInvite = isInvite;
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

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
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

    public String getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(String isJoin) {
        this.isJoin = isJoin;
    }

    public String getIsInvite() {
        return isInvite;
    }

    public void setIsInvite(String isInvite) {
        this.isInvite = isInvite;
    }

    @Override
    public String toString() {
        return "ModGroup{" +
                "groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", groupDescription='" + groupDescription + '\'' +
                ", isActive='" + isActive + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", isJoin='" + isJoin + '\'' +
                ", isInvite='" + isInvite + '\'' +
                '}';
    }
}
