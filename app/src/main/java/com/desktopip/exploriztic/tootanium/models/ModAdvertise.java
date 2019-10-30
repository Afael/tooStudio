package com.desktopip.exploriztic.tootanium.models;

/**
 * Created by Jayus on 01/08/2018.
 */

public class ModAdvertise {

    String advGroupId;
    String advGroupName;
    String advGroupDesc;
    String advGroupPath;
    String advGroupIsActive;
    String advGroupCreateDate;
    String advGroupCreatedBy;
    String advCountMembers;
    String groupAccessId;

    public ModAdvertise() {
    }

    public ModAdvertise(String advGroupId, String advGroupName, String advGroupDesc, String advGroupPath
            , String advGroupIsActive, String advGroupCreateDate, String advGroupCreatedBy, String advCountMembers, String groupAccessId) {
        this.advGroupId = advGroupId;
        this.advGroupName = advGroupName;
        this.advGroupDesc = advGroupDesc;
        this.advGroupPath = advGroupPath;
        this.advGroupIsActive = advGroupIsActive;
        this.advGroupCreateDate = advGroupCreateDate;
        this.advGroupCreatedBy = advGroupCreatedBy;
        this.advCountMembers = advCountMembers;
        this.groupAccessId = groupAccessId;
    }

    public String getAdvGroupId() {
        return advGroupId;
    }

    public void setAdvGroupId(String advGroupId) {
        this.advGroupId = advGroupId;
    }

    public String getAdvGroupName() {
        return advGroupName;
    }

    public void setAdvGroupName(String advGroupName) {
        this.advGroupName = advGroupName;
    }

    public String getAdvGroupDesc() {
        return advGroupDesc;
    }

    public void setAdvGroupDesc(String advGroupDesc) {
        this.advGroupDesc = advGroupDesc;
    }

    public String getAdvGroupPath() {
        return advGroupPath;
    }

    public void setAdvGroupPath(String advGroupPath) {
        this.advGroupPath = advGroupPath;
    }

    public String getAdvGroupIsActive() {
        return advGroupIsActive;
    }

    public void setAdvGroupIsActive(String advGroupIsActive) {
        this.advGroupIsActive = advGroupIsActive;
    }

    public String getAdvGroupCreateDate() {
        return advGroupCreateDate;
    }

    public void setAdvGroupCreateDate(String advGroupCreateDate) {
        this.advGroupCreateDate = advGroupCreateDate;
    }

    public String getAdvGroupCreatedBy() {
        return advGroupCreatedBy;
    }

    public void setAdvGroupCreatedBy(String advGroupCreatedBy) {
        this.advGroupCreatedBy = advGroupCreatedBy;
    }

    public String getAdvCountMembers() {
        return advCountMembers;
    }

    public void setAdvCountMembers(String advCountMembers) {
        this.advCountMembers = advCountMembers;
    }

    public String getGroupAccessId() {
        return groupAccessId;
    }

    public void setGroupAccessId(String groupAccessId) {
        this.groupAccessId = groupAccessId;
    }

    @Override
    public String toString() {
        return "ModAdvertise{" +
                "advGroupId='" + advGroupId + '\'' +
                ", advGroupName='" + advGroupName + '\'' +
                ", advGroupDesc='" + advGroupDesc + '\'' +
                ", advGroupPath='" + advGroupPath + '\'' +
                ", advGroupIsActive='" + advGroupIsActive + '\'' +
                ", advGroupCreateDate='" + advGroupCreateDate + '\'' +
                ", advGroupCreatedBy='" + advGroupCreatedBy + '\'' +
                ", advCountMembers='" + advCountMembers + '\'' +
                ", groupAccessId='" + groupAccessId + '\'' +
                '}';
    }
}
