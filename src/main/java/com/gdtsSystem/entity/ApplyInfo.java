package com.gdtsSystem.entity;

import java.sql.Timestamp;

public class ApplyInfo {
    private String applyid;
    private String sid;
    private String gdtid;
    private String gdtname;
    private Timestamp applytime;
    private Timestamp replytime;
    private String status;

    public ApplyInfo() {
    }

    public ApplyInfo(String applyid, String sid, String gdtid, String gdtname, Timestamp applytime, Timestamp replytime, String status) {
        this.applyid = applyid;
        this.sid = sid;
        this.gdtid = gdtid;
        this.gdtname = gdtname;
        this.applytime = applytime;
        this.replytime = replytime;
        this.status = status;
    }

    public String getApplyid() {
        return applyid;
    }

    public void setApplyid(String applyid) {
        this.applyid = applyid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getGdtid() {
        return gdtid;
    }

    public void setGdtid(String gdtid) {
        this.gdtid = gdtid;
    }

    public String getGdtname() {
        return gdtname;
    }

    public void setGdtname(String gdtname) {
        this.gdtname = gdtname;
    }

    public Timestamp getApplytime() {
        return applytime;
    }

    public void setApplytime(Timestamp applytime) {
        this.applytime = applytime;
    }

    public Timestamp getReplytime() {
        return replytime;
    }

    public void setReplytime(Timestamp replytime) {
        this.replytime = replytime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ApplyInfo{" +
                "applyid='" + applyid + '\'' +
                ", sid=" + sid +
                ", gdtid='" + gdtid + '\'' +
                ", gdtname='" + gdtname + '\'' +
                ", applytime='" + applytime + '\'' +
                ", replytime='" + replytime + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
