package com.gdtsSystem.entity;

public class TeacherInfo {
    private String tid;
    private String tname;
    private String tsex;
    private int tage;
    private String tlvl;
    private String tmail;

    public TeacherInfo() {
    }

    public TeacherInfo(String tid, String tname, String tsex, int tage, String tlvl, String tmail) {
        this.tid = tid;
        this.tname = tname;
        this.tsex = tsex;
        this.tage = tage;
        this.tlvl = tlvl;
        this.tmail = tmail;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTsex() {
        return tsex;
    }

    public void setTsex(String tsex) {
        this.tsex = tsex;
    }

    public int getTage() {
        return tage;
    }

    public void setTage(int tage) {
        this.tage = tage;
    }

    public String getTlvl() {
        return tlvl;
    }

    public void setTlvl(String tlvl) {
        this.tlvl = tlvl;
    }

    public String getTmail() {
        return tmail;
    }

    public void setTmail(String tmail) {
        this.tmail = tmail;
    }

    @Override
    public String toString() {
        return "{" +
                "\"tid\":\"" + tid + "\"," +
                "\"tname\":\"" + tname + "\"," +
                "\"tsex\":\"" + tsex + "\"," +
                "\"tage\":\"" + tage + "\"," +
                "\"tlvl\":\"" + tlvl + "\"," +
                "\"tmail\":\"" + tmail + "\"" +
                "}";
    }
}
