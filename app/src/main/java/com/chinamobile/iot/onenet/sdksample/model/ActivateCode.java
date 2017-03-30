package com.chinamobile.iot.onenet.sdksample.model;

import java.io.Serializable;

public class ActivateCode implements Serializable {

    private String mt;
    private String mid;

    public String getMt() {
        return mt;
    }

    public void setMt(String mt) {
        this.mt = mt;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }
}
