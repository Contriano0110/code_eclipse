package com.gdtsSystem.entity;

public class GdtType {
    private int typeid;
    private String typename;

    public GdtType() {
    }

    public GdtType(int typeid, String typename) {
        this.typeid = typeid;
        this.typename = typename;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    @Override
    public String toString() {
        return "GdtType{" +
                "typeid=" + typeid +
                ", typename='" + typename + '\'' +
                '}';
    }
}
