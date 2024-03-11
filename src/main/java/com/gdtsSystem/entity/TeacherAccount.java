package com.gdtsSystem.entity;

public class TeacherAccount {
    private int tid;
    private String passwd;

    public TeacherAccount() {
    }

    public TeacherAccount(int tid, String passwd) {
        this.tid = tid;
        this.passwd = passwd;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public String toString() {
        return "teacher_account{" +
                "tid=" + tid +
                ", passwd='" + passwd + '\'' +
                '}';
    }
}
