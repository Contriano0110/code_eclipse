package com.gdtsSystem.entity;

public class StudentInfo {
    private String sid;
    private String sname;
    private String ssex;
    private int sage;
    private String smail;

    public StudentInfo() {
    }



    public StudentInfo(String sid, String sname, String ssex, int sage, String smail) {
        this.sid = sid;
        this.sname = sname;
        this.ssex = ssex;
        this.sage = sage;
        this.smail = smail;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSsex() {
        return ssex;
    }

    public void setSsex(String ssex) {
        this.ssex = ssex;
    }

    public int getSage() {
        return sage;
    }

    public void setSage(int sage) {
        this.sage = sage;
    }

    public String getSmail() {
        return smail;
    }

    public void setSmail(String smail) {
        this.smail = smail;
    }

    @Override
    public String toString() {
        return "{" +
                "\"sid\":\"" + sid + "\"," +
                "\"sname\":\"" + sname + "\"," +
                "\"ssex\":\"" + ssex + "\"," +
                "\"sage\":\"" + sage + "\"," +
                "\"smail\":\"" + smail + "\"" +
                "}";
    }
}
