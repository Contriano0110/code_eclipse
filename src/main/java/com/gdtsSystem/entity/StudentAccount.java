package com.gdtsSystem.entity;

public class StudentAccount {
    private int sid;
    private String passwd;

    public StudentAccount() {
    }

    public StudentAccount(int sid, String passwd) {
        this.sid = sid;
        this.passwd = passwd;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public String toString() {
        return "Student_account{" +
                "sid=" + sid +
                ", passwd='" + passwd + '\'' +
                '}';
    }
}
