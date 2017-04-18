package com.chinamobile.iot.onenet.sdksample.model;

import com.google.gson.annotations.SerializedName;

public class TriggerItem {
    private String id;
    private String title;
    @SerializedName("ds_uuids")
    private String[] dsUuids;
    @SerializedName("dev_ids")
    private String[] devIds;
    @SerializedName("ds_id")
    private String dsId;
    private String url;
    private String type;
    private double threshold;
    private boolean invalid;
    @SerializedName("create_time")
    private String createTime;

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

    public String[] getDsUuids() {
        return dsUuids;
    }

    public void setDsUuids(String[] dsUuids) {
        this.dsUuids = dsUuids;
    }

    public String[] getDevIds() {
        return devIds;
    }

    public void setDevIds(String[] devIds) {
        this.devIds = devIds;
    }

    public String getDsId() {
        return dsId;
    }

    public void setDsId(String dsId) {
        this.dsId = dsId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
