package com.chinamobile.iot.onenet.sdksample.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeviceItem implements Serializable {
    private String id;
    private String title;
    private String desc;
    @SerializedName("private")
    private boolean isPrivate;
    private String protocol = "HTTP";
    private boolean online;
    private Location location;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("auth_info")
    private String authInfo;
    @SerializedName("activite_code")
    private ActivateCode activateCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAuthInfo() {
        return authInfo;
    }

    public void setAuthInfo(String authInfo) {
        this.authInfo = authInfo;
    }

    public ActivateCode getActivateCode() {
        return activateCode;
    }

    public void setActivateCode(ActivateCode activateCode) {
        this.activateCode = activateCode;
    }
}
