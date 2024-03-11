package com.gdtsSystem.entity;

public class GdtInfo {
    private String gdtid;
    private String gdtname;
    private int gdttype;
    private String gdtreq;
    private String gdtintro;
    private String tid;
    private String sid;

    public GdtInfo() {
    }

    public GdtInfo(String gdtid, String gdtname, int gdttype, String gdtreq, String gdtintro, String tid, String sid) {
        this.gdtid = gdtid;
        this.gdtname = gdtname;
        this.gdttype = gdttype;
        this.gdtreq = gdtreq;
        this.gdtintro = gdtintro;
        this.tid = tid;
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

    public int getGdttype() {
        return gdttype;
    }

    public void setGdttype(int gdttype) {
        this.gdttype = gdttype;
    }

    public String getGdtreq() {
        return gdtreq;
    }

    public void setGdtreq(String gdtreq) {
        this.gdtreq = gdtreq;
    }

    public String getGdtintro() {
        return gdtintro;
    }

    public void setGdtintro(String gdtintro) {
        this.gdtintro = gdtintro;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @Override
    public String toString() {
        return "GdtInfo{" +
                "gdtid='" + gdtid + '\'' +
                ", gdtname='" + gdtname + '\'' +
                ", gdttype=" + gdttype +
                ", gdtreq='" + gdtreq + '\'' +
                ", gdtintro='" + gdtintro + '\'' +
                ", tid='" + tid + '\'' +
                ", sid='" + sid + '\'' +
                '}';
    }
}
