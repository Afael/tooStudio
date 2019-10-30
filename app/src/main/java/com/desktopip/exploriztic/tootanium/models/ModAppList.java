package com.desktopip.exploriztic.tootanium.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ModAppList {

    @SerializedName("template")
    public List<Template> template;
    @SerializedName("apps_recents")
    public List<AppsRecent> appsRecants;
    @SerializedName("status")
    public String status;

    public List<Template> getTemplate() {
        return template;
    }

    public void setTemplate(List<Template> template) {
        this.template = template;
    }

    public List<AppsRecent> getAppsRecants() {
        return appsRecants;
    }

    public void setAppsRecants(List<AppsRecent> appsRecants) {
        this.appsRecants = appsRecants;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Template{
        @SerializedName("U_APP_ID")
        public String U_APP_ID;
        @SerializedName("GID")
        public String GID;
        @SerializedName("GROUP_NAME")
        public String GROUP_NAME;
        @SerializedName("APPS_LIST")
        public List<APPS> APPS_LIST;

        public void addAppList(APPS apps){
            if(APPS_LIST == null){
                APPS_LIST = new ArrayList<>();
            }
            APPS_LIST.add(apps);
        }

        public String getU_APP_ID() {
            return U_APP_ID;
        }

        public void setU_APP_ID(String u_APP_ID) {
            U_APP_ID = u_APP_ID;
        }

        public String getGID() {
            return GID;
        }

        public void setGID(String GID) {
            this.GID = GID;
        }

        public String getGROUP_NAME() {
            return GROUP_NAME;
        }

        public void setGROUP_NAME(String GROUP_NAME) {
            this.GROUP_NAME = GROUP_NAME;
        }

        public List<APPS> getAPPS_LIST() {
            return APPS_LIST;
        }

        public void setAPPS_LIST(List<APPS> APPS_LIST) {
            this.APPS_LIST = APPS_LIST;
        }
    }

    public static class APPS{
        @SerializedName("APP_IDS")
        public String APP_IDS;
        @SerializedName("APPS_NAME")
        public String APPS_NAME;
        @SerializedName("APPS_PATH")
        public String APPS_PATH;
        @SerializedName("ICONS")
        public String ICONS;
        @SerializedName("APP_ACTIVE")
        public String APP_ACTIVE;

        public APPS(String APP_IDS, String APPS_NAME, String APPS_PATH, String ICONS, String APP_ACTIVE) {
            this.APP_IDS = APP_IDS;
            this.APPS_NAME = APPS_NAME;
            this.APPS_PATH = APPS_PATH;
            this.ICONS = ICONS;
            this.APP_ACTIVE = APP_ACTIVE;
        }
    }

    public static class AppsRecent{
        @SerializedName("ID")
        public String ID;
        @SerializedName("APP_ID")
        public String APP_ID;
        @SerializedName("USER")
        public String USER;
        @SerializedName("CLICK")
        public String CLICK;
        @SerializedName("DATE_CREATED")
        public String DATE_CREATED;
        @SerializedName("DATE_MODIFIED")
        public String DATE_MODIFIED;
        @SerializedName("APP_NAME")
        public String APP_NAME;
        @SerializedName("APP_PATH")
        public String APP_PATH;
        @SerializedName("ICON")
        public String ICON;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getAPP_ID() {
            return APP_ID;
        }

        public void setAPP_ID(String APP_ID) {
            this.APP_ID = APP_ID;
        }

        public String getUSER() {
            return USER;
        }

        public void setUSER(String USER) {
            this.USER = USER;
        }

        public String getCLICK() {
            return CLICK;
        }

        public void setCLICK(String CLICK) {
            this.CLICK = CLICK;
        }

        public String getDATE_CREATED() {
            return DATE_CREATED;
        }

        public void setDATE_CREATED(String DATE_CREATED) {
            this.DATE_CREATED = DATE_CREATED;
        }

        public String getDATE_MODIFIED() {
            return DATE_MODIFIED;
        }

        public void setDATE_MODIFIED(String DATE_MODIFIED) {
            this.DATE_MODIFIED = DATE_MODIFIED;
        }

        public String getAPP_NAME() {
            return APP_NAME;
        }

        public void setAPP_NAME(String APP_NAME) {
            this.APP_NAME = APP_NAME;
        }

        public String getAPP_PATH() {
            return APP_PATH;
        }

        public void setAPP_PATH(String APP_PATH) {
            this.APP_PATH = APP_PATH;
        }

        public String getICON() {
            return ICON;
        }

        public void setICON(String ICON) {
            this.ICON = ICON;
        }
    }
}
